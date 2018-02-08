package at.ac.oeaw.routes;

import at.ac.oeaw.Viewable.Viewable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;


@Path("/")
public class Distanbol {

    @Context
    ServletContext servletContext;

    @GET
    @Path("/")
    public Response getIndex() {
        try {
            String html = readFile("/WEB-INF/classes/view/index.html");

            return Response.accepted().entity(html).type("text/html").build();
        } catch (IOException e) {
            System.err.println("Cant read index html file");
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/convert")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response convert(@FormDataParam("file") InputStream inputStream, @FormDataParam("file") FormDataContentDisposition fileDetail) {
        String json = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            json = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().entity("Can't read input json file").build();
        }

        try {
//            String json = readFile("/WEB-INF/classes/example.json");//TODO read input instead of example


            String html = readFile("/WEB-INF/classes/view/view.html");
            Document doc = Jsoup.parse(html);


            //todo put the labels in properties file(even though the architecture is a little bit fuzzy for me right now)

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(json);
            JsonNode graph = jsonNode.get("@graph");
//null check todo

            if (graph == null) {
                return Response.status(400).entity("The provided json-ld is not in a known Stanbol output format. Make sure that it has the '@graph' element.").build();
            }
            if (graph.isArray()) {
                Iterator<JsonNode> iterator = graph.elements();

                Element viewablesHTML = doc.getElementById("viewables");

                Element script = doc.getElementById("script");

                while (iterator.hasNext()) {
                    JsonNode item = iterator.next();

                    Viewable viewable = createViewableFromItem(item);

                    //dont put these in the view.html
                    if (!viewable.getId().startsWith("urn:content") && !viewable.getId().startsWith("urn:enhancement")) {

                        String templateText = "<div><b>%s</b>%s</div>";

                        if (viewable.getId() != null) {
                            String id = String.format(templateText, "id:", viewable.getId());
                            viewablesHTML.append(id);
                        }

                        if (viewable.getLabel() != null) {
                            String label = String.format(templateText, "label:", viewable.getLabel());
                            viewablesHTML.append(label);
                        }

                        if (viewable.getComment() != null) {
                            String comment = String.format(templateText, "comment:", viewable.getComment());
                            viewablesHTML.append(comment);
                        }

                        if (viewable.getTypes() != null && !viewable.getTypes().isEmpty()) {
                            String typesHTML = "";
                            for (String type : viewable.getTypes()) {
                                typesHTML += "<li>" + type + "</li>";
                            }
                            String types = "<div><b>types:</b><ul>" + typesHTML + "</ul></div>";
                            viewablesHTML.append(types);
                        }


                        if (viewable.getDepiction() != null) {//TODO maps longitude lattitude
                            String depiction = String.format("<div><b>depiction:</b><div><img src=\"%s\"></img></div></div>", viewable.getDepiction());
                            viewablesHTML.append(depiction);
                        }

                        if (viewable.getLatitude() != null && !viewable.getLatitude().equals("") && viewable.getLongitude() != null && !viewable.getLongitude().equals("")) {
                            script.appendText("addMarker("+viewable.getLongitude()+","+viewable.getLatitude()+");");
                        }


                        viewablesHTML.append("<hr>");


//                    System.out.println(viewable.toString());
//                    System.out.println(doc.html());
                    }


                }
                viewablesHTML.children().last().remove();
            }


            return Response.accepted().entity(doc.html()).type("text/html").build();
//            return Response.accepted().entity(json).type("application/json").build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().entity("Cant read input json file").build();
        }


    }

    private Viewable createViewableFromItem(JsonNode item) {
        String id = item.get("@id").asText();

        ArrayList<String> types = new ArrayList<String>();
        JsonNode typeArray = item.get("@type");
        if (typeArray != null && typeArray.isArray()) {
            Iterator<JsonNode> iterator = typeArray.elements();
            while (iterator.hasNext()) {
                JsonNode type = iterator.next();
                types.add(type.asText());
            }
        }


        String depiction = item.get("foaf:depiction") == null ? null : item.get("foaf:depiction").get(0).asText();

        String longitude = null;
        if(item.get("geo:long") != null){
            if(item.get("geo:long").isArray()){
                longitude = item.get("geo:long").get(0).asText();
            }else{
                longitude = item.get("geo:long").asText();
            }
        }

        String latitude = null;
        if(item.get("geo:lat") != null){
            if(item.get("geo:lat").isArray()){
                latitude = item.get("geo:lat").get(0).asText();
            }else{
                latitude = item.get("geo:lat").asText();
            }
        }

        String label = null;
        JsonNode labelArray = item.get("rdfs:label");
        if (labelArray != null && labelArray.isArray()) {
            Iterator<JsonNode> iterator = labelArray.elements();
            while (iterator.hasNext()) {
                JsonNode labelPair = iterator.next();
                String language = labelPair.get("@language").asText();
                if (language.equals("en")) {
                    label = labelPair.get("@value").asText();
                }
            }
        }

        String comment = item.get("rdfs:comment") == null ? null : item.get("rdfs:comment").get("@value").asText();

        return new Viewable(id, types, depiction, comment, label, latitude, longitude);

    }

    private String readFile(String filePath) throws IOException {
        InputStream is = new FileInputStream(servletContext.getRealPath(filePath));
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();

        while (line != null) {
            sb.append(line).append("\n");
            line = buf.readLine();
        }

        String file = sb.toString();


        return file;
    }
}
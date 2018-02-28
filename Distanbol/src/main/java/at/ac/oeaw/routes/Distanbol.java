package at.ac.oeaw.routes;

import at.ac.oeaw.Viewable.Viewable;
import at.ac.oeaw.helpers.FileReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientProperties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


@Path("/")
public class Distanbol {

    final static Logger logger = Logger.getLogger(Distanbol.class);

    @Context
    ServletContext servletContext;

    @GET
    @Path("/")
    public Response convert(@QueryParam("URL") String URL) {

        if (URL == null) {//return index page, which explains how it works
            try {
                String html = FileReader.readFile(servletContext.getRealPath("/WEB-INF/classes/view/index.html"));
                return Response.status(200).entity(html).type("text/html").build();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Can't read index html file");
                return Response.serverError().build();
            }
        }

        //validate input
        String[] schemes = {"http", "https"}; // DEFAULT schemes = "http", "https", "ftp"
        UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.ALLOW_LOCAL_URLS);//allow local for testing

        String input = URL;

        if (!URL.startsWith("http://") && !URL.startsWith("https://")) {
            URL = "http://" + URL;
        }

        if (!urlValidator.isValid(URL)) {
            return Response.status(400).entity("The given URL: '" + input + "' is not valid.").build();
        }

        //send request
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(URL);

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();

        //validate redirect if any then send request to redirected
        if(response.getStatus() == 302){
            URL = response.getHeaderString("Location");
            if (!urlValidator.isValid(URL)) {
                return Response.status(400).entity("The given URL: '" + input + "' is not valid.").build();
            }
            webTarget = client.target(URL);

            invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            response = invocationBuilder.get();
        }

        //disallow more redirects
        if(response.getStatus() == 302){
            return Response.status(400).entity("Distanbol only allows one redirect when accessing the given URL input").build();
        }

        //validate response body
        String contentType = response.getHeaderString("Content-Type");
        if(!contentType.equals("application/json") && !contentType.equals("application/ld+json")){
            return Response.status(400).entity("The given URL: '" + input + "' doesn't point to a json or jsonld file.").build();
        }

        //read response body
        String json = response.readEntity(String.class);

        //create html from json
        try {
            String html = FileReader.readFile(servletContext.getRealPath("/WEB-INF/classes/view/view.html"));
            Document doc = Jsoup.parse(html);


            //todo put the labels in properties file(even though the architecture is a little bit fuzzy for me right now)

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(json);
            JsonNode graph = jsonNode.get("@graph");

            if (graph == null) {
                return Response.status(400).entity("The provided json-ld is not in a known Stanbol output format. Make sure that it has the '@graph' element.").build();
            }
            if (graph.isArray()) {
                Iterator<JsonNode> iterator = graph.elements();

                Element rawJsonHTML = doc.getElementById("rawJson");

                rawJsonHTML.append("Stanbol JSON input: <a href=\""+URL+"\">"+URL+"</a>");

                Element viewablesHTML = doc.getElementById("viewables");

                Element script = doc.getElementById("script");

                while (iterator.hasNext()) {
                    JsonNode item = iterator.next();//todo somewhere here for the depictions check if exists then choose one or display both i dont know

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
                            script.appendText("addMarker(" + viewable.getLongitude() + "," + viewable.getLatitude() + ");");
                        }


                        viewablesHTML.append("<hr>");

                    }


                }
                viewablesHTML.children().last().remove();
            }

            return Response.accepted().entity(doc.html()).type("text/html").build();

        } catch (IOException e) {
            logger.error("Can't read input json file: "+e.getMessage());
            return Response.serverError().entity("Can't read input json file").build();
        }

    }


    private Viewable createViewableFromItem(JsonNode item) {
        String id = item.get("@id").asText();

        ArrayList<String> types = new ArrayList<>();
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
        if (item.get("geo:long") != null) {
            if (item.get("geo:long").isArray()) {
                longitude = item.get("geo:long").get(0).asText();
            } else {
                longitude = item.get("geo:long").asText();
            }
        }

        String latitude = null;
        if (item.get("geo:lat") != null) {
            if (item.get("geo:lat").isArray()) {
                latitude = item.get("geo:lat").get(0).asText();
            } else {
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


}

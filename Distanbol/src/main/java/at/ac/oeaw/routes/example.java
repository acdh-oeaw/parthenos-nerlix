package at.ac.oeaw.routes;

import at.ac.oeaw.helpers.FileReader;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/example")
public class example {

    final static Logger logger = Logger.getLogger(example.class);

    @Context
    ServletContext servletContext;

    @GET
    @Path("/{exampleName}.json")
    @Produces("application/json")
    public Response getExamples(@PathParam("exampleName") String exampleName){

        try {
            String html = FileReader.readFile(servletContext.getRealPath("/WEB-INF/classes/examples/"+exampleName+".json"));

            return Response.accepted().entity(html).type("application/json").build();
        } catch (IOException e) {
            logger.error("Can't read example file, no such file probably");
            return Response.status(404).entity("{\"error\":\"There is no such file with the name: "+exampleName+".json.\"}").build();
        }

    }
}

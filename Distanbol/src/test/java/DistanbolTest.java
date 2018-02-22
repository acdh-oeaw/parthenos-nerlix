//import at.ac.oeaw.routes.Distanbol;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.jersey.servlet.ServletContainer;
//import org.glassfish.jersey.test.DeploymentContext;
//import org.glassfish.jersey.test.JerseyTest;
//import org.glassfish.jersey.test.ServletDeploymentContext;
//import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
//import org.glassfish.jersey.test.spi.TestContainerFactory;
//import org.junit.Test;
//
//import javax.ws.rs.core.Response;
//
//import static junit.framework.Assert.assertEquals;
//
//public class DistanbolTest extends JerseyTest {
//
//    @Override
//    protected DeploymentContext configureDeployment() {
//        ResourceConfig config = new ResourceConfig(Distanbol.class);
//        return ServletDeploymentContext.forServlet(
//                new ServletContainer(config)).build();
//    }
//
//    @Override
//    protected TestContainerFactory getTestContainerFactory() {
//        return new GrizzlyWebTestContainerFactory();
//    }
//
//
//    @Test
//    public void test() {
//
//        Response response = target("/").request().get();
//
//        assertEquals(200, response.getStatus());
//    }
//}

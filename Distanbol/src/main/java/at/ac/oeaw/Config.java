//package at.ac.oeaw;
//
//import at.ac.oeaw.helpers.FileReader;
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//
//import java.io.IOException;
//
//public class Config implements ServletContextListener {
//    public void contextInitialized(ServletContextEvent e) {
//        ServletContext servletContext = e.getServletContext();
//
//        try {//war environment
//            FileReader.readFile(servletContext.getRealPath("/WEB-INF/web.xml"));
//            servletContext.setAttribute("realPath", servletContext.getRealPath("/WEB-INF/classes/"));
//        } catch (IOException ex) {//test and dev environment
//            servletContext.setAttribute("realPath", servletContext.getRealPath("src/main/resources/"));
//        }
//    }
//
//    public void contextDestroyed(ServletContextEvent e) {
//
//    }
//}

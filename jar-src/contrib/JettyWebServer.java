/*
 * provides Jetty 6.1.26 embedding bindings and control
 *
 * THIS IS A SINGLETON IMPLEMENTATION see:
 * http://www.javaworld.com/javaworld/jw-04-2003/jw-0425-designpatterns.html
 */
package contrib;

import java.io.IOException;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;
import org.springframework.core.io.ClassPathResource;
import servlets.ZoneLibrary;
import servlets.ZonePlaylist;
import servlets.ZoneSelection_Page;
import servlets.ZoneSearchMedia_DialogPage;

/**
 * @author Jason Zerbe
 */
public class JettyWebServer {

    private static JettyWebServer jws_SingleInstance = null;
    protected Server jws_serverInstance = null;
    protected static int jws_serverPortInt = 2320;
    protected String webAppContextPathStr = "/";
    protected String webAppDirStr = "../webapp";

    protected JettyWebServer(int theServerPortInt) {
        jws_serverInstance = new Server(theServerPortInt);
        jws_serverPortInt = theServerPortInt;

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setServer(jws_serverInstance);
        webAppContext.setContextPath(webAppContextPathStr);
        try {
            webAppContext.setResourceBase(new ClassPathResource(webAppDirStr).getURL().toString());
        } catch (IOException ex) {
            System.err.println(ex);
        }
        webAppContext.addServlet(new ServletHolder(new ZoneSelection_Page()), "/servlets/list-zones");
        webAppContext.addServlet(new ServletHolder(new ZonePlaylist()), "/servlets/playlist");
        webAppContext.addServlet(new ServletHolder(new ZoneLibrary()), "/servlets/library");
        webAppContext.addServlet(new ServletHolder(new ZoneSearchMedia_DialogPage()), "/servlets/library-search-dialog");
        jws_serverInstance.addHandler(webAppContext);
    }

    public static JettyWebServer getInstance() {
        if (jws_SingleInstance == null) {
            jws_SingleInstance = new JettyWebServer(jws_serverPortInt);
        }
        return jws_SingleInstance;
    }

    public static JettyWebServer getInstance(int theServerPortInt) {
        if (jws_SingleInstance == null) {
            jws_SingleInstance = new JettyWebServer(theServerPortInt);
        }
        return jws_SingleInstance;
    }

    public int getServerPortInt() {
        return jws_serverPortInt;
    }

    public void startServer() {
        try {
            jws_serverInstance.start();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public boolean isServerRunning() {
        return jws_serverInstance.isRunning();
    }

    public void stopServer() {
        try {
            jws_serverInstance.stop();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}

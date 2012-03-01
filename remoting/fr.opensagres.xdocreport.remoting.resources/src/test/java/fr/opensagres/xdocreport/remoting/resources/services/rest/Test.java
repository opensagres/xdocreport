package fr.opensagres.xdocreport.remoting.resources.services.rest;

import javax.ws.rs.core.Application;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.rest.client.JAXRSResourcesServiceClientFactory;

public class Test
{

    private static final int PORT = 9999;

    private static Server server;

    private static final String BASE_ADDRESS = "http://localhost:" + PORT;

    public static void main( String[] args )
        throws Exception
    {
        ServletHolder servlet = new ServletHolder( CXFNonSpringJaxrsServlet.class );

        servlet.setInitParameter( Application.class.getName(), MockJAXRSResourcesApplication.class.getName() );
        servlet.setInitParameter( "jaxrs.serviceClasses", MockJAXRSResourcesService.class.getName() );

        servlet.setInitParameter( "timeout", "60000" );
        server = new Server( PORT );

        ServletContextHandler context = new ServletContextHandler( server, "/", ServletContextHandler.SESSIONS );

        context.addServlet( servlet, "/*" );
        server.start();

        ResourcesService client = JAXRSResourcesServiceClientFactory.create( BASE_ADDRESS );
        Resource root = client.getRoot();
        
        while ( System.in.read() == -1 )
        {

        }
        server.stop();
    }
}

package fr.opensagres.xdocreport.remoting.repository.services.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.opensagres.xdocreport.document.ProcessState;
import fr.opensagres.xdocreport.remoting.repository.Data;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceContent;

public class JAXRSRepositoryServiceTestCase
{

    private static final int PORT = 9999;

    private static Server server;

    private static final String BASE_ADDRESS = "http://localhost:" + PORT;

    public File tempFolder = new File( "target" );

    @BeforeClass
    public static void startServer()
        throws Exception
    {

        ServletHolder servlet = new ServletHolder( CXFNonSpringJaxrsServlet.class );

        servlet.setInitParameter( Application.class.getName(), JAXRSRepositoryApplication.class.getName() );
        servlet.setInitParameter( "jaxrs.serviceClasses", JAXRSRepositoryService.class.getName() );

        servlet.setInitParameter( "timeout", "60000" );
        server = new Server( PORT );

        ServletContextHandler context = new ServletContextHandler( server, "/", ServletContextHandler.SESSIONS );

        context.addServlet( servlet, "/*" );
        server.start();

    }

    @Test
    public void upload()
        throws IOException
    {

        uploadAFile( "reportID1" );

    }

    private static int uploaded = 0;

    private void uploadAFile( String reportID1 )
        throws IOException
    {
        WebClient client = WebClient.create( BASE_ADDRESS );
        client.path( "upload" );
        client.type( MediaType.APPLICATION_JSON_TYPE );

        ResourceContent content = new ResourceContent();
        content.setId( reportID1 );

        InputStream in = Data.class.getResourceAsStream( "bo.docx" );
        content.setContent( fr.opensagres.xdocreport.core.io.IOUtils.toByteArray( in ) );
        uploaded = uploaded + 1;
        client.post( content );
    }

    @Test
    public void download()
        throws IOException
    {
        // first upload a file...
        String reportID = "id";
        uploadAFile( reportID );

        WebClient client = WebClient.create( BASE_ADDRESS );
        client.path( "download/" + reportID );
       // client.accept( MediaType.APPLICATION_JSON );
        System.out.println( client.getCurrentURI() );
        
        ResourceContent r = client.get(ResourceContent.class);
        System.err.println(r);
        
        //Response resp = client.get();
        //System.out.println( resp.getStatus() );
        // byte[] flux= client.get( byte[].class );
        // assertNotNull(flux);
        // createFile( flux,"result.docx" );

    }

    @AfterClass
    public static void stopServer()
        throws Exception
    {
        server.stop();
    }
}

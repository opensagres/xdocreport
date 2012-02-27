package fr.opensagres.xdocreport.remoting.repository.services.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.Application;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.opensagres.xdocreport.remoting.repository.Data;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceContent;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceMetadata;
import fr.opensagres.xdocreport.remoting.repository.services.IRepositoryService;
import fr.opensagres.xdocreport.remoting.repository.services.rest.server.JAXRSRepositoryApplication;
import fr.opensagres.xdocreport.remoting.repository.services.rest.server.JAXRSRepositoryService;

public class JAXRSRepositoryServiceTestCase2
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
        IRepositoryService client = JAXRSClientFactory.create( BASE_ADDRESS, IJAXRSRepositoryService.class );

        ResourceContent content = new ResourceContent();
        content.setId( reportID1 );

        InputStream in = Data.class.getResourceAsStream( "bo.docx" );
        content.setContent( fr.opensagres.xdocreport.core.io.IOUtils.toByteArray( in ) );
        uploaded = uploaded + 1;
        client.upload( content );
    }

    @Test
    public void resources()
        throws IOException
    {

        IRepositoryService client = JAXRSClientFactory.create( BASE_ADDRESS, IJAXRSRepositoryService.class );
        List<ResourceMetadata> metadatas = client.getMetadatas();
        for ( ResourceMetadata metadata : metadatas )
        {
            System.err.println( metadata.getId() );
        }
    }

    //@Ignore( "Ne peux pas fonctionner tant qu'un doc n'a pas été processé...s" )
    @Test
    public void download()
        throws IOException
    {
        // // first upload a file...
        // String reportID = "download";
        // uploadAFile( reportID );

        uploadAFile( "DOWNLOAD" );
        
        IRepositoryService client = JAXRSClientFactory.create( BASE_ADDRESS, IJAXRSRepositoryService.class );
        ResourceContent resourceContent = client.download( "DOWNLOAD" );

       createFile( resourceContent.getContent(), "download.docx" );
    }

    private void createFile( byte[] flux, String filename )
        throws FileNotFoundException, IOException
    {
        File aFile = new File( tempFolder, filename );
        FileOutputStream fos = new FileOutputStream( aFile );
        fos.write( flux );
        fos.close();
    }

    @AfterClass
    public static void stopServer()
        throws Exception
    {
        server.stop();
    }
}

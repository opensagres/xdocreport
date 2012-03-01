package fr.opensagres.xdocreport.remoting.repository.services.rest.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Application;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.opensagres.xdocreport.remoting.repository.Data;
import fr.opensagres.xdocreport.remoting.repository.domain.Resource;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceContent;
import fr.opensagres.xdocreport.remoting.repository.services.IRepositoryService;
import fr.opensagres.xdocreport.remoting.repository.services.rest.IJAXRSRepositoryService;
import fr.opensagres.xdocreport.remoting.repository.services.rest.MockJAXRSRepositoryApplication;
import fr.opensagres.xdocreport.remoting.repository.services.rest.MockJAXRSRepositoryService;

public class JAXRSRepositoryServiceCGLibClientTestCase
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

        servlet.setInitParameter( Application.class.getName(), MockJAXRSRepositoryApplication.class.getName() );
        servlet.setInitParameter( "jaxrs.serviceClasses", MockJAXRSRepositoryService.class.getName() );

        servlet.setInitParameter( "timeout", "60000" );
        server = new Server( PORT );

        ServletContextHandler context = new ServletContextHandler( server, "/", ServletContextHandler.SESSIONS );

        context.addServlet( servlet, "/*" );
        server.start();

    }

    @Test
    public void name()
    {
        IRepositoryService client = JAXRSClientFactory.create( BASE_ADDRESS, IJAXRSRepositoryService.class );
        String name = client.getName();
        Assert.assertEquals( "Test-RepositoryService", name );
    }

    @Test
    public void root()
        throws IOException
    {

        IRepositoryService client = JAXRSClientFactory.create( BASE_ADDRESS, IJAXRSRepositoryService.class );
        Resource root = client.getRoot();

        // Document coming from the folder src/test/resources/fr/opensagres/xdocreport/remoting/repository
        // See class MockRepositoryService
        Assert.assertNotNull( root );
        Assert.assertEquals( "repository", root.getName() );
        Assert.assertEquals( 4, root.getChildren().size() );
        Assert.assertEquals( "Custom", root.getChildren().get( 0 ).getName() );
        Assert.assertEquals( Resource.FOLDER_TYPE, root.getChildren().get( 0 ).getType() );
        Assert.assertEquals( "Opensagres", root.getChildren().get( 1 ).getName() );
        Assert.assertEquals( Resource.FOLDER_TYPE, root.getChildren().get( 1 ).getType() );        
        Assert.assertEquals( "Simple.docx", root.getChildren().get( 2 ).getName() );
        Assert.assertEquals( "Simple.odt", root.getChildren().get( 3 ).getName() );

    }

    //@Test
    public void upload()
        throws IOException
    {

        // uploadAFile( "reportID1" );

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
        client.upload( reportID1, fr.opensagres.xdocreport.core.io.IOUtils.toByteArray( in ) );
    }

    // @Ignore( "Ne peux pas fonctionner tant qu'un doc n'a pas été processé...s" )
    //@Test
    public void download()
        throws IOException
    {
        // // first upload a file...
        // String reportID = "download";
        // uploadAFile( reportID );

        uploadAFile( "DOWNLOAD" );

        IRepositoryService client = JAXRSClientFactory.create( BASE_ADDRESS, IJAXRSRepositoryService.class );
        // ResourceContent resourceContent = client.download( "DOWNLOAD" );

        // createFile( resourceContent.getContent(), "download.docx" );
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

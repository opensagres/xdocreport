package fr.opensagres.xdocreport.remoting.resources.services.rest.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

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

import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.rest.JAXRSResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.rest.MockJAXRSResourcesApplication;
import fr.opensagres.xdocreport.remoting.resources.services.rest.MockJAXRSResourcesService;

public class JAXRSResourcesServiceCGLibClientTestCase
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

        servlet.setInitParameter( Application.class.getName(), MockJAXRSResourcesApplication.class.getName() );
        servlet.setInitParameter( "jaxrs.serviceClasses", MockJAXRSResourcesService.class.getName() );

        servlet.setInitParameter( "timeout", "60000" );
        server = new Server( PORT );

        ServletContextHandler context = new ServletContextHandler( server, "/", ServletContextHandler.SESSIONS );

        context.addServlet( servlet, "/*" );
        server.start();

    }

    @Test
    public void name()
    {
        ResourcesService client = JAXRSClientFactory.create( BASE_ADDRESS, JAXRSResourcesService.class );
        String name = client.getName();
        Assert.assertEquals( "Test-RepositoryService", name );
    }

    @Test
    public void root()
        throws IOException
    {

        ResourcesService client = JAXRSClientFactory.create( BASE_ADDRESS, JAXRSResourcesService.class );
        Resource root = client.getRoot();

        // Document coming from the folder src/test/resources/fr/opensagres/xdocreport/resources/repository
        // See class MockRepositoryService
        Assert.assertNotNull( root );
        Assert.assertEquals( "resources", root.getName() );
        Assert.assertEquals( 4, root.getChildren().size() );

        // Sort the list of Resource because File.listFiles() doeesn' given the same order
        // between different OS.
        Collections.sort( root.getChildren(), ResourceComparator.INSTANCE );

        Assert.assertEquals( "Custom", root.getChildren().get( 0 ).getName() );
        Assert.assertEquals( Resource.FOLDER_TYPE, root.getChildren().get( 0 ).getType() );
        Assert.assertEquals( "Opensagres", root.getChildren().get( 1 ).getName() );
        Assert.assertEquals( Resource.FOLDER_TYPE, root.getChildren().get( 1 ).getType() );
        Assert.assertEquals( "Simple.docx", root.getChildren().get( 2 ).getName() );
        Assert.assertEquals( "Simple.odt", root.getChildren().get( 3 ).getName() );

    }

    @Test
    public void download()
        throws FileNotFoundException, IOException
    {
        String resourcePath = "Simple.docx";
        ResourcesService client = JAXRSClientFactory.create( BASE_ADDRESS, JAXRSResourcesService.class );
        byte[] document = client.download( resourcePath );
        Assert.assertNotNull( document );
        createFile( document, resourcePath );
    }

    private void createFile( byte[] flux, String filename )
        throws FileNotFoundException, IOException
    {
        File aFile = new File( tempFolder, this.getClass().getSimpleName() + "_" + filename );
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

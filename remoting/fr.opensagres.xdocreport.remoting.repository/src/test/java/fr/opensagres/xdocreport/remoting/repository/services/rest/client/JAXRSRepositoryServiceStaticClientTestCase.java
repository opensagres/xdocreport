package fr.opensagres.xdocreport.remoting.repository.services.rest.client;

import java.io.File;

import javax.ws.rs.core.Application;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.opensagres.xdocreport.remoting.repository.domain.Resource;
import fr.opensagres.xdocreport.remoting.repository.services.IRepositoryService;
import fr.opensagres.xdocreport.remoting.repository.services.rest.MockJAXRSRepositoryApplication;
import fr.opensagres.xdocreport.remoting.repository.services.rest.MockJAXRSRepositoryService;

public class JAXRSRepositoryServiceStaticClientTestCase
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
        IRepositoryService client = JAXRSRepositoryServiceClientFactory.create( BASE_ADDRESS );
        String name = client.getName();
        Assert.assertEquals( "Test-RepositoryService", name );
    }

    @Test
    public void root()
    {
        IRepositoryService client = JAXRSRepositoryServiceClientFactory.create( BASE_ADDRESS );
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

    @AfterClass
    public static void stopServer()
        throws Exception
    {
        server.stop();
    }
}

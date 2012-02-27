package fr.opensagres.xdocreport.document.tools.repository;

import java.io.File;
import java.io.FileReader;

import javax.ws.rs.core.Application;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.remoting.repository.services.rest.server.JAXRSRepositoryApplication;
import fr.opensagres.xdocreport.remoting.repository.services.rest.server.JAXRSRepositoryService;

public class MainTest
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
    public void testRepositoryName()
        throws Exception
    {
        File nameFile = new File( tempFolder, "name.txt" );
        String[] args = { "-baseAddress", BASE_ADDRESS, "-out", nameFile.getPath() };
        Main.main( args );

        Assert.assertTrue( nameFile.exists() );
        String s = IOUtils.toString( new FileReader( nameFile ) );
        Assert.assertEquals( "XDocReport", s );
    }

    @AfterClass
    public static void stopServer()
        throws Exception
    {
        server.stop();
    }
}

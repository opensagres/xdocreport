package fr.opensagres.xdocreport.document.tools.remoting.resources;

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
import fr.opensagres.xdocreport.document.tools.remoting.resources.Main;
import fr.opensagres.xdocreport.document.tools.remoting.resources.rest.MockJAXRSRepositoryApplication;
import fr.opensagres.xdocreport.document.tools.remoting.resources.rest.MockJAXRSRepositoryService;
import fr.opensagres.xdocreport.remoting.resources.services.ServiceName;

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

        servlet.setInitParameter( Application.class.getName(), MockJAXRSRepositoryApplication.class.getName() );
        servlet.setInitParameter( "jaxrs.serviceClasses", MockJAXRSRepositoryService.class.getName() );

        servlet.setInitParameter( "timeout", "60000" );
        server = new Server( PORT );

        ServletContextHandler context = new ServletContextHandler( server, "/", ServletContextHandler.SESSIONS );

        context.addServlet( servlet, "/*" );
        server.start();

    }

    @Test
    public void testBadBaseAddress()
        throws Exception
    {
        File nameFile = new File( tempFolder, "name.txt" );
        File errFile = new File( tempFolder, "name.err.txt" );
        String[] args =
            { "-baseAddress", "http://localhost:99/remaaaaa=aaaa", "-out", nameFile.getPath(), "-err",
                errFile.getPath() };
        Main.main( args );

        Assert.assertTrue( errFile.exists() );
        String s = IOUtils.toString( new FileReader( errFile ) );
        Assert.assertNotNull( s );
        // System.err.println( s );
        // Assert.assertEquals( "XDocReport", s );
    }

    @Test
    public void name()
        throws Exception
    {
        File nameFile = new File( tempFolder, "name.txt" );
        String[] args =
            { "-baseAddress", BASE_ADDRESS, "-serviceName", ServiceName.name.name(), "-out", nameFile.getPath() };
        Main.main( args );

        Assert.assertTrue( nameFile.exists() );
        String s = IOUtils.toString( new FileReader( nameFile ) );
        Assert.assertEquals( "Test-Tools-RepositoryService", s );
    }

    @Test
    public void root()
        throws Exception
    {
        File rootFile = new File( tempFolder, "root.xml" );
        String[] args =
            { "-baseAddress", BASE_ADDRESS, "-serviceName", ServiceName.root.name(), "-out", rootFile.getPath() };
        Main.main( args );

        Assert.assertTrue( rootFile.exists() );
        String s = IOUtils.toString( new FileReader( rootFile ) );
        Assert.assertEquals( "<resource name=\"resources\" type=\"0\"><resource name=\"Custom\" type=\"0\"><resource name=\"CustomSimple.docx\" type=\"1\"></resource><resource name=\"CustomSimple.odt\" type=\"1\"></resource></resource><resource name=\"Opensagres\" type=\"0\"><resource name=\"OpensagresSimple.docx\" type=\"1\"></resource><resource name=\"OpensagresSimple.odt\" type=\"1\"></resource></resource><resource name=\"Simple.docx\" type=\"1\"></resource><resource name=\"Simple.odt\" type=\"1\"></resource></resource>",
                             s );
    }

    @Test
    public void download()
        throws Exception
    {
        String fileToDownload = "Simple.docx";
        File downlodedFile = new File( tempFolder, "DownlodedSimple.docx" );
        String[] args =
            { "-baseAddress", BASE_ADDRESS, "-serviceName", ServiceName.download.name(), "-resources", fileToDownload,
                "-out", downlodedFile.getPath() };
        Main.main( args );
        Assert.assertTrue( downlodedFile.exists() );

    }

    @AfterClass
    public static void stopServer()
        throws Exception
    {
        server.stop();
    }
}

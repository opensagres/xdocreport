package fr.opensagres.xdocreport.remoting.resources.services.client.jaxrs.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.remoting.resources.Data;
import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.domain.ResourceType;
import fr.opensagres.xdocreport.remoting.resources.services.FileUtils;
import fr.opensagres.xdocreport.remoting.resources.services.ResourceComparator;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesServiceName;
import fr.opensagres.xdocreport.remoting.resources.services.client.jaxrs.MockJAXRSResourcesApplication;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.Providers;

public class JAXRSResourcesServiceWebClientTestCase
{

    private static final int PORT = 9999;

    private static Server server;

    private static final String BASE_ADDRESS = "http://localhost:" + PORT;

    public static File srcFolder = new File( "src/test/resources/fr/opensagres/xdocreport/remoting/resources" );

    public static File tempFolder = new File( "target" );

    public static File resourcesFolder = new File( tempFolder, "resources" );

    @BeforeClass
    public static void startServer()
        throws Exception
    {
        // 1) Copy resources in the target folder.
        initResources();

        // 2) Start Jetty Server
        ServletHolder servlet = new ServletHolder( CXFNonSpringJaxrsServlet.class );
        servlet.setInitParameter( "javax.ws.rs.Application", MockJAXRSResourcesApplication.class.getName() );

        servlet.setInitParameter( "timeout", "60000" );
        server = new Server( PORT );

        ServletContextHandler context = new ServletContextHandler( server, "/", ServletContextHandler.SESSIONS );

        context.addServlet( servlet, "/*" );
        server.start();

    }

    protected WebClient getClient()
    {
        // registrer here client side providers
        return WebClient.create( BASE_ADDRESS, Providers.get() );
    }
    @Ignore
    @Test
    public void name()
    {
        WebClient client = getClient();
        String name = client.path( ResourcesServiceName.name ).accept( MediaType.TEXT_PLAIN ).get( String.class );
        Assert.assertEquals( "Test-RepositoryService", name );
    }
    @Ignore
    @Test
    public void root()
        throws IOException
    {

        WebClient client = getClient();
        Resource root =
            client.path( ResourcesServiceName.root ).accept( MediaType.APPLICATION_JSON ).get( Resource.class );

        // Document coming from the folder
        // src/test/resources/fr/opensagres/xdocreport/resources/repository
        // See class MockRepositoryService
        Assert.assertNotNull( root );
        Assert.assertEquals( "resources", root.getName() );
        Assert.assertTrue( root.getChildren().size() >= 4 );

        // Sort the list of Resource because File.listFiles() doeesn' given the
        // same order
        // between different OS.
        Collections.sort( root.getChildren(), ResourceComparator.INSTANCE );

        Assert.assertEquals( "Custom", root.getChildren().get( 0 ).getName() );
        Assert.assertEquals( ResourceType.CATEGORY, root.getChildren().get( 0 ).getType() );
        Assert.assertEquals( "Opensagres", root.getChildren().get( 1 ).getName() );
        Assert.assertEquals( ResourceType.CATEGORY, root.getChildren().get( 1 ).getType() );
        Assert.assertEquals( "Simple.docx", root.getChildren().get( 2 ).getName() );
        Assert.assertEquals( "Simple.odt", root.getChildren().get( 3 ).getName() );
    }

    @Ignore
    @Test
    public void download()
        throws FileNotFoundException, IOException
    {
        String resourcePath = "Simple.docx";
        WebClient client = getClient();

        StringBuilder path = new StringBuilder( ResourcesServiceName.download.name() );
        path.append( "/" );
        path.append( resourcePath );

        BinaryData document =
            client.path( path.toString() ).accept( MediaType.APPLICATION_JSON_TYPE ).get( BinaryData.class );
        Assert.assertNotNull( document );
        Assert.assertNotNull( document.getContent() );
        createFile( document.getContent(), resourcePath );
    }

    private void createFile( byte[] stream, String filename )
        throws FileNotFoundException, IOException
    {
        File aFile = new File( tempFolder, this.getClass().getSimpleName() + "_" + filename );
        FileOutputStream fos = new FileOutputStream( aFile );
        IOUtils.write( stream, fos );
        fos.close();
    }

    // @Test
    public void uploadARootFile()
        throws FileNotFoundException, IOException
    {

        String resourceId = "ZzzNewSimple_" + this.getClass().getSimpleName() + ".docx";
        InputStream document =  Data.class.getResourceAsStream( "Simple.docx" ) ;

        BinaryData dataIn = new BinaryData();
        dataIn.setResourceId( resourceId );
        dataIn.setContent( IOUtils.toByteArray(document) );

        WebClient client = getClient();
        client.path( ResourcesServiceName.upload );
        client.type( MediaType.APPLICATION_JSON_TYPE );
        client.post( dataIn );

        client.reset();

        StringBuilder path = new StringBuilder( ResourcesServiceName.download.name() );
        path.append( "/" );
        path.append( resourceId );
        byte[] downloadedDocument =
            client.path( path.toString() ).accept( MediaType.APPLICATION_JSON_TYPE ).get( byte[].class );
        Assert.assertNotNull( document );

        Assert.assertNotNull( downloadedDocument );
    }

    @Test
    public void uploadAFileInFolder()
        throws FileNotFoundException, IOException
    {
        String resourceId = "ZzzCustom____NewCustomSimple_" + this.getClass().getSimpleName() + ".docx";
        InputStream document =  Data.class.getResourceAsStream( "Simple.docx" ) ;


        BinaryData dataIn = new BinaryData();
        dataIn.setResourceId( resourceId );
        dataIn.setContent( IOUtils.toByteArray(document) );

        WebClient client = getClient();
        client.path( ResourcesServiceName.upload );
        client.type( MediaType.APPLICATION_JSON_TYPE );
        client.post( dataIn );

        // Test if file was uploaded in the target/resources folder
        Assert.assertTrue( new File( resourcesFolder, "ZzzCustom/NewCustomSimple_" + this.getClass().getSimpleName()
            + ".docx" ).exists() );

        client.reset();

        // Test if download with the resourceId returns a non null binary data.
        StringBuilder path = new StringBuilder( ResourcesServiceName.download.name() );
        path.append( "/" );
        path.append( resourceId );
        byte[] downloadedDocument =
            client.path( path.toString() ).accept( MediaType.APPLICATION_JSON_TYPE ).get( byte[].class );
        Assert.assertNotNull( downloadedDocument );
    }

    // @Test
    // public void upload()
    // throws IOException
    // {
    //
    // uploadAFile( "reportID1" );
    //
    // }
    //
    // private static int uploaded = 0;
    //
    // private void uploadAFile( String reportID1 )
    // throws IOException
    // {
    // WebClient client = WebClient.create( BASE_ADDRESS );
    // client.path( "upload" );
    // client.type( MediaType.APPLICATION_JSON_TYPE );
    //
    // ResourceContent content = new ResourceContent();
    // content.setId( reportID1 );
    //
    // InputStream in = Data.class.getResourceAsStream( "bo.docx" );
    // content.setContent( fr.opensagres.xdocreport.core.io.IOUtils.toByteArray(
    // in ) );
    // uploaded = uploaded + 1;
    // client.post( content );
    // }

    // //@Test
    // public void download()
    // throws IOException
    // {
    // // first upload a file...
    // String reportID = "id";
    // uploadAFile( reportID );
    //
    // WebClient client = WebClient.create( BASE_ADDRESS );
    // client.path( "download/" + reportID );
    // // client.accept( MediaType.APPLICATION_JSON );
    // System.out.println( client.getCurrentURI() );
    //
    // ResourceContent r = client.get( ResourceContent.class );
    // System.err.println( r );
    //
    // // Response resp = client.get();
    // // System.out.println( resp.getStatus() );
    // // byte[] flux= client.get( byte[].class );
    // // assertNotNull(flux);
    // // createFile( flux,"result.docx" );
    //
    // }

    @AfterClass
    public static void stopServer()
        throws Exception
    {
        server.stop();
    }

    private static void initResources()
        throws IOException
    {
        if ( resourcesFolder.exists() )
        {
            resourcesFolder.delete();
        }
        FileUtils.copyDirectory( srcFolder, resourcesFolder );
    }
}

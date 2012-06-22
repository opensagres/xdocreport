package fr.opensagres.xdocreport.remoting.resources.services.client.jaxrs.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.remoting.resources.Data;
import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.LargeBinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.domain.ResourceType;
import fr.opensagres.xdocreport.remoting.resources.services.FileUtils;
import fr.opensagres.xdocreport.remoting.resources.services.ResourceComparator;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;
import fr.opensagres.xdocreport.remoting.resources.services.client.jaxrs.MockJAXRSResourcesApplication;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.JAXRSResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.Providers;

public abstract class AbstractJAXRSResourcesServiceTest {

	private static final int PORT = 9997;
	private static Server server;
	protected static final String BASE_ADDRESS = "http://localhost:" + PORT;
	public static File srcFolder = new File( "src/test/resources/fr/opensagres/xdocreport/remoting/resources" );
	public static File tempFolder = new File( "target" );
	public static File resourcesFolder = new File( tempFolder, "resources" );

	@BeforeClass
	public static void startServer() throws Exception {

	    // 1) Copy resources in the target folder.
	    if ( resourcesFolder.exists() )
	    {
	        resourcesFolder.delete();
	    }
	    FileUtils.copyDirectory( srcFolder, resourcesFolder );

	    // 2) Start Jetty Server

	    ServletHolder servlet = new ServletHolder( CXFNonSpringJaxrsServlet.class );
	    servlet.setInitParameter( "javax.ws.rs.Application", MockJAXRSResourcesApplication.class.getName() );

	    servlet.setInitParameter( "timeout", "60000" );
	    server = new Server( PORT );

	    ServletContextHandler context = new ServletContextHandler( server, "/", ServletContextHandler.SESSIONS );

	    context.addServlet( servlet, "/*" );
	    server.start();

	}

	public static void main(String[] args) throws IOException {
	    String resourceId = "Opensagres____ODTCV.odt";
	    JAXRSResourcesService client =
	        JAXRSClientFactory.create( "http://xdocreport.opensagres.cloudbees.net/cxf", JAXRSResourcesService.class, Providers.get() );
	    InputStream document = Data.class.getResourceAsStream( "Simple.docx" ) ;

	    ClientConfiguration config = WebClient.getConfig(client);
	    HTTPConduit http = (HTTPConduit)config.getConduit();
	    //Turn off chunking so that NTLM can occur
	    HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
	    //httpClientPolicy.setConnectionTimeout(36000);
	    //httpClientPolicy.setAllowChunking(false);
	    http.setClient(httpClientPolicy);

	    config.getInInterceptors().add(new LoggingInInterceptor());
	    config.getOutInterceptors().add(new LoggingOutInterceptor());

	    BinaryData dataIn = new BinaryData();
	    dataIn.setResourceId( resourceId );
	    dataIn.setContent( IOUtils.toByteArray(document) );

	    client.upload( dataIn );

	}

	@Test
	public void name() {
		JAXRSResourcesService client = getClient();
	    String name = client.getName();
	    Assert.assertEquals( "Test-RepositoryService", name );
	}

	 abstract JAXRSResourcesService getClient() ;

	@Test
	public void root() throws IOException, ResourcesException {

		JAXRSResourcesService client = getClient();
	    Resource root = client.getRoot();

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
	    Assert.assertEquals( ResourceType.FOLDER, root.getChildren().get( 0 ).getType() );
	    Assert.assertEquals( "Opensagres", root.getChildren().get( 1 ).getName() );
	    Assert.assertEquals( ResourceType.FOLDER, root.getChildren().get( 1 ).getType() );
	    Assert.assertEquals( "Simple.docx", root.getChildren().get( 2 ).getName() );
	    Assert.assertEquals( "Simple.odt", root.getChildren().get( 3 ).getName() );

	}

	@Test
	public void downloadARootFile() throws FileNotFoundException,
			IOException, ResourcesException {
			    String resourceId = "Simple.docx";
			    JAXRSResourcesService client = getClient();

			    BinaryData document = client.download( resourceId );
			    assertDocument(document);
			    createFile( document.getContent(), resourceId );
			}

	@Test
	public void downloadLargeARootFile() throws FileNotFoundException,
			IOException, ResourcesException {
			    String resourceId = "Simple.docx";
			    JAXRSResourcesService client = getClient();

			    LargeBinaryData document = client.downloadLarge( resourceId );
			    Assert.assertNotNull( document );
			    Assert.assertNotNull( document.getContent() );
			    createFile( document.getContent(), resourceId );
			}

	@Test
	public void downloadAFileInFolder() throws FileNotFoundException,
			IOException, ResourcesException {
			    String resourceId = "Custom%2FCustomSimple.docx";
			    JAXRSResourcesService client = getClient();
			    BinaryData document = client.download( resourceId );
			    assertDocument(document);
			    createFile( document.getContent(), resourceId );
			}

	private void createFile(InputStream content, String filename)
			throws FileNotFoundException, IOException {
			    File aFile = new File( tempFolder, this.getClass().getSimpleName() + "_" + filename );
			    FileOutputStream fos = new FileOutputStream( aFile );
			    IOUtils.copy(content, fos );
			    fos.close();
			}

	private void createFile(byte[] content, String filename)
			throws FileNotFoundException, IOException {
			    File aFile = new File( tempFolder, this.getClass().getSimpleName() + "_" + filename );
			    FileOutputStream fos = new FileOutputStream( aFile );
			    IOUtils.write(content, fos );
			    fos.close();
			}

	@AfterClass
	public static void stopServer() throws Exception {
	    server.stop();
	}

	@Test
	public void uploadARootFile() throws FileNotFoundException, IOException,
			ResourcesException {
			    String resourceId = "ZzzNewSimple_" + this.getClass().getSimpleName() + ".docx";
			    JAXRSResourcesService client = getClient();
			    InputStream document =  Data.class.getResourceAsStream( "Simple.docx" ) ;

			    BinaryData dataIn = new BinaryData();
			    dataIn.setResourceId( resourceId );
			    dataIn.setContent( IOUtils.toByteArray(document) );

			    client.upload( dataIn );

			    // Test if file was uploaded in the target/resources folder
			    Assert.assertTrue( new File( resourcesFolder, resourceId ).exists() );

			    // Test if download with the resourceId returns a non null binary data.
			    BinaryData downloadedDocument = client.download( resourceId );
			    assertDocument(downloadedDocument);
			}

	@Test
	public void uploadLargeARootFile() throws FileNotFoundException,
			IOException, ResourcesException {
			    String resourceId = "ZzzNewSimple_" + this.getClass().getSimpleName() + ".docx";
			    JAXRSResourcesService client = getClient();
			    InputStream document =  Data.class.getResourceAsStream( "Simple.docx" ) ;

			    LargeBinaryData dataIn = new LargeBinaryData();
			    dataIn.setResourceId( resourceId );
			    dataIn.setContent( document );

			    client.uploadLarge( dataIn );

			    // Test if file was uploaded in the target/resources folder
			    Assert.assertTrue( new File( resourcesFolder, resourceId ).exists() );

			    // Test if download with the resourceId returns a non null binary data.
			    BinaryData downloadedDocument = client.download( resourceId );
			    assertDocument(downloadedDocument);
			}

	public void assertDocument(BinaryData downloadedDocument) {
		Assert.assertNotNull( downloadedDocument );
	    Assert.assertNotNull( downloadedDocument.getContent() );
	}

	@Test
	public void uploadAFileInFolder() throws FileNotFoundException,
			IOException, ResourcesException {
			    String resourceId = "ZzzCustom____NewCustomSimple_" + this.getClass().getSimpleName() + ".docx";
			    JAXRSResourcesService client = getClient();
			    InputStream document =  Data.class.getResourceAsStream( "Simple.docx" ) ;

			    BinaryData dataIn = new BinaryData();
			    dataIn.setResourceId( resourceId );
			    dataIn.setContent( IOUtils.toByteArray(document) );

			    client.upload( dataIn );

			    // Test if file was uploaded in the target/resources folder
			    Assert.assertTrue( new File( resourcesFolder, "ZzzCustom/NewCustomSimple_" + this.getClass().getSimpleName()
			        + ".docx" ).exists() );

			    // Test if download with the resourceId returns a non null binary data.
			    BinaryData downloadedDocument = client.download( resourceId );
			    assertDocument(downloadedDocument);
			}

	public AbstractJAXRSResourcesServiceTest() {
		super();
	}

}
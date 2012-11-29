package fr.opensagres.xdocreport.remoting.reporting.server;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.opensagres.xdocreport.core.io.IOUtils;

public class ReportingServiceTestCase
{

    private static final int PORT = 8082;

    private static final String ROOT_ADDRESS = "http://localhost:" + PORT;

    private static final String BASE_ADDRESS = ROOT_ADDRESS;

    private String root = ReportingServiceTestCase.class.getClassLoader().getResource( "." ).getFile();

    @BeforeClass
    public static void startServer()
        throws Exception
    {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses( ReportingServiceImpl.class );
        sf.setAddress( ROOT_ADDRESS );
        Server server = sf.create();
        System.out.println( server.getEndpoint() );
    }

    @Test
    public void convertPDF()
        throws Exception
    {

        PostMethod post = new PostMethod( "http://localhost:" + PORT + "/report" );

        String ct = "multipart/mixed";
        post.setRequestHeader( "Content-Type", ct );
        Part[] parts = new Part[5];
        String fileName = "DocxProjectWithVelocityAndImageList.docx";

        parts[0] =
            new FilePart( "templateDocument", new File( root, fileName ), "application/vnd.oasis.opendocument.text",
                          "UTF-8" );
        parts[1] = new StringPart( "templateEngineKind", "Velocity" );
        
        // JSON data which must be merged with the docx template
        String jsonData = 
                "{" +
        		  "project:" +
        		  "{Name:'XDocReport', URL:'http://code.google.com/p/xdocreport'}, " +
        		  "developers:" +
        		  "[" +
        		   "{Name: 'ZERR', Mail: 'angelo.zerr@gmail.com',LastName: 'Angelo'}," +
        		   "{Name: 'Leclercq', Mail: 'pascal.leclercq@gmail.com',LastName: 'Pascals'}," + 
        		  "]" +
        		"}";
        parts[2] = new StringPart( "data", jsonData );        
        
        parts[3] = new StringPart( "dataType", "json" );
        parts[4] = new StringPart( "outFileName", "report.docx" );
        post.setRequestEntity( new MultipartRequestEntity( parts, post.getParams() ) );

        HttpClient httpclient = new HttpClient();

        try
        {
            int result = httpclient.executeMethod( post );
            Assert.assertEquals( 200, result );
            Assert.assertEquals( "attachment; filename=\"report.docx\"",
                                 post.getResponseHeader( "Content-Disposition" ).getValue() );

            byte[] convertedDocument = post.getResponseBody();
            Assert.assertNotNull( convertedDocument );

            File outFile = new File( "target/report.docx" );
            outFile.getParentFile().mkdirs();
            IOUtils.copy( new ByteArrayInputStream( convertedDocument ), new FileOutputStream( outFile ) );

        }
        finally
        {

            post.releaseConnection();
        }
    }

}

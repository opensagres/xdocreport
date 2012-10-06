package fr.opensagres.xdocreport.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.cxf.attachment.ByteDataSource;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.opensagres.xdocreport.converter.internal.BinaryFileMessageBodyReader;
import fr.opensagres.xdocreport.converter.internal.BinaryFileMessageBodyWriter;
import fr.opensagres.xdocreport.converter.internal.ConverterResourceImpl;
import fr.opensagres.xdocreport.core.io.IOUtils;

public class JAXWSResourcesServiceClientTestCase
{

    private static final int PORT = 8082;

    private static final String ROOT_ADDRESS = "http://localhost:" + PORT;

    private static final String BASE_ADDRESS = ROOT_ADDRESS;

    private String root = JAXWSResourcesServiceClientTestCase.class.getClassLoader().getResource( "." ).getFile();

    @BeforeClass
    public static void startServer()
        throws Exception
    {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses( ConverterResourceImpl.class );
        sf.setProvider( new BinaryFileMessageBodyWriter() );
        sf.setAddress( ROOT_ADDRESS );
        Server server = sf.create();
        System.out.println( server.getEndpoint() );
    }


    @Test
    public void convertPDF_OLD()
        throws Exception
    {
        List<Object> providers = new ArrayList<Object>();
        providers.add( new BinaryFileMessageBodyReader() );

        ConverterResource converterService =
            JAXRSClientFactory.create( BASE_ADDRESS, ConverterResource.class, providers );

        String fileName = "ODTCV.odt";

        FileInputStream fileInputStream = new FileInputStream( new File( root, fileName ) );
        Request request = new Request();
        request.setFilename( fileName );

        int size = fileInputStream.available();
        byte[] bytes = new byte[size];
        fileInputStream.read( bytes );
        fileInputStream.close();
        request.setContent( bytes );

        BinaryFile response = converterService.convertPDF( request );
        Assert.assertEquals( fileName + ".pdf", response.getFileName() );

        FileOutputStream out = new FileOutputStream( new File( root, response.getFileName() ) );

        IOUtils.copyLarge( response.getContent(), out );
        out.close();
        System.out.println( size );
    }

//    @Test
    public void convertPDF()
        throws Exception
    {

        List<Object> providers = new ArrayList<Object>();
        providers.add( new BinaryFileMessageBodyReader() );
        ConverterResourceImpl converterService = JAXRSClientFactory.create( BASE_ADDRESS, ConverterResourceImpl.class, providers );

        String fileName = "ODTCV.odt";

        FileInputStream fileInputStream = new FileInputStream( new File( root, fileName ) );
        Request request = new Request();
        request.setFilename( fileName );

        int size = fileInputStream.available();
        byte[] bytes = new byte[size];
        fileInputStream.read( bytes );
        fileInputStream.close();
        request.setContent( bytes );

        javax.activation.DataSource dataSource = new ByteDataSource( bytes);
        Response response = converterService.convert( "PDF", dataSource, "" );
        InputStream inputStream = (InputStream) response.getEntity();
        String responseFileName = null;
        Assert.assertEquals( fileName + ".pdf", responseFileName );

        FileOutputStream out = new FileOutputStream( new File( root, responseFileName ) );
        IOUtils.copyLarge( inputStream, out );
        out.close();
        System.out.println( size );
    }

}

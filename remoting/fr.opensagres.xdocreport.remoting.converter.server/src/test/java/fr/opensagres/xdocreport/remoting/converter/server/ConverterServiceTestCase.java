/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.xdocreport.remoting.converter.server;

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

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.core.io.IOUtils;

public class ConverterServiceTestCase
{

    private static final int PORT = 8082;

    private static final String ROOT_ADDRESS = "http://localhost:" + PORT;

    private static final String BASE_ADDRESS = ROOT_ADDRESS;

    private String root = ConverterServiceTestCase.class.getClassLoader().getResource( "." ).getFile();

    @BeforeClass
    public static void startServer()
        throws Exception
    {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses( ConverterServiceImpl.class );
        sf.setAddress( ROOT_ADDRESS );
        Server server = sf.create();
        System.out.println( server.getEndpoint() );
    }

    // @Test
    // public void convertPDF_OLD() throws Exception {
    // List<Object> providers = new ArrayList<Object>();
    // providers.add(new BinaryFileMessageBodyReader());
    //
    // ConverterService converterService = JAXRSClientFactory.create(
    // BASE_ADDRESS, ConverterService.class, providers);
    //
    // String fileName = "ODTCV.odt";
    //
    // FileInputStream fileInputStream = new FileInputStream(new File(root,
    // fileName));
    // Request request = new Request();
    // request.setFilename(fileName);
    //
    // request.setContent(IOUtils.toByteArray(fileInputStream));
    //
    // BinaryFile response = converterService.convertPDF(request);
    // Assert.assertEquals(fileName + ".pdf", response.getFileName());
    //
    // FileOutputStream out = new FileOutputStream(new File(root,
    // response.getFileName()));
    //
    // IOUtils.copyLarge(response.getContent(), out);
    // out.close();
    // System.out.println(response.getContent().available());
    // }

    @Test
    public void convertPDF()
        throws Exception
    {

        PostMethod post = new PostMethod( "http://localhost:" + PORT + "/convert" );

        String ct = "multipart/mixed";
        post.setRequestHeader( "Content-Type", ct );
        Part[] parts = new Part[4];
        String fileName = "ODTCV.odt";
        System.out.println("root : "+root);
        System.out.println("fileName : "+fileName);
        parts[0] =
            new FilePart( "document", new File( root, fileName ), "application/vnd.oasis.opendocument.text", "UTF-8" );
        parts[1] = new StringPart( "outputFormat", ConverterTypeTo.PDF.name() );
        parts[2] = new StringPart( "via", ConverterTypeVia.ODFDOM.name() );
        parts[3] = new StringPart( "download", "true" );
        post.setRequestEntity( new MultipartRequestEntity( parts, post.getParams() ) );

        HttpClient httpclient = new HttpClient();

        try
        {
            int result = httpclient.executeMethod( post );
            Assert.assertEquals( 200, result );
            Assert.assertEquals( "attachment; filename=\"ODTCV_odt.pdf\"",
                                 post.getResponseHeader( "Content-Disposition" ).getValue() );

            byte[] convertedDocument = post.getResponseBody();
            Assert.assertNotNull( convertedDocument );

            File outFile = new File( "target/ODTCV_odt.pdf" );
            outFile.getParentFile().mkdirs();
            IOUtils.copy( new ByteArrayInputStream( convertedDocument ), new FileOutputStream( outFile ) );

        }
        finally
        {

            post.releaseConnection();
        }
    }

}

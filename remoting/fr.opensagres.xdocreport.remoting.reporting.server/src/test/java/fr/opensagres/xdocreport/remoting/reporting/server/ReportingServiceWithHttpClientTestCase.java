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
package fr.opensagres.xdocreport.remoting.reporting.server;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;

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
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class ReportingServiceWithHttpClientTestCase
{

    private static final int PORT = 8082;

    private static final String ROOT_ADDRESS = "http://localhost:" + PORT;

    private static final String BASE_ADDRESS = ROOT_ADDRESS;

    private String root = ReportingServiceWithHttpClientTestCase.class.getClassLoader().getResource( "." ).getFile();

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
    public void generateReport()
        throws Exception
    {

        PostMethod post = new PostMethod( "http://localhost:" + PORT + "/report" );

        String ct = "multipart/mixed";
        post.setRequestHeader( "Content-Type", ct );
        Part[] parts = new Part[6];
        String fileName = "DocxProjectWithVelocityAndImageList.docx";

        // 1) Param [templateDocument]: input stream of the docx, odt...
        parts[0] =
            new FilePart( "templateDocument", new File( root, fileName ), "application/vnd.oasis.opendocument.text",
                          "UTF-8" );

        // 2) Param [templateEngineKind]: Velocity or Freemarker
        parts[1] = new StringPart( "templateEngineKind", "Velocity" );

        // 3) Param [metadata]: XML fields metadata
        // XML fields metadata
        // ex:<?xml version="1.0" encoding="UTF-8" standalone="yes"?><fields templateEngineKind=""
        // ><description><![CDATA[]]></description><field name="developers.Name" list="true" imageName=""
        // syntaxKind=""><description><![CDATA[]]></description></field><field name="developers.LastName" list="true"
        // imageName="" syntaxKind=""><description><![CDATA[]]></description></field><field name="developers.Mail"
        // list="true" imageName="" syntaxKind=""><description><![CDATA[]]></description></field></fields>
        FieldsMetadata metadata = new FieldsMetadata();

        // manage lazy loop for the table which display list of developers in the docx
        metadata.addFieldAsList( "developers.Name" );
        metadata.addFieldAsList( "developers.LastName" );
        metadata.addFieldAsList( "developers.Mail" );

        StringWriter xml = new StringWriter();
        metadata.saveXML( xml );
        parts[2] = new StringPart( "metadata", xml.toString() );

        // 4) Param [data]: JSON data which must be merged with the docx template
        String jsonData =
            "{" + "project:" + "{Name:'XDocReport', URL:'http://code.google.com/p/xdocreport'}, " + "developers:" + "["
                + "{Name: 'ZERR', Mail: 'angelo.zerr@gmail.com',LastName: 'Angelo'},"
                + "{Name: 'Leclercq', Mail: 'pascal.leclercq@gmail.com',LastName: 'Pascal'}" + "]" + "}";
        parts[3] = new StringPart( "data", jsonData );

        // 4) Param [dataType]: data type
        parts[4] = new StringPart( "dataType", "json" );
        // 5) Param [outFileName]: output file name
        parts[5] = new StringPart( "outFileName", "report.docx" );
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

    @Test
    public void generateReportandConvertToPDF()
        throws Exception
    {

        PostMethod post = new PostMethod( "http://localhost:" + PORT + "/report" );

        String ct = "multipart/mixed";
        post.setRequestHeader( "Content-Type", ct );
        Part[] parts = new Part[7];
        String fileName = "DocxProjectWithVelocityAndImageList.docx";

        // 1) Param [templateDocument]: input stream of the docx, odt...
        parts[0] =
            new FilePart( "templateDocument", new File( root, fileName ), "application/vnd.oasis.opendocument.text",
                          "UTF-8" );

        // 2) Param [templateEngineKind]: Velocity or Freemarker
        parts[1] = new StringPart( "templateEngineKind", "Velocity" );

        // 3) Param [metadata]: XML fields metadata
        // XML fields metadata
        // ex:<?xml version="1.0" encoding="UTF-8" standalone="yes"?><fields templateEngineKind=""
        // ><description><![CDATA[]]></description><field name="developers.Name" list="true" imageName=""
        // syntaxKind=""><description><![CDATA[]]></description></field><field name="developers.LastName" list="true"
        // imageName="" syntaxKind=""><description><![CDATA[]]></description></field><field name="developers.Mail"
        // list="true" imageName="" syntaxKind=""><description><![CDATA[]]></description></field></fields>
        FieldsMetadata metadata = new FieldsMetadata();

        // manage lazy loop for the table which display list of developers in the docx
        metadata.addFieldAsList( "developers.Name" );
        metadata.addFieldAsList( "developers.LastName" );
        metadata.addFieldAsList( "developers.Mail" );

        StringWriter xml = new StringWriter();
        metadata.saveXML( xml );
        parts[2] = new StringPart( "metadata", xml.toString() );

        // 4) Param [data]: JSON data which must be merged with the docx template
        String jsonData =
            "{" + "project:" + "{Name:'XDocReport', URL:'http://code.google.com/p/xdocreport'}, " + "developers:" + "["
                + "{Name: 'ZERR', Mail: 'angelo.zerr@gmail.com',LastName: 'Angelo'},"
                + "{Name: 'Leclercq', Mail: 'pascal.leclercq@gmail.com',LastName: 'Pascal'}" + "]" + "}";
        parts[3] = new StringPart( "data", jsonData );

        // 4) Param [dataType]: data type
        parts[4] = new StringPart( "dataType", "json" );
        // 5) Param [outFileName]: output file name
        parts[5] = new StringPart( "outFileName", "report.pdf" );
        parts[6] = new StringPart( "outFormat", "PDF" );
        
        post.setRequestEntity( new MultipartRequestEntity( parts, post.getParams() ) );

        
        
        HttpClient httpclient = new HttpClient();

        try
        {
            int result = httpclient.executeMethod( post );
            Assert.assertEquals( 200, result );
            Assert.assertEquals( "attachment; filename=\"report.pdf\"",
                                 post.getResponseHeader( "Content-Disposition" ).getValue() );

            byte[] convertedDocument = post.getResponseBody();
            Assert.assertNotNull( convertedDocument );

            File outFile = new File( "target/report.pdf" );
            outFile.getParentFile().mkdirs();
            IOUtils.copy( new ByteArrayInputStream( convertedDocument ), new FileOutputStream( outFile ) );

        }
        finally
        {

            post.releaseConnection();
        }
    }

}

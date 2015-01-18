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
package fr.opensagres.xdocreport.document.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.domain.DataContext;
import fr.opensagres.xdocreport.document.internal.XDocReportServiceImpl;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class XDocReportServiceProcessTestCase
    extends TestCase
{

    public void testProcessReportWithoutCache()
        throws Exception
    {

        // 1) Prepare Parameters
        byte[] document =
            getDocumentAsByteArray( XDocReportServiceProcessTestCase.class.getResourceAsStream( "ODTHelloWordWithVelocity.odt" ) );
        FieldsMetadata fieldsMetadata = null;
        String templateEngineId = TemplateEngineKind.Velocity.name();

        // Data Context parameters
        List<DataContext> dataContext = new ArrayList<DataContext>();
        DataContext data = new DataContext();
        data.setKey( "name" );
        data.setValue( "world" );
        dataContext.add( data );

        Options options = null;

        // 2) Call process which returns byte array
        byte[] result =
            XDocReportServiceImpl.INSTANCE.process( document, fieldsMetadata, templateEngineId, dataContext, options );
        assertNotNull( result );

        // 3) Save generated report
        File outFile = new File( "target/ODTHelloWordWithVelocity_WithoutCache.odt" );
        FileOutputStream out = new FileOutputStream( outFile );
        try
        {
            out.write( result );
            out.flush();
        }
        finally
        {
            out.close();
        }
    }

    public void testProcessReportWithCache()
        throws Exception
    {

        // 1) Prepare Parameters
        String reportId = "MyId";
        byte[] document =
            getDocumentAsByteArray( XDocReportServiceProcessTestCase.class.getResourceAsStream( "ODTHelloWordWithVelocity.odt" ) );
        FieldsMetadata fieldsMetadata = null;
        String templateEngineId = TemplateEngineKind.Velocity.name();

        // Data Context parameters
        List<DataContext> dataContext = new ArrayList<DataContext>();
        DataContext data = new DataContext();
        data.setKey( "name" );
        data.setValue( "world" );
        dataContext.add( data );

        Options options = null;

        // 2) Register the report
        XDocReportServiceImpl.INSTANCE.registerReport( reportId, document, fieldsMetadata, templateEngineId );

        // 3) Call process which returns byte array
        byte[] result = XDocReportServiceImpl.INSTANCE.process( reportId, dataContext, options );
        assertNotNull( result );

        // 3) Save generated report
        File outFile = new File( "target/ODTHelloWordWithVelocity_WithCache.odt" );
        FileOutputStream out = new FileOutputStream( outFile );
        try
        {
            out.write( result );
            out.flush();
        }
        finally
        {
            out.close();
        }
    }

    protected byte[] getDocumentAsByteArray( InputStream in )
        throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy( in, out );
        return out.toByteArray();
    }

}

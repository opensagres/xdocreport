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
package fr.opensagres.xdocreport.examples.odt;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.odt.ODTConstants;
import fr.opensagres.xdocreport.document.odt.ODTReport;
import fr.opensagres.xdocreport.document.odt.ODTUtils;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

/**
 * Example with Open Office ODT which contains the content Hello !$name. Merge with Velocity template engine will
 * replace this cell with Hello world!
 */
public class ODTHelloWordWithVelocityTestCase
{

    @Before
    public void onBefore()
    {
        // Clear report cached in the registry before execute a test.
        XDocReportRegistry.getRegistry().clear();
    }

    @Test
    public void testOne()
        throws IOException, XDocReportException
    {

        // 1) Load ODT file by filling Velocity template engine and cache it
        // to the registry
        IXDocReport report =
            XDocReportRegistry.getRegistry().loadReport( ODTHelloWordWithVelocityTestCase.class.getResourceAsStream( "ODTHelloWordWithVelocity.odt" ),
                                                         TemplateEngineKind.Velocity );

        Assert.assertTrue( "This is a odt file, ODTReport implementation should have been resolved....",
                           report instanceof ODTReport );

        // 2) Create context Java model
        IContext context = report.createContext();
        context.put( "name", "world" );

        // 3) Merge Java model with the ODT
        File out = new File( "target" );
        out.mkdirs();
        File file = new File( out, "ODTHelloWordWithVelocity.odt" );
        report.process( context, new FileOutputStream( file ) );

    }

    @Test
    public void loadNonExistingReport()
    {

        try
        {
            XDocArchive.readZip( ODTHelloWordWithVelocityTestCase.class.getResourceAsStream( "not_found" ) );
            fail( "'not_found' does not exists " );
        }
        catch ( IOException e )
        {
            // success
        }
    }

    @Test
    public void loadExistingODTReport()
    {
        String fileName = "ODTHelloWordWithVelocity.odt";
        // 1) Load ODT file by filling Velocity template engine and cache it
        // to the registry
        IXDocReport report = null;
        try
        {

            report =
                XDocReportRegistry.getRegistry().loadReport( ODTHelloWordWithVelocityTestCase.class.getResourceAsStream( fileName ),
                                                             TemplateEngineKind.Velocity );

        }
        catch ( Exception e )
        {
            fail( "Unable to load " + fileName + " " + e.getMessage() );
        }

        assertThat( "This is a odt file, ODTReport implementation should have been resolved....", report,
                    instanceOf( ODTReport.class ) );

    }

    @Test
    public void loadReportWithId()
    {
        String fileName = "ODTHelloWordWithVelocity.odt";

        IXDocReport report = null;
        try
        {

            report =
                XDocReportRegistry.getRegistry().loadReport( ODTHelloWordWithVelocityTestCase.class.getResourceAsStream( fileName ),
                                                             fileName, TemplateEngineKind.Velocity );

        }
        catch ( Exception e )
        {
            fail( "Unable to load " + fileName + " " + e.getMessage() );
        }

        assertEquals( fileName, report.getId() );
        assertEquals( report, XDocReportRegistry.getRegistry().getReport( fileName ) );
    }

    @Test
    public void cannotRegisterTwoTimeSameId()
    {
        String fileName = "ODTHelloWordWithVelocity.odt";

        IXDocReport report = null;
        try
        {

            report =
                XDocReportRegistry.getRegistry().loadReport( ODTHelloWordWithVelocityTestCase.class.getResourceAsStream( fileName ),
                                                             "id", TemplateEngineKind.Velocity );

            XDocReportRegistry.getRegistry().loadReport( ODTHelloWordWithVelocityTestCase.class.getResourceAsStream( fileName ),
                                                         "id", TemplateEngineKind.Velocity );
            fail( "cannot register 2 reports with the same id" );
        }
        catch ( Exception e )
        {
            // success
        }

    }

    @Test
    public void checkXDocArchiveContent()
        throws IOException, XDocReportException
    {
        String fileName = "ODTHelloWordWithVelocity.odt";

        IXDocReport report = null;

        report =
            XDocReportRegistry.getRegistry().loadReport( ODTHelloWordWithVelocityTestCase.class.getResourceAsStream( fileName ),
                                                         TemplateEngineKind.Velocity );

        XDocArchive archive = report.getPreprocessedDocumentArchive();

        assertNotNull( archive );
        assertTrue( archive.hasEntry( ODTConstants.CONTENT_XML_ENTRY ) );
        assertTrue( ODTUtils.isODT( archive ) );

        Reader reader = archive.getEntryReader( ODTConstants.CONTENT_XML_ENTRY );
        StringWriter writer = new StringWriter();
        IOUtils.copy( reader, writer );

        String contentAsString = writer.toString();
        // System.out.println(contentAsString);
        assertTrue( contentAsString.contains( "$name" ) );
    }
}

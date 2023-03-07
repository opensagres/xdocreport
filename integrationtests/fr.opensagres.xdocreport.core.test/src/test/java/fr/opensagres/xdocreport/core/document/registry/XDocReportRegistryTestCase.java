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
package fr.opensagres.xdocreport.core.document.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;

import org.junit.Test;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.odt.ODTReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.FieldExtractor;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerTemplateEngine;
import fr.opensagres.xdocreport.template.velocity.internal.VelocityTemplateEngine;

/**
 * Test case for {@link XDocReportRegistry}.
 */
public class XDocReportRegistryTestCase
{

    @Test
    public void testReportNoExists()
        throws Exception
    {
        // Tests no existence with XDocReportRegistry#existsReport
        boolean exists = XDocReportRegistry.getRegistry().existsReport( "Unknown" );
        assertFalse( "IXDocReport with id='Unknown' must not exists", exists );

        // Tests no existence with XDocReportRegistry#getReport
        IXDocReport report = XDocReportRegistry.getRegistry().getReport( "Unknown" );
        assertNull( "IXDocReport with id='Unknown' must be null.", report );
    }

    @Test
    public void testRegisterAndUnRegisterReportWithDefaultId()
    {
        Exception ex = null;
        try
        {
            IXDocReport report =
                XDocReportRegistry.getRegistry().loadReport( XDocReportRegistryTestCase.class.getResourceAsStream( "ODTHelloWordWithFreemarker.odt" ), true );
            // Report is created
            assertNotNull( "Report must be not null", report );

            assertTrue( "This is a odt file, ODTReport implementation should have been resolved....",
                        report instanceof ODTReport );

            // Report id by default is toString
            assertEquals( "Report id", report.toString(), report.getId() );

            // Search report with id in the registry
            String reportId = report.getId();
            IXDocReport report2 = XDocReportRegistry.getRegistry().getReport( reportId );
            assertNotNull( "Report from registry must be not null", report2 );
            assertEquals( "Report from registry is not equals", report, report2 );

            // UnRegister the report
            XDocReportRegistry.getRegistry().unregisterReport( report );
            boolean exists = XDocReportRegistry.getRegistry().existsReport( report.getId() );
            assertFalse( "IXDocReport must not exists", exists );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            ex = e;
        }
        assertNull( "Error while loading report", ex );
    }

    @Test
    public void testRegisterAndUnRegisterReportWithGivenId()
    {
        Exception ex = null;
        try
        {
            String reportId = "MyReportId";
            IXDocReport report =
                XDocReportRegistry.getRegistry().loadReport( XDocReportRegistryTestCase.class.getResourceAsStream( "ODTHelloWordWithFreemarker.odt" ),
                                                             reportId );
            // Report is created
            assertNotNull( "Report must be not null", report );

            assertTrue( "This is a odt file, ODTReport implementation should have been resolved....",
                        report instanceof ODTReport );

            // Report id by default is the given report id
            assertEquals( "Report id", reportId, report.getId() );

            // Search report with id in the registry
            IXDocReport report2 = XDocReportRegistry.getRegistry().getReport( reportId );
            assertNotNull( "Report from registry must be not null", report2 );
            assertEquals( "Report from registry is not equals", report, report2 );

            // UnRegister the report
            XDocReportRegistry.getRegistry().unregisterReport( report );
            boolean exists = XDocReportRegistry.getRegistry().existsReport( reportId );
            assertFalse( "IXDocReport must not exists", exists );

        }
        catch ( Exception e )
        {
            ex = e;
        }
        assertNull( "Error while loading report", ex );
    }

    @Test
    public void testWithFreemarker()
    {
        Exception ex = null;
        try
        {
            // Load report and set Freemarker as template engine
            String reportId = "MyFreemarkerReportId";
            IXDocReport report =
                XDocReportRegistry.getRegistry().loadReport( XDocReportRegistryTestCase.class.getResourceAsStream( "ODTHelloWordWithFreemarker.odt" ),
                                                             reportId, TemplateEngineKind.Freemarker );

            // test ODT
            assertTrue( "This is a odt file, ODTReport implementation should have been resolved....",
                        report instanceof ODTReport );

            // Test Freemarker
            assertTrue( "Template engine must be Freemarker",
                        report.getTemplateEngine() instanceof FreemarkerTemplateEngine );

            // Process template engine with context
            IContext context = report.createContext();
            context.put( "name", "world" );
            String contentXML = processTemplateEngineForEntry( report, context, "content.xml" );
            assertNotNull( contentXML );
            assertTrue( contentXML.contains( ">Hello world!<" ) );

        }
        catch ( Exception e )
        {
            ex = e;
        }
        assertNull( "Error while loading report", ex );
    }

    @Test
    public void testWithFreemarkerAndRefreshSourceODT()
    {
        Exception ex = null;
        try
        {
            // Load report and set Freemarker as template engine
            String reportId = "MyFreemarkerReportAndRefreshSourceODTId";
            IXDocReport report =
                XDocReportRegistry.getRegistry().loadReport( XDocReportRegistryTestCase.class.getResourceAsStream( "ODTHelloWordWithFreemarker.odt" ),
                                                             reportId, TemplateEngineKind.Freemarker );

            // test ODT
            assertTrue( "This is a odt file, ODTReport implementation should have been resolved....",
                        report instanceof ODTReport );

            // Test Freemarker
            assertTrue( "Template engine must be Freemarker",
                        report.getTemplateEngine() instanceof FreemarkerTemplateEngine );

            // Process template engine with context
            IContext context = report.createContext();
            context.put( "name", "world" );
            String contentXML = processTemplateEngineForEntry( report, context, "content.xml" );
            assertNotNull( contentXML );
            assertTrue( contentXML.contains( ">Hello world!<" ) );

            // Refresh report with new content
            report.load( XDocReportRegistryTestCase.class.getResourceAsStream( "ODTHelloWordWithFreemarker2.odt" ) );
            // Thread.sleep(1000);
            String newContentXML = processTemplateEngineForEntry( report, context, "content.xml" );
            assertNotNull( newContentXML );
            assertTrue( "content XML must contains '>Hello world! You are welcome!<'",
                        newContentXML.contains( ">Hello world! You are welcome!<" ) );

        }
        catch ( Exception e )
        {
            ex = e;
            e.printStackTrace();
        }
        assertNull( "Error while loading report", ex );
    }

    @Test
    public void testWithVelocity()
    {
        Exception ex = null;
        try
        {
            // Load report and set Velocity as template engine
            String reportId = "MyVelocityReportId";
            IXDocReport report =
                XDocReportRegistry.getRegistry().loadReport( XDocReportRegistryTestCase.class.getResourceAsStream( "ODTHelloWordWithFreemarker.odt" ),
                                                             reportId, TemplateEngineKind.Velocity );

            // test ODT
            assertTrue( "This is a odt file, ODTReport implementation should have been resolved....",
                        report instanceof ODTReport );

            // Test Freemarker
            assertTrue( "Template engine must be Velocity",
                        report.getTemplateEngine() instanceof VelocityTemplateEngine );

            // Process template engine with context
            IContext context = report.createContext();
            context.put( "name", "world" );
            String contentXML = processTemplateEngineForEntry( report, context, "content.xml" );
            assertNotNull( contentXML );
            assertTrue( contentXML.contains( ">Hello world!<" ) );

        }
        catch ( Exception e )
        {
            ex = e;
        }
        assertNull( "Error while loading report", ex );
    }

    @Test
    public void testWithVelocityAndRefreshSourceODT()
    {
        Exception ex = null;
        try
        {
            // Load report and set Velocity as template engine
            String reportId = "MyVelocityReportAndRefreshSourceODTId";
            IXDocReport report =
                XDocReportRegistry.getRegistry().loadReport( XDocReportRegistryTestCase.class.getResourceAsStream( "ODTHelloWordWithVelocity.odt" ),
                                                             reportId, TemplateEngineKind.Velocity );

            // test ODT
            assertTrue( "This is a odt file, ODTReport implementation should have been resolved....",
                        report instanceof ODTReport );

            // Test Velocity
            assertTrue( "Template engine must be Velocity",
                        report.getTemplateEngine() instanceof VelocityTemplateEngine );

            // Process template engine with context
            IContext context = report.createContext();
            context.put( "name", "world" );
            String contentXML = processTemplateEngineForEntry( report, context, "content.xml" );
            assertNotNull( contentXML );
            assertTrue( contentXML.contains( ">Hello world!<" ) );

            // Refresh report with new content
            report.load( XDocReportRegistryTestCase.class.getResourceAsStream( "ODTHelloWordWithVelocity2.odt" ) );

            // Sleep with 1sec to wait that Velocity cache refresh.
            // TODO : I don't know how manage that better
            // "report.resource.loader.modification_check_interval" (please see
            // VelocityTemplayteEngine) can accept
            // ONLY second interval to refresh cache???
            // Thread.sleep(1000);

            String newContentXML = processTemplateEngineForEntry( report, context, "content.xml" );
            assertNotNull( newContentXML );
            assertTrue( newContentXML.contains( ">Hello world!" ) );
            // You are welcome!<
        }
        catch ( Exception e )
        {
            ex = e;
            e.printStackTrace();
        }
        assertNull( "Error while loading report", ex );
    }

    @Test
    public void testWithLoadingWithBOMMarkup()
    {
        Exception ex = null;
        try
        {
            IXDocReport report =
                XDocReportRegistry.getRegistry().loadReport( XDocReportRegistryTestCase.class.getResourceAsStream( "bom.docx" ),
                                                             TemplateEngineKind.Velocity );
            report.extractFields(new FieldsExtractor<FieldExtractor>());

        }
        catch ( Exception e )
        {
            ex = e;
            e.printStackTrace();
        }
        assertNull( "Error while loading report", ex );

    }
    
    private String processTemplateEngineForEntry( IXDocReport report, IContext context, String entryName )
        throws IOException, XDocReportException
    {
        File tempFile = null;
        try
        {
            tempFile = File.createTempFile( "document", ".zip" );
            report.process( context, new FileOutputStream( tempFile ) );
            XDocArchive documentArchive = XDocArchive.readZip( new FileInputStream( tempFile ) );

            Reader contentXMLreader = documentArchive.getEntryReader( entryName );
            return IOUtils.toString( contentXMLreader );

        }
        finally
        {
            if ( tempFile != null )
            {
                tempFile.delete();
            }
        }
    }
}

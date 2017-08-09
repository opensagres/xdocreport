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
package fr.opensagres.xdocreport.document.odt.discovery;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.odt.ODTReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;

public class ODTReportFactoryDiscoveryTestCase
    extends TestCase
{

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

    public void testRegisterAndUnRegisterReportWithDefaultId()
    {
        Exception ex = null;
        try
        {
            IXDocReport report =
                XDocReportRegistry.getRegistry().loadReport( ODTReportFactoryDiscoveryTestCase.class.getResourceAsStream( "ODTHelloWordWithFreemarker.odt" ), true );
            // Report is created
            assertNotNull( "Report must ne not null", report );

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

    public void testRegisterAndUnRegisterReportWithGivenId()
    {
        Exception ex = null;
        try
        {
            String reportId = "MyReportId";
            IXDocReport report =
                XDocReportRegistry.getRegistry().loadReport( ODTReportFactoryDiscoveryTestCase.class.getResourceAsStream( "ODTHelloWordWithFreemarker.odt" ),
                                                             reportId );
            // Report is created
            assertNotNull( "Report must ne not null", report );

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
}

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
package fr.opensagres.xdocreport.osgi.integrationtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.when;

import java.io.File;
import java.util.Collection;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Customizer;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.BundleContext;

import fr.opensagres.xdocreport.document.discovery.IXDocReportFactoryDiscovery;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;

@RunWith( PaxExam.class )
@ExamReactorStrategy(PerMethod.class)
public class DocumentTest
{

    @Inject
    BundleContext bundleContext = null;

    @Configuration
    public static Option[] configure()
    {
        return options(
        		allowCustomLocalRepository(),
        		CoreOptions.junitBundles(),
        		CoreOptions.cleanCaches(),
                        //
                        // PaxRunnerOptions.vmOption("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5006"),
                        // equinox(),

                        // logProfile(),
                        // this is how you set the default log level when using pax
                        // logging (logProfile)
                        systemProperty( "org.ops4j.pax.logging.DefaultServiceLog.level" ).value( "WARN" ),
                        mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.core" ).versionAsInProject(),
                        // converter api
                        mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.converter" ).versionAsInProject(),

                        // template API
                        mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.template" ).versionAsInProject(),
                        // document API
                        mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.document" ).versionAsInProject().noStart(),

                        // document Impl
                        mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.document.docx" ).versionAsInProject().noStart(),
                        // document Impl
                        mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.document.odt" ).versionAsInProject().noStart(),

                        mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.document.odp" ).versionAsInProject().noStart(),
                        mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.document.ods" ).versionAsInProject().noStart(),
                        mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.document.pptx" ).versionAsInProject().noStart(),
                        new Customizer()
                        {

                            @Override
                            public void customizeEnvironment( File workingFolder )
                            {

                                System.out.println( "Hello World: " + workingFolder.getAbsolutePath() );
                            }
                        } );
    }

    @Test
    public void countDiscoveries()
        throws Exception
    {

        Collection<IXDocReportFactoryDiscovery> discoveries =
            XDocReportRegistry.getRegistry().getReportFactoryDiscoveries();
        assertNotNull( discoveries );

        assertEquals( 5, discoveries.size() );
    }
    private static Option allowCustomLocalRepository() {
		//see: https://ops4j1.jira.com/browse/PAXEXAM-543
		String localRepo = System.getProperty("maven.repo.local", "");
		return when(localRepo.length() > 0).useOptions(
		    systemProperty("org.ops4j.pax.url.mvn.localRepository").value(localRepo)
		);
	}
    
   
}

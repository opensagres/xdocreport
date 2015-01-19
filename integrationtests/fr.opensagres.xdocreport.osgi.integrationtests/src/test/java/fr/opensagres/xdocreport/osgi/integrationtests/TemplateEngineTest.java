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
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;

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

import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.registry.TemplateEngineRegistry;

@RunWith( PaxExam.class )
@ExamReactorStrategy(PerMethod.class)
public class TemplateEngineTest
{

    @Inject
    BundleContext bundleContext = null;

    @Configuration
    public static Option[] configure()
    {

        return options(
        		CoreOptions.junitBundles(),
                        //
                        // PaxRunnerOptions.vmOption("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5006"),
                        // equinox(),
        				allowCustomLocalRepository(),
                        // logProfile(),
                        // this is how you set the default log level when using pax
                        // logging (logProfile)
                        systemProperty( "org.ops4j.pax.logging.DefaultServiceLog.level" ).value( "WARN" ),
                        mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.core" ).versionAsInProject(),
                        // template API
                        mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.template" ).versionAsInProject(),

                        // template fragments
                        mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.template.freemarker" ).versionAsInProject().noStart(),
                        mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.template.velocity" ).versionAsInProject().noStart(),
                        mavenBundle( "org.freemarker", "freemarker", "2.3.20" ) ,
                        mavenBundle( "commons-collections", "commons-collections", "3.2.1" ),
                        mavenBundle( "commons-lang", "commons-lang", "2.4" ),
                        mavenBundle( "org.apache.velocity", "velocity", "1.7" ),
                        wrappedBundle( mavenBundle( "oro", "oro", "2.0.8" ) ), new Customizer()
                        {

                            @Override
                            public void customizeEnvironment( File workingFolder )
                            {

                                System.out.println( "Hello World: " + workingFolder.getAbsolutePath() );
                            }
                        } );
    }

    @Test
    public void templateEngineRegistry()
        throws Exception
    {
        // FIXME PLQ
        // // Test if converter is not null
        Collection<ITemplateEngine> templateEngines = TemplateEngineRegistry.getRegistry().getTemplateEngines();

        assertNotNull( templateEngines );
        assertEquals( 2, templateEngines.size() );

        Collection<String> kinds = TemplateEngineRegistry.getRegistry().getTemplateEngineKinds();
        assertNotNull( kinds );
        assertEquals( 2, kinds.size() );

    }

    private static Option allowCustomLocalRepository() {
		//see: https://ops4j1.jira.com/browse/PAXEXAM-543
		String localRepo = System.getProperty("maven.repo.local", "");
		return when(localRepo.length() > 0).useOptions(
		    systemProperty("org.ops4j.pax.url.mvn.localRepository").value(localRepo)
		);
	}
}

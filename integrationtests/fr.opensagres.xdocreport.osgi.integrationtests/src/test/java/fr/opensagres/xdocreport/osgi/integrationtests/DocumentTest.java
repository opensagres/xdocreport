/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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

import java.io.File;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Customizer;
import org.ops4j.pax.exam.Inject;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.BundleContext;

import fr.opensagres.xdocreport.document.discovery.IXDocReportFactoryDiscovery;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;

@RunWith(JUnit4TestRunner.class)
public class DocumentTest {

	@Inject
	BundleContext bundleContext = null;

	@Configuration
	public static Option[] configure() {
		// XXX pass -Dproject.version=XXX int the IDE, otherwise maven will
		// inject Its version
		final String projectVersion = System.getProperty("project.version");
		return options(

				//
				// PaxRunnerOptions.vmOption("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5006"),
				// equinox(),

				// logProfile(),
				// this is how you set the default log level when using pax
				// logging (logProfile)
				systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level")
						.value("WARN"),
				mavenBundle().groupId("fr.opensagres.xdocreport")
						.artifactId("fr.opensagres.xdocreport.core")
						.version(projectVersion),
				// converter api
				mavenBundle().groupId("fr.opensagres.xdocreport")
						.artifactId("fr.opensagres.xdocreport.converter")
						.version(projectVersion),

				// template API
				mavenBundle().groupId("fr.opensagres.xdocreport")
						.artifactId("fr.opensagres.xdocreport.template")
						.version(projectVersion),
				// document API
				mavenBundle().groupId("fr.opensagres.xdocreport")
						.artifactId("fr.opensagres.xdocreport.document")
						.version(projectVersion),

				// document Impl
				mavenBundle().groupId("fr.opensagres.xdocreport")
						.artifactId("fr.opensagres.xdocreport.document.docx")
						.version(projectVersion).noStart(),
				// document Impl
				mavenBundle().groupId("fr.opensagres.xdocreport")
						.artifactId("fr.opensagres.xdocreport.document.odt")
						.version(projectVersion).noStart(),
				mavenBundle().groupId("fr.opensagres.xdocreport")
						.artifactId("fr.opensagres.xdocreport.document.odp")
						.version(projectVersion).noStart(),
				mavenBundle().groupId("fr.opensagres.xdocreport")
						.artifactId("fr.opensagres.xdocreport.document.ods")
						.version(projectVersion).noStart(),
				mavenBundle().groupId("fr.opensagres.xdocreport")
						.artifactId("fr.opensagres.xdocreport.document.pptx")
						.version(projectVersion).noStart(), new Customizer() {

					@Override
					public void customizeEnvironment(File workingFolder) {
						System.out.println("Hello World: "
								+ workingFolder.getAbsolutePath());
					}
				});
	}

	@Test
	public void countDiscoveries() throws Exception {
		
	
			Collection<IXDocReportFactoryDiscovery> discoveries = XDocReportRegistry
					.getRegistry().getReportFactoryDiscoveries();
			assertNotNull(discoveries);
		
			assertEquals(5, discoveries.size());
		
		
	}
}

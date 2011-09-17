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
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;

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

import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.registry.TemplateEngineRegistry;

@RunWith(JUnit4TestRunner.class)
public class TemplateTest {

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
				mavenBundle().groupId("fr.opensagres")
						.artifactId("fr.opensagres.xdocreport.core")
						.version(projectVersion),
				// template API
				mavenBundle().groupId("fr.opensagres")
						.artifactId("fr.opensagres.xdocreport.template")
						.version(projectVersion),

				// template fragments
				mavenBundle()
						.groupId("fr.opensagres")
						.artifactId(
								"fr.opensagres.xdocreport.template.freemarker")
						.version(projectVersion).noStart(),
				mavenBundle()
						.groupId("fr.opensagres")
						.artifactId(
								"fr.opensagres.xdocreport.template.velocity")
						.version(projectVersion).noStart(),
				wrappedBundle(mavenBundle().groupId("org.freemarker")
						.artifactId("freemarker").version("2.3.16")),
				mavenBundle().groupId("commons-collections")
						.artifactId("commons-collections").version("3.2.1"),
				mavenBundle().groupId("commons-lang")
						.artifactId("commons-lang").version("2.4"),
				mavenBundle().groupId("org.apache.velocity")
						.artifactId("velocity").version("1.7"),
						wrappedBundle(mavenBundle().groupId("oro")
						.artifactId("oro").version("2.0.8")),		
				new Customizer() {

					@Override
					public void customizeEnvironment(File workingFolder) {

						System.out.println("Hello World: "
								+ workingFolder.getAbsolutePath());
					}
				});
	}

	@Test
	public void templateEngineRegistry() throws Exception {
		// FIXME PLQ
		// // Test if converter is not null
		Collection<ITemplateEngine> templateEngines = TemplateEngineRegistry
				.getRegistry().getTemplateEngines();

		assertNotNull(templateEngines);
		assertEquals(2, templateEngines.size());

		Collection<String> kinds = TemplateEngineRegistry.getRegistry()
				.getTemplateEngineKinds();
		assertNotNull(kinds);
		assertEquals(2, kinds.size());

	}

}

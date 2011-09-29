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

import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Customizer;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;

import fr.opensagres.xdocreport.converter.ConverterRegistry;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.document.DocumentKind;

@RunWith(JUnit4TestRunner.class)
public class ConverterTest {

	/*
	 * You can configure all kinds of stuff. You will learn about most of it on
	 * the project wiki. Here's a typical example: - add a log service to your
	 * runtime - add custom bundles via the mvn handler - add additional, non
	 * bundlized dependencies. (wrapping on the fly)
	 */
	@Configuration
	public static Option[] configure() {

		// XXX pass -Dproject.version=XXX int the IDE, otherwise maven will
		// inject Its version
		final String projectVersion = System.getProperty("project.version");

		return options(

				// uncomment for "remote debugging"
				// PaxRunnerOptions.vmOption("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5006"),
				// equinox(),

				systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level")
						.value("DEBUG"),

				mavenBundle().groupId("fr.opensagres.xdocreport")
						.artifactId("fr.opensagres.xdocreport.core")
						.version(projectVersion),
				// converter API
				mavenBundle().groupId("fr.opensagres.xdocreport")
						.artifactId("fr.opensagres.xdocreport.converter")
						.version(projectVersion),
				// converter Iml
				mavenBundle()
						.groupId("fr.opensagres.xdocreport")
						.artifactId(
								"fr.opensagres.xdocreport.converter.odt.odfdom")
						.version(projectVersion).noStart(),

				mavenBundle()
						.groupId("fr.opensagres.xdocreport")
						.artifactId(
								"fr.opensagres.xdocreport.converter.docx.xwpf")
						.version(projectVersion).noStart(),

				// 3rd parties extensions...

				mavenBundle().groupId("fr.opensagres.xdocreport")
						.artifactId("org.odftoolkit.odfdom.converter")
						.version(projectVersion),
				mavenBundle().groupId("fr.opensagres.xdocreport")
						.artifactId("fr.opensagres.xdocreport.itext.extension")
						.version(projectVersion),

				mavenBundle().groupId("fr.opensagres.xdocreport")
						.artifactId("org.apache.poi.xwpf.converter")
						.version(projectVersion),
				// 3rd parties modules...
				wrappedBundle(mavenBundle().groupId("org.apache.poi")
						.artifactId("poi").version("3.7")),
				wrappedBundle(mavenBundle().groupId("org.apache.poi")
						.artifactId("poi-ooxml").version("3.7")),
				wrappedBundle(mavenBundle().groupId("org.apache.xmlbeans")
						.artifactId("xmlbeans").version("2.3.0")),

				wrappedBundle(mavenBundle().groupId("org.apache.poi")
						.artifactId("ooxml-schemas").version("1.1")),

				wrappedBundle(mavenBundle().groupId("org.odftoolkit")
						.artifactId("odfdom-java").version("0.8.7")),

				wrappedBundle(mavenBundle().groupId("com.lowagie")
						.artifactId("itext").version("2.1.7")),

				wrappedBundle(mavenBundle().groupId("stax")
						.artifactId("stax-api").version("1.0.1")),

				new Customizer() {

					@Override
					public void customizeEnvironment(File workingFolder) {
						System.out.println("Hello World: "
								+ workingFolder.getAbsolutePath());
					}
				});
	}

	@Test
	public void findFromODTToPDFViaITextConverter() throws Exception {
		try {

			Options o = Options.getFrom(DocumentKind.ODT)
					.to(ConverterTypeTo.PDF).via(ConverterTypeVia.ITEXT);

			// Test if converter is not null
			IConverter converter = ConverterRegistry.getRegistry()
					.getConverter(o);

			assertNotNull(converter);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void findFromDOCXToPDFViaITextConverter() throws Exception {
		try {
			Options o = Options.getFrom(DocumentKind.DOCX)
					.to(ConverterTypeTo.PDF).via(ConverterTypeVia.ITEXT);

			// Test if converter is not null
			IConverter converter = ConverterRegistry.getRegistry()
					.getConverter(o);

			assertNotNull(converter);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// @Ignore
	// @Test
	// public void findFromODTToPDFViaITextConverterViaRegistry(
	// BundleContext context) throws Exception {
	// Options o = Options.getFrom(DocumentKind.ODT).to(ConverterTypeTo.PDF)
	// .via(ConverterTypeVia.ITEXT);
	// // context.createFilter(arg0)
	// // Test if converter is not null
	// // context.getServiceReferences(arg0, arg1)
	// // ServiceReference reference =
	// // context.getServiceReference(IConverter.class.getName());
	// // TODO: Filter filter = new Filter("");
	// // ServiceTracker tracker = new ServiceTracker(context, filter,
	// // customizer);
	// // IConverter converter=reference.
	// // assertNotNull(converter);
	//
	// }
}

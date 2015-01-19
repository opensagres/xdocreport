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
package fr.opensagres.xdocreport.osgi.integrationtests.remoting;

import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.when;

import java.io.IOException;
import java.net.Socket;

import javax.inject.Inject;

import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.PaxExamRuntime;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.BundleContext;

import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.JAXRSResourcesService;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class RemotingTest {

	@Configuration
	public Option[] config() {
		return 

				options(
						CoreOptions.junitBundles(),
						systemProperty("org.osgi.service.http.port").value(
								Integer.toString(PORT)),
						systemProperty("org.ops4j.pax.web.session.timeout")
								.value(Integer.toString(300)),
						CoreOptions
								.cleanCaches(),
						// Run this test under Felix.
						CoreOptions.composite(cfxdosgi()),
						// TODO:
						// bundle("file:multibundle/apache-cxf-dosgi-ri-1.2/dosgi_bundles/spring-osgi-extender-1.2.0.jar").startLevel(6),
						systemProperty(
								"org.osgi.framework.startlevel.beginning")
								.value("" + 100)

				// For debugging...
				// opts.add(PaxRunnerOptions.vmOption(
				// "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=6006"
				// ));
				// opts.add(CoreOptions.waitForFrameworkStartup());
				// end debugging section.
				// end debugging section.
				);
	}


	protected Option[] cfxdosgi() {

		Option[] options = {
				
				
				allowCustomLocalRepository(),
				//CoreOptions.webProfile(),
                mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.remoting.resources" ).versionAsInProject(),
                // converter API
                mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.remoting.resources.services.server" ).versionAsInProject(),
                // converter Iml
                mavenBundle( "fr.opensagres.xdocreport", "fr.opensagres.xdocreport.core" ).versionAsInProject(),

				
				
				
				
				
				// ***************** Gemini blueprint ********************
				mavenBundle("org.springframework.osgi", "spring-osgi-core","1.2.1"),
				mavenBundle("org.springframework.osgi", "spring-osgi-io","1.2.1"),
				mavenBundle("org.springframework.osgi", "spring-osgi-extender","1.2.1").startLevel(5),

				
				mavenBundle("org.apache.geronimo.specs",
						"geronimo-annotation_1.0_spec", "1.1.1"),

				mavenBundle("javax.activation",
						"com.springsource.javax.activation", "1.1.1"),

				mavenBundle("javax.mail", "com.springsource.javax.mail",
						"1.4.0"),
				mavenBundle("org.apache.geronimo.specs",
						"geronimo-ws-metadata_2.0_spec", "1.1.2"),
				mavenBundle("org.apache.commons","com.springsource.org.apache.commons.logging","1.1.1"),
				mavenBundle("org.jdom", "com.springsource.org.jdom")
						.versionAsInProject(),

				mavenBundle("org.aopalliance",
						"com.springsource.org.aopalliance", "1.0.0"),
				mavenBundle("org.springframework", "org.springframework.aop",
						"3.0.6.RELEASE"),
				mavenBundle("org.springframework", "org.springframework.beans",
						"3.0.6.RELEASE"),
				mavenBundle("org.springframework",
						"org.springframework.context", "3.0.6.RELEASE"),
				mavenBundle("org.springframework", "org.springframework.core",
						"3.0.6.RELEASE"),
				mavenBundle("org.springframework",
						"org.springframework.transaction", "3.0.6.RELEASE"),
				mavenBundle("org.springframework", "org.springframework.orm",
						"3.0.6.RELEASE"),
				mavenBundle("org.springframework", "org.springframework.jdbc",
						"3.0.6.RELEASE"),
				mavenBundle("org.springframework", "org.springframework.asm",
						"3.0.6.RELEASE"),
				mavenBundle("org.springframework",
						"org.springframework.expression", "3.0.6.RELEASE"),
				mavenBundle("org.slf4j", "slf4j-api", "1.6.1"),

				mavenBundle("org.slf4j", "slf4j-simple", "1.6.1").noStart(),

				mavenBundle("org.ops4j.pax.web", "pax-web-jetty-bundle",
						"1.1.3"),

				mavenBundle("org.apache.servicemix.bundles",
						"org.apache.servicemix.bundles.wsdl4j", "1.6.1_1"),

				mavenBundle("org.apache.cxf", "cxf-bundle-minimal").version(
						"2.5.2"),

				mavenBundle("org.apache.cxf.dosgi",
						"cxf-dosgi-ri-discovery-local").versionAsInProject(),

				mavenBundle("org.apache.cxf.dosgi", "cxf-dosgi-ri-dsw-cxf")
						.versionAsInProject(),

				mavenBundle("org.apache.cxf.dosgi",
						"cxf-dosgi-ri-topology-manager").versionAsInProject(),
				mavenBundle("org.apache.servicemix.bundles",
						"org.apache.servicemix.bundles.xmlsec")
						.versionAsInProject(),

				mavenBundle("org.apache.ws.xmlschema", "xmlschema-core",
						"2.0.2"),

				mavenBundle("org.apache.servicemix.bundles",
						"org.apache.servicemix.bundles.opensaml", "2.5.1_2"),

				mavenBundle("org.apache.servicemix.bundles",
						"org.apache.servicemix.bundles.asm", "2.2.3_1"),

				mavenBundle("org.apache.servicemix.bundles",
						"org.apache.servicemix.bundles.xmlresolver", "1.2_1"),

				mavenBundle("org.apache.neethi", "neethi", "3.0.2"),

				mavenBundle("org.apache.servicemix.bundles",
						"org.apache.servicemix.bundles.woodstox", "3.2.7_1"),

				mavenBundle("org.apache.servicemix.bundles",
						"org.apache.servicemix.bundles.commons-pool", "1.5.4_1"),

				mavenBundle("org.apache.servicemix.specs",
						"org.apache.servicemix.specs.saaj-api-1.3", "1.3.0"),

				mavenBundle("org.apache.servicemix.specs",
						"org.apache.servicemix.specs.stax-api-1.0", "1.3.0"),

				mavenBundle("org.apache.servicemix.specs",
						"org.apache.servicemix.specs.jaxb-api-2.2", "2.0.0"),

				mavenBundle("org.apache.servicemix.specs",
						"org.apache.servicemix.specs.jaxws-api-2.1", "1.3.0"),

				mavenBundle("org.apache.servicemix.specs",
						"org.apache.servicemix.specs.jsr311-api-1.1.1", "1.9.0"),

				mavenBundle("org.apache.servicemix.bundles",
						"org.apache.servicemix.bundles.jaxb-impl", "2.1.6_1"),

				systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level")
						.value("DEBUG"),
//				PaxRunnerOptions.cleanCaches(),
//				mavenBundle("org.osgi","org.osgi.compendium","4.2.0"),
				mavenBundle("org.osgi","org.osgi.enterprise","4.2.0"),
//				// CoreOptions.junitBundles(),
//
//				// CoreOptions.compendiumProfile(),
//				// ***************** Gemini dependencies ********************
				mavenBundle("org.codehaus.jackson", "jackson-jaxrs", "1.9.0"),
				mavenBundle("org.codehaus.jackson", "jackson-core-asl", "1.9.0"),
				mavenBundle("org.codehaus.jackson", "jackson-mapper-asl",
						"1.9.0"),

		};

		return options;
	}

	protected static final int PORT = 10202;

	private static String createURL(String relativePath) {
		return "http://localhost:" + PORT + relativePath;
	}

	private void waitForPortToBeAvailable(int port) throws Exception {
		waitForFullInitialization();

		for (int i = 0; i < 20; i++) {
			Socket s = null;
			try {
				s = new Socket((String) null, port);
				// yep, its available
				return;
			} catch (IOException e) {
				// wait
			} finally {
				if (s != null) {
					try {
						s.close();
					} catch (IOException e) {
					}
				}
			}
			System.out.println("Waiting for server to appear on port: " + port);
			Thread.sleep(1000);
		}
		throw new java.util.concurrent.TimeoutException();
	}

	protected static boolean initialized = false;

	public static boolean isInitialized() {
		return initialized;
	}

	public static void setInitialized(boolean initialized) {
		RemotingTest.initialized = initialized;
	}

	private void waitForFullInitialization() throws InterruptedException {
		if (!isInitialized()) {
			// I have to "wait" until the OSGi platform is fully initialized...
			// I'm still looking for a clever way of doing this (possibly
			// through listeners).
			Thread.sleep(15000);
			setInitialized(true);
		}
	}

	@Before
	public void setUp() throws Exception {

		waitForPortToBeAvailable(PORT);

		// Make an actual invocation on the remote service.
		cl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(
				JAXRSClientFactoryBean.class.getClassLoader());
	}

	private ClassLoader cl;

	@After
	public void tearDown() {
		Thread.currentThread().setContextClassLoader(cl);
	}

	@Inject
	protected BundleContext ctx;

	
	@Ignore("Requires DOSGi")
	@Test
	public void getName() throws Exception {
		setUp();
		assertNotNull(ctx);
		System.out.println("ctx " + ctx);
		//TODO
//		WebClient webClient = createWebClient();
//		System.out.println("webClient " + webClient);
//		assertNotNull(webClient);
//		String name = webClient.accept(MediaType.APPLICATION_JSON)
//				.path("getName").get(String.class);
//		System.out.println(name);
		tearDown();

	}
	
	public static void main(String[] args) throws Exception {
		RemotingTest jaxrsSMultibundleDistributionUnitTest=new RemotingTest();
		PaxExamRuntime.createContainer(
				PaxExamRuntime.createTestSystem(jaxrsSMultibundleDistributionUnitTest.config()
						)).start();
	}

	
	 private static Option allowCustomLocalRepository() {
			//see: https://ops4j1.jira.com/browse/PAXEXAM-543
			String localRepo = System.getProperty("maven.repo.local", "");
			return when(localRepo.length() > 0).useOptions(
			    systemProperty("org.ops4j.pax.url.mvn.localRepository").value(localRepo)
			);
		}
	 
	private WebClient createWebClient() {

		JAXRSClientFactoryBean factory = new JAXRSClientFactoryBean();
		factory.setAddress(createURL("/"));
		factory.setResourceClass(JAXRSResourcesService.class);
		JacksonJsonProvider provider = new JacksonJsonProvider();
		factory.setProvider(provider);
		WebClient webClient = factory.createWebClient();
		return webClient;
	}
}

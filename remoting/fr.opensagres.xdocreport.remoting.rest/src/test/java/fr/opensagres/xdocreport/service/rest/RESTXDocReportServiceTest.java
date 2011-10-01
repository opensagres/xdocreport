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
package fr.opensagres.xdocreport.service.rest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.core.Application;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import fr.opensagres.xdocreport.document.service.DataContext;
import fr.opensagres.xdocreport.document.service.Report;
import fr.opensagres.xdocreport.document.service.ReportId;
import fr.opensagres.xdocreport.document.service.WSOptions;

public class RESTXDocReportServiceTest {

	private static final int PORT = 9999;
	private static Server server;

	private static final String BASE_ADDRESS = "http://localhost:" + PORT ;

	@BeforeClass
	public static void startServer() throws Exception {

		ServletHolder servlet = new ServletHolder(CXFNonSpringJaxrsServlet.class);

		servlet.setInitParameter(Application.class.getName(), fr.opensagres.xdocreport.service.rest.XDocreportApplication.class.getName());
		servlet.setInitParameter("jaxrs.serviceClasses", RESTXDocReportService.class.getName());
		server = new Server(PORT);
		ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

		context.addServlet(servlet, "/*");
		server.start();
	}
	


	
	@Test
	public void upload() throws IOException {
		
		
        WebClient client = WebClient.create(BASE_ADDRESS);
        client.path("upload");
		Report report = new Report();
		
		InputStream in=RESTXDocReportServiceTest.class.getClassLoader().getResourceAsStream("bo.docx");
		report.setReportID("reportID1");
		report.setDocument(fr.opensagres.xdocreport.core.io.IOUtils.toByteArray(in));
		report.setTemplateEngine("Velocity");
		report.getFieldsMetaData().add("test");
		report.setTemplateEngine("Velocity");
		
		client.post(report);
		
	}

	
	@Test
	public void listReports() throws Exception {

	
		WebClient client = WebClient.create(BASE_ADDRESS);
        client.path("listReports");

		@SuppressWarnings("unchecked")
		Collection<ReportId> reports = (Collection<ReportId>) client.getCollection(ReportId.class);
		System.out.println(reports);
		assertEquals(1, reports.size());
		assertEquals("reportID1", reports.iterator().next().getReportID());
		

	}
	@Ignore
	@Test
	public void processReport() throws IOException {
		
		
        WebClient client = WebClient.create(BASE_ADDRESS);
        client.path("processReport");
		Report report = new Report();
		//client.accept("multipart/related");
		//client=client.type("multipart/form-data");
	//	client=client.accept("multipart/form-data");
		InputStream in=RESTXDocReportServiceTest.class.getClassLoader().getResourceAsStream("bo.docx");
		report.setReportID("reportID1");
		report.setDocument(fr.opensagres.xdocreport.core.io.IOUtils.toByteArray(in));
		report.setTemplateEngine("Velocity");
		report.getFieldsMetaData().add("test");
		report.setTemplateEngine("Velocity");
		
		Form aForm =new Form();
		aForm.set("report", report);
		aForm.set("dataContext", new ArrayList<DataContext>());
		WSOptions options=new WSOptions();
		options.setFrom("A");
		options.setTo("B");
		options.setVia("C");
		aForm.set("wsOptions", options);
		
		
//		List one = new ArrayList();
//		one.add( report);
//		map.put("report", one);
//		List two = new ArrayList();
//		two.add( new ArrayList<DataContext>());
//		map.put("dataContext", two);
//		List three = new ArrayList();
//		three.add(new WSOptions())
//;		map.put("report", three);
//		
//		Map<String, List<Object>> objects = new MultiValued<String, List<Object>>();
//		List toto = new ArrayList();
//        objects.put("report", new ArrayList());
//        objects.put("dataContext", new ArrayList());
//        objects.put("wsOptions", new ArrayList());
		//client.form(aForm);
		
		
		client.form(aForm);
	}
	

	@AfterClass
	public static void stopServer() throws Exception {
		server.stop();
	}
}

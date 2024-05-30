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
package fr.opensagres.xdocreport.remoting.resources.services.server.jaxrs.internal;

import java.io.File;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import fr.opensagres.xdocreport.remoting.resources.services.ResourcesServicesRegistry;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.JAXRSResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.server.file.FileResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.server.jaxrs.JAXRSResourcesServiceImpl;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {

		Dictionary<String, String> restProps = new Hashtable<String, String>();

		restProps.put("service.exported.interfaces", "*");
		restProps.put("service.exported.configs", "org.apache.cxf.rs");
		restProps.put("service.exported.intents", "HTTP");
		restProps.put("org.apache.cxf.rs.provider","org.codehaus.jackson.jaxrs.JacksonJsonProvider");
		restProps.put("org.apache.cxf.rs.httpservice.context", "/cxf");

		File root = new File(".");
		FileResourcesService fileResourcesService = new FileResourcesService(
				root) {

			//@Override
			@GET
			@Path("/name")
			@Consumes("application/json")
			@Produces("text/plain")
			public String getName() {
				return "OSGi";
			}
		};
		context.registerService(JAXRSResourcesService.class.getName(),
				new JAXRSResourcesServiceImpl(fileResourcesService), restProps);

	}

	public void stop(BundleContext context) throws Exception {
		//
	}

	
}

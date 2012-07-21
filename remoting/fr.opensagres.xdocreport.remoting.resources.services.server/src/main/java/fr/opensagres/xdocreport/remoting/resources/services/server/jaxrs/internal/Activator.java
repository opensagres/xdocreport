package fr.opensagres.xdocreport.remoting.resources.services.server.jaxrs.internal;

import java.io.File;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

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

			@Override
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

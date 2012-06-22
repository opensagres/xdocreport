package fr.opensagres.xdocreport.remoting.resources.services.client.jaxrs.client;


import org.apache.cxf.jaxrs.client.JAXRSClientFactory;


import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.JAXRSResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.Providers;

public class JAXRSResourcesServiceCGLibClientTestCase extends AbstractJAXRSResourcesServiceTest
{

    public JAXRSResourcesService getClient() {
		JAXRSResourcesService client =
            JAXRSClientFactory.create( BASE_ADDRESS, JAXRSResourcesService.class, Providers.get() );
		return client;
	}
}

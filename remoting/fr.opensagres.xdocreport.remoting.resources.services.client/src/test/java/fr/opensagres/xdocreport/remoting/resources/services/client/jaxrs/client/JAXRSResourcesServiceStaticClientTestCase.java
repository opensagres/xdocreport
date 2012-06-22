package fr.opensagres.xdocreport.remoting.resources.services.client.jaxrs.client;

import fr.opensagres.xdocreport.remoting.resources.services.client.jaxrs.JAXRSResourcesServiceClientFactory;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.JAXRSResourcesService;

public class JAXRSResourcesServiceStaticClientTestCase  extends AbstractJAXRSResourcesServiceTest
{





	public JAXRSResourcesService getClient() {
		JAXRSResourcesService client = JAXRSResourcesServiceClientFactory.create( BASE_ADDRESS );
		return client;
	}

}

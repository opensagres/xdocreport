package fr.opensagres.xdocreport.remoting.resources.services.rest.client;

import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;

public class JAXRSResourcesServiceClientFactory
{

    public static ResourcesService create( String baseAddress )
    {
        return new JAXRSResourcesServiceClient( baseAddress );
    }
}

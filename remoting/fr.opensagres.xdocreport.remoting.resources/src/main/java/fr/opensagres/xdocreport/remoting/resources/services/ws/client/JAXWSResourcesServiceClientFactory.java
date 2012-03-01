package fr.opensagres.xdocreport.remoting.resources.services.ws.client;

import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;

public class JAXWSResourcesServiceClientFactory
{

    public static ResourcesService create( String baseAddress )
    {
        return new JAXWSResourcesServiceClient( baseAddress );
    }
}

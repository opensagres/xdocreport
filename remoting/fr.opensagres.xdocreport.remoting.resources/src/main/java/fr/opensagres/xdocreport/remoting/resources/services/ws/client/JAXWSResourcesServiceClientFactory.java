package fr.opensagres.xdocreport.remoting.resources.services.ws.client;

import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;

public class JAXWSResourcesServiceClientFactory
{

    public static ResourcesService create( String baseAddress )
    {
        return new JAXWSResourcesServiceClient( baseAddress, null, null, null, null );
    }

    public static ResourcesService create( String baseAddress, String user, String password, Long connectionTimeout,
                                           Boolean allowChunking )
    {
        return new JAXWSResourcesServiceClient( baseAddress, user, password, connectionTimeout, allowChunking );
    }
}

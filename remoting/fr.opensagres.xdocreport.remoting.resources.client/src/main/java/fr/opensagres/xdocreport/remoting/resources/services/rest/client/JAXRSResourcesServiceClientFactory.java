package fr.opensagres.xdocreport.remoting.resources.services.rest.client;

import fr.opensagres.xdocreport.remoting.resources.services.rest.JAXRSResourcesService;


public class JAXRSResourcesServiceClientFactory
{
    public static JAXRSResourcesService create( String baseAddress )
    {
        return new JAXRSResourcesServiceClient( baseAddress, null, null, null, null );
    }

    public static JAXRSResourcesService create( String baseAddress, String username, String password,
                                           Long connectionTimeout, Boolean allowChunking )
    {
        return new JAXRSResourcesServiceClient( baseAddress, username, password, connectionTimeout, allowChunking );
    }
}

package fr.opensagres.xdocreport.remoting.repository.services.ws.client;

import fr.opensagres.xdocreport.remoting.repository.services.IRepositoryService;

public class JAXWSRepositoryServiceClientFactory
{

    public static IRepositoryService create( String baseAddress )
    {
        return new JAXWSRepositoryServiceClient( baseAddress );
    }
}

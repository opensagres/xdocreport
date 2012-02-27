package fr.opensagres.xdocreport.remoting.repository.services.rest.client;

import fr.opensagres.xdocreport.remoting.repository.services.IRepositoryService;

public class JAXRSRepositoryServiceClientFactory
{

    public static IRepositoryService create( String baseAddress )
    {
        return new JAXRSRepositoryServiceClient( baseAddress );
    }
}

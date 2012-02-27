package fr.opensagres.xdocreport.remoting.repository.services;

import fr.opensagres.xdocreport.remoting.repository.services.rest.client.JAXRSRepositoryServiceClientFactory;
import fr.opensagres.xdocreport.remoting.repository.services.ws.client.JAXWSRepositoryServiceClientFactory;

public class RepositoryServiceClientFactory
{

    public static IRepositoryService create( String baseAddress, ServiceType serviceType, String user, String password )
    {
        if ( serviceType == ServiceType.REST )
        {
            return JAXRSRepositoryServiceClientFactory.create( baseAddress );
        }
        return JAXWSRepositoryServiceClientFactory.create( baseAddress );
    }
}

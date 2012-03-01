package fr.opensagres.xdocreport.remoting.repository.services.rest;

import fr.opensagres.xdocreport.remoting.repository.services.IRepositoryService;
import fr.opensagres.xdocreport.remoting.repository.services.MockRepositoryService;
import fr.opensagres.xdocreport.remoting.repository.services.rest.server.JAXRSRepositoryService;

public class MockJAXRSRepositoryService extends JAXRSRepositoryService
{

    @Override
    protected IRepositoryService getDelegate()
    {        
        return MockRepositoryService.INSTANCE;
    }
}

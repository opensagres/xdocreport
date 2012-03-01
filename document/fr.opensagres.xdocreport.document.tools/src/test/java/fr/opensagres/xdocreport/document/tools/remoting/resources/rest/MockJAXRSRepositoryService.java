package fr.opensagres.xdocreport.document.tools.remoting.resources.rest;

import fr.opensagres.xdocreport.document.tools.remoting.resources.MockRepositoryService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.rest.server.JAXRSResourcesServiceImpl;


public class MockJAXRSRepositoryService extends JAXRSResourcesServiceImpl
{

    @Override
    protected ResourcesService getDelegate()
    {        
        return MockRepositoryService.INSTANCE;
    }
}

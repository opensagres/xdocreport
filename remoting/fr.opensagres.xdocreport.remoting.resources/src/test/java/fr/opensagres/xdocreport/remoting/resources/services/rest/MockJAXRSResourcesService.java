package fr.opensagres.xdocreport.remoting.resources.services.rest;

import fr.opensagres.xdocreport.remoting.resources.services.MockResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.rest.server.JAXRSResourcesServiceImpl;

public class MockJAXRSResourcesService extends JAXRSResourcesServiceImpl
{

    @Override
    protected ResourcesService getDelegate()
    {        
        return MockResourcesService.INSTANCE;
    }
}

package fr.opensagres.xdocreport.remoting.resources.services.ws;

import java.util.List;

import javax.jws.WebService;

import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.MockResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ws.server.JAXWSResourcesServiceImpl;

@WebService( name = "ResourcesService" )
public class MockJAXWSResourcesService extends JAXWSResourcesServiceImpl implements JAXWSResourcesService
{

    @Override
    public String getName()
    {
        return super.getName();
    }

    @Override
    public Resource getRoot()
    {
        return super.getRoot();
    }

    @Override
    public Resource getRoot( Filter filter )
    {
        return super.getRoot( filter );
    }

    @Override
    public byte[] download( String resourcePath )
    {
        return super.download( resourcePath );
    }

    @Override
    public List<byte[]> download( List<String> resourcePaths )
    {
        return super.download( resourcePaths );
    }

    @Override
    public void upload( String resourcePath, byte[] content )
    {
        super.upload( resourcePath, content );
    }
    
    @Override
    protected ResourcesService getDelegate()
    {        
        return MockResourcesService.INSTANCE;
    }
}

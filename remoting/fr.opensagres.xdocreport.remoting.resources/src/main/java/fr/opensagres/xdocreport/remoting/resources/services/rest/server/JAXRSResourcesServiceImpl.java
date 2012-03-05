package fr.opensagres.xdocreport.remoting.resources.services.rest.server;

import java.util.List;

import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.DelegateResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.rest.JAXRSResourcesService;

public abstract class JAXRSResourcesServiceImpl
    extends DelegateResourcesService
    implements JAXRSResourcesService
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
    public byte[] download( String resourceId )
    {
        return super.download( resourceId );
    }

    @Override
    public List<byte[]> download( List<String> resourceIds )
    {
        return super.download( resourceIds );
    }

    @Override
    public void upload( String resourceId, byte[] content )
    {
        super.upload( resourceId, content );
    }

}

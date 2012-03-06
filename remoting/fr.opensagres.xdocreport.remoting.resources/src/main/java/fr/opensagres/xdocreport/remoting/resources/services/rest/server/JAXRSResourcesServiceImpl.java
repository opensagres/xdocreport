package fr.opensagres.xdocreport.remoting.resources.services.rest.server;

import java.util.List;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.DelegateResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.rest.JAXRSResourcesService;

public class JAXRSResourcesServiceImpl
    extends DelegateResourcesService
    implements JAXRSResourcesService
{

    public JAXRSResourcesServiceImpl( ResourcesService delegate )
    {
        super( delegate );
    }

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
    public BinaryData download( String resourceId )
    {
        return super.download( resourceId );
    }

    @Override
    public List<BinaryData> download( List<String> resourceIds )
    {
        return super.download( resourceIds );
    }

    @Override
    public void upload( BinaryData dataIn )
    {
        super.upload( dataIn );
    }

}

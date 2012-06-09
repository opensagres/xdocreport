package fr.opensagres.xdocreport.remoting.resources.services.ws.server;

import java.util.List;

import javax.jws.WebService;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.DelegateResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;

@WebService( endpointInterface = "fr.opensagres.xdocreport.remoting.resources.services.ws.JAXWSResourcesService", serviceName = "ResourcesServiceService" )
public class JAXWSResourcesServiceImpl
    extends DelegateResourcesService
    implements ResourcesService
{

    public JAXWSResourcesServiceImpl( ResourcesService delegate )
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
        throws ResourcesException
    {
        return super.getRoot();
    }

    @Override
    public Resource getRoot( Filter filter )
        throws ResourcesException
    {
        return super.getRoot( filter );
    }

    @Override
    public BinaryData download( String resourceId )
        throws ResourcesException
    {
        return super.download( resourceId );
    }

    @Override
    public List<BinaryData> download( List<String> resourceIds )
        throws ResourcesException
    {
        return super.download( resourceIds );
    }

    @Override
    public void upload( BinaryData dataIn )
        throws ResourcesException
    {
        super.upload( dataIn );
    }
}

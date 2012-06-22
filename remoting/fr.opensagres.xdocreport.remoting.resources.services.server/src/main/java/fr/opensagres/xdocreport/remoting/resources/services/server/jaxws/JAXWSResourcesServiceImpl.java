package fr.opensagres.xdocreport.remoting.resources.services.server.jaxws;

import java.util.List;

import javax.jws.WebService;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.DelegateResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.jaxws.JAXWSResourcesService;

@WebService( endpointInterface = "fr.opensagres.xdocreport.remoting.resources.services.jaxws.JAXWSResourcesService", serviceName = "ResourcesServiceService" )
public class JAXWSResourcesServiceImpl
    extends DelegateResourcesService
    implements JAXWSResourcesService
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
    {
        return super.getRoot();
    }

    @Override
    public Resource getRootWithFilter( Filter filter )
        throws ResourcesException
    {
        return super.getRootWithFilter( filter );
    }

    @Override
    public BinaryData download( String resourceId )
        throws ResourcesException
    {
        return super.download( resourceId );
    }

    @Override
    public List<BinaryData> downloadMultiple( List<String> resourceIds )
        throws ResourcesException
    {
        return super.downloadMultiple( resourceIds );
    }

    @Override
    public void upload( BinaryData dataIn )
        throws ResourcesException
    {
        super.upload( dataIn );
    }
}

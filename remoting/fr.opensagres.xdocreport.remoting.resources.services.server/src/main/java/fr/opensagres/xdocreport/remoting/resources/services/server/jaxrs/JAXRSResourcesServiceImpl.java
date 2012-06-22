package fr.opensagres.xdocreport.remoting.resources.services.server.jaxrs;

import java.util.List;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.LargeBinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.DelegateResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.JAXRSResourcesService;

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
        throws ResourcesException
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

	public LargeBinaryData downloadLarge(String resourceId)
			throws ResourcesException {
		return getDelegate().downloadLarge(resourceId);

	}

	@Override
	protected JAXRSResourcesService getDelegate() {
		return (JAXRSResourcesService)super.getDelegate();
	}


	public void uploadLarge(LargeBinaryData data) throws ResourcesException {
		 getDelegate().uploadLarge(data);

	}

}

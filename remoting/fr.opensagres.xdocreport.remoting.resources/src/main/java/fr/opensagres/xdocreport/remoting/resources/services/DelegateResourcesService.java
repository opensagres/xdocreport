package fr.opensagres.xdocreport.remoting.resources.services;

import java.util.List;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;

public class DelegateResourcesService
    implements ResourcesService
{

    private final ResourcesService delegate;

    public DelegateResourcesService( ResourcesService delegate )
    {
        this.delegate = delegate;
    }

    public String getName()
    {
        return getDelegate().getName();
    }

    public Resource getRoot()
        throws ResourcesException
    {
        return getDelegate().getRoot();
    }

    public Resource getRootWithFilter( Filter filter )
        throws ResourcesException
    {
        return getDelegate().getRootWithFilter( filter );
    }

    public List<BinaryData> downloadMultiple( List<String> resourceIds )
        throws ResourcesException
    {
        return getDelegate().downloadMultiple( resourceIds );
    }

    public BinaryData download( String resourceId )
        throws ResourcesException
    {
        return getDelegate().download( resourceId );
    }

    public void upload( BinaryData dataIn )
        throws ResourcesException
    {
        getDelegate().upload( dataIn );
    }

    protected ResourcesService getDelegate()
    {
        return delegate;
    }

}

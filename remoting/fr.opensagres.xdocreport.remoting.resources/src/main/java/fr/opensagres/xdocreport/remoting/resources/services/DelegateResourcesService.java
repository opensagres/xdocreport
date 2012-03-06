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
    {
        return getDelegate().getRoot();
    }

    public Resource getRoot( Filter filter )
    {
        return getDelegate().getRoot( filter );
    }

    public List<BinaryData> download( List<String> resourceIds )
    {
        return getDelegate().download( resourceIds );
    }

    public BinaryData download( String resourceId )
    {
        return getDelegate().download( resourceId );
    }

    public void upload( BinaryData dataIn )
    {
        getDelegate().upload( dataIn );
    }

    protected ResourcesService getDelegate()
    {
        return delegate;
    }

}

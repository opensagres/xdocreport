package fr.opensagres.xdocreport.remoting.resources.services;

import java.util.List;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryDataIn;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;

public abstract class DelegateResourcesService
    implements ResourcesService
{

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

    public List<byte[]> download( List<String> resourceIds )
    {
        return getDelegate().download( resourceIds );
    }

    public byte[] download( String resourceId )
    {
        return getDelegate().download( resourceId );
    }

    public void upload( BinaryDataIn dataIn )
    {
        getDelegate().upload( dataIn );
    }

    protected abstract ResourcesService getDelegate();
    // {
    // // TODO : manage implementation with SPI
    // // By default it's XDocReportRepositoryService.
    // return XDocReportRepositoryService.getDefault();
    // }
}

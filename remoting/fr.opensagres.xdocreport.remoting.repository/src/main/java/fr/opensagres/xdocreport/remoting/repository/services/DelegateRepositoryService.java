package fr.opensagres.xdocreport.remoting.repository.services;

import java.util.List;

import fr.opensagres.xdocreport.remoting.repository.domain.Filter;
import fr.opensagres.xdocreport.remoting.repository.domain.Resource;

public class DelegateRepositoryService
    implements IRepositoryService
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

    public List<byte[]> download( List<String> resourcePaths )
    {
        return getDelegate().download( resourcePaths );
    }

    public byte[] download( String resourcePath )
    {
        return getDelegate().download( resourcePath );
    }

    public void upload( String resourcePath, byte[] content )
    {
        getDelegate().upload( resourcePath, content );
    }

    protected IRepositoryService getDelegate()
    {
        // TODO : manage implementation with SPI
        // By default it's XDocReportRepositoryService.
        return XDocReportRepositoryService.getDefault();
    }
}

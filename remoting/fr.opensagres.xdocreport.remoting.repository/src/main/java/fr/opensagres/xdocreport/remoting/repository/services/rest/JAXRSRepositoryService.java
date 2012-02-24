package fr.opensagres.xdocreport.remoting.repository.services.rest;

import java.util.List;

import fr.opensagres.xdocreport.remoting.repository.domain.Filter;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceContent;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceMetadata;
import fr.opensagres.xdocreport.remoting.repository.services.DelegateRepositoryService;

public class JAXRSRepositoryService
    extends DelegateRepositoryService
    implements IJAXRSRepositoryService
{

    @Override
    public ResourceContent download( String resourceId )
    {
        return super.download( resourceId );
    }

    @Override
    public ResourceContent download( String resourceId, Filter filter )
    {
        return super.download( resourceId, filter );
    }

    @Override
    public ResourceMetadata getMetadata( String resourceId )
    {
        return super.getMetadata( resourceId );
    }

    @Override
    public ResourceMetadata getMetadata( String resourceId, Filter filter )
    {
        return super.getMetadata( resourceId, filter );
    }

    @Override
    public List<ResourceMetadata> getMetadatas()
    {
        return super.getMetadatas();
    }

    @Override
    public List<ResourceMetadata> getMetadatas( Filter filter )
    {
        return super.getMetadatas( filter );
    }

    // Override must be done to use JAX-RS annotations from IJAXRSRepositoryService
    @Override
    public void upload( ResourceContent content )
    {
        super.upload( content );
    }
}

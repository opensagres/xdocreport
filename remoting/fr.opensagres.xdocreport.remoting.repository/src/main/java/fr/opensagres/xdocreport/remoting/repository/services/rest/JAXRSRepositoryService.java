package fr.opensagres.xdocreport.remoting.repository.services.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fr.opensagres.xdocreport.remoting.repository.domain.Filter;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceContent;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceMetadata;
import fr.opensagres.xdocreport.remoting.repository.services.DelegateRepositoryService;

public class JAXRSRepositoryService
    extends DelegateRepositoryService
    implements IJAXRSRepositoryService
{

    // JAX-RS Annotations must be declared here, otheriwise resourceId is empty????

    @GET
    @Path( "/download/{resourceId}" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.WILDCARD )
    @Override
    public ResourceContent download( @PathParam( "resourceId" )
    String resourceId )
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

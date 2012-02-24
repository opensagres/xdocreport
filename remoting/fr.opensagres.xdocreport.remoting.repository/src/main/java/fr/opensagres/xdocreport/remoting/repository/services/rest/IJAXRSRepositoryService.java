package fr.opensagres.xdocreport.remoting.repository.services.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fr.opensagres.xdocreport.remoting.repository.domain.ResourceContent;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceMetadata;
import fr.opensagres.xdocreport.remoting.repository.services.IRepositoryService;

@Path( "/" )
public interface IJAXRSRepositoryService
    extends IRepositoryService
{

    @GET
    @Path( "/download/{resourceId}" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.WILDCARD )
    ResourceContent download( @PathParam( "resourceId" )
    String resourceId );

    @GET
    @Path( "/metadata/{resourceId}" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    ResourceMetadata getMetadata( @PathParam( "resourceId" )
    String resourceId );

    @GET
    @Path( "/metadatas" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_XML )
    List<ResourceMetadata> getMetadatas();

    @POST
    @Path( "/upload" )
    @Consumes( MediaType.APPLICATION_JSON )
    void upload( ResourceContent content );

}

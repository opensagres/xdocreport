package fr.opensagres.xdocreport.remoting.resources.services.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;

@Path( "/" )
public interface JAXRSResourcesService
    extends ResourcesService
{

    @GET
    @Path( "/name" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.TEXT_PLAIN )
    String getName();

    @GET
    @Path( "/root" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.APPLICATION_JSON )
    Resource getRoot();

    @GET
    @Path( "/download/{resourcePath}" )
    @Consumes( MediaType.APPLICATION_JSON )
    @Produces( MediaType.WILDCARD )
    byte[] download( @PathParam( "resourcePath" )
    String resourcePath );

    // @GET
    // @Path( "/download/{resourceId}" )
    // @Consumes( MediaType.APPLICATION_JSON )
    // @Produces( MediaType.WILDCARD )
    // ResourceContent download( @PathParam( "resourceId" )
    // String resourceId );
    //
    // @GET
    // @Path( "/metadata/{resourceId}" )
    // @Consumes( MediaType.APPLICATION_JSON )
    // @Produces( MediaType.APPLICATION_JSON )
    // ResourceMetadata getMetadata( @PathParam( "resourceId" )
    // String resourceId );
    //
    // @GET
    // @Path( "/metadatas" )
    // @Consumes( MediaType.APPLICATION_JSON )
    // @Produces( MediaType.APPLICATION_XML )
    // List<ResourceMetadata> getMetadatas();
    //
    // @POST
    // @Path( "/upload" )
    // @Consumes( MediaType.APPLICATION_JSON )
    // void upload( ResourceContent content );

}

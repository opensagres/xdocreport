package fr.opensagres.xdocreport.converter;

import javax.activation.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;

public interface ConverterResource
{


    @POST
    @Path( "convertPDF" )
    @Consumes( MediaType.WILDCARD )
    @Produces( MediaType.WILDCARD )
    BinaryFile convertPDF( Request request );


    @POST
    @Consumes( MediaType.WILDCARD )
    @Produces( MediaType.WILDCARD )
    @Path( "/convert" )
    Response convert( @Multipart( "outputFormat" )
    String outputFormat, @Multipart( "datafile" )
    DataSource content, @Multipart( "operation" )
    String operation );
}
package fr.opensagres.xdocreport.converter;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


public interface ConverterResource
{


    @POST
    @Path( "convertPDF" )
    @Consumes( MediaType.WILDCARD )
    @Produces( MediaType.WILDCARD )
    BinaryFile convertPDF( Request request );

}
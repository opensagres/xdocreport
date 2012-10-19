package fr.opensagres.xdocreport.remoting.converter;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


public interface ConverterResource
{


    @POST
    @Path( "convertPDF" )
    @Consumes( {MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON} )
    @Produces( {MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON} )
    BinaryFile convertPDF( Request request );

}
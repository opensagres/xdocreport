package fr.opensagres.xdocreport.document.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.domain.DataContext;
import fr.opensagres.xdocreport.document.domain.ReportAndDataRepresentation;
import fr.opensagres.xdocreport.document.domain.ReportId;
import fr.opensagres.xdocreport.document.domain.ReportRepresentation;

public interface XDocReportService
{

    @GET
    @Path( "/download/{reportID}/{processState}" )
    @Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
    @Produces( MediaType.WILDCARD )
    public abstract byte[] download( @PathParam( "reportID" )
    String reportID, @PathParam( "processState" )
    String processState )
        throws XDocReportException;

    @GET
    @Path( "/listReports" )
    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    public abstract List<ReportId> listReports();

    @POST
    @Path( "/upload" )
    @Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
    public abstract void upload( ReportRepresentation report )
        throws XDocReportException;

    @POST
    @Path( "/processReport" )
    @Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON } )
    @Produces( MediaType.WILDCARD )
    public abstract byte[] processReport( ReportAndDataRepresentation reportAndDataRepresentation )
        throws XDocReportException;

    public abstract byte[] processReport( String reportId, List<DataContext> dataContext, Options options )
        throws XDocReportException;

    public abstract void unRegister( String reportId );

}
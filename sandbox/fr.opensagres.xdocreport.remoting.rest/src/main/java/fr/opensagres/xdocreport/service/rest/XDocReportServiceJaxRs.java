/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.xdocreport.service.rest;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.domain.ReportAndDataRepresentation;
import fr.opensagres.xdocreport.document.domain.ReportId;
import fr.opensagres.xdocreport.document.domain.ReportRepresentation;
import fr.opensagres.xdocreport.document.domain.WSOptions;
import fr.opensagres.xdocreport.document.internal.XDocReportServiceImpl;
import fr.opensagres.xdocreport.document.service.RemoteInvocationException;
import fr.opensagres.xdocreport.document.service.XDocReportService;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

@Path( "/" )
public class XDocReportServiceJaxRs implements XDocReportService
{

    private XDocReportServiceImpl delegate = XDocReportServiceImpl.INSTANCE;

    /* (non-Javadoc)
     * @see fr.opensagres.xdocreport.service.rest.XDocReportService#download(java.lang.String, java.lang.String)
     */
    @GET
    @Path( "/download/{reportID}/{processState}" )
    @Consumes(  MediaType.APPLICATION_JSON  )
    @Produces(MediaType.WILDCARD)
    public byte[] download( @PathParam("reportID") String reportID, @PathParam("processState")  String processState ) throws RemoteInvocationException
    {
        System.err.println("reportId "+reportID);
        System.err.println("processState "+processState);
        try
        {
            return delegate.download( reportID, processState );
        }
        catch ( XDocReportException e )
        {
         throw new RemoteInvocationException(e.getMessage(),e.getStackTrace());
        }
    }


    /* (non-Javadoc)
     * @see fr.opensagres.xdocreport.service.rest.XDocReportService#listReports()
     */
    @GET
    @Path( "/listReports" )
    @Produces(  MediaType.APPLICATION_JSON  )
    public List<ReportId> listReports()
    {
        return delegate.listReports();
    }


    /* (non-Javadoc)
     * @see fr.opensagres.xdocreport.service.rest.XDocReportService#upload(fr.opensagres.xdocreport.document.domain.ReportRepresentation)
     */
    @POST
    @Path( "/upload" )
    @Consumes(  MediaType.APPLICATION_JSON  )
    public void upload( ReportRepresentation report )throws RemoteInvocationException
    {

        fr.opensagres.xdocreport.template.formatter.FieldsMetadata fieldsMetadata2 =
            new fr.opensagres.xdocreport.template.formatter.FieldsMetadata();
        for ( String field : report.getFieldsMetaData() )
        {
            fieldsMetadata2.addFieldAsList( field );
        }

        try
        {
            delegate.registerReport( report.getReportID(), report.getDocument(), fieldsMetadata2, "Velocity" );
        }
        catch ( XDocReportException e )
        {
            throw new RemoteInvocationException(e.getMessage(),e.getStackTrace());
        }

    }

    /* (non-Javadoc)
     * @see fr.opensagres.xdocreport.service.rest.XDocReportService#processReport(fr.opensagres.xdocreport.document.domain.ReportAndDataRepresentation)
     */
    @POST
    @Path( "/processReport" )
    @Consumes(  MediaType.APPLICATION_JSON  )
    @Produces(MediaType.WILDCARD)
    public byte[] processReport( ReportAndDataRepresentation reportAndDataRepresentation ) throws RemoteInvocationException
    {

        System.err.println(reportAndDataRepresentation);
        FieldsMetadata fieldsMetadata = new FieldsMetadata();
        List<String> fields = reportAndDataRepresentation.getFieldsMetaData();
        for ( String field : fields )
        {
            fieldsMetadata.addFieldAsList( field );
        }

        WSOptions wsOptions = reportAndDataRepresentation.getOptions();

        Options options=null;
        if(wsOptions!=null){
            options = Options.getFrom( wsOptions.getFrom() ).to( wsOptions.getTo() ).via( wsOptions.getVia() );
        }


        byte[] result;
        try
        {
            result = delegate.process( reportAndDataRepresentation.getDocument(), fieldsMetadata, reportAndDataRepresentation.getTemplateEngine(), reportAndDataRepresentation.getDataContext(), options );
        }
        catch ( XDocReportException e )
        {
            throw new RemoteInvocationException(e.getMessage(),e.getStackTrace());
        }

        return result;
    }


    /* (non-Javadoc)
     * @see fr.opensagres.xdocreport.service.rest.XDocReportService#unRegister(java.lang.String)
     */
    public void unRegister( String reportId ) throws RemoteInvocationException
    {
        delegate.unregisterReport( reportId );
    }

}

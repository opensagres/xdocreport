/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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
import fr.opensagres.xdocreport.document.domain.WSOptions;
import fr.opensagres.xdocreport.document.internal.XDocReportServiceImpl;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

@Path( "/" )
public class RESTXDocReportService
{

    private XDocReportServiceImpl delegate = XDocReportServiceImpl.INSTANCE;

    @GET
    @Path( "/download/{reportID}/{processState}" )
    @Consumes( { MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON  })
    @Produces(MediaType.WILDCARD)
    public byte[] download( @PathParam("reportID") String reportID, @PathParam("processState")  String processState )
        throws XDocReportException
    {
        System.err.println("reportId "+reportID);
        System.err.println("processState "+processState);
        return delegate.download( reportID, processState );
    }


    @GET
    @Path( "/listReports" )
    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
    public List<ReportId> listReports()
    {
        return delegate.listReports();
    }


    @POST
    @Path( "/upload" )
    @Consumes( { MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON  })
    public void upload( ReportRepresentation report )
        throws XDocReportException
    {

        fr.opensagres.xdocreport.template.formatter.FieldsMetadata fieldsMetadata2 =
            new fr.opensagres.xdocreport.template.formatter.FieldsMetadata();
        for ( String field : report.getFieldsMetaData() )
        {
            fieldsMetadata2.addFieldAsList( field );
        }

        delegate.registerReport( report.getReportID(), report.getDocument(), fieldsMetadata2, "Velocity" );

    }

    @POST
    @Path( "/processReport" )
    @Consumes( { MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON  })
    @Produces(MediaType.WILDCARD)
    public byte[] processReport( ReportAndDataRepresentation reportAndDataRepresentation )
        throws XDocReportException
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


        byte[] result= delegate.process( reportAndDataRepresentation.getDocument(), fieldsMetadata, reportAndDataRepresentation.getTemplateEngine(), reportAndDataRepresentation.getDataContext(), options );

        return result;
    }

    public byte[] processReport( String reportId, List<DataContext> dataContext, Options options )
        throws XDocReportException
    {
        return delegate.process( reportId, dataContext, options );
    }

    public void unRegister( String reportId )
    {
        delegate.unregisterReport( reportId );
    }

}

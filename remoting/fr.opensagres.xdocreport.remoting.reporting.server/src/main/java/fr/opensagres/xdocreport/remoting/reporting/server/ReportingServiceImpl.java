package fr.opensagres.xdocreport.remoting.reporting.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.core.utils.HttpHeaderUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.XDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.remoting.reporting.ReportingService;
import fr.opensagres.xdocreport.remoting.reporting.json.JSONObject;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

/**
 * Reporting REST Web Service implementation.
 */
@Path( "/" )
public class ReportingServiceImpl
    implements ReportingService
{

    private static final Logger LOGGER = LogUtils.getLogger( ReportingServiceImpl.class );

    @POST
    @Consumes( MediaType.WILDCARD )
    @Produces( MediaType.WILDCARD )
    @Path( "/report" )
    public Response report( @Multipart( "templateDocument" )
    final DataSource templateDocument, @Multipart( "data" )
    String data, @Multipart( "dataType" )
    String dataType, @Multipart( "templateEngineKind" )
    final String templateEngineKind, @Multipart( "outFileName" )
    final String outFileName )
    {
        try
        {
            // TODO : manage FieldsMetadata
            FieldsMetadata metadata = null;
            // Load report
            final IXDocReport report =
                XDocReport.loadReport( templateDocument.getInputStream(), templateEngineKind, metadata,
                                       XDocReportRegistry.getRegistry() );
            return doReport( report, data, dataType, outFileName );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    @POST
    @Consumes( MediaType.WILDCARD )
    @Produces( MediaType.WILDCARD )
    @Path( "/report2" )
    public Response report2( String reportId, @Multipart( "data" )
    String data, @Multipart( "dataType" )
    String dataType, @Multipart( "templateEngineKind" )
    final String templateEngineKind, @Multipart( "outFileName" )
    final String outFileName )
    {
        try
        {
            // TODO : manage FieldsMetadata
            FieldsMetadata metadata = null;
            // Load report

            final IXDocReport report = null;
            return doReport( report, data, dataType, outFileName );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    private Response doReport( final IXDocReport report, String data, String dataType, final String outFileName )
        throws Exception
    {
        // Transform string data to Map.
        final Map contextMap = toMap( data, dataType );

        StreamingOutput output = new StreamingOutput()
        {
            public void write( OutputStream out )
                throws IOException, WebApplicationException
            {
                try
                {
                    long start = System.currentTimeMillis();

                    report.process( contextMap, out );

                    if ( LOGGER.isLoggable( Level.INFO ) )
                    {
                        LOGGER.info( "Time spent to generate report " + report.getId() + ": "
                            + ( System.currentTimeMillis() - start ) + " ms " );
                    }
                }
                catch ( XDocReportException e )
                {

                    if ( LOGGER.isLoggable( Level.SEVERE ) )
                    {
                        LOGGER.log( Level.SEVERE, "Converter error", e );
                    }
                    throw new WebApplicationException( e );
                }
                catch ( RuntimeException e )
                {

                    if ( LOGGER.isLoggable( Level.SEVERE ) )
                    {
                        LOGGER.log( Level.SEVERE, "RuntimeException", e );
                    }
                    throw new WebApplicationException( e );
                }
                finally
                {
                    IOUtils.closeQuietly( out );
                }

            }
        };
        // 5) Create the JAX-RS response builder.
        ResponseBuilder responseBuilder =
            Response.ok( output, MediaType.valueOf( report.getMimeMapping().getMimeType() ) );

        if ( StringUtils.isNotEmpty( outFileName ) )
        {
            // The generated report document must be downloaded, add the well
            // // content-disposition header.
            responseBuilder.header( HttpHeaderUtils.CONTENT_DISPOSITION_HEADER,
                                    HttpHeaderUtils.getAttachmentFileName( outFileName ) );
        }
        return responseBuilder.build();

    }

    protected Map toMap( String data, String dataType )
        throws Exception
    {
        // TODO : use well serializer of the data according dataType (json, xml, etc)
        return new JSONObject( data );
    }
}

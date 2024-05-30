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
package fr.opensagres.xdocreport.remoting.reporting.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.activation.DataSource;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.StreamingOutput;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.core.utils.HttpHeaderUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.XDocReport;
import fr.opensagres.xdocreport.document.json.JSONObject;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.remoting.reporting.ReportingService;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadataXMLSerializer;

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
    DataSource templateDocument, @Multipart( "templateEngineKind" )
    String templateEngineKind, @Multipart( value = "metadata", required = false )
    String xmlFieldsMetadata, @Multipart( "data" )
    String data, @Multipart( value = "dataType", required = false )
    String dataType, @Multipart( value="outFileName", required = false )
    String outFileName, @Multipart( value = "outFormat", required = false )
    String outFormat, @Multipart( value = "outFormatVia", required = false )
    final String via )
    {
        try
        {
            FieldsMetadata metadata = getFieldsMetadata( xmlFieldsMetadata );
            // Load report
            final IXDocReport report =
                XDocReport.loadReport( templateDocument.getInputStream(), templateEngineKind, metadata,
                                       XDocReportRegistry.getRegistry() );
            return doReport( report, data, dataType, outFileName, outFormat, via );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    private FieldsMetadata getFieldsMetadata( String xmlFieldsMetadata )
        throws SAXException, IOException
    {
        FieldsMetadata metadata = null;
        if ( StringUtils.isNotEmpty( xmlFieldsMetadata ) )
        {
            metadata = FieldsMetadataXMLSerializer.getInstance().load( new StringReader( xmlFieldsMetadata ) );
        }
        return metadata;
    }

    @POST
    @Consumes( MediaType.WILDCARD )
    @Produces( MediaType.WILDCARD )
    @Path( "/report2" )
    public Response report2( String reportId, @Multipart( "data" )
    String data, @Multipart( value = "dataType", required = false )
    String dataType, @Multipart( "templateEngineKind" )
    final String templateEngineKind, @Multipart( "outFileName" )
    final String outFileName, @Multipart( value = "outFormat", required = false )
    String outFormat, @Multipart( value = "outFormatVia", required = false )
    final String via )
    {
        try
        {
            // TODO : manage FieldsMetadata
            FieldsMetadata metadata = null;
            // Load report

            final IXDocReport report = null;
            return doReport( report, data, dataType, outFileName, outFormat, via );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    private Response doReport( final IXDocReport report, String data, String dataType, final String outFileName,
                               final String outFormat, final String via )
        throws Exception
    {
        // Transform string data to Map.
        final Map contextMap = toMap( data, dataType );

        Options options = getOptions( outFormat, via );
        StreamingOutput output = new StreamingOutput()
        {
            public void write( OutputStream out )
                throws IOException, WebApplicationException
            {
                try
                {
                    long start = System.currentTimeMillis();

                    if ( StringUtils.isNotEmpty( outFormat ) )
                    {
                        Options options = getOptions( outFormat, via );
                        report.convert( contextMap, options, out );
                    }
                    else
                    {
                        report.process( contextMap, out );
                    }
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
        MediaType mediaType = getMediaType( report, options );

        ResponseBuilder responseBuilder = Response.ok( output, mediaType );

        if ( StringUtils.isNotEmpty( outFileName ) )
        {
            // The generated report document must be downloaded, add the well
            // // content-disposition header.
            responseBuilder.header( HttpHeaderUtils.CONTENT_DISPOSITION_HEADER,
                                    HttpHeaderUtils.getAttachmentFileName( outFileName ) );
        }
        return responseBuilder.build();

    }

    private MediaType getMediaType( IXDocReport report, Options options )
        throws XDocConverterException
    {
        if ( options == null )
        {
            return MediaType.valueOf( report.getMimeMapping().getMimeType() );
        }
        IConverter converter = report.getConverter( options );
        return MediaType.valueOf( converter.getMimeMapping().getMimeType() );
    }

    private Options getOptions( final String outFormat, final String via )
    {
        if ( StringUtils.isEmpty( outFormat ) )
        {
            return null;
        }
        Options options = Options.getTo( outFormat );
        if ( StringUtils.isNotEmpty( via ) )
        {
            options.via( via );
        }
        return options;
    }

    protected Map toMap( String data, String dataType )
        throws Exception
    {
        // TODO : use well serializer of the data according dataType (json, xml, etc)
        return new JSONObject( data );
    }
}

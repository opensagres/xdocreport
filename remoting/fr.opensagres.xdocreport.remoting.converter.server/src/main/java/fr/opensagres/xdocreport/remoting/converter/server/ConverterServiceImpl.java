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
package fr.opensagres.xdocreport.remoting.converter.server;

import java.io.IOException;
import java.io.OutputStream;
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

import fr.opensagres.xdocreport.converter.ConverterRegistry;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.core.utils.Assert;
import fr.opensagres.xdocreport.core.utils.HttpHeaderUtils;
import fr.opensagres.xdocreport.remoting.converter.ConverterService;

/**
 * Document converter REST Web Service implementation.
 */
@Path( "/" )
public class ConverterServiceImpl
    implements ConverterService
{

    private static final Logger LOGGER = LogUtils.getLogger( ConverterServiceImpl.class );

    @POST
    @Consumes( MediaType.WILDCARD )
    @Produces( MediaType.WILDCARD )
    @Path( "/convert" )
    public Response convert( @Multipart( "document" )
    final DataSource content, @Multipart( "outputFormat" )
    String outputFormat, @Multipart( "via" )
    final String via, @Multipart( "download" )
    boolean download )
    {

        try
        {
            Assert.notNull( content.getName(), "file is required" );
            Assert.notNull( outputFormat, "outputFormat is required" );
            Assert.notNull( via, "via is required" );
            // 1) Get the converter type to use
            ConverterTypeTo to = ConverterTypeTo.valueOf( outputFormat );
            if ( to == null )
            {
                throw new XDocConverterException( "Converter service cannot support the output format=" + outputFormat );
            }

            // 2) retrieve the document kind from the input mimeType
            String mimeType = content.getContentType();
            DocumentKind documentKind = DocumentKind.fromMimeType( mimeType );
            if ( documentKind == null )
            {
                throw new XDocConverterException( "Converter service cannot support mime-type=" + mimeType );
            }

            // 3) Get the converter from the registry
            final Options options = Options.getFrom( documentKind ).to( to ).via( via );
            final IConverter converter = ConverterRegistry.getRegistry().findConverter( options );

            // 4) Create an instance of JAX-RS StreamingOutput to convert the inputstream and set the result in the
            // response stream.
            StreamingOutput output = new StreamingOutput()
            {
                public void write( OutputStream out )
                    throws IOException, WebApplicationException
                {
                    try
                    {
                        long start = System.currentTimeMillis();
                        converter.convert( content.getInputStream(), out, options );

                        if ( LOGGER.isLoggable( Level.INFO ) )
                        {
                            LOGGER.info( "Time spent to convert " + content.getName() + ": "
                                + ( System.currentTimeMillis() - start ) + " ms using " + via );
                        }
                    }
                    catch ( XDocConverterException e )
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
            ResponseBuilder responseBuilder = Response.ok( output, MediaType.valueOf( to.getMimeType() ) );
            if ( download )
            {
                // The converted document must be downloaded, add teh well content-disposition header.
                String fileName = content.getName();
                responseBuilder.header( HttpHeaderUtils.CONTENT_DISPOSITION_HEADER,
                                        HttpHeaderUtils.getAttachmentFileName( getOutputFileName( fileName, to ) ) );
            }
            return responseBuilder.build();

        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Returns the output file name.
     * 
     * @param filename
     * @param to
     * @return
     */
    protected String getOutputFileName( String filename, ConverterTypeTo to )
    {
        StringBuilder name = new StringBuilder( filename.replace( '.', '_' ) );
        name.append( '.' );
        name.append( to.getExtension() );
        return name.toString();
    }
}
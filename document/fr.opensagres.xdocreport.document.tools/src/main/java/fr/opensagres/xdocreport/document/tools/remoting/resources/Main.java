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
package fr.opensagres.xdocreport.document.tools.remoting.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Writer;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

import com.fasterxml.jackson.jakarta.rs.json.JacksonXmlBindJsonProvider;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.tools.internal.ArgContext;
import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.LargeBinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesServiceName;
import fr.opensagres.xdocreport.remoting.resources.services.ServiceType;
import fr.opensagres.xdocreport.remoting.resources.services.client.jaxrs.JAXRSResourcesServiceClientFactory;
import fr.opensagres.xdocreport.remoting.resources.services.client.jaxws.JAXWSResourcesServiceClientFactory;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.JAXRSResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.LargeBinaryDataMessageBodyReader;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.LargeBinaryDataMessageBodyWriter;
import fr.opensagres.xdocreport.remoting.resources.services.jaxws.JAXWSResourcesService;

public class Main
{

    public static final String BASE_ADDRESS_ARG = "-baseAddress";

    public static void main( String[] args )
        throws Exception
    {

        ArgContext context = new ArgContext( args );
        String baseAddress = context.get( BASE_ADDRESS_ARG );
        String user = context.get( "-password" );
        String password = context.get( "-password" );
        Long connectionTimeout = StringUtils.asLong( context.get( "-timeout" ) );
        Boolean allowChunking = StringUtils.asBoolean( context.get( "-chunk" ) );

        ServiceType serviceType = getServiceType( context.get( "-serviceType" ) );
        ResourcesServiceName serviceName = getServiceName( context.get( "-serviceName" ) );

        String out = context.get( "-out" );
        String err = context.get( "-err" );

        // Err
        File fileErr = null;
        if ( err != null )
        {
            fileErr = new File( err );
            if ( fileErr.exists() )
            {
                fileErr.delete();
            }
            else
            {
                fileErr.getParentFile().mkdirs();
            }
        }

        if ( fileErr == null )
        {
            process( baseAddress, user, password, connectionTimeout, allowChunking, serviceType, serviceName, out,
                     context );
        }
        else
        {
            try
            {
                process( baseAddress, user, password, connectionTimeout, allowChunking, serviceType, serviceName, out,
                         context );
            }
            catch ( Throwable e )
            {
                e.printStackTrace( new PrintStream( fileErr ) );
            }
        }
    }

    private static ServiceType getServiceType( String serviceType )
    {
        if ( ServiceType.JAXWS.name().equalsIgnoreCase( serviceType ) )
        {
            return ServiceType.JAXWS;
        }
        return ServiceType.JAXRS;
    }

    private static ResourcesServiceName getServiceName( String value )
    {
        return ResourcesServiceName.getServiceName( value );
    }

    private static void process( String baseAddress, String user, String password, Long connectionTimeout,
                                 Boolean allowChunking, ServiceType serviceType, ResourcesServiceName serviceName,
                                 String out, ArgContext context )
        throws IOException, ResourcesException
    {

        if ( serviceType == ServiceType.JAXRS )
        {
            processJAXRS( baseAddress, user, password, connectionTimeout, allowChunking, serviceName, out, context );
        }
        else
        {
            processJAXWS( baseAddress, user, password, connectionTimeout, allowChunking, serviceName, out, context );
        }

    }

    private static void processJAXRS( String baseAddress, String user, String password, Long connectionTimeout,
                                      Boolean allowChunking, ResourcesServiceName serviceName, String out,
                                      ArgContext context )
        throws IOException
    {
        String resources = null;
      Client client =  ClientBuilder.newBuilder()
    		  .register(JacksonXmlBindJsonProvider.class)
    		  .register(LargeBinaryDataMessageBodyReader.class)
    		  .register(LargeBinaryDataMessageBodyWriter.class)
    		  .build();

      JAXRSResourcesService jaxrsClient =
            JAXRSResourcesServiceClientFactory.create(client, baseAddress);
        switch ( serviceName )
        {
            case uploadLarge:
                resources = context.get( "-resources" );
                processUploadLargeLargeFile( jaxrsClient, resources, out );
                break;
            case downloadLarge:
                resources = context.get( "-resources" );
                processDownloadLargeFile( jaxrsClient, resources, out );
                break;
            default:
                processCommons( jaxrsClient, serviceName, out, context );
        }
    }

    private static void processJAXWS( String baseAddress, String user, String password, Long connectionTimeout,
                                      Boolean allowChunking, ResourcesServiceName serviceName, String out,
                                      ArgContext context )
        throws IOException
    {
        JAXWSResourcesService client =
            JAXWSResourcesServiceClientFactory.create( baseAddress, user, password, connectionTimeout, allowChunking );
        processCommons( client, serviceName, out, context );

    }

    private static void processCommons( ResourcesService client, ResourcesServiceName serviceName, String out,
                                        ArgContext context )
        throws IOException
    {
        String resources = null;
        switch ( serviceName )
        {
            case name:
                processName( client.getName(), new File( out ) );
                break;
            case root:
                processRoot( client.getRoot(), new File( out ) );
                break;
            case download:
                resources = context.get( "-resources" );
                processDownload( client, resources, out );
                break;
            case upload:
                resources = context.get( "-resources" );
                processUpload( client, resources, out );
                break;
        }
    }

    private static void processName( String repositoryName, File file )
        throws IOException
    {
        Writer writer = new FileWriter( file );
        try
        {
            writer.write( repositoryName );
        }
        finally
        {

            IOUtils.closeQuietly( writer );
        }
    }

    private static void processRoot( Resource root, File file )
        throws IOException
    {
        Writer writer = new FileWriter( file );
        try
        {
            toXML( root, writer );
        }
        finally
        {
            IOUtils.closeQuietly( writer );
        }
    }

    private static void processDownload( ResourcesService client, String resources, String out )
        throws IOException, ResourcesException
    {
        if ( StringUtils.isEmpty( resources ) )
        {
            throw new IOException( "resources must be not empty" );
        }
        if ( resources.indexOf( ";" ) == -1 )
        {

            BinaryData data = client.download( resources );
            binaryDataContentToFile( new File( out ), data );
        }
        else
        {
            // TODO : manage list of download
        }

        // String[] resources= s.split( ";" );
        // String[] outs= out.split( ";" );

    }

    private static void processUpload( ResourcesService client, String resources, String out )
        throws IOException
    {

        if ( StringUtils.isEmpty( resources ) )
        {
            throw new IOException( "resources must be not empty" );
        }
        if ( resources.indexOf( ";" ) == -1 )
        {
            BinaryData data = createBinaryDataFromFile( resources, new File( out ) );
            client.upload( data );
        }
        else
        {
            // TODO : manage list of uppload
        }

        // String[] resources= s.split( ";" );
        // String[] outs= out.split( ";" );

    }

    private static BinaryData createBinaryDataFromFile( String resourceId, File file )
        throws FileNotFoundException, IOException
    {
        FileInputStream input = new FileInputStream( file );

        byte[] content = IOUtils.toByteArray( input );
        // BinaryData data = new BinaryData( content, out.getName() );
        BinaryData data = new BinaryData();
        data.setContent( content );
        data.setFileName( file.getName() );
        data.setResourceId( resourceId );
        return data;
    }

    private static void binaryDataContentToFile( File outFile, BinaryData data )
        throws IOException
    {
        if ( data.getContent() != null )
        {
            createFile( data.getContent(), outFile );
        }
        else
        {
            createFile( data.getContent(), outFile );
        }
    }

    private static void processDownloadLargeFile( JAXRSResourcesService client, String resources, String out )
        throws IOException
    {
        if ( StringUtils.isEmpty( resources ) )
        {
            throw new IOException( "resources must be not empty" );
        }
        if ( resources.indexOf( ";" ) == -1 )
        {

            try ( LargeBinaryData data = client.downloadLarge( resources ) ) {
                binaryDataContentToFile( new File( out ), data );
            }
        }
        else
        {
            // TODO : manage list of download
        }

        // String[] resources= s.split( ";" );
        // String[] outs= out.split( ";" );

    }

    private static void processUploadLargeLargeFile( JAXRSResourcesService client, String resources, String out )
        throws IOException
    {
        if ( StringUtils.isEmpty( resources ) )
        {
            throw new IOException( "resources must be not empty" );
        }
        if ( resources.indexOf( ";" ) == -1 )
        {
            try ( LargeBinaryData data = createLargeBinaryDataFromFile( resources, new File( out ) ) ) {
                client.uploadLarge(data);
            }

        }
        else
        {
            // TODO : manage list of uppload
        }

        // String[] resources= s.split( ";" );
        // String[] outs= out.split( ";" );

    }

    private static LargeBinaryData createLargeBinaryDataFromFile( String resourceId, File file )
        throws FileNotFoundException, IOException
    {
        FileInputStream input = new FileInputStream( file );

        // BinaryData data = new BinaryData( content, out.getName() );
        LargeBinaryData data = new LargeBinaryData();
        data.setContent( input );
        data.setFileName( file.getName() );
        data.setResourceId( resourceId );
        return data;
    }

    private static void binaryDataContentToFile( File outFile, LargeBinaryData data )
        throws IOException
    {
        if ( data.getContent() != null )
        {
            createFile( data.getContent(), outFile );
        }
        else
        {
            createFile( data.getContent(), outFile );
        }
    }

    private static void createFile( byte[] flux, File outFile )
        throws IOException
    {
        if ( outFile.getParentFile() != null )
        {
            outFile.getParentFile().mkdirs();
        }
        FileOutputStream fos = new FileOutputStream( outFile );
        fos.write( flux );
        fos.close();
    }

    private static void createFile( InputStream input, File outFile )
        throws IOException
    {
        if ( outFile.getParentFile() != null )
        {
            outFile.getParentFile().mkdirs();
        }
        FileOutputStream out = new FileOutputStream( outFile );
        IOUtils.copy( input, out );
    }

    private static void toXML( Resource resource, Writer writer )
        throws IOException
    {
        String elementName = getElementName( resource );
        writer.write( "<" );
        writer.write( elementName );
        if ( StringUtils.isNotEmpty( resource.getId() ) )
        {
            writer.write( " id=\"" );
            writer.write( resource.getId() );
            writer.write( "\"" );
        }
        writer.write( " name=\"" );
        writer.write( resource.getName() );
        writer.write( "\"" );
        boolean hasChildren = resource.getChildren() != null && resource.getChildren().size() > 0;
        if ( hasChildren )
        {
            writer.write( ">" );
            for ( Resource child : resource.getChildren() )
            {
                toXML( child, writer );
            }
            writer.write( "</" );
            writer.write( elementName );
            writer.write( ">" );
        }
        else
        {
            writer.write( "/>" );
        }
    }

    private static String getElementName( Resource resource )
    {
        switch ( resource.getType() )
        {
            case DOCUMENT:
                // Document
                return "document";
            case TEMPLATE:
                // Template
                return "template";
            default:
                // Category
                return "category";
        }
    }

    private static void printUsage()
    {
        // TODO Auto-generated method stub

    }
}

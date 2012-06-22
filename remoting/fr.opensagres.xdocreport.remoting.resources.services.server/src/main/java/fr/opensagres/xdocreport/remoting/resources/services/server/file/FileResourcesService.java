package fr.opensagres.xdocreport.remoting.resources.services.server.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.LargeBinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.domain.ResourceFactory;
import fr.opensagres.xdocreport.remoting.resources.domain.ResourceType;
import fr.opensagres.xdocreport.remoting.resources.services.AbstractResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;
import fr.opensagres.xdocreport.remoting.resources.services.jaxrs.JAXRSResourcesService;

public abstract class FileResourcesService
    extends AbstractResourcesService
    implements JAXRSResourcesService
{

    private final File rootFolder;

    private final boolean templateHierarchy;

    public FileResourcesService( File rootFolder )
    {
        this( rootFolder, false );
    }

    public FileResourcesService( File rootFolder, boolean templateHierarchy )
    {
        this.rootFolder = rootFolder;
        this.templateHierarchy = templateHierarchy;
    }

    public Resource getRootWithFilter( Filter filter )
    {
        return toResource( getRootFolder(), null );
    }

    public BinaryData download( String resourceId )
        throws ResourcesException
    {
        String resourcePath = getResourcePath( resourceId );
        File file = new File( getRootFolder(), resourcePath );
        try
        {
            FileInputStream input = new FileInputStream( file );
            byte[] content = IOUtils.toByteArray( input );

            BinaryData data = new BinaryData();
            data.setContent( content );
            data.setFileName( file.getName() );
            data.setResourceId( resourceId );
            return data;
        }
        catch ( Exception e )
        {
            throw new ResourcesException( e );
        }
    }

    protected String getResourcePath( String resourceId )
    {
        return StringUtils.replaceAll( resourceId, "____", "/" );
    }

    public LargeBinaryData downloadLarge( String resourceId )
        throws ResourcesException
    {
        String resourcePath = getResourcePath( resourceId );
        File file = new File( getRootFolder(), resourcePath );
        try
        {
            FileInputStream input = new FileInputStream( file );
            LargeBinaryData data = new LargeBinaryData();
            data.setContent( input );
            data.setFileName( file.getName() );
            data.setResourceId( resourceId );
            return data;
        }
        catch ( Exception e )
        {
            throw new ResourcesException( e );
        }

    }

    public void uploadLarge( LargeBinaryData data )
        throws ResourcesException
    {

        String resourceId = data.getResourceId();
        InputStream input = data.getContent();
        String resourcePath = getResourcePath( resourceId );
        File file = new File( getRootFolder(), resourcePath );
        if ( !file.getParentFile().exists() )
        {
            file.getParentFile().mkdirs();
        }

        OutputStream output = null;
        try
        {

            output = new FileOutputStream( file );
            IOUtils.copyLarge( input, output );
        }
        catch ( IOException e )
        {
            throw new ResourcesException( e );
        }
        finally
        {

            if ( input != null )
            {
                IOUtils.closeQuietly( input );
            }
            if ( output != null )
            {
                IOUtils.closeQuietly( output );
            }
        }

    }

    public void upload( BinaryData data )
        throws ResourcesException
    {
        String resourceId = data.getResourceId();
        byte[] input = data.getContent();
        String resourcePath = getResourcePath( resourceId );
        File file = new File( getRootFolder(), resourcePath );
        if ( !file.getParentFile().exists() )
        {
            file.getParentFile().mkdirs();
        }

        OutputStream output = null;
        try
        {

            output = new FileOutputStream( file );
            IOUtils.write( input, output );
        }
        catch ( IOException e )
        {
            throw new ResourcesException( e );
        }
        finally
        {

            if ( output != null )
            {
                IOUtils.closeQuietly( output );
            }
        }

    }

    public File getRootFolder()
    {
        return rootFolder;
    }

    public Resource toSimpleResource( File file, Resource linkedResource )
    {
        boolean directory = file.isDirectory();
        Resource resource =
            ResourceFactory.createResource( file.getName(), directory ? ResourceType.FOLDER : ResourceType.FILE,
                                           linkedResource );
        if ( directory )
        {
            resource.setType( ResourceType.FOLDER );
            File[] files = file.listFiles();
            if ( files.length > 0 )
            {
                for ( int i = 0; i < files.length; i++ )
                {
                    toResource( files[i], resource );
                }
            }
        }
        return resource;
    }

    public Resource toResource( File file, Resource linkedResource )
    {
        if ( templateHierarchy && file.isFile() )
        {
            return ResourceFactory.createTemplate( file.getName(), linkedResource );
        }
        else
        {
            return toSimpleResource( file, linkedResource );
        }
    }

}

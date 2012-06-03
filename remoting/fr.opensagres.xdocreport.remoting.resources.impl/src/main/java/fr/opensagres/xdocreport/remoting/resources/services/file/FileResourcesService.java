package fr.opensagres.xdocreport.remoting.resources.services.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.AbstractResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;

public abstract class FileResourcesService
    extends AbstractResourcesService
{

    private final File rootFolder;

    public FileResourcesService( File rootFolder )
    {
        this.rootFolder = rootFolder;
    }

    public Resource getRoot( Filter filter )
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
        	FileInputStream input = new FileInputStream(file);
        	byte[] content=IOUtils.toByteArray(input);
            //BinaryData data = new BinaryData( content, file.getName() );
        	BinaryData data = new BinaryData( );
        	data.setContent(input);
        	data.setFileName(file.getName());
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

    public void upload( BinaryData data ) throws ResourcesException
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

    public File getRootFolder()
    {
        return rootFolder;
    }

    public static Resource toResource( File file, Resource linkedResource )
    {
        Resource resource = new Resource();
        // resource.setId( getId(file, linkedResource) );
        // resource.setParent( linkedResource );
        resource.setName( file.getName() );
        resource.setChildren( Collections.EMPTY_LIST );
        if ( linkedResource != null )
        {
            linkedResource.getChildren().add( resource );
        }
        if ( file.isDirectory() )
        {
            resource.setType( Resource.FOLDER_TYPE );
            File[] files = file.listFiles();
            if ( files.length > 0 )
            {
                resource.setChildren( new ArrayList<Resource>() );
                for ( int i = 0; i < files.length; i++ )
                {
                    toResource( files[i], resource );
                }
            }
        }
        else
        {
            resource.setType( Resource.FILE_TYPE );
        }

        return resource;
    }

}

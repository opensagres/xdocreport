package fr.opensagres.xdocreport.remoting.resources.services.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.remoting.resources.domain.BinaryDataIn;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.AbstractResourcesService;

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

    public byte[] download( String resourceId )
    {
        String resourcePath = getPath( resourceId );
        File file = new File( getRootFolder(), resourcePath );
        try
        {
            return IOUtils.toByteArray( new FileInputStream( file ) );
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private String getPath( String resourceId )
    {
        return StringUtils.replaceAll( resourceId, "____", "/" );
    }

    public void upload( BinaryDataIn data )
    {
        String resourceId = data.getResourceId();
        byte[] content = data.getContent();
        String resourcePath = getPath( resourceId );
        File file = new File( getRootFolder(), resourcePath );
        if ( !file.getParentFile().exists() )
        {
            file.getParentFile().mkdirs();
        }
        InputStream input = null;
        OutputStream output = null;
        try
        {
            input = new ByteArrayInputStream( content );
            output = new FileOutputStream( file );
            IOUtils.copyLarge( input, output );
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

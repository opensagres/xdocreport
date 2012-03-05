package fr.opensagres.xdocreport.remoting.resources.services.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.AbstractResourcesService;

public abstract class FileRepositoryService
    extends AbstractResourcesService
{

    private final File rootFolder;

    public FileRepositoryService( File rootFolder )
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
        // TODO Auto-generated method stub
        return StringUtils.replaceAll( resourceId, "____", "/" );
    }

    public void upload( String resourcePath, byte[] content )
    {
        // TODO Auto-generated method stub

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

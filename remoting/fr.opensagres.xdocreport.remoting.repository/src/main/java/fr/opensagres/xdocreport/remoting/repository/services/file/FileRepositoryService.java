package fr.opensagres.xdocreport.remoting.repository.services.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.opensagres.xdocreport.remoting.repository.domain.Filter;
import fr.opensagres.xdocreport.remoting.repository.domain.Resource;
import fr.opensagres.xdocreport.remoting.repository.services.AbstractRepositoryService;

public abstract class FileRepositoryService
    extends AbstractRepositoryService
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

    public List<byte[]> download( List<String> resourcePaths )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public byte[] download( String resourcePath )
    {
        // TODO Auto-generated method stub
        return null;
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
        //resource.setParent( linkedResource );
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

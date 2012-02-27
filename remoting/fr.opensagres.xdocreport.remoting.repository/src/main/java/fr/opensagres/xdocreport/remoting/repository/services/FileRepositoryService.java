package fr.opensagres.xdocreport.remoting.repository.services;

import java.io.File;
import java.util.List;

import fr.opensagres.xdocreport.remoting.repository.domain.Filter;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceContent;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceMetadata;

public abstract class FileRepositoryService
    extends AbstractRepositoryService
{

    private final File rootFolder;

    public FileRepositoryService( File rootFolder )
    {
        this.rootFolder = rootFolder;
    }

    public List<ResourceMetadata> getMetadatas( Filter filter )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public ResourceMetadata getMetadata( String resourceId, Filter filter )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public ResourceContent download( String resourceId, Filter filter )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void upload( ResourceContent content )
    {
        // TODO Auto-generated method stub

    }

}

package fr.opensagres.xdocreport.remoting.repository.services;

import java.util.List;

import fr.opensagres.xdocreport.remoting.repository.domain.Filter;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceContent;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceMetadata;

public interface IRepositoryService
{

    /**
     * Returns the repository name.
     * 
     * @return
     */
    String getName();

    List<ResourceMetadata> getMetadatas();

    List<ResourceMetadata> getMetadatas( Filter filter );

    ResourceMetadata getMetadata( String resourceId );

    ResourceMetadata getMetadata( String resourceId, Filter filter );

    ResourceContent download( String resourceId );

    ResourceContent download( String resourceId, Filter filter );

    void upload( ResourceContent content );

}
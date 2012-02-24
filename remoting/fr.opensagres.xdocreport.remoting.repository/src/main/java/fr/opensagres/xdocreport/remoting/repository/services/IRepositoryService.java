package fr.opensagres.xdocreport.remoting.repository.services;

import java.util.List;

import fr.opensagres.xdocreport.remoting.repository.domain.ResourceContent;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceMetadata;
import fr.opensagres.xdocreport.remoting.repository.domain.Filter;

public interface IRepositoryService
{

    List<ResourceMetadata> getMetadatas();

    List<ResourceMetadata> getMetadatas( Filter filter );

    ResourceMetadata getMetadata( String resourceId );

    ResourceMetadata getMetadata( String resourceId, Filter filter );

    ResourceContent download( String resourceId );

    ResourceContent download( String resourceId, Filter filter );

    void upload( ResourceContent content );

}
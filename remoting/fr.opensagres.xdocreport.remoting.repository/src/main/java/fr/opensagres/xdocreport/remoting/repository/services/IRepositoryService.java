package fr.opensagres.xdocreport.remoting.repository.services;

import java.util.List;

import fr.opensagres.xdocreport.remoting.repository.domain.Filter;
import fr.opensagres.xdocreport.remoting.repository.domain.Resource;

public interface IRepositoryService
{

    /**
     * Returns the repository name.
     * 
     * @return
     */
    String getName();

    /**
     * Returns the root resource.
     * 
     * @return
     */
    Resource getRoot();

    Resource getRoot( Filter filter );

    List<byte[]> download( List<String> resourcePaths );

    byte[] download( String resourcePath );

    void upload( String resourcePath, byte[] content );

}
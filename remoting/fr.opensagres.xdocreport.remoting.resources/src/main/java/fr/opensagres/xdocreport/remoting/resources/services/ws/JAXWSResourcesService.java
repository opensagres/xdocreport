package fr.opensagres.xdocreport.remoting.resources.services.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;

@WebService( name = "ResourcesService" )
public interface JAXWSResourcesService
    extends ResourcesService
{

    /**
     * Returns the repository name.
     *
     * @return
     */
    @WebMethod( operationName = "getName")
    String getName();

    /**
     * Returns the root resource.
     *
     * @return
     */
    @WebMethod( operationName = "getRoot")
    Resource getRoot()
        throws ResourcesException;

    @WebMethod( operationName = "getRoot1")
    Resource getRoot( @WebParam( name = "filter" )
    Filter filter )
        throws ResourcesException;

    @WebMethod( operationName = "download")
    List<BinaryData> download( @WebParam( name = "resourceIds" )
    List<String> resourceIds )
        throws ResourcesException;

    /**
     * Download the content of the given unique resource id.
     *
     * @param resourcePath the unique resource id.
     * @return the byte array of the content of the given resourcePath.
     */
    @WebMethod( operationName = "download1")
    BinaryData download( @WebParam( name = "resourceId" )
    String resourceId )
        throws ResourcesException;

    @WebMethod( operationName = "upload")
    void upload( @WebParam( name = "data" )
    BinaryData data )
        throws ResourcesException;
}

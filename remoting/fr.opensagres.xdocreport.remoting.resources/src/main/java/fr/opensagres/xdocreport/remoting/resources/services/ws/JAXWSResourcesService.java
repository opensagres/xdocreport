package fr.opensagres.xdocreport.remoting.resources.services.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;

@WebService( name = "ResourcesService" )
public interface JAXWSResourcesService
{

    /**
     * Returns the repository name.
     *
     * @return
     */
    @WebMethod
    String getName();

    /**
     * Returns the root resource.
     *
     * @return
     */
    @WebMethod
    Resource getRoot();

    @WebMethod
    Resource getRootWithFilter( @WebParam( name = "filter" )
    Filter filter );

    @WebMethod
    List<BinaryData> downloadMultiple( @WebParam( name = "resourceIds" )
    List<String> resourceIds );

    /**
     * Download the content of the given unique resource id.
     *
     * @param resourcePath the unique resource id.
     * @return the byte array of the content of the given resourcePath.
     */
    @WebMethod
    BinaryData download( @WebParam( name = "resourceId" )
    String resourceId );

    @WebMethod
    void upload( @WebParam( name = "data" )
    BinaryData data );
}

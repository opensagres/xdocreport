package fr.opensagres.xdocreport.remoting.resources.services.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;

@WebService( name = "ResourcesService", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
public interface JAXWSResourcesService
    extends ResourcesService
{

    /**
     * Returns the repository name.
     * 
     * @return
     */
    @WebMethod( operationName = "getName", action = "urn:GetName" )
    @RequestWrapper( className = "fr.opensagres.xdocreport.remoting.resources.services.ws.client.jaxws.GetName", localName = "getName", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
    @ResponseWrapper( className = "fr.opensagres.xdocreport.remoting.resources.services.ws.client.jaxws.GetNameResponse", localName = "getNameResponse", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
    String getName();

    /**
     * Returns the root resource.
     * 
     * @return
     */
    @WebMethod( operationName = "getRoot", action = "urn:GetRoot" )
    @RequestWrapper( className = "fr.opensagres.xdocreport.remoting.resources.services.ws.client.jaxws.GetRoot", localName = "getRoot", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
    @ResponseWrapper( className = "fr.opensagres.xdocreport.remoting.resources.services.ws.client.jaxws.GetRootResponse", localName = "getRootResponse", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
    Resource getRoot()
        throws ResourcesException;

    @WebMethod( operationName = "getRoot1", action = "urn:GetRoot1" )
    @RequestWrapper( className = "fr.opensagres.xdocreport.remoting.resources.services.ws.client.jaxws.GetRoot1", localName = "getRoot1", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
    @ResponseWrapper( className = "fr.opensagres.xdocreport.remoting.resources.services.ws.client.jaxws.GetRootResponse1", localName = "getRootResponse1", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
    Resource getRoot( @WebParam( name = "arg0" )
    Filter filter )
        throws ResourcesException;

    @WebMethod( operationName = "download", action = "urn:Download" )
    @RequestWrapper( className = "fr.opensagres.xdocreport.remoting.resources.services.ws.client.jaxws.Download", localName = "download", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
    @ResponseWrapper( className = "fr.opensagres.xdocreport.remoting.resources.services.ws.client.jaxws.DownloadResponse", localName = "downloadResponse", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
    List<BinaryData> download( @WebParam( name = "arg0" )
    List<String> resourceIds )
        throws ResourcesException;

    /**
     * Download the content of the given unique resource id.
     * 
     * @param resourcePath the unique resource id.
     * @return the byte array of the content of the given resourcePath.
     */
    @WebMethod( operationName = "download1", action = "urn:Download1" )
    @RequestWrapper( className = "fr.opensagres.xdocreport.remoting.resources.services.ws.client.jaxws.Download1", localName = "download1", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
    @ResponseWrapper( className = "fr.opensagres.xdocreport.remoting.resources.services.ws.client.jaxws.DownloadResponse1", localName = "downloadResponse1", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
    BinaryData download( @WebParam( name = "arg0" )
    String resourceId )
        throws ResourcesException;

    @WebMethod( operationName = "upload", action = "urn:Upload" )
    @RequestWrapper( className = "fr.opensagres.xdocreport.remoting.resources.services.ws.client.jaxws.Upload", localName = "upload", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
    @ResponseWrapper( className = "fr.opensagres.xdocreport.remoting.resources.services.ws.client.jaxws.UploadResponse", localName = "uploadResponse", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
    void upload( @WebParam( name = "arg0" )
    BinaryData data )
        throws ResourcesException;
}

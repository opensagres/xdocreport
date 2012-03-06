package fr.opensagres.xdocreport.remoting.resources.services.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;

@WebService( name = "ResourcesService", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
public interface JAXWSResourcesService
    extends ResourcesService
{

    @WebMethod( operationName = "getName", action = "urn:GetName" )
    public String getName();

    @WebMethod( operationName = "getRoot", action = "urn:GetRoot" )
    public Resource getRoot();

    @WebMethod( operationName = "download", action = "urn:Download1" )
    @RequestWrapper( className = "fr.opensagres.xdocreport.remoting.resources.services.jaxws.Download1", localName = "download1", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
    @ResponseWrapper( className = "fr.opensagres.xdocreport.remoting.resources.services.jaxws.DownloadResponse1", localName = "download1Response", targetNamespace = "http://services.resources.remoting.xdocreport.opensagres.fr/" )
    public BinaryData download( @WebParam( name = "resourceId" )
    String resourceId );
}

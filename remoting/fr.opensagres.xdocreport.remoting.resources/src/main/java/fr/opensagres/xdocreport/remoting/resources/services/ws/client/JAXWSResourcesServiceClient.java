package fr.opensagres.xdocreport.remoting.resources.services.ws.client;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesException;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ws.JAXWSResourcesService;

public class JAXWSResourcesServiceClient
    implements JAXWSResourcesService
{

    private final ResourcesService client;

    public JAXWSResourcesServiceClient( String baseAddress )
    {
        QName serviceName =
            new QName( "http://services.resources.remoting.xdocreport.opensagres.fr/", "ResourcesServiceService" );
        QName portName =
            new QName( "http://services.resources.remoting.xdocreport.opensagres.fr/", "ResourcesServicePort" );

        Service service = Service.create( serviceName );
        service.addPort( portName, SOAPBinding.SOAP11HTTP_BINDING, baseAddress );
        this.client = service.getPort( portName, JAXWSResourcesService.class );

    }

    public String getName()
    {
        return client.getName();
    }

    public Resource getRoot()
        throws ResourcesException
    {
        return client.getRoot();
    }

    public Resource getRoot( Filter filter )
        throws ResourcesException
    {
        return client.getRoot( filter );
    }

    public List<BinaryData> download( List<String> resourceIds )
        throws ResourcesException
    {
        return client.download( resourceIds );
    }

    public BinaryData download( String resourceId )
        throws ResourcesException
    {
        return client.download( resourceId );
    }

    public void upload( BinaryData data )
        throws ResourcesException
    {
        client.upload( data );

    }
}

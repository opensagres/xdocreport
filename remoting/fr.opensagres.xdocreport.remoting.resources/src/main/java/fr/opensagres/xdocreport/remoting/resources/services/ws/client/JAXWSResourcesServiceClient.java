package fr.opensagres.xdocreport.remoting.resources.services.ws.client;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;

public class JAXWSResourcesServiceClient
    implements ResourcesService
{

    private final ResourcesService client;

    public JAXWSResourcesServiceClient( String baseAddress )
    {
        QName serviceName =
            new QName( "http://services.resources.remoting.xdocreport.opensagres.fr/", "ResourcesServiceImplService" );
        QName portName =
            new QName( "http://services.resources.remoting.xdocreport.opensagres.fr/", "ResourcesServiceImplPort" );

        Service service = Service.create( serviceName );
        service.addPort( portName, SOAPBinding.SOAP11HTTP_BINDING, baseAddress + "/ResourcesServiceImplPort" );
        this.client =
            service.getPort( portName, fr.opensagres.xdocreport.remoting.resources.services.ResourcesService.class );

    }

    public String getName()
    {
        return client.getName();
    }

    public Resource getRoot()
    {
        return client.getRoot();
    }

    public Resource getRoot( Filter filter )
    {
        return client.getRoot( filter );
    }

    public List<byte[]> download( List<String> resourcePaths )
    {
        return client.download( resourcePaths );
    }

    public byte[] download( String resourcePath )
    {
        return client.download( resourcePath );
    }

    public void upload( String resourcePath, byte[] content )
    {
        client.upload( resourcePath, content );
    }

}

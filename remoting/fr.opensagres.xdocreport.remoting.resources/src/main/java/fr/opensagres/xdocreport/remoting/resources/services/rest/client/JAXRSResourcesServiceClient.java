package fr.opensagres.xdocreport.remoting.resources.services.rest.client;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;

import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesServiceName;

public class JAXRSResourcesServiceClient
    implements ResourcesService
{

    private final WebClient client;

    public JAXRSResourcesServiceClient( String baseAddress )
    {
        this.client = WebClient.create( baseAddress );
    }

    public String getName()
    {
        return client.path( ResourcesServiceName.name ).accept( MediaType.TEXT_PLAIN ).get( String.class );
    }

    public Resource getRoot()
    {
        return client.path( ResourcesServiceName.root ).accept( MediaType.APPLICATION_JSON ).get( Resource.class );
    }

    public Resource getRoot( Filter filter )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public List<byte[]> download( List<String> resourcePaths )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public byte[] download( String resourcePath )
    {
        StringBuilder path = new StringBuilder( ResourcesServiceName.download.name() );
        path.append( "/" );
        path.append( resourcePath );
        return client.path( path.toString() ).accept( MediaType.APPLICATION_JSON_TYPE ).get( byte[].class );
    }

    public void upload( String resourcePath, byte[] content )
    {
        // TODO Auto-generated method stub

    }

}

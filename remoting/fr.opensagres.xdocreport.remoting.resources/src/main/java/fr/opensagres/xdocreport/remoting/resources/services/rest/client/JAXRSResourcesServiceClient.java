package fr.opensagres.xdocreport.remoting.resources.services.rest.client;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.remoting.resources.domain.BinaryDataIn;
import fr.opensagres.xdocreport.remoting.resources.domain.Filter;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesService;
import fr.opensagres.xdocreport.remoting.resources.services.ResourcesServiceName;

public class JAXRSResourcesServiceClient
    implements ResourcesService
{

    private final WebClient client;

    public JAXRSResourcesServiceClient( String baseAddress, String username, String password )
    {
        if ( StringUtils.isNotEmpty( username ) )
        {
            this.client = WebClient.create( baseAddress, username, password, null );
        }
        else
        {
            this.client = WebClient.create( baseAddress );
        }

    }

    public String getName()
    {
        reset();
        return client.path( ResourcesServiceName.name ).accept( MediaType.TEXT_PLAIN ).get( String.class );
    }

    public Resource getRoot()
    {
        reset();
        return client.path( ResourcesServiceName.root ).accept( MediaType.APPLICATION_JSON ).get( Resource.class );
    }

    public Resource getRoot( Filter filter )
    {
        reset();
        return null;
    }

    public List<byte[]> download( List<String> resourceIds )
    {
        reset();
        return null;
    }

    public byte[] download( String resourceId )
    {
        reset();
        StringBuilder path = new StringBuilder( ResourcesServiceName.download.name() );
        path.append( "/" );
        path.append( resourceId );
        return client.path( path.toString() ).accept( MediaType.APPLICATION_JSON_TYPE ).get( byte[].class );
    }

    public void upload( BinaryDataIn data )
    {
        reset();
        client.path( ResourcesServiceName.upload ).type( MediaType.APPLICATION_JSON ).post( data );
    }

    protected void reset()
    {
        client.reset();

    }
}

package fr.opensagres.xdocreport.remoting.repository.services.rest.client;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;

import fr.opensagres.xdocreport.remoting.repository.domain.Filter;
import fr.opensagres.xdocreport.remoting.repository.domain.Resource;
import fr.opensagres.xdocreport.remoting.repository.services.IRepositoryService;
import fr.opensagres.xdocreport.remoting.repository.services.ServiceName;

public class JAXRSRepositoryServiceClient
    implements IRepositoryService
{

    private final WebClient client;

    public JAXRSRepositoryServiceClient( String baseAddress )
    {
        this.client = WebClient.create( baseAddress );
    }

    public String getName()
    {
        return client.path( ServiceName.name ).accept( MediaType.TEXT_PLAIN ).get( String.class );
    }

    public Resource getRoot()
    {
        return client.path( ServiceName.root ).accept( MediaType.APPLICATION_JSON ).get( Resource.class );
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
        // TODO Auto-generated method stub
        return null;
    }

    public void upload( String resourcePath, byte[] content )
    {
        // TODO Auto-generated method stub

    }

}

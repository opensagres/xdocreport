package fr.opensagres.xdocreport.remoting.repository.services.rest.client;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;

import fr.opensagres.xdocreport.remoting.repository.domain.Filter;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceContent;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceMetadata;
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
        return client.path( ServiceName.name ).accept( MediaType.APPLICATION_JSON ).get( String.class );
    }

    public List<ResourceMetadata> getMetadatas()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public List<ResourceMetadata> getMetadatas( Filter filter )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public ResourceMetadata getMetadata( String resourceId )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public ResourceMetadata getMetadata( String resourceId, Filter filter )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public ResourceContent download( String resourceId )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public ResourceContent download( String resourceId, Filter filter )
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void upload( ResourceContent content )
    {
        // TODO Auto-generated method stub

    }

}

package fr.opensagres.xdocreport.remoting.repository.services.ws.client;

import java.util.List;

import fr.opensagres.xdocreport.remoting.repository.domain.Filter;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceContent;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceMetadata;
import fr.opensagres.xdocreport.remoting.repository.services.IRepositoryService;

public class JAXWSRepositoryServiceClient
    implements IRepositoryService
{

    public JAXWSRepositoryServiceClient( String baseAddress )
    {
        // TODO Auto-generated constructor stub
    }

    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
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

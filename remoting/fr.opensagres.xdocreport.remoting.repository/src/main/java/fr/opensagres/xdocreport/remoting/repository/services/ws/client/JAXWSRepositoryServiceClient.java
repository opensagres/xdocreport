package fr.opensagres.xdocreport.remoting.repository.services.ws.client;

import java.util.List;

import fr.opensagres.xdocreport.remoting.repository.domain.Filter;
import fr.opensagres.xdocreport.remoting.repository.domain.Resource;
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

    public Resource getRoot()
    {
        // TODO Auto-generated method stub
        return null;
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

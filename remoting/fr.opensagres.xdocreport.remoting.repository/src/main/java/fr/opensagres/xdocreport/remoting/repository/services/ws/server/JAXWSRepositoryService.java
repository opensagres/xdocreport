package fr.opensagres.xdocreport.remoting.repository.services.ws.server;

import java.util.List;

import fr.opensagres.xdocreport.remoting.repository.domain.Filter;
import fr.opensagres.xdocreport.remoting.repository.domain.Resource;
import fr.opensagres.xdocreport.remoting.repository.services.DelegateRepositoryService;

public class JAXWSRepositoryService
    extends DelegateRepositoryService
{

    @Override
    public String getName()
    {
        return super.getName();
    }

    @Override
    public Resource getRoot()
    {
        return super.getRoot();
    }

    @Override
    public Resource getRoot( Filter filter )
    {
        return super.getRoot( filter );
    }

    @Override
    public byte[] download( String resourcePath )
    {
        return super.download( resourcePath );
    }

    @Override
    public List<byte[]> download( List<String> resourcePaths )
    {
        return super.download( resourcePaths );
    }

    @Override
    public void upload( String resourcePath, byte[] content )
    {
        super.upload( resourcePath, content );
    }
}

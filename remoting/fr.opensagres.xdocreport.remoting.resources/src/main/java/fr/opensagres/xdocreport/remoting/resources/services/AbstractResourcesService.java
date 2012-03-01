package fr.opensagres.xdocreport.remoting.resources.services;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;

public abstract class AbstractResourcesService
    implements ResourcesService
{

    public Resource getRoot()
    {
        return getRoot( null );
    }

    public List<byte[]> download( List<String> resourcePaths )
    {
        List<byte[]> contents = new ArrayList<byte[]>( resourcePaths.size() );
        for ( String resourcePath : resourcePaths )
        {
            contents.add( download( resourcePath ) );
        }
        return contents;
    }

    protected ByteArrayInputStream getInputStream( byte[] content )
        throws XDocReportException
    {
        if ( content == null )
        {
            throw new XDocReportException( "Byte array of the document cannot be null." );
        }
        return new ByteArrayInputStream( content );
    }

}

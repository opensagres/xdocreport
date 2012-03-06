package fr.opensagres.xdocreport.remoting.resources.services;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.remoting.resources.domain.BinaryData;
import fr.opensagres.xdocreport.remoting.resources.domain.Resource;

public abstract class AbstractResourcesService
    implements ResourcesService
{

    public Resource getRoot()
        throws ResourcesException
    {
        return getRoot( null );
    }

    public List<BinaryData> download( List<String> resourceIds )
        throws ResourcesException
    {
        List<BinaryData> contents = new ArrayList<BinaryData>( resourceIds.size() );
        for ( String id : resourceIds )
        {
            contents.add( download( id ) );
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

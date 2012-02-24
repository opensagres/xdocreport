package fr.opensagres.xdocreport.remoting.repository.services;

import java.io.ByteArrayInputStream;
import java.util.List;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceContent;
import fr.opensagres.xdocreport.remoting.repository.domain.ResourceMetadata;

public abstract class AbstractRepositoryService
    implements IRepositoryService
{

    public List<ResourceMetadata> getMetadatas()
    {
        return getMetadatas( null );
    }

    public ResourceMetadata getMetadata( String resourceId )
    {
        return getMetadata( resourceId, null );
    }

    public ResourceContent download( String resourceId )
    {
        return download( resourceId, null );
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

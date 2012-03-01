package fr.opensagres.xdocreport.remoting.repository.services;

import java.io.ByteArrayInputStream;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.remoting.repository.domain.Resource;

public abstract class AbstractRepositoryService
    implements IRepositoryService
{

    public Resource getRoot()
    {
        return getRoot( null );
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

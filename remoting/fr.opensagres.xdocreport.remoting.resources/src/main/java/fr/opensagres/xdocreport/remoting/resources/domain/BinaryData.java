package fr.opensagres.xdocreport.remoting.resources.domain;

import java.io.InputStream;

public class BinaryData
{

    private InputStream stream;

    public InputStream getStream()
    {
        return stream;
    }

//    void save( OutputStream out )
//        throws IOException
//    {
//        IOUtils.copy( getStream(), out );
//    }
}

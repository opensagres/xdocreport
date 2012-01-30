package fr.opensagres.xdocreport.document.tools;

import java.io.IOException;
import java.io.InputStream;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.tools.internal.StringBuilderOutputStream;

public abstract class AbstractDataProvider
    implements IDataProvider
{

    private final InputStream data;

    private final InputStream properties;

    public AbstractDataProvider( InputStream data, InputStream properties )
    {
        this.data = data;
        this.properties = properties;
    }

    public InputStream getData()
    {
        return data;
    }

    public InputStream getProperties()
    {
        return properties;
    }

    public String getDataAsString()
        throws IOException
    {
        StringBuilderOutputStream writer = new StringBuilderOutputStream();
        IOUtils.copy( getData(), writer );
        return writer.toString();
    }

}

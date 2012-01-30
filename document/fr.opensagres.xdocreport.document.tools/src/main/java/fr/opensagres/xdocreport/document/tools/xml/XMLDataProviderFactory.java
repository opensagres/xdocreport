package fr.opensagres.xdocreport.document.tools.xml;

import java.io.InputStream;
import java.io.OutputStream;

import fr.opensagres.xdocreport.document.tools.AbstractDataProviderFactory;
import fr.opensagres.xdocreport.document.tools.IDataProvider;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class XMLDataProviderFactory
    extends AbstractDataProviderFactory
{

    private static final String ID = "xml";

    private static final String DESCRIPTION = "XML Data Provider";

    public XMLDataProviderFactory()
    {
        super( ID, DESCRIPTION );
    }

    public IDataProvider create( InputStream data, InputStream properties )
        throws Exception
    {
        return new XMLDataProvider( data, properties );
    }

    public void generateDefaultData( FieldsMetadata fieldsMetadata, OutputStream out )
        throws Exception
    {
        // TODO Auto-generated method stub

    }
}

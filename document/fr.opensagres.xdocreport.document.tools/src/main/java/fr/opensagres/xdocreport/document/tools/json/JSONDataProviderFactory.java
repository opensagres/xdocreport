package fr.opensagres.xdocreport.document.tools.json;

import java.io.InputStream;
import java.io.OutputStream;

import fr.opensagres.xdocreport.document.tools.AbstractDataProviderFactory;
import fr.opensagres.xdocreport.document.tools.IDataProvider;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class JSONDataProviderFactory
    extends AbstractDataProviderFactory
{

    private static final String ID = "json";

    private static final String DESCRIPTION = "JSON Data Provider";

    public JSONDataProviderFactory()
    {
        super( ID, DESCRIPTION );
    }

    public IDataProvider create( InputStream data, InputStream properties )
        throws Exception
    {
        return new JSONDataProvider( data, properties );
    }

    public void generateDefaultData( FieldsMetadata fieldsMetadata, OutputStream out )
        throws Exception
    {
        // Generate JSON
        FieldsMetadataJSONSerializer.getInstance().save( fieldsMetadata, out, true );
    }
}

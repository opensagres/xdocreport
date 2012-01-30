package fr.opensagres.xdocreport.document.tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.registry.AbstractRegistry;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class DataProviderFactoryRegistry
    extends AbstractRegistry<IDataProviderFactory>
{

    private static final DataProviderFactoryRegistry INSTANCE = new DataProviderFactoryRegistry();

    private Map<String, IDataProviderFactory> factories = new HashMap<String, IDataProviderFactory>();

    public static DataProviderFactoryRegistry getRegistry()
    {
        return INSTANCE;
    }

    public DataProviderFactoryRegistry()
    {
        super( IDataProviderFactory.class );
    }

    @Override
    protected boolean registerInstance( IDataProviderFactory factory )
    {
        factories.put( factory.getId(), factory );
        return true;
    }

    @Override
    protected void doDispose()
    {
        factories.clear();
    }

    public IDataProvider create( String id, InputStream data, InputStream properties )
        throws Exception
    {
        if ( id == null )
        {
            return null;
        }
        IDataProviderFactory factory = getFactory( id );
        if ( factory == null )
        {
            return null;
        }
        return factory.create( data, properties );
    }

    public void generateDefaultData( String id, FieldsMetadata fieldsMetadata, OutputStream out )
        throws Exception
    {
        if ( id == null )
        {
            return;
        }
        IDataProviderFactory factory = getFactory( id );
        if ( factory == null )
        {
            return;
        }
        factory.generateDefaultData( fieldsMetadata, out );
    }

    public IDataProviderFactory getFactory( String id )
    {
        initializeIfNeeded();
        return factories.get( id );
    }

}

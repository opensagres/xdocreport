package fr.opensagres.xdocreport.template.registry;

import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.registry.AbstractRegistry;
import fr.opensagres.xdocreport.template.formatter.IFieldsMetadataClassSerializer;

public class FieldsMetadataClassSerializerRegistry
    extends AbstractRegistry<IFieldsMetadataClassSerializer>
{

    private static final FieldsMetadataClassSerializerRegistry INSTANCE = new FieldsMetadataClassSerializerRegistry();

    private Map<String, IFieldsMetadataClassSerializer> serializers =
        new HashMap<String, IFieldsMetadataClassSerializer>();

    public static FieldsMetadataClassSerializerRegistry getRegistry()
    {
        return INSTANCE;
    }

    public FieldsMetadataClassSerializerRegistry()
    {
        super( IFieldsMetadataClassSerializer.class );
    }

    @Override
    protected boolean registerInstance( IFieldsMetadataClassSerializer factory )
    {
        serializers.put( factory.getId(), factory );
        return true;
    }

    @Override
    protected void doDispose()
    {
        serializers.clear();
    }

    public IFieldsMetadataClassSerializer getSerializer( String id )
    {
        if ( id == null )
        {
            return null;
        }
        initializeIfNeeded();
        return serializers.get( id );
    }

}

package fr.opensagres.xdocreport.template.formatter;

import java.util.Comparator;

public class FieldsMetadataComparator
    implements Comparator<FieldMetadata>
{

    private static final FieldsMetadataComparator INSTANCE = new FieldsMetadataComparator();

    public static FieldsMetadataComparator getInstance()
    {
        return INSTANCE;
    }

    @Override
    public int compare( FieldMetadata field1, FieldMetadata field2 )
    {
        return FieldsNameComparator.getInstance().compare( field1.getFieldName(), field2.getFieldName() );
    }

}

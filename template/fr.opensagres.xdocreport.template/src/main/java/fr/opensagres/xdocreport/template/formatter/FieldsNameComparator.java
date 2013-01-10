package fr.opensagres.xdocreport.template.formatter;

import java.util.Comparator;

public class FieldsNameComparator
    implements Comparator<String>
{

    private static final FieldsNameComparator INSTANCE = new FieldsNameComparator();

    public static FieldsNameComparator getInstance()
    {
        return INSTANCE;
    }

    @Override
    public int compare( String field1, String field2 )
    {
        return field2.compareTo( field1 );
    }

}

package fr.opensagres.xdocreport.document.docx.preprocessor.sax.numbering;

import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class NumberingRegistry
{

    public static final String numbersMethod = "numbers";

    private List<NumberInfo> numbers;

    public NumberingRegistry()
    {
        this.numbers = new ArrayList<NumberInfo>();
    }

    public static boolean hasDynamicNumbering( FieldsMetadata fieldsMetadata )
    {
        if ( fieldsMetadata == null )
        {
            return false;
        }
        if ( fieldsMetadata.getFieldsAsTextStyling().size() < 1 )
        {
            return false;
        }
        return true;
    }

    public NumberInfo addNum( int abstractNumId, Integer maxNumId )
    {
        int numId = maxNumId != null ? maxNumId.intValue() + 1 + numbers.size() : numbers.size();
        NumberInfo info = new NumberInfo( numId, abstractNumId );
        numbers.add( info );
        return info;
    }

    public List<NumberInfo> getNumbers()
    {
        return numbers;
    }
}

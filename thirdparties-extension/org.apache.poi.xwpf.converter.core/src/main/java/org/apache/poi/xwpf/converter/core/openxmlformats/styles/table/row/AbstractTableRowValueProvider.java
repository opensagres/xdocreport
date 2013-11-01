package org.apache.poi.xwpf.converter.core.openxmlformats.styles.table.row;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;

public abstract class AbstractTableRowValueProvider<Value>
{

    public Value getValue( CTRow row, XWPFStylesDocument document )
    {
        Value value = null;
        // from paragraph
        CTTrPr trPr = row.getTrPr();
        if ( trPr != null )
        {
            // from table row inline
            value = getValue( trPr, document );
            if ( value != null )
            {
                return value;
            }

        }
        return null;
    }

    public abstract Value getValue( CTTrPr row, XWPFStylesDocument document );
}

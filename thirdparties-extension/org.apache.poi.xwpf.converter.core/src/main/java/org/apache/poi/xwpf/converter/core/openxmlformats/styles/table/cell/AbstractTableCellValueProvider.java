package org.apache.poi.xwpf.converter.core.openxmlformats.styles.table.cell;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

public abstract class AbstractTableCellValueProvider<Value>
{

    public Value getValue( CTTc cell, XWPFStylesDocument document )
    {
        Value value = null;
        // from paragraph
        CTTcPr tcPr = cell.getTcPr();
        if ( tcPr != null )
        {
            // from table cell inline
            value = getValue( tcPr, document );
            if ( value != null )
            {
                return value;
            }
        }
        return null;
    }

    public abstract Value getValue( CTTcPr tcPr, XWPFStylesDocument document );
}

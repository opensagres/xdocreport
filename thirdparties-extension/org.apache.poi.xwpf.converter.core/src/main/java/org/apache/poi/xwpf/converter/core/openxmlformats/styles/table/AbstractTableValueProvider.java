package org.apache.poi.xwpf.converter.core.openxmlformats.styles.table;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;

public abstract class AbstractTableValueProvider<Value>
{

    public Value getValue( CTTbl table, XWPFStylesDocument document )
    {
        Value value = null;
        // from paragraph
        CTTblPr tblPr = table.getTblPr();
        if ( tblPr != null )
        {
            // from table inline
            value = getValue( tblPr, document );
            if ( value != null )
            {
                return value;
            }
            // from table style
            value = getValueFromStyle( tblPr.getTblStyle(), document );
            if ( value != null )
            {
                return value;
            }

        }
        return null;
    }

    private Value getValueFromStyle( CTString styleId, XWPFStylesDocument document )
    {
        // Get the style
        CTStyle style = document.getStyle( styleId );
        if ( style != null )
        {
            return getValue( style.getTblPr(), document );
        }
        return null;
    }

    public abstract Value getValue( CTTblPr tblPr, XWPFStylesDocument document );

    public abstract Value getValue( CTTblPrBase tblPr, XWPFStylesDocument document );
}

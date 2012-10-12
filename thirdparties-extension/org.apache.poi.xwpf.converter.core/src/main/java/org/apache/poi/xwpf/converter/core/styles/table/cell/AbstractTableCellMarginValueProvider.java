package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.apache.poi.xwpf.converter.core.utils.DxaUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

public abstract class AbstractTableCellMarginValueProvider
    extends AbstractTableCellValueProvider<Float>
{

    @Override
    public Float getValue( CTTcPr tcPr )
    {
        if ( tcPr == null )
        {
            return null;
        }
        // see http://officeopenxml.com/WPtableCellMargins.php
        CTTcMar margin = tcPr.getTcMar();
        if ( margin != null )
        {
            CTTblWidth tblWidth = getValue( margin );
            if ( tblWidth == null )
            {
                return null;
            }
            // type:
            // => dxa - Specifies that the value is in twentieths of a point (1/1440 of an inch).
            // => nil - Specifies a value of zero
            STTblWidth.Enum type = tblWidth.getType();
            if ( type != null && type.equals( STTblWidth.NIL ) )
            {
                return 0f;
            }
            return DxaUtil.dxa2points( tblWidth.getW() );
        }
        return null;
    }

    public abstract CTTblWidth getValue( CTTcMar margin );

}

package org.apache.poi.xwpf.converter.core.styles.table.row;

import static org.apache.poi.xwpf.converter.core.utils.DxaUtil.dxa2points;

import java.math.BigInteger;

import org.apache.poi.xwpf.converter.core.TableHeight;
import org.apache.poi.xwpf.converter.core.TableWidth;
import org.apache.poi.xwpf.converter.core.utils.DxaUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

public class TableRowHeightValueProvider
    extends AbstractTableRowValueProvider<TableHeight>
{

    public static final TableRowHeightValueProvider INSTANCE = new TableRowHeightValueProvider();

    @Override
    public TableHeight getValue( CTTrPr trPr )
    {
        if ( trPr == null )
        {
            return null;
        }
        if ( trPr.sizeOfTrHeightArray() == 0 )
        {
            return null;
        }
        // see http://officeopenxml.com/WPtableRowProperties.php
        CTHeight trHeight = trPr.getTrHeightArray( 0 );
        org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule.Enum hRule = trHeight.getHRule();
        boolean minimum = true;
        // hRule -- Specifies the meaning of the height. Possible values :
        if ( hRule != null )
        {
            switch ( hRule.intValue() )
            {
                case STHeightRule.INT_AT_LEAST:
                    // are atLeast (height should be at leasat the
                    // value specified)
                    minimum = true;
                    break;
                case STHeightRule.INT_EXACT:
                    // exact (height should be exactly the value specified)
                    minimum = false;
                    break;
                case STHeightRule.INT_AUTO:
                    // auto (default value--height is determined based on the height of the contents, so the value is
                    // ignored)
                    return null;
            }
        }
        // val -- Specifies the row's height, in twentieths of a point.
        BigInteger value = trHeight.getVal();
        float height = DxaUtil.dxa2points( value );
        return new TableHeight( height, minimum );
    }

    public TableWidth getTableWidth( CTTblWidth tblWidth )
    {
        if ( tblWidth == null )
        {
            return null;
        }
        float width = tblWidth.getW().intValue();
        boolean percentUnit = ( STTblWidth.INT_PCT == tblWidth.getType().intValue() );
        if ( percentUnit )
        {
            width = width / 100f;
        }
        else
        {
            width = dxa2points( width );
        }
        return new TableWidth( width, percentUnit );
    }
}

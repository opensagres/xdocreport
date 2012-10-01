package org.apache.poi.xwpf.converter.core.styles.table;

import static org.apache.poi.xwpf.converter.core.utils.DxaUtil.dxa2points;

import org.apache.poi.xwpf.converter.core.utils.TableWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

public class TableWidthValueProvider
    extends AbstractTableValueProvider<TableWidth>
{

    public static final TableWidthValueProvider INSTANCE = new TableWidthValueProvider();

    @Override
    public TableWidth getValue( CTTblPr tblPr )
    {
        if ( tblPr == null )
        {
            return null;
        }
        CTTblWidth tblWidth = tblPr.getTblW();
        return getTableWidth( tblWidth );
    }

    @Override
    public TableWidth getValue( CTTblPrBase tblPr )
    {
        CTTblWidth tblWidth = tblPr.getTblW();
        return getTableWidth( tblWidth );
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

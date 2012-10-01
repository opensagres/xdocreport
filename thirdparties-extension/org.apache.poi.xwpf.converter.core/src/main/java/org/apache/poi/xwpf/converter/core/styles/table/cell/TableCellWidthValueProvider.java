package org.apache.poi.xwpf.converter.core.styles.table.cell;

import static org.apache.poi.xwpf.converter.core.utils.DxaUtil.dxa2points;

import org.apache.poi.xwpf.converter.core.utils.TableWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

public class TableCellWidthValueProvider
    extends AbstractTableCellValueProvider<TableWidth>
{

    public static final TableCellWidthValueProvider INSTANCE = new TableCellWidthValueProvider();

    @Override
    public TableWidth getValue( CTTcPr tcPr )
    {
        if ( tcPr == null )
        {
            return null;
        }
        CTTblWidth tblWidth = tcPr.getTcW();
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

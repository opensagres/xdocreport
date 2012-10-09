package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

public class TableCellBordersValueProvider
    extends AbstractTableCellValueProvider<CTTcBorders>
{

    public static final TableCellBordersValueProvider INSTANCE = new TableCellBordersValueProvider();

    @Override
    public CTTcBorders getValue( CTTcPr tcPr )
    {
        if ( tcPr == null )
        {
            return null;
        }
        return tcPr.getTcBorders();
    }

}

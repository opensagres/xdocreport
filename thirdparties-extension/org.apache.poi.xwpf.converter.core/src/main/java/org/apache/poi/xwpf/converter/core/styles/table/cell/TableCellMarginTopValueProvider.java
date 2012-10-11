package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcMar;

public class TableCellMarginTopValueProvider
    extends AbstractTableCellMarginValueProvider
{

    public static final TableCellMarginTopValueProvider INSTANCE =
        new TableCellMarginTopValueProvider();

    @Override
    public CTTblWidth getValue( CTTcMar margin )
    {
        return margin.getTop();
    }
}

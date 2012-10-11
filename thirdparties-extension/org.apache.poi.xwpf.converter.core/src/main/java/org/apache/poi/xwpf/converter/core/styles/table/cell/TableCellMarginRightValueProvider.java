package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcMar;

public class TableCellMarginRightValueProvider
    extends AbstractTableCellMarginValueProvider
{

    public static final TableCellMarginRightValueProvider INSTANCE =
        new TableCellMarginRightValueProvider();

    @Override
    public CTTblWidth getValue( CTTcMar margin )
    {
        return margin.getRight();
    }
}

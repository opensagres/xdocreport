package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;

public class TableCellBorderBottomValueProvider
    extends AbstractTableCellBorderValueProvider
{

    public static final TableCellBorderBottomValueProvider INSTANCE = new TableCellBorderBottomValueProvider();

    @Override
    public CTBorder getBorder( CTTcBorders borders )
    {
        return borders.getBottom();
    }

}

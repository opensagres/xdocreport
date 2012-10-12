package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;

public class TableCellBorderLeftValueProvider
    extends AbstractTableCellBorderValueProvider
{

    public static final TableCellBorderLeftValueProvider INSTANCE = new TableCellBorderLeftValueProvider();

    @Override
    public CTBorder getBorder( CTTcBorders borders )
    {
        return borders.getLeft();
    }
    
    @Override
    protected boolean isInside()
    {
        return false;
    }

}

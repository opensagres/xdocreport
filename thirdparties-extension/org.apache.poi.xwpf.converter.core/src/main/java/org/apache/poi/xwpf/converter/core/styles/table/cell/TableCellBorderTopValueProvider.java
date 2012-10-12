package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;

public class TableCellBorderTopValueProvider
    extends AbstractTableCellBorderValueProvider
{

    public static final TableCellBorderTopValueProvider INSTANCE = new TableCellBorderTopValueProvider();

    @Override
    public CTBorder getBorder( CTTcBorders borders )
    {
        return borders.getTop();
    }
    
    @Override
    protected boolean isInside()
    {
        return false;
    }

}

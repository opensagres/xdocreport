package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;

public class TableCellBorderInsideVValueProvider
    extends AbstractTableCellBorderValueProvider
{

    public static final TableCellBorderInsideVValueProvider INSTANCE = new TableCellBorderInsideVValueProvider();

    @Override
    public CTBorder getBorder( CTTcBorders borders )
    {
        return borders.getInsideV();
    }
    
    @Override
    protected boolean isInside()
    {
        return true;
    }

}

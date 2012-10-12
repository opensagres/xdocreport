package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;

public class TableCellBorderInsideHValueProvider
    extends AbstractTableCellBorderValueProvider
{

    public static final TableCellBorderInsideHValueProvider INSTANCE = new TableCellBorderInsideHValueProvider();

    @Override
    public CTBorder getBorder( CTTcBorders borders )
    {
        return borders.getInsideH();
    }
    
    @Override
    protected boolean isInside()
    {
        return true;
    }

}

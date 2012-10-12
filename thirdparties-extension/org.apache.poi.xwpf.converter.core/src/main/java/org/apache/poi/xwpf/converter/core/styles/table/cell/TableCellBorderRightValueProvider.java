package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;

public class TableCellBorderRightValueProvider
    extends AbstractTableCellBorderValueProvider
{

    public static final TableCellBorderRightValueProvider INSTANCE = new TableCellBorderRightValueProvider();

    @Override
    public CTBorder getBorder( CTTcBorders borders )
    {
        return borders.getRight();
    }
    
    @Override
    protected boolean isInside()
    {
        return false;
    }

}

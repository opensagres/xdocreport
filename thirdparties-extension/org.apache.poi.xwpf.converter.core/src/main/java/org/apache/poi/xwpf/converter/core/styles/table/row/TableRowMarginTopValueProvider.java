package org.apache.poi.xwpf.converter.core.styles.table.row;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;

public class TableRowMarginTopValueProvider
    extends AbstractTableRowMarginValueProvider
{

    public static final TableRowMarginTopValueProvider INSTANCE = new TableRowMarginTopValueProvider();

    @Override
    public CTTblWidth getValue( CTTblCellMar margin )
    {
        return margin.getTop();
    }

}

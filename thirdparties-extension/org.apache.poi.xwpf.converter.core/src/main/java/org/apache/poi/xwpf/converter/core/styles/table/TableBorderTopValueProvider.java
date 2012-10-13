package org.apache.poi.xwpf.converter.core.styles.table;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;

public class TableBorderTopValueProvider
    extends AbstractTableBorderValueProvider
{

    public static final TableBorderTopValueProvider INSTANCE = new TableBorderTopValueProvider();

    @Override
    public CTBorder getBorder( CTTblBorders borders )
    {
        return borders.getTop();
    }

}

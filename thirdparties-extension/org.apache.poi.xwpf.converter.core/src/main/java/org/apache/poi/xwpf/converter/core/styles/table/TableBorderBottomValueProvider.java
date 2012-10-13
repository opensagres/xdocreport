package org.apache.poi.xwpf.converter.core.styles.table;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;

public class TableBorderBottomValueProvider
    extends AbstractTableBorderValueProvider
{

    public static final TableBorderBottomValueProvider INSTANCE = new TableBorderBottomValueProvider();

    @Override
    public CTBorder getBorder( CTTblBorders borders )
    {
        return borders.getBottom();
    }

}

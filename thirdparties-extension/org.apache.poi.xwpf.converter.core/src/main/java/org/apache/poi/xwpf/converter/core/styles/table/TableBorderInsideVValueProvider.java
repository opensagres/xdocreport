package org.apache.poi.xwpf.converter.core.styles.table;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;

public class TableBorderInsideVValueProvider
    extends AbstractTableBorderValueProvider
{

    public static final TableBorderInsideVValueProvider INSTANCE = new TableBorderInsideVValueProvider();

    @Override
    public CTBorder getBorder( CTTblBorders borders )
    {
        return borders.getInsideV();
    }

}

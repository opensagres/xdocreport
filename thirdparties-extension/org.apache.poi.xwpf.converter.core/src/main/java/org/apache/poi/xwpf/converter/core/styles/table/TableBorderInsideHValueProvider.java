package org.apache.poi.xwpf.converter.core.styles.table;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;

public class TableBorderInsideHValueProvider
    extends AbstractTableBorderValueProvider
{

    public static final TableBorderInsideHValueProvider INSTANCE = new TableBorderInsideHValueProvider();

    @Override
    public CTBorder getBorder( CTTblBorders borders )
    {
        return borders.getInsideH();
    }

    @Override
    protected boolean isInside()
    {
        return true;
    }
}

package org.apache.poi.xwpf.converter.core.styles.table;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;

public class TableBorderRightValueProvider
    extends AbstractTableBorderValueProvider
{

    public static final TableBorderRightValueProvider INSTANCE = new TableBorderRightValueProvider();

    @Override
    public CTBorder getBorder( CTTblBorders borders )
    {
        return borders.getRight();
    }

}

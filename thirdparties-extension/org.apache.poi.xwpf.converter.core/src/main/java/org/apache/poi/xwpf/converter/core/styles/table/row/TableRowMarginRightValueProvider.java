package org.apache.poi.xwpf.converter.core.styles.table.row;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;

public class TableRowMarginRightValueProvider
    extends AbstractTablelRowMarginValueProvider
{

    public static final TableRowMarginRightValueProvider INSTANCE = new TableRowMarginRightValueProvider();

    @Override
    public CTTblWidth getValue( CTTblCellMar margin )
    {
        return margin.getRight();
    }

}

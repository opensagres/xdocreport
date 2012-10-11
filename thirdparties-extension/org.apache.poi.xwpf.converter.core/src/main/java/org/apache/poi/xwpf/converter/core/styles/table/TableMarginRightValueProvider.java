package org.apache.poi.xwpf.converter.core.styles.table;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;

public class TableMarginRightValueProvider
    extends AbstractTablelMarginValueProvider
{

    public static final TableMarginRightValueProvider INSTANCE = new TableMarginRightValueProvider();

    @Override
    public CTTblWidth getValue( CTTblCellMar margin )
    {
        return margin.getRight();
    }

}

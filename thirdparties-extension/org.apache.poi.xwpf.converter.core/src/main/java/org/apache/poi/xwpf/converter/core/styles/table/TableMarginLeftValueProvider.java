package org.apache.poi.xwpf.converter.core.styles.table;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;

public class TableMarginLeftValueProvider
    extends AbstractTablelMarginValueProvider
{

    public static final TableMarginLeftValueProvider INSTANCE = new TableMarginLeftValueProvider();

    @Override
    public CTTblWidth getValue( CTTblCellMar margin )
    {
        return margin.getLeft();
    }

}

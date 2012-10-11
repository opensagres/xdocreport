package org.apache.poi.xwpf.converter.core.styles.table;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;

public class TableMarginTopValueProvider
    extends AbstractTablelMarginValueProvider
{

    public static final TableMarginTopValueProvider INSTANCE = new TableMarginTopValueProvider();

    @Override
    public CTTblWidth getValue( CTTblCellMar margin )
    {
        return margin.getTop();
    }

}

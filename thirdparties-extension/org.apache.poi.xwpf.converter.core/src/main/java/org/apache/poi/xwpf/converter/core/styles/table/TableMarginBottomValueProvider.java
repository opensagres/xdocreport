package org.apache.poi.xwpf.converter.core.styles.table;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;

public class TableMarginBottomValueProvider
    extends AbstractTablelMarginValueProvider
{

    public static final TableMarginBottomValueProvider INSTANCE = new TableMarginBottomValueProvider();

    @Override
    public CTTblWidth getValue( CTTblCellMar margin )
    {
        return margin.getBottom();
    }

}

package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.apache.poi.xwpf.converter.core.utils.XWPFUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

public class TableCellNoWrapValueProvider
    extends AbstractTableCellValueProvider<Boolean>
{

    public static final TableCellNoWrapValueProvider INSTANCE = new TableCellNoWrapValueProvider();

    @Override
    public Boolean getValue( CTTcPr tcPr )
    {
        if ( tcPr != null )
        {
            CTOnOff noWrap = tcPr.getNoWrap();
            if ( noWrap != null )
            {
                return XWPFUtils.isCTOnOff( noWrap );
            }
        }
        return null;
    }
}

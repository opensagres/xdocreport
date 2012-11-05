package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge.Enum;

public class TableCellVMergeValueProvider
    extends AbstractTableCellValueProvider<Enum>
{

    public static final TableCellVMergeValueProvider INSTANCE = new TableCellVMergeValueProvider();

    @Override
    public Enum getValue( CTTcPr tcPr )
    {
        if ( tcPr != null )
        {
            CTVMerge vMerge = tcPr.getVMerge();
            if ( vMerge != null )
            {
                Enum val = vMerge.getVal();
                if ( val == null )
                {
                    return STMerge.CONTINUE;
                }
                return val;
            }
        }
        return null;
    }
}

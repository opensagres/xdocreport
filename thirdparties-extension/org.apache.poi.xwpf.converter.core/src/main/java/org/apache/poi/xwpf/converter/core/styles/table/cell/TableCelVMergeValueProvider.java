package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge.Enum;

public class TableCelVMergeValueProvider
    extends AbstractTableCellValueProvider<Enum>
{

    public static final TableCelVMergeValueProvider INSTANCE = new TableCelVMergeValueProvider();

    @Override
    public Enum getValue( CTTcPr tcPr )
    {
        if ( tcPr != null )
        {
            CTVMerge vMerge = tcPr.getVMerge();
            if ( vMerge != null )
            {
                return vMerge.getVal();
            }
        }
        return null;
    }
}

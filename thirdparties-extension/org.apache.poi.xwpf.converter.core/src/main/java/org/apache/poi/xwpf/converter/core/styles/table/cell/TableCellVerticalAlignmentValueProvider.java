package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc.Enum;

public class TableCellVerticalAlignmentValueProvider
    extends AbstractTableCellValueProvider<Enum>
{

    public static final TableCellVerticalAlignmentValueProvider INSTANCE =
        new TableCellVerticalAlignmentValueProvider();

    @Override
    public Enum getValue( CTTcPr tcPr )
    {
        if ( tcPr != null )
        {
            CTVerticalJc jc = tcPr.getVAlign();
            if ( jc != null )
            {
                return jc.getVal();
            }
        }
        return null;
    }
}

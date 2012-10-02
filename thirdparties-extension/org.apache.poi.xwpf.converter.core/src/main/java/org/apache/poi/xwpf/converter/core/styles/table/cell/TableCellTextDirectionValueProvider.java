package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection;

public class TableCellTextDirectionValueProvider
    extends AbstractTableCellValueProvider<CTTextDirection>
{

    public static final TableCellTextDirectionValueProvider INSTANCE = new TableCellTextDirectionValueProvider();

    @Override
    public CTTextDirection getValue( CTTcPr tcPr )
    {
        if ( tcPr != null )
        {
            return tcPr.getTextDirection();
        }
        return null;
    }
}

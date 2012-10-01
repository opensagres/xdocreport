package org.apache.poi.xwpf.converter.core.styles.table.cell;

import java.math.BigInteger;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

public class TableCellGridSpanValueProvider
    extends AbstractTableCellValueProvider<BigInteger>
{

    public static final TableCellGridSpanValueProvider INSTANCE =
        new TableCellGridSpanValueProvider();

    @Override
    public BigInteger getValue( CTTcPr tcPr )
    {
        if ( tcPr != null )
        {
            CTDecimalNumber gridSpan = tcPr.getGridSpan();
            if ( gridSpan != null )
            {
                return gridSpan.getVal();
            }
        }
        return null;
    }
}

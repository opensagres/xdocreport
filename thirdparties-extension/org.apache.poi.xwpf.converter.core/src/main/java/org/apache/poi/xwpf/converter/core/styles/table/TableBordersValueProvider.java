package org.apache.poi.xwpf.converter.core.styles.table;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;

public class TableBordersValueProvider
    extends AbstractTableValueProvider<CTTblBorders>
{

    public static final TableBordersValueProvider INSTANCE = new TableBordersValueProvider();

    @Override
    public CTTblBorders getValue( CTTblPr tblPr )
    {
        return CTTblBorders( tblPr );
    }

    @Override
    public CTTblBorders getValue( CTTblPrBase tblPr )
    {
        return CTTblBorders( tblPr );
    }

    private CTTblBorders CTTblBorders( CTTblPrBase tblPr )
    {
        if ( tblPr == null )
        {
            return null;
        }
        return tblPr.getTblBorders();
    }

}

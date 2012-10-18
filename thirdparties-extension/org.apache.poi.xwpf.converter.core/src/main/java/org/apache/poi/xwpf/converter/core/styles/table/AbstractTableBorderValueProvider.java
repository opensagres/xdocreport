package org.apache.poi.xwpf.converter.core.styles.table;

import org.apache.poi.xwpf.converter.core.TableCellBorder;
import org.apache.poi.xwpf.converter.core.utils.XWPFTableUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;

public abstract class AbstractTableBorderValueProvider
    extends AbstractTableValueProvider<TableCellBorder>
{

    @Override
    public TableCellBorder getValue( CTTblPr tblPr )
    {
        return getTableCellBorder( tblPr );
    }

    @Override
    public TableCellBorder getValue( CTTblPrBase tblPr )
    {
        return getTableCellBorder( tblPr );
    }

    public TableCellBorder getTableCellBorder( CTTblPrBase tblPr )
    {
        if ( tblPr == null )
        {
            return null;
        }
        CTTblBorders borders = tblPr.getTblBorders();
        return getTableCellBorder( borders );
    }

    private TableCellBorder getTableCellBorder( CTTblBorders borders )
    {
        if ( borders != null )
        {
            CTBorder border = getBorder( borders );
            return XWPFTableUtil.getTableCellBorder( border, false );
        }
        return null;
    }

    public abstract CTBorder getBorder( CTTblBorders borders );

}

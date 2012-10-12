package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.apache.poi.xwpf.converter.core.TableCellBorder;
import org.apache.poi.xwpf.converter.core.utils.XWPFTableUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

public abstract class AbstractTableCellBorderValueProvider
    extends AbstractTableCellValueProvider<TableCellBorder>
{

    @Override
    public TableCellBorder getValue( CTTcPr tcPr )
    {
        if ( tcPr == null )
        {
            return null;
        }
        CTTcBorders borders = tcPr.getTcBorders();
        return getTableCellBorder( borders );
    }

    private TableCellBorder getTableCellBorder( CTTcBorders borders )
    {
        if ( borders != null )
        {
            CTBorder border = getBorder( borders );
            return XWPFTableUtil.getTableCellBorder( border, isInside() );
        }
        return null;
    }
    
    protected abstract boolean isInside();

    public abstract CTBorder getBorder( CTTcBorders borders );

}

package org.apache.poi.xwpf.converter.core.styles.table.cell;

import java.awt.Color;

import org.apache.poi.xwpf.converter.core.utils.ColorHelper;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

public class TableCellBackgroundColorValueProvider
    extends AbstractTableCellValueProvider<Color>
{

    public static final TableCellBackgroundColorValueProvider INSTANCE = new TableCellBackgroundColorValueProvider();

    @Override
    public Color getValue( CTTcPr tcPr )
    {
        if ( tcPr != null )
        {
            return ColorHelper.getFillColor( tcPr.getShd() );
        }
        return null;
    }
}

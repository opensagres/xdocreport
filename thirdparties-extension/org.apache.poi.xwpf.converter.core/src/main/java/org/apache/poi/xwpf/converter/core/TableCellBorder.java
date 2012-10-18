package org.apache.poi.xwpf.converter.core;

import java.awt.Color;

public class TableCellBorder
{

    private final boolean hasBorder;

    private final Float borderSize;

    private final Color borderColor;

    private final boolean fromTableCell;

    public TableCellBorder( boolean hasBorder, boolean fromTableCell )
    {
        this.hasBorder = hasBorder;
        this.borderSize = null;
        this.borderColor = null;
        this.fromTableCell = fromTableCell;
    }

    public TableCellBorder( Float borderSize, Color borderColor, boolean fromTableCell )
    {
        this.hasBorder = true;
        this.borderSize = borderSize;
        this.borderColor = borderColor;
        this.fromTableCell = fromTableCell;
    }

    public boolean hasBorder()
    {
        return hasBorder;
    }

    public Float getBorderSize()
    {
        return borderSize;
    }

    public Color getBorderColor()
    {
        return borderColor;
    }

    public boolean isFromTableCell()
    {
        return fromTableCell;
    }

}

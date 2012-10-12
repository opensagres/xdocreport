package org.apache.poi.xwpf.converter.core;

import java.awt.Color;

public class TableCellBorder
{

    private final boolean hasBorder;

    private final Float borderSize;

    private final Color borderColor;

    public TableCellBorder( boolean hasBorder )
    {
        this.hasBorder = hasBorder;
        this.borderSize = null;
        this.borderColor = null;
    }

    public TableCellBorder( Float borderSize, Color borderColor )
    {
        this.hasBorder = true;
        this.borderSize = borderSize;
        this.borderColor = borderColor;
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

}

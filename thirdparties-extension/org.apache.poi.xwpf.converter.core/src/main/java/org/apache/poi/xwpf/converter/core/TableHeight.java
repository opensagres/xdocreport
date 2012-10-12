package org.apache.poi.xwpf.converter.core;

public class TableHeight
{

    public final float height;

    public final boolean minimum;

    public TableHeight( float height, boolean minimum )
    {
        this.height = height;
        this.minimum = minimum;
    }
}

package org.apache.poi.xwpf.converter.xhtml.internal.styles;

public class CSSProperty
{

    private final String name;

    private final String value;

    public CSSProperty( String name, String value )
    {
        this.name = name;
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }
}

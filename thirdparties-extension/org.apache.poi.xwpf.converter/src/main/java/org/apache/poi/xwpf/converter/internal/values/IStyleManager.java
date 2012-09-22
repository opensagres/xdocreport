package org.apache.poi.xwpf.converter.internal.values;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

public interface IStyleManager
{
    public static final Object EMPTY_VALUE = new Object();

    CTStyle getStyle( String styleId );

    Object getValue( IValueProvider provider, String styleId );

}

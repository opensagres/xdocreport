package org.apache.poi.xwpf.converter.internal.values;

public interface IValueProvider<Value, XWPElement>
{
    Value getValue( XWPElement element, IStyleManager styleManager );

}

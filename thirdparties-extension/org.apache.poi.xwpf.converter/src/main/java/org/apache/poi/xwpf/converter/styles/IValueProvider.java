package org.apache.poi.xwpf.converter.styles;

public interface IValueProvider<Value, XWPElement>
{
    Value getValue( XWPElement element, XWPFStylesDocument styleManager );

}

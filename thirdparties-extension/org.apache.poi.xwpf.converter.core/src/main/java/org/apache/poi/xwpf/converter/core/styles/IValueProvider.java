package org.apache.poi.xwpf.converter.core.styles;


public interface IValueProvider<Value, XWPElement>
{
    Value getValue( XWPElement element, XWPFStylesDocument styleManager );

}

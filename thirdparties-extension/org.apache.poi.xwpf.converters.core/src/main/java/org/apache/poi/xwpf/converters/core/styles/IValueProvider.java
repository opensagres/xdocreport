package org.apache.poi.xwpf.converters.core.styles;


public interface IValueProvider<Value, XWPElement>
{
    Value getValue( XWPElement element, XWPFStylesDocument styleManager );

}

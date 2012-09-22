package org.apache.poi.xwpf.converter.styles;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

public abstract class AbstractValueProvider<Value, XWPFElement>
    implements IValueProvider<Value, XWPFElement>
{

    public final Value getValue( XWPFElement element, XWPFStylesDocument styleManager )
    {
        // Returns value retrieved from the XWPF element (XWPFParagraph, XWPFTable etc)
        Value value = getValueFromElement( element );
        if ( value != null )
        {
            return value;
        }
        if ( styleManager == null )
        {
            return null;
        }
        // Search value from styles
        return getValueFromStyles( element, styleManager );
    }

    public abstract Value getValueFromElement( XWPFElement element );

    public Value getValueFromStyles( XWPFElement element, XWPFStylesDocument styleManager )
    {
        Value value = getValueFromStyleId( element, styleManager );
        if ( value != null )
        {
            return value;
        }
        return getValueFromDefaultStyle( element, styleManager );
    }

    private Value getValueFromStyleId( XWPFElement element, XWPFStylesDocument styleManager )
    {
        String styleId = getStyleID( element );

        // Value value = styleManager.getValue( this, styleId );
        // if ( value != null )
        // {
        // return (Value) value;
        // }
        Value value = null;
        CTStyle style = styleManager.getStyle( styleId );
        while ( style != null )
        {
            value = getValueFromStyle( style, styleManager );
            if ( value != null )
            {
                return null;
            }
            style = styleManager.getStyle( ( getBasisStyleID( style ) ) );
        }

        return null;
    }

    private String getBasisStyleID( CTStyle style )
    {
        if ( style.getBasedOn() != null )
            return style.getBasedOn().getVal();
        else
            return null;
    }

    private Value getValueFromStyle( CTStyle style, XWPFStylesDocument styleManager )
    {
        return getValueFromStyle( style );
    }

    protected abstract String getStyleID( XWPFElement element );

    protected abstract Value getValueFromStyle( CTStyle style );

    protected Value getValueFromDefaultStyle( XWPFElement element, XWPFStylesDocument styleManager )
    {
        Value value = null;
        CTStyle style = getDefaultStype( element, styleManager );
        if ( style != null )
        {
            value = getValueFromStyle( style );
        }
        return value;
    }

    protected abstract CTStyle getDefaultStype( XWPFElement element, XWPFStylesDocument styleManager );

}

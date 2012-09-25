package org.apache.poi.xwpf.converter.styles;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

public abstract class AbstractValueProvider<Value, XWPFElement>
    implements IValueProvider<Value, XWPFElement>
{

    public Value getValue( XWPFElement element, XWPFStylesDocument stylesDocument )
    {
        // Returns value retrieved from the XWPF element (XWPFParagraph, XWPFTable etc)
        Value value = getValueFromElement( element );
        if ( value != null )
        {
            return value;
        }
        if ( stylesDocument == null )
        {
            return null;
        }

        return getValueFromStyles( element, stylesDocument );
    }

    public Value getValueFromStyles( XWPFElement element, XWPFStylesDocument stylesDocument )
    {
        Value value = null;
        String[] styleIds = getStyleID( element );
        if ( styleIds != null )
        {
            String styleId = null;
            for ( int i = 0; i < styleIds.length; i++ )
            {
                styleId = styleIds[i];

                String key = getKey( element, stylesDocument, styleId );

                Object result = stylesDocument.getValue( key );
                if ( result != null )
                {
                    // System.err.println("use cache=" + key);
                    return result.equals( XWPFStylesDocument.EMPTY_VALUE ) ? null : (Value) result;
                }

                // Search value from styles
                // System.err.println("compute style=" + key);
                value = getValueFromStyles( element, stylesDocument, styleId, key );
                if ( value != null )
                {
                    stylesDocument.setValue( key, value );
                    return value;
                }
                else
                {
                    stylesDocument.setValue( key, XWPFStylesDocument.EMPTY_VALUE );
                }
            }
        }
        return value;
    }

    public abstract Value getValueFromElement( XWPFElement element );

    public Value getValueFromStyles( XWPFElement element, XWPFStylesDocument stylesDocument, String styleId, String key )
    {

        Value value = getValueFromStyleId( element, stylesDocument, styleId );
        if ( value != null )
        {
            return value;
        }
        return getDefaultValue( element, stylesDocument );
    }

    protected Value getDefaultValue( XWPFElement element, XWPFStylesDocument stylesDocument )
    {
        Value value;
        value = getValueFromDefaultStyle( element, stylesDocument );
        if ( value != null )
        {
            return value;
        }
        value = getValueFromDocDefaultsStyle( element, stylesDocument );
        if ( value != null )
        {
            return value;
        }
        return getStaticValue( element, stylesDocument );
    }

    protected Value getStaticValue( XWPFElement element, XWPFStylesDocument stylesDocument )
    {
        return null;
    }

    private Value getValueFromStyleId( XWPFElement element, XWPFStylesDocument stylesDocument, String styleId )
    {

        // Value value = stylesDocument.getValue( this, styleId );
        // if ( value != null )
        // {
        // return (Value) value;
        // }
        Value value = null;
        CTStyle style = stylesDocument.getStyle( styleId );
        while ( style != null )
        {
            value = getValueFromStyle( style, stylesDocument );
            if ( value != null )
            {
                return value;
            }
            style = stylesDocument.getStyle( ( getBasisStyleID( style ) ) );
        }

        return null;
    }

    protected String getKey( XWPFElement element, XWPFStylesDocument stylesDocument, String styleId )
    {
        if ( styleId != null && styleId.length() > 0 )
        {
            return new StringBuilder( this.getClass().getName() ).append( "_" ).append( styleId ).toString();
        }
        return this.getClass().getName();
    }

    private String getBasisStyleID( CTStyle style )
    {
        if ( style.getBasedOn() != null )
            return style.getBasedOn().getVal();
        else
            return null;
    }

    private Value getValueFromStyle( CTStyle style, XWPFStylesDocument stylesDocument )
    {
        return getValueFromStyle( style );
    }

    protected abstract String[] getStyleID( XWPFElement element );

    protected abstract Value getValueFromStyle( CTStyle style );

    protected Value getValueFromDefaultStyle( XWPFElement element, XWPFStylesDocument stylesDocument )
    {
        Value value = null;
        CTStyle style = getDefaultStyle( element, stylesDocument );
        if ( style != null )
        {
            value = getValueFromStyle( style );
        }
        return value;
    }

    protected Value getValueFromDocDefaultsStyle( XWPFElement element, XWPFStylesDocument stylesDocument )
    {
        CTDocDefaults docDefaults = stylesDocument.getDocDefaults();
        if ( docDefaults == null )
        {
            return null;
        }
        Value value = getValueFromDocDefaultsStyle( stylesDocument.getDocDefaults() );
        if ( value != null )
        {
            return value;
        }
        return null;
    }

    protected abstract Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults );

    protected abstract CTStyle getDefaultStyle( XWPFElement element, XWPFStylesDocument stylesDocument );

}

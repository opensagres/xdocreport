package org.apache.poi.xwpf.converter.styles;

import org.apache.poi.xwpf.converter.internal.StringUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

public abstract class AbstractValueProvider<Value, XWPFElement>
    implements IValueProvider<Value, XWPFElement>
{

    public Value getValue( XWPFElement element, XWPFStylesDocument stylesDocument )
    {
        // long start = System.currentTimeMillis();
        Value value = internalGetValue( element, stylesDocument );
        // System.err.println( "value=" + value + " with " + ( System.currentTimeMillis() - start ) + "ms" );
        return value;

    };

    public Value internalGetValue( XWPFElement element, XWPFStylesDocument stylesDocument )
    {
        // 1) Inline style : search value retrieved from the XWPF element (XWPFParagraph, XWPFTable etc)
        Value value = getValueFromElement( element );
        if ( value != null )
        {
            // Value declared in the inline style, return it.
            return value;
        }
        if ( stylesDocument == null )
        {
            return null;
        }
        // 2) External styles: search value declared in a style.
        return getValueFromStyles( element, stylesDocument );
    }

    public Value getValueFromStyles( XWPFElement element, XWPFStylesDocument stylesDocument )
    {

        // 1) At first get from cache or compute the default value
        String key = getKey( element, stylesDocument, null );
        // search from the cache
        Object defaultValue = stylesDocument.getValue( key );
        if ( defaultValue == null )
        {
            // compute the default value and cache it
            defaultValue = getDefaultValue( element, stylesDocument );
            if ( defaultValue == null )
            {
                defaultValue = XWPFStylesDocument.EMPTY_VALUE;
            }
            updateValueCache( stylesDocument, key, defaultValue );
        }

        // 2) Search value from the linked style
        Object result = getValueFromStyleIds( element, stylesDocument, defaultValue );
        if ( result != null )
        {
            return getValueOrNull( result );
        }
        updateValueCache( stylesDocument, key, defaultValue );
        return getValueOrNull( defaultValue );
    }

    private Value getValueOrNull( Object result )
    {
        return result.equals( XWPFStylesDocument.EMPTY_VALUE ) ? null : (Value) result;
    }

    public Object getValueFromStyleIds( XWPFElement element, XWPFStylesDocument stylesDocument, Object defaultValue )
    {
        String[] styleIds = getStyleID( element );
        if ( styleIds != null )
        {
            String styleId = null;
            for ( int i = 0; i < styleIds.length; i++ )
            {
                styleId = styleIds[i];
                Object value = getValueFromStyleId( element, stylesDocument, styleId, defaultValue );
                if ( value != null )
                {
                    return value;
                }
            }
        }
        return null;
    }

    public abstract Value getValueFromElement( XWPFElement element );

    protected Value getDefaultValue( XWPFElement element, XWPFStylesDocument stylesDocument )
    {
        Value value = getValueFromDefaultStyle( element, stylesDocument );
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

    private Object getValueFromStyleId( XWPFElement element, XWPFStylesDocument stylesDocument, String styleId,
                                        Object defaultValue )
    {

        if ( StringUtils.isEmpty( styleId ) )
        {
            return null;
        }

        // Search from cache
        String key = getKey( element, stylesDocument, styleId );
        Object result = stylesDocument.getValue( key );
        if ( result != null )
        {
            return result;
        }

        // Value is not computed, compute it
        CTStyle style = stylesDocument.getStyle( styleId );
        if ( style == null )
        {
            // should never come
            stylesDocument.setValue( key, defaultValue );
            return null;
        }

        // try to compute value
        Object value = getValueFromStyle( style );
        if ( value != null )
        {
            // Value is computed, cache it and return it.
            stylesDocument.setValue( key, value );
            return value;
        }

        // Check if style has ancestor with basedOn
        value = getValueFromStyleId( element, stylesDocument, getBasisStyleID( style ), defaultValue );
        value = value != null ? value : defaultValue;
        updateValueCache( stylesDocument, key, value );
        return value;
    }

    private void updateValueCache( XWPFStylesDocument stylesDocument, String key, Object value )
    {
        if ( value != null )
        {
            // Value is computed, cache it and return it.
            stylesDocument.setValue( key, value );
        }
        else
        {
            stylesDocument.setValue( key, XWPFStylesDocument.EMPTY_VALUE );
        }
    }

    protected String getKey( XWPFElement element, XWPFStylesDocument stylesDocument, String styleId )
    {
        if ( StringUtils.isNotEmpty( styleId ) )
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

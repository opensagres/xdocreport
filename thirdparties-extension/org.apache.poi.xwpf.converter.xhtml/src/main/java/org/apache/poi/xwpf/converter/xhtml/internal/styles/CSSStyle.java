package org.apache.poi.xwpf.converter.xhtml.internal.styles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.xwpf.converter.xhtml.internal.utils.SAXHelper;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class CSSStyle
{

    private final String tagName;

    private final String className;

    private List<CSSProperty> properties;

    public CSSStyle( String tagName, String className )
    {
        this.tagName = tagName;
        this.className = className;
        this.properties = null;
    }

    public void save( ContentHandler contentHandler )
        throws SAXException
    {
        StringBuilder style = new StringBuilder( tagName );
        style.append( '.' );
        style.append( className );
        style.append( '{' );

        buildInlineStyles( style );

        style.append( '}' );

        SAXHelper.characters( contentHandler, style.toString() );
    }

    public String getInlineStyles()
    {
        if ( properties == null )
        {
            return "";
        }
        StringBuilder styles = new StringBuilder();
        buildInlineStyles( styles );
        return styles.toString();
    }

    public void buildInlineStyles( StringBuilder style )
    {
        List<CSSProperty> properties = getProperties();
        for ( CSSProperty property : properties )
        {
            style.append( property.getName() );
            style.append( ':' );
            style.append( property.getValue() );
            style.append( ';' );
        }

    }

    public void addProperty( String name, String value )
    {
        if ( properties == null )
        {
            properties = new ArrayList<CSSProperty>();
        }
        properties.add( new CSSProperty( name, value ) );
    }

    public List<CSSProperty> getProperties()
    {
        if ( properties == null )
        {
            return Collections.emptyList();
        }
        return properties;
    }

    public boolean isEmpty()
    {
        return properties != null && properties.size() > 0;
    }

}

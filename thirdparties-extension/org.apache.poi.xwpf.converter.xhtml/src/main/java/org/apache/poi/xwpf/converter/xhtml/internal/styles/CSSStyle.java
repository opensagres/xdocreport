package org.apache.poi.xwpf.converter.xhtml.internal.styles;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.converter.xhtml.internal.SAXHelper;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class CSSStyle
{

    private final String tagName;

    private final String className;

    private final List<CSSProperty> properties;

    public CSSStyle( String tagName, String className )
    {
        this.tagName = tagName;
        this.className = className;
        this.properties = new ArrayList<CSSProperty>();
    }

    public void save( ContentHandler contentHandler )
        throws SAXException
    {
        StringBuilder style = new StringBuilder( tagName );
        style.append( '.' );
        style.append( className );
        style.append( '{' );

        List<CSSProperty> properties = getProperties();
        for ( CSSProperty property : properties )
        {
            style.append( property.getName() );
            style.append( ':' );
            style.append( property.getValue() );
            style.append( ';' );
        }

        style.append( '}' );

        SAXHelper.characters( contentHandler, style.toString() );
    }

    public void addProperty( String name, String value )
    {
        properties.add( new CSSProperty( name, value ) );
    }

    public List<CSSProperty> getProperties()
    {
        return properties;
    }

}

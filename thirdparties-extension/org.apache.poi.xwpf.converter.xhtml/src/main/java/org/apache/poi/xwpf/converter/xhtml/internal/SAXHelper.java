package org.apache.poi.xwpf.converter.xhtml.internal;

import org.apache.poi.xwpf.converter.xhtml.XHTMLConstants;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class SAXHelper
{

    public static void startElement( ContentHandler contentHandler, String name, Attributes attributes )
        throws SAXException
    {
        contentHandler.startElement( "", name, name, attributes != null ? attributes : EmptyAttributes.INSTANCE );
    }

    public static void endElement( ContentHandler contentHandler, String name )
        throws SAXException
    {
        contentHandler.endElement( "", name, name );
    }

    public static void characters( ContentHandler contentHandler, String content )
        throws SAXException
    {
        char[] chars = content.toCharArray();
        contentHandler.characters( chars, 0, chars.length );
    }

    /**
     * @param attributes
     * @param name
     * @param value
     */
    public static AttributesImpl addAttrValue( AttributesImpl attributes, String name, String value )
    {
        if ( attributes == null )
        {
            attributes = new AttributesImpl();
        }
        attributes.addAttribute( "", name, name, XHTMLConstants.CDATA_TYPE, value );
        return attributes;
    }
}

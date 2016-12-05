/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.poi.xwpf.converter.xhtml.internal.utils;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import fr.opensagres.poi.xwpf.converter.xhtml.internal.EmptyAttributes;
import fr.opensagres.poi.xwpf.converter.xhtml.internal.XHTMLConstants;

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
    public static AttributesImpl addAttrValue( AttributesImpl attributes, String name, int value )
   {
    return addAttrValue( attributes, name, String.valueOf( value ));    
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

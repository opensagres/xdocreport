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
package fr.opensagres.poi.xwpf.converter.xhtml.internal.styles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import fr.opensagres.poi.xwpf.converter.xhtml.internal.utils.SAXHelper;

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
        if ( className != null )
        {
            style.append( '.' );
            style.append( className );
        }
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

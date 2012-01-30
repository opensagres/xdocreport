/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
 * 
 * All rights reserved.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS  IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.xdocreport.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import fr.opensagres.xdocreport.core.internal.IXMLPrettyPrinter;
import fr.opensagres.xdocreport.core.internal.IndentNumberPrettyPrinter;
import fr.opensagres.xdocreport.core.internal.NoIndentNumberPrettyPrinter;
import fr.opensagres.xdocreport.core.internal.NoPrettyPrinter;
import fr.opensagres.xdocreport.core.internal.XSLTPrettyPrinter;

/**
 * XML Utilities to indent XML.
 */
public class XMLUtils
{

    public static final Integer INDENT_NUMBER = new Integer( 4 );

    public static final List<IXMLPrettyPrinter> PRINTERS;

    private static IXMLPrettyPrinter wellPrinter = null;

    static
    {
        PRINTERS = new ArrayList<IXMLPrettyPrinter>();
        PRINTERS.add( IndentNumberPrettyPrinter.INSTANCE );
        PRINTERS.add( XSLTPrettyPrinter.INSTANCE );
        PRINTERS.add( NoIndentNumberPrettyPrinter.INSTANCE );
        PRINTERS.add( NoPrettyPrinter.INSTANCE );
    }

    /**
     * Indent the given xml with the 4 indentation.
     * 
     * @param xml XML to indent
     * @return
     * @throws Exception
     */
    public static String prettyPrint( String xml )
    {
        return prettyPrint( xml, INDENT_NUMBER );
    }

    /**
     * Indent the given xml with the given indent number.
     * 
     * @param xml XML to indent
     * @param indent the indent number.
     * @return
     * @throws Exception
     */
    public static String prettyPrint( String xml, int indent )
    {
        if ( wellPrinter == null )
        {
            // Loop for printers to get the well printer which doesn't crash.
            for ( IXMLPrettyPrinter printer : PRINTERS )
            {
                try
                {
                    String result = printer.prettyPrint( xml, indent );
                    wellPrinter = printer;
                    return result;
                }
                catch ( Throwable e )
                {

                }
            }
            // If error occurs, returns the xml source (with no indentation).
            return xml;

        }
        // Here printer was found, use it.
        try
        {
            return wellPrinter.prettyPrint( xml, indent );
        }
        catch ( Throwable e )
        {
            // If error occurs, returns the xml source (with no indentation).
            return xml;
        }
    }

    /**
     * Get the SAX {@link AttributesImpl} of teh given attributes to modify attribute values.
     * 
     * @param attributes
     * @return
     */
    public static AttributesImpl toAttributesImpl( Attributes attributes )
    {
        if ( attributes instanceof AttributesImpl )
        {
            return (AttributesImpl) attributes;
        }
        // Another SAX Implementation, create a new instance.
        AttributesImpl attributesImpl = new AttributesImpl();
        int length = attributes.getLength();
        for ( int i = 0; i < length; i++ )
        {
            attributesImpl.addAttribute( attributes.getURI( i ), attributes.getLocalName( i ),
                                         attributes.getQName( i ), attributes.getType( i ), attributes.getValue( i ) );
        }
        return attributesImpl;
    }
}

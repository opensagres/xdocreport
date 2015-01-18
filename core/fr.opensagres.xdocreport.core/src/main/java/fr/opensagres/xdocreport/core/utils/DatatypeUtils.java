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
package fr.opensagres.xdocreport.core.utils;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class DatatypeUtils
{

    /** JAXP DatatypeFactory */
    private static DatatypeFactory datatypeFactory;

    /**
     * Gets a static instance of a JAXP DatatypeFactory.
     * 
     * @return the factory or null if the factory could not be created
     */
    private static DatatypeFactory getDataTypeFactory()
    {
        if ( datatypeFactory == null )
        {
            try
            {
                datatypeFactory = DatatypeFactory.newInstance();
            }
            catch ( DatatypeConfigurationException e )
            {
                e.printStackTrace();
            }
        }
        return datatypeFactory;
    }

    /**
     * <p>
     * Return the lexical representation of the given Date The format is specified in <a
     * href="http://www.w3.org/TR/xmlschema-2/#dateTime-order">XML Schema 1.0 Part 2, Section 3.2.[7-14].1, <i>Lexical
     * Representation</i>".</a>
     * </p>
     * <p>
     * Specific target lexical representation format is determined by {@link #getXMLSchemaType()}.
     * </p>
     * 
     * @return XML, as <code>String</code>, representation of this <code>XMLGregorianCalendar</code>
     * @throws IllegalStateException if the combination of set fields does not match one of the eight defined XML Schema
     *             builtin date/time datatypes.
     */
    public static String formatAsXSDateTime( Date date )
    {
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime( date );
        XMLGregorianCalendar xmlCalendar = getDataTypeFactory().newXMLGregorianCalendar( gCalendar );
        return xmlCalendar.toXMLFormat();
    }

    /**
     * <p>
     * Create a new Date by parsing the String as a lexical representation.
     * </p>
     * <p>
     * Parsing the lexical string representation is defined in <a
     * href="http://www.w3.org/TR/xmlschema-2/#dateTime-order">XML Schema 1.0 Part 2, Section 3.2.[7-14].1,
     * <em>Lexical Representation</em>.</a>
     * </p>
     * <p>
     * The string representation may not have any leading and trailing whitespaces.
     * </p>
     * <p>
     * The parsing is done field by field so that the following holds for any lexically correct String x:
     * </p>
     * 
     * <pre>
     * newXMLGregorianCalendar( x ).toXMLFormat().equals( x )
     * </pre>
     * <p>
     * Except for the noted lexical/canonical representation mismatches listed in <a
     * href="http://www.w3.org/2001/05/xmlschema-errata#e2-45"> XML Schema 1.0 errata, Section 3.2.7.2</a>.
     * </p>
     * 
     * @param lexicalRepresentation Lexical representation of one the eight XML Schema date/time datatypes.
     * @return <code>XMLGregorianCalendar</code> created from the <code>lexicalRepresentation</code>.
     * @throws IllegalArgumentException If the <code>lexicalRepresentation</code> is not a valid
     *             <code>XMLGregorianCalendar</code>.
     * @throws NullPointerException If <code>lexicalRepresentation</code> is <code>null</code>.
     */
    public static Date parseXSDateTime( String lexicalRepresentation )
    {
        XMLGregorianCalendar xmlCalendar = getDataTypeFactory().newXMLGregorianCalendar( lexicalRepresentation );
        return xmlCalendar.toGregorianCalendar().getTime();
    }

}

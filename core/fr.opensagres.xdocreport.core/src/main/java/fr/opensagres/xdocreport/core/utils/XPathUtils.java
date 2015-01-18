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

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.NodeList;

public class XPathUtils
{

    private static XPathFactory factory;

    public static NodeList evaluateNodeSet( Object source, String xpath, NamespaceContext namespaceContext )
        throws XPathExpressionException
    {
        return (NodeList) evaluate( source, XPathConstants.NODESET, xpath, namespaceContext );
    }

    private static Object evaluate( Object source, QName name, String xpath, NamespaceContext namespaceContext )
        throws XPathExpressionException
    {
        XPathExpression expr = getXPathExpression( xpath, namespaceContext );
        return expr.evaluate( source, name );
    }

    private static XPathExpression getXPathExpression( String xpath, NamespaceContext namespaceContext )
        throws XPathExpressionException
    {
        return createXPathExpression( xpath, namespaceContext );

    }

    private static XPathExpression createXPathExpression( String expression, NamespaceContext namespaceContext )
        throws XPathExpressionException
    {
        XPath xpath = getFactory().newXPath();
        if ( namespaceContext != null )
        {
            xpath.setNamespaceContext( namespaceContext );
        }
        XPathExpression expr = xpath.compile( expression );
        return expr;
    }

    public static XPathFactory getFactory()
    {
        if ( factory == null )
        {
            factory = createFactory();
        }
        return factory;
    }

    public static void setFactory( XPathFactory factory )
    {
        XPathUtils.factory = factory;
    }

    private static XPathFactory createFactory()
    {
        return XPathFactory.newInstance();
    }
}

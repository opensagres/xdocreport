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

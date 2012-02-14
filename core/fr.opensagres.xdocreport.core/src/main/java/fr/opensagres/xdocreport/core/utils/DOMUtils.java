package fr.opensagres.xdocreport.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.core.EncodingConstants;
import fr.opensagres.xdocreport.core.io.IOUtils;

public class DOMUtils
{

    public static Document load( InputStream stream )
        throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware( true );
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse( stream );
    }

    public static Document load( String xml )
        throws ParserConfigurationException, SAXException, IOException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware( true );
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse( IOUtils.toInputStream( xml, EncodingConstants.UTF_8.name() ) );
    }

    public static void save( Node node, Writer writer )
        throws TransformerException
    {
        // Use a Transformer for output
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();

        DOMSource source = new DOMSource( node );
        StreamResult result = new StreamResult( writer );
        transformer.transform( source, result );

    }

    public static void save( Node node, OutputStream out )
        throws TransformerException
    {
        // Use a Transformer for output
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();

        DOMSource source = new DOMSource( node );
        StreamResult result = new StreamResult( out );
        transformer.transform( source, result );

    }

    /**
     * Returns the first child element retrieved by tag name from the parent node and null otherwise.
     * 
     * @param parentNode parent node.
     * @param elementName element name to found.
     * @return the first child element
     */
    public static Element getFirstChildElementByTagName( Node parentNode, String elementName )
    {
        Element result = null;

        if ( parentNode.getNodeType() == Node.DOCUMENT_NODE )
        {
            result = ( (Document) parentNode ).getDocumentElement();
            if ( !result.getNodeName().equals( elementName ) )
            {
                result = null;
            }
        }
        else
        {
            NodeList nodes = parentNode.getChildNodes();
            Node node;
            for ( int i = 0; i < nodes.getLength(); i++ )
            {
                node = nodes.item( i );
                if ( node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals( elementName ) )
                {
                    result = (Element) node;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Returns list of the first child element retrieved by tag name from the parent node and null otherwise.
     * 
     * @param parentNode parent node.
     * @param elementName element name to found.
     * @return list of the first child element
     */
    public static Collection<Element> getFirstChildElementsByTagName( Node contextNode, String elementName )
    {
        Collection<Element> elements = null;
        Element result = null;

        if ( contextNode.getNodeType() == Node.DOCUMENT_NODE )
        {
            result = ( (Document) contextNode ).getDocumentElement();
            if ( !result.getNodeName().equals( elementName ) )
            {
                result = null;
            }
        }
        else
        {
            NodeList nodes = contextNode.getChildNodes();
            Node node;
            for ( int i = 0; i < nodes.getLength(); i++ )
            {
                node = nodes.item( i );
                if ( node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals( elementName ) )
                {
                    if ( elements == null )
                    {
                        elements = new ArrayList<Element>();
                    }
                    result = (Element) node;
                    elements.add( result );
                }
            }
        }
        if ( elements == null )
        {
            return Collections.emptyList();
        }
        return elements;
    }

}

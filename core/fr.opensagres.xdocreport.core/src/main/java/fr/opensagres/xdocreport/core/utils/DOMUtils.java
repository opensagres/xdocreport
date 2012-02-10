package fr.opensagres.xdocreport.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

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
}

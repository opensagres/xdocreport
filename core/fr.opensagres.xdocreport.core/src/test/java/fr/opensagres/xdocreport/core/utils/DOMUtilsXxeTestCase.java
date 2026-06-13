package fr.opensagres.xdocreport.core.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.SAXParser;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Regression test for the XXE hardening (incomplete fix of CVE-2025-65482): the secure
 * factories returned by {@link DOMUtils} reject a DOCTYPE and do not resolve external entities.
 */
public class DOMUtilsXxeTestCase
{

    private static final String XXE =
        "<?xml version=\"1.0\"?>\n"
            + "<!DOCTYPE r [ <!ENTITY x SYSTEM \"file:///etc/hostname\"> ]>\n"
            + "<r>&x;</r>";

    @Test
    public void loadRejectsDoctype()
        throws Exception
    {
        try
        {
            DOMUtils.load( new ByteArrayInputStream( XXE.getBytes( StandardCharsets.UTF_8 ) ) );
            fail( "DOMUtils.load must reject a DOCTYPE" );
        }
        catch ( SAXException e )
        {
            assertTrue( e.getMessage() != null );
        }
    }

    @Test
    public void secureSAXParserRejectsDoctype()
        throws Exception
    {
        SAXParser parser = DOMUtils.newSecureSAXParserFactory().newSAXParser();
        try
        {
            parser.parse( new ByteArrayInputStream( XXE.getBytes( StandardCharsets.UTF_8 ) ), new DefaultHandler() );
            fail( "Secure SAXParser must reject a DOCTYPE" );
        }
        catch ( SAXException e )
        {
            assertTrue( e.getMessage() != null );
        }
    }
}

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
package fr.opensagres.xdocreport.document.preprocessor.sax;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import fr.opensagres.xdocreport.core.io.IOUtils;

public class SAXXDocPreprocessorTestCase
    extends TestCase
{

    public void testLT()
        throws Exception
    {
        MockSAXXDocPreprocessor preprocessor = new MockSAXXDocPreprocessor();
        InputStream stream =
            IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><p>&lt;</p>" );
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, null );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><p>&lt;</p>", writer.toString() );
    }

    public void testAPOS()
        throws Exception
    {
        MockSAXXDocPreprocessor preprocessor = new MockSAXXDocPreprocessor();
        InputStream stream =
            IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><p>It&apos;s a document <span>to test</span></p>" );
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, null );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><p>It&apos;s a document <span>to test</span></p>",
                      writer.toString() );
    }

    public void testAMP()
        throws Exception
    {
        MockSAXXDocPreprocessor preprocessor = new MockSAXXDocPreprocessor();
        InputStream stream =
            IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><p>a&amp;b</p>" );
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, null );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><p>a&amp;b</p>", writer.toString() );
    }

    public void testElementWithTextContent()
        throws Exception
    {
        MockSAXXDocPreprocessor preprocessor = new MockSAXXDocPreprocessor();
        InputStream stream =
            IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><a>aaa</a>" );
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, null );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><a>aaa</a>", writer.toString() );
    }

    public void testElementClosed()
        throws Exception
    {
        MockSAXXDocPreprocessor preprocessor = new MockSAXXDocPreprocessor();
        InputStream stream =
            IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><a/>" );
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, null );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><a/>", writer.toString() );
    }

    public void testSeveralElements()
        throws Exception
    {
        MockSAXXDocPreprocessor preprocessor = new MockSAXXDocPreprocessor();
        InputStream stream =
            IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><root><a><b/></a><c>vvvvv<d/>jjjj</c></root>" );
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, null );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><root><a><b/></a><c>vvvvv<d/>jjjj</c></root>",
                      writer.toString() );
    }

    public void testSeveralElements2()
        throws Exception
    {
        MockSAXXDocPreprocessor preprocessor = new MockSAXXDocPreprocessor();
        InputStream stream =
            IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><root><p><span>Name</span>:<span>$d.Name</span></p></root>" );
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, null );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><root><p><span>Name</span>:<span>$d.Name</span></p></root>",
                      writer.toString() );
    }

    public void testSpecialCharsInAttr()
        throws Exception
    {
        MockSAXXDocPreprocessor preprocessor = new MockSAXXDocPreprocessor();
        InputStream stream =
            IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><root attr=\"a&amp;b\"/>" );
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, null );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><root attr=\"a&amp;b\"/>",
                      writer.toString() );
    }

    public void testSpecialEncoding()
        throws Exception
    {

        try
        {
            String s =
                IOUtils.toString( MockSAXXDocPreprocessor.class.getResourceAsStream( "styles_with_special_encoding.xml" ) );
            StringReader reader = new StringReader( s );
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();
            xmlReader.setContentHandler( new DefaultHandler() );
            xmlReader.parse( new InputSource( reader ) );
            assertTrue( "The 'styles_with_special_encoding.xml' has special encoding, use Reader should crash SAX.",
                        false );
        }
        catch ( Throwable e )
        {
            // [Fatal Error] :1:1: Content is not allowed in prolog.
            assertTrue( "The 'styles_with_special_encoding.xml' has special encoding, use Reader crash SAX.", true );
        }

        MockSAXXDocPreprocessor preprocessor = new MockSAXXDocPreprocessor();
        InputStream stream = MockSAXXDocPreprocessor.class.getResourceAsStream( "styles_with_special_encoding.xml" );
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, null );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><a/>", writer.toString() );

        //
        // reader =
        // new StringReader( s );
        // MockSAXXDocPreprocessor preprocessor = new MockSAXXDocPreprocessor();
        //
        // //System.err.println(s);
        // StringWriter writer = new StringWriter();
        // preprocessor.preprocess( "word/styles.xml", reader, writer, null, null, null );
        //
        //
        // assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><root attr=\"a&amp;b\"/>",
        // writer.toString() );
    }
}

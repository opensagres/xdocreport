package fr.opensagres.xdocreport.document.odt.styling;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import fr.opensagres.xdocreport.document.odt.textstyling.ODTDocumentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedDocument;
import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;
import fr.opensagres.xdocreport.document.textstyling.ITextStylingTransformer;
import fr.opensagres.xdocreport.document.textstyling.html.HTMLTextStylingTransformer;
import fr.opensagres.xdocreport.template.IContext;

/**
 * Check ODT Styling generation by comparing generated result against the expected XML
 * 
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 */
public class TestODTStyling
{

    public String read( InputStream in )
        throws IOException
    {
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[256];
        try
        {
            int read;
            while ( ( read = in.read( buffer ) ) != -1 )
            {
                sb.append( new String( buffer, 0, read ) );
            }
        }
        finally
        {
            in.close();
        }
        return sb.toString();
    }

    public String formatXML( String unformattedXml )
        throws Exception
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource( new StringReader( unformattedXml ) );
            final Document document = db.parse( is );

            OutputFormat format = new OutputFormat( document );
            format.setLineWidth( 100 );
            format.setIndenting( true );
            format.setIndent( 2 );
            Writer out = new StringWriter();

            XMLSerializer serializer = new XMLSerializer( out, format );
            serializer.serialize( document );

            return out.toString();
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }

    @Test
    public void testODTStylingGeneration()
        throws Exception
    {

        ITextStylingTransformer formatter = HTMLTextStylingTransformer.INSTANCE;

        BufferedDocument parent = new BufferedDocument();

        IDocumentHandler handler = new ODTDocumentHandler( parent, new IContext()
        {
            public Object put( String key, Object value )
            {
                return null;
            }

            public Object get( String key )
            {
                return null;
            }
        } );
        InputStream htmlStream = this.getClass().getClassLoader().getResourceAsStream( "HtmlSource.html" );
        formatter.transform( read( htmlStream ), handler );

        String result = handler.getTextEnd();
        result = formatXML( "<root>" + result + "</root>" );

        InputStream xmlStream = this.getClass().getClassLoader().getResourceAsStream( "OOoResult.xml" );
        String expectedXML = formatXML( read( xmlStream ) );

        Assert.assertEquals( expectedXML, result );

    }
}

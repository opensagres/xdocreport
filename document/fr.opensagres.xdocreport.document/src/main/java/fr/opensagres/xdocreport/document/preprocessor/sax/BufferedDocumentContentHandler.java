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

import static fr.opensagres.xdocreport.core.EncodingConstants.AMP;
import static fr.opensagres.xdocreport.core.EncodingConstants.APOS;
import static fr.opensagres.xdocreport.core.EncodingConstants.GT;
import static fr.opensagres.xdocreport.core.EncodingConstants.LT;
import static fr.opensagres.xdocreport.core.EncodingConstants.QUOT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import fr.opensagres.xdocreport.core.utils.StringUtils;

/**
 * SAX Content Handler which build a {@link BufferedDocument} from the XML source stream.
 */
public class BufferedDocumentContentHandler<Document extends BufferedDocument>
    extends DefaultHandler
{

    private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

    public static final String CDATA_TYPE = "CDATA";

    private final List<PrefixMapping> prefixs;

    protected final Document bufferedDocument;

    // protected IBufferedRegion currentRegion;
    private final StringBuilder currentCharacters = new StringBuilder();

    private boolean startingElement = false;

    private int elementIndex = 0;

    public BufferedDocumentContentHandler()
    {
        this.bufferedDocument = createDocument();
        this.prefixs = new ArrayList<PrefixMapping>();
    }

    @SuppressWarnings( "unchecked" )
    protected Document createDocument()
    {
        return (Document) new BufferedDocument();
    }

    public Document getBufferedDocument()
    {
        return bufferedDocument;
    }

    @Override
    public void startDocument()
        throws SAXException
    {
        this.bufferedDocument.append( XML_DECLARATION );
    }

    @Override
    public void startPrefixMapping( String prefix, String uri )
        throws SAXException
    {
        String xmlnsPrefix = StringUtils.isEmpty( prefix ) ? "xmlns" : "xmlns:" + prefix;
        prefixs.add( new PrefixMapping( xmlnsPrefix, uri ) );
    }

    @Override
    public final void startElement( String uri, String localName, String name, Attributes attributes )
        throws SAXException
    {
        if ( startingElement )
        {
            getCurrentElement().append( ">" );
        }
        if ( currentCharacters.length() > 0 )
        {
            flushCharacters( currentCharacters.toString() );
            resetCharacters();
        }
        BufferedElement element = null;
        try
        {
            // Start of start element to create element
            element = bufferedDocument.onStartStartElement( uri, localName, name, attributes );
            // Generate content of the start element
            startingElement = doStartElement( uri, localName, name, element.getAttributes() );
            elementIndex++;
        }
        finally
        {
            // End of start element
            bufferedDocument.onEndStartElement( element, uri, localName, name, attributes );
        }
    }

    public BufferedElement getCurrentElement()
    {
        return bufferedDocument.getCurrentElement();
    }

    protected BufferedElement findParentElementInfo( List<String> names )
    {
        return findParentElementInfo( getCurrentElement(), names );
    }

    protected BufferedElement findParentElementInfo( BufferedElement elementInfo, List<String> names )
    {
        if ( elementInfo == null )
        {
            return null;
        }
        for( String name : names )
        {
            if ( elementInfo.match( name ) )
            {
                return elementInfo;
            }
        }
        return findParentElementInfo( elementInfo.getParent(), names );
    }

    public boolean doStartElement( String uri, String localName, String name, Attributes attributes )
        throws SAXException
    {
        BufferedElement currentRegion = getCurrentElement();
        currentRegion.append( "<" );
        currentRegion.append( name );
        String attrName = null;
        String attrValue = null;
        // prefix mapping
        if ( prefixs.size() > 0 )
        {
            // generate prefixs mapping. Switch SAXParser implementation, prefix mapping are stored like this :
            // - for Xerces "com.sun.org.apache.xerces.internal.parsers.SAXParser", prefix mapping is stored just in
            // prefixs list.
            // - for Oracle "oracle.xml.parser.v2.SAXParser", prefix mapping are stored in the prefixs list and in the
            // to avoid having twice teh prefix mappings, addedPrefixs Map is populated with prefixs
            // see http://code.google.com/p/xdocreport/issues/detail?id=150
            int length = attributes.getLength();
            Map<String, PrefixMapping> addedPrefixs = length > 0 ? new HashMap<String, PrefixMapping>() : null;

            // 1) generate prefixs
            // ex: <w:document xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
            for ( PrefixMapping prefix : prefixs )
            {
                attrName = prefix.getPrefix();
                attrValue = prefix.getURI();
                currentRegion.append( ' ' );
                currentRegion.append( attrName );
                currentRegion.append( "=\"" );
                currentRegion.append( attrValue );
                currentRegion.append( "\"" );

                if ( addedPrefixs != null )
                {
                    addedPrefixs.put( prefix.getPrefix(), prefix );
                }
            }

            // 2) generate static attributes
            if ( length > 0 )
            {
                PrefixMapping prefix = null;
                for ( int i = 0; i < length; i++ )
                {
                    attrName = attributes.getQName( i );

                    // test if attribute name is not a prefix (comes from when Oracle SAXParsre is used).
                    if ( addedPrefixs != null )
                    {
                        prefix = addedPrefixs.get( attrName );
                    }
                    if ( prefix == null )
                    {
                        // teh attribute is not a prefix, add it.
                        currentRegion.append( ' ' );
                        attrValue = attributes.getValue( i );
                        currentRegion.append( attrName );
                        currentRegion.append( "=\"" );
                        printEscaped( attrValue, currentRegion );
                        currentRegion.append( "\"" );
                    }
                }
            }
            if ( addedPrefixs != null )
            {
                addedPrefixs.clear();
                addedPrefixs = null;
            }
            prefixs.clear();
        }
        else
        {
            // static attributes
            int length = attributes.getLength();
            if ( length > 0 )
            {
                for ( int i = 0; i < length; i++ )
                {
                    currentRegion.append( ' ' );
                    attrName = attributes.getQName( i );
                    attrValue = attributes.getValue( i );
                    currentRegion.append( attrName );
                    currentRegion.append( "=\"" );
                    printEscaped( attrValue, currentRegion );
                    currentRegion.append( "\"" );
                }
            }
        }
        // register dynamic attributes region if needed.
        currentRegion.registerDynamicAttributes();
        return true;
    }

    @Override
    public final void endElement( String uri, String localName, String name )
        throws SAXException
    {
        elementIndex--;
        if ( currentCharacters.length() > 0 )
        {
            // Flush caracters
            flushCharacters( currentCharacters.toString() );
            resetCharacters();
        }
        try
        {
            // Start of end element
            bufferedDocument.onStartEndElement( uri, localName, name );
            if ( startingElement )
            {
                IBufferedRegion currentRegion = getCurrentElement();
                currentRegion.append( "/>" );
                startingElement = false;
            }
            else
            {
                doEndElement( uri, localName, name );
            }
        }
        finally
        {
            // End of end element
            bufferedDocument.onEndEndElement( uri, localName, name );
        }
    }

    public void doEndElement( String uri, String localName, String name )
        throws SAXException
    {
        IBufferedRegion currentRegion = getCurrentElement();
        currentRegion.append( "</" );
        currentRegion.append( name );
        currentRegion.append( ">" );
    }

    @Override
    public final void characters( char[] ch, int start, int length )
        throws SAXException
    {
        if ( startingElement )
        {
            IBufferedRegion currentRegion = getCurrentElement();
            currentRegion.append( ">" );
        }
        startingElement = false;
        char c;
        for ( int i = start; i < start + length; i++ )
        {
            c = ch[i];
            if ( mustEncodeCharachers() )
            {
                if ( c == '<' )
                {
                    currentCharacters.append( LT );
                }
                else if ( c == '>' )
                {
                    currentCharacters.append( GT );
                }
                else if ( c == '\'' )
                {
                    currentCharacters.append( APOS );
                }
                else if ( c == '&' )
                {
                    currentCharacters.append( AMP );
                }
                else
                {
                    currentCharacters.append( c );
                }
            }
            else
            {
                currentCharacters.append( c );
            }
        }
    }

    protected boolean mustEncodeCharachers()
    {
        return true;
    }

    protected void flushCharacters( String characters )
    {
        IBufferedRegion currentRegion = getCurrentElement();
        currentRegion.append( characters );
    }

    protected void resetCharacters()
    {
        currentCharacters.setLength( 0 );
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

    //
    // Printing attribute value
    //
    protected void printEscaped( String source, IBufferedRegion region )
    {
        int length = source.length();
        for ( int i = 0; i < length; ++i )
        {
            int ch = source.charAt( i );
            // if (!XMLChar.isValid(ch)) {
            // if (++i < length) {
            // surrogates(ch, source.charAt(i));
            // } else {
            // fatalError("The character '" + (char) ch +
            // "' is an invalid XML character");
            // }
            // continue;
            // }
            // escape NL, CR, TAB
            if ( ch == '\n' || ch == '\r' || ch == '\t' )
            {
                printHex( ch, region );
            }
            else if ( ch == '<' )
            {
                region.append( LT );
            }
            else if ( ch == '&' )
            {
                region.append( AMP );
            }
            else if ( ch == '"' )
            {
                region.append( QUOT );
            }
            else
            {
                region.append( (char) ch );
            }
            // else if ((ch >= ' ' && _encodingInfo.isPrintable((char) ch))) {
            // _printer.printText((char) ch);
            // } else {
            // printHex(ch, region);
            // }
        }
    }

    /**
     * Escapes chars
     */
    final void printHex( int ch, IBufferedRegion region )
    {
        region.append( "&#x" );
        region.append( Integer.toHexString( ch ) );
        region.append( ';' );
    }

    public int getElementIndex()
    {
        return elementIndex;
    }
}

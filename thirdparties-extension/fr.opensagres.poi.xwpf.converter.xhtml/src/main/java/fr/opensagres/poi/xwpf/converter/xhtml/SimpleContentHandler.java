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
package fr.opensagres.poi.xwpf.converter.xhtml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SimpleContentHandler
    extends DefaultHandler
{

    private final OutputStream out;

    private final Writer writer;

    private boolean startingElement;

    private StringBuilder currentCharacters;

    private final Integer indent;
    
    private int nbElements;

    private boolean firstElement;

    
    public SimpleContentHandler( OutputStream out )
    {
        this( out, null );
    }

    public SimpleContentHandler( OutputStream out, Integer indent )
    {
        this( out, null, indent );
    }

    public SimpleContentHandler( Writer writer )
    {
        this( writer, null );
    }

    public SimpleContentHandler( Writer writer, Integer indent )
    {
        this( null, writer, indent );
    }

    private SimpleContentHandler( OutputStream out, Writer writer, Integer indent )
    {
        this.out = out;
        this.writer = writer;
        this.currentCharacters = new StringBuilder();
        this.indent = indent;
        this.firstElement = true;
    }

    @Override
    public void startElement( String uri, String localName, String name, Attributes attributes )
        throws SAXException
    {        
        if ( startingElement )
        {
            write( ">" );
        }
        if ( currentCharacters.length() > 0 )
        {
            flushCharacters( currentCharacters.toString() );
            resetCharacters();
        }

        doIndentIfNeeded();
        write( "<" );
        write( localName );
        int length = attributes.getLength();
        if ( length > 0 )
        {
            String attrName = null;
            String attrValue = null;
            for ( int i = 0; i < length; i++ )
            {
                attrName = attributes.getLocalName( i );
                attrValue = attributes.getValue( i );
                write( " " );
                write( attrName );
                write( "=\"" );
                write( attrValue );
                write( "\"" );
            }
        }
        startingElement = true;
        firstElement = false;
        nbElements++;
    }

    private void doIndentIfNeeded() throws SAXException
    {
        if (indent == null || firstElement) {
            return;
        }
        StringBuilder content = new StringBuilder("\n");
        for ( int i = 0; i < nbElements; i++ )
        {
            for ( int j = 0; j < indent; j++ )
            {
                content.append( ' ' );
            }            
        }
        write(content.toString());
    }

    @Override
    public final void endElement( String uri, String localName, String name )
        throws SAXException
    {
        nbElements--;
        if ( currentCharacters.length() > 0 )
        {
            // Flush caracters
            flushCharacters( currentCharacters.toString() );
            resetCharacters();
        }
        // Start of end element
        if ( startingElement )
        {
            write( "/>" );
            startingElement = false;
        }
        else
        {
            doIndentIfNeeded();
            write( "</" );
            write( localName );
            write( ">" );
        }        
    }

    @Override
    public final void characters( char[] ch, int start, int length )
        throws SAXException
    {
        if ( startingElement )
        {
            write( ">" );
        }
        startingElement = false;
        char c;
        for ( int i = start; i < start + length; i++ )
        {
            c = ch[i];
            // if ( mustEncodeCharachers() )
            // {
            // if ( c == '<' )
            // {
            // currentCharacters.append( LT );
            // }
            // else if ( c == '>' )
            // {
            // currentCharacters.append( GT );
            // }
            // else if ( c == '\'' )
            // {
            // currentCharacters.append( APOS );
            // }
            // else if ( c == '&' )
            // {
            // currentCharacters.append( AMP );
            // }
            // else
            // {
            // currentCharacters.append( c );
            // }
            // }
            // else
            // {
            currentCharacters.append( c );
            // }

        }
    }

    protected boolean mustEncodeCharachers()
    {
        return true;
    }

    protected void flushCharacters( String characters )
        throws SAXException
    {
        write( characters );
    }

    protected void resetCharacters()
    {
        currentCharacters.setLength( 0 );
    }

    private void write( String content )
        throws SAXException
    {
        try
        {
            if ( out != null )
            {
                out.write( content.getBytes() );
            }
            else
            {
                writer.write( content );
            }
        }
        catch ( IOException e )
        {
            throw new SAXException( e );
        }
    }

}

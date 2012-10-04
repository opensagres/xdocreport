package org.apache.poi.xwpf.converter.xhtml;

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

    public SimpleContentHandler( OutputStream out )
    {
        this( out, null );
    }

    public SimpleContentHandler( Writer writer )
    {
        this( null, writer );
    }

    private SimpleContentHandler( OutputStream out, Writer writer )
    {
        this.out = out;
        this.writer = writer;
        this.currentCharacters = new StringBuilder();
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
    }

    @Override
    public final void endElement( String uri, String localName, String name )
        throws SAXException
    {
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

    private void flushCharacters( String characters )
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

    private void write( char c )
    {
        // TODO Auto-generated method stub

    }
}

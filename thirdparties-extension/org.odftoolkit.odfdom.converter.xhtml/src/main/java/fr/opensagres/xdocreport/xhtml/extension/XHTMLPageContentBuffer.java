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
package fr.opensagres.xdocreport.xhtml.extension;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

public abstract class XHTMLPageContentBuffer
    extends AbstractContentBuffer
    implements XHTMLConstants
{

    protected StringBuilder currentBuffer = null;

    protected int currentElementIndex;

    public XHTMLPageContentBuffer( int indent )
    {
        super( indent );
        this.currentBuffer = new StringBuilder();
    }

    public void startEndElement( String elementName )
    {
        startElementNotEnclosed( elementName );
        currentBuffer.append( '/' );
        currentBuffer.append( '>' );
    }

    public void startElement( String elementName )
    {
        startElement( elementName, true, getCurrentBuffer(), currentElementIndex );
        currentElementIndex++;
    }

    public void startElementNotEnclosed( String elementName )
    {
        startElement( elementName, false, getCurrentBuffer(), currentElementIndex );
        currentElementIndex++;
    }

    public void endElementNotEnclosed()
    {
        getCurrentBuffer().append( '>' );
    }

    public XHTMLPageContentBuffer setAttribute( String name, String value )
    {
        startAttribute( name ).addAttributeValue( value, true ).endAttribute();
        return this;
    }

    public XHTMLPageContentBuffer setAttribute( String name, Integer value )
    {
        setAttribute( name, String.valueOf( value ) );
        return this;
    }

    public XHTMLPageContentBuffer addAttributeValue( String value, boolean firstValue )
    {
        if ( !firstValue )
        {
            getCurrentBuffer().append( ' ' );
        }
        getCurrentBuffer().append( value );
        return this;
    }

    public XHTMLPageContentBuffer startAttribute( String name )
    {
        getCurrentBuffer().append( ' ' ).append( name ).append( "=\"" );
        return this;
    }

    public XHTMLPageContentBuffer endAttribute()
    {
        getCurrentBuffer().append( "\"" );
        return this;
    }

    protected StringBuilder startElement( String elementName, boolean endsElement, StringBuilder buffer, int index )
    {
        doIndentIfNeeded( buffer, index );
        buffer.append( '<' );
        buffer.append( elementName );
        if ( endsElement )
        {
            buffer.append( '>' );
        }
        return buffer;
    }

    protected void startElement( String elementName, boolean endsElement, Writer writer, int index )
        throws IOException
    {
        doIndentIfNeeded( writer, index );
        writer.write( '<' );
        writer.write( elementName );
        if ( endsElement )
        {
            writer.write( '>' );
        }
    }

    protected void startElement( String elementName, boolean endsElement, OutputStream out, int index )
        throws IOException
    {
        doIndentIfNeeded( out, index );
        out.write( '<' );
        out.write( elementName.getBytes() );
        if ( endsElement )
        {
            out.write( '>' );
        }
    }

    public void endElement( String elementName )
    {
        currentElementIndex--;
        endElement( elementName, getCurrentBuffer(), currentElementIndex );
    }

    protected StringBuilder endElement( String elementName, StringBuilder buffer, int index )
    {
        doIndentIfNeeded( buffer, index );
        buffer.append( '<' );
        buffer.append( '/' );
        buffer.append( elementName );
        buffer.append( '>' );
        return buffer;
    }

    protected void endElement( String elementName, Writer writer, int index )
        throws IOException
    {
        doIndentIfNeeded( writer, index );
        writer.write( '<' );
        writer.write( '/' );
        writer.write( elementName );
        writer.write( '>' );
    }

    protected void endElement( String elementName, OutputStream out, int index )
        throws IOException
    {
        doIndentIfNeeded( out, index );
        out.write( '<' );
        out.write( '/' );
        out.write( elementName.getBytes() );
        out.write( '>' );
    }

    @Override
    protected StringBuilder getCurrentBuffer()
    {
        return currentBuffer;
    }

    public void save( Writer writer )
        throws IOException
    {
        writer.write( getCurrentBuffer().toString() );
    }

    public void save( OutputStream out )
        throws IOException
    {
        out.write( getCurrentBuffer().toString().getBytes() );
    }

    @Override
    public String toString()
    {
        StringWriter writer = new StringWriter();
        try
        {
            save( writer );
        }
        catch ( IOException e )
        {
            // Do nothing
        }
        return writer.toString();
    }
}

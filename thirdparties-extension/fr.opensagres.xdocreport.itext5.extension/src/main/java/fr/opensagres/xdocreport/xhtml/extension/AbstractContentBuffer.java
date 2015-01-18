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
import java.io.Writer;

public abstract class AbstractContentBuffer
{

    protected final int indent;

    public AbstractContentBuffer( final int indent )
    {
        this.indent = indent;
    }

    public void setText( String content )
    {
        getCurrentBuffer().append( content );
    }

    protected void doIndentIfNeeded( StringBuilder buffer, int index )
    {
        if ( indent > 0 )
        {
            buffer.append( "\n" );
            for ( int i = 0; i < index; i++ )
            {
                for ( int j = 0; j < indent; j++ )
                {
                    buffer.append( ' ' );
                }
            }
        }
    }

    protected void doIndentIfNeeded( OutputStream out, int index )
        throws IOException
    {
        if ( indent > 0 )
        {
            out.write( '\n' );
            for ( int i = 0; i < index; i++ )
            {
                for ( int j = 0; j < indent; j++ )
                {
                    out.write( ' ' );
                }
            }
        }
    }

    protected void doIndentIfNeeded( Writer writer, int index )
        throws IOException
    {
        if ( indent > 0 )
        {
            writer.write( '\n' );
            for ( int i = 0; i < index; i++ )
            {
                for ( int j = 0; j < indent; j++ )
                {
                    writer.write( ' ' );
                }
            }
        }
    }

    protected abstract StringBuilder getCurrentBuffer();
}

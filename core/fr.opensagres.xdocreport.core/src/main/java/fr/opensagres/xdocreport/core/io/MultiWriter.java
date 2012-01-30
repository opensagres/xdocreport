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
package fr.opensagres.xdocreport.core.io;

import java.io.IOException;
import java.io.Writer;

/**
 * Multiple writer used to write for several writer.
 */
public class MultiWriter
    extends Writer
    implements StreamCancelable
{

    private Writer[] writers;

    /**
     * New MultiWriter for specified writers. Note that flush is only done on explicit call.
     */
    public MultiWriter( Writer... writers )
    {
        this.writers = new Writer[writers.length];
        for ( int i = 0; i < writers.length; i++ )
        {
            this.writers[i] = writers[i];
        }
    }

    @Override
    public void write( char[] cbuf, int off, int len )
        throws IOException
    {
        for ( Writer w : writers )
        {
            w.write( cbuf, off, len );
            w.flush();
        }
    }

    @Override
    public void flush()
        throws IOException
    {
        for ( Writer w : writers )
        {
            w.flush();
        }
    }

    @Override
    public void close()
        throws IOException
    {
        for ( Writer w : writers )
        {
            w.close();
        }
    }

    public void cancel()
    {
        for ( Writer w : writers )
        {
            if ( w instanceof StreamCancelable )
            {
                ( (StreamCancelable) w ).cancel();
            }
        }

    }

    /**
     * Returns writer by index.
     * 
     * @param index
     * @return
     */
    public Writer getWriter( int index )
    {
        return this.writers[index];
    }
}

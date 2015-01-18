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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Base buffered region.
 */
public class BufferedRegionAdpater
    implements IBufferedRegion
{

    private final BufferedElement ownerElement;

    private final IBufferedRegion parent;

    public BufferedRegionAdpater( BufferedElement ownerElement, IBufferedRegion parent )
    {
        this.ownerElement = ownerElement;
        this.parent = parent;
        if ( parent != null )
        {
            parent.addRegion( this );
        }
    }

    public void save( Writer writer )
        throws IOException
    {

    }

    public boolean isString()
    {
        return false;
    }

    public void append( String content )
    {

    }

    public void append( char[] ch, int start, int length )
    {

    }

    public void append( char c )
    {

    }

    public void addRegion( ISavable region )
    {

    }

    public IBufferedRegion getParent()
    {
        return parent;
    }

    public BufferedElement getOwnerElement()
    {
        return ownerElement;
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

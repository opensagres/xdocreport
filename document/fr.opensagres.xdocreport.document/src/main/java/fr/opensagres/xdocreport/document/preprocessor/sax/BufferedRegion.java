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
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Buffered region.
 */
public class BufferedRegion
    extends BufferedRegionAdpater
{

    protected final List<ISavable> regions = new ArrayList<ISavable>();

    private IBufferedRegion currentRegion;

    public BufferedRegion( BufferedElement ownerElement, IBufferedRegion parent )
    {
        super( ownerElement, parent );
    }

    @Override
    public void save( Writer writer )
        throws IOException
    {
        for ( ISavable region : regions )
        {
            region.save( writer );
        }
    }

    @Override
    public void addRegion( ISavable region )
    {
        currentRegion = ( ( region instanceof IBufferedRegion ) ? (IBufferedRegion) region : currentRegion );
        regions.add( region );
    }

    @Override
    public void append( String content )
    {
        getStringBufferedRegion().append( content );
    }

    @Override
    public void append( char c )
    {
        getStringBufferedRegion().append( c );
    }

    @Override
    public boolean isString()
    {
        return false;
    }

    @Override
    public void append( char[] ch, int start, int length )
    {
        getStringBufferedRegion().append( ch, start, length );
    }

    private StringBufferedRegion getStringBufferedRegion()
    {
        if ( currentRegion == null || !currentRegion.isString() )
        {
            currentRegion = new StringBufferedRegion( getOwnerElement(), this );
        }
        return (StringBufferedRegion) currentRegion;
    }

    public void reset()
    {
        regions.clear();
        currentRegion = null;
    }
}

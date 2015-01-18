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

/**
 * Base class for start/end tag element. This buffer stores content of the start/end tag element and is enable to add
 * some content (ex: template engine script) on before/after the content of the start/end tag element.
 */
public class BufferedTagElement
    extends BufferedRegion
{

    private StringBuilder before;

    private StringBuilder after;

    public BufferedTagElement( BufferedElement ownerElement )
    {
        super( ownerElement, null );
    }

    @Override
    public void save( Writer writer )
        throws IOException
    {
        if ( before != null )
        {
            writer.write( before.toString() );
        }
        super.save( writer );
        if ( after != null )
        {
            writer.write( after.toString() );
        }
    }

    public void setBefore( String before )
    {
        if ( this.before == null )
        {
            this.before = new StringBuilder( before );
        }
        else
        {
            this.before.append( before );
        }
    }

    public String getBefore()
    {
        return before != null ? before.toString() : null;
    }

    public void setAfter( String after )
    {
        if ( this.after == null )
        {
            this.after = new StringBuilder( after );
        }
        else
        {
            this.after.append( after );
        }
    }

    public String getAfter()
    {
        return after != null ? after.toString() : null;
    }

}

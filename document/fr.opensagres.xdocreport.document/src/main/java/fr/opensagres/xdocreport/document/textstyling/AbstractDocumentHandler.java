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
package fr.opensagres.xdocreport.document.textstyling;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Stack;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.template.IContext;

/**
 * Abstract class for document handler {@link IDocumentHandler}.
 */
public abstract class AbstractDocumentHandler
    extends Writer
    implements IDocumentHandler
{

    private final BufferedElement parent;

    private final IContext context;

    private final Stack<Boolean> listStack;

    private StringWriter beforeWriter;

    private StringWriter bodyWriter;

    private StringWriter endWriter;

    private StringWriter currentWriter;

    public AbstractDocumentHandler( BufferedElement parent, IContext context )
    {
        this.parent = parent;
        this.context = context;
        // Stack of boolean (ordered or not) for the ordered/unordered list
        this.listStack = new Stack<Boolean>();
    }

    public void handleString( String s )
        throws IOException
    {
        getCurrentWriter().write( s );
    }

    public final void startOrderedList()
        throws IOException
    {
        listStack.push( true );
        doStartOrderedList();
    }

    public final void endOrderedList()
        throws IOException
    {
        listStack.pop();
        doEndOrderedList();
    }

    public final void startUnorderedList()
        throws IOException
    {
        listStack.push( false );
        doStartUnorderedList();
    }

    public final void endUnorderedList()
        throws IOException
    {
        listStack.pop();
        doEndUnorderedList();
    }

    protected boolean getCurrentListOrder()
    {
        if ( listStack.isEmpty() )
        {
            return false;
        }
        return listStack.peek();
    }

    protected int getCurrentListIndex()
    {
        if ( listStack.isEmpty() )
        {
            return 0;
        }
        return listStack.size() - 1;
    }

    public BufferedElement getParent()
    {
        return parent;
    }

    public IContext getContext()
    {
        return context;
    }

    public String getTextBefore()
    {
        if ( beforeWriter != null )
        {
            return beforeWriter.toString();
        }
        return "";
    }

    public String getTextBody()
    {
        if ( bodyWriter != null )
        {
            return bodyWriter.toString();
        }
        return "";
    }

    public String getTextEnd()
    {
        if ( endWriter != null )
        {
            return endWriter.toString();
        }
        return "";
    }

    public void setTextLocation( TextLocation location )
    {
        switch ( location )
        {
            case Before:
                if ( beforeWriter == null )
                {
                    beforeWriter = new StringWriter();
                }
                currentWriter = beforeWriter;
                break;
            case Body:
                if ( bodyWriter == null )
                {
                    bodyWriter = new StringWriter();
                }
                currentWriter = bodyWriter;
                break;
            case End:
                if ( endWriter == null )
                {
                    endWriter = new StringWriter();
                }
                currentWriter = endWriter;
                break;
        }
    }

    @Override
    public void close()
        throws IOException
    {
        getCurrentWriter().close();
    }

    @Override
    public void flush()
        throws IOException
    {
        getCurrentWriter().flush();
    }

    @Override
    public void write( char[] arg0, int arg1, int arg2 )
        throws IOException
    {
        getCurrentWriter().write( arg0, arg1, arg2 );
    }

    public Writer getCurrentWriter()
    {
        if ( currentWriter == null )
        {
            setTextLocation( TextLocation.Body );
        }
        return currentWriter;
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append( "@textBefore=" + getTextBefore() );
        result.append( "\n" );
        result.append( "@textBody=" + getTextBody() );
        result.append( "\n" );
        result.append( "@textend=" + getTextEnd() );
        return result.toString();
    }

    protected abstract void doEndUnorderedList()
        throws IOException;

    protected abstract void doEndOrderedList()
        throws IOException;

    protected abstract void doStartUnorderedList()
        throws IOException;

    protected abstract void doStartOrderedList()
        throws IOException;
}

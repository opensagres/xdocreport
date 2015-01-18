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

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class TransformedBufferedDocument
    extends BufferedDocument
{

    // Table stack
    private final Stack<TableBufferedRegion> tableStack = new Stack<TableBufferedRegion>();

    protected RowBufferedRegion currentRow;

    @Override
    protected BufferedElement createElement( BufferedElement parent, String uri, String localName, String name,
                                             Attributes attributes )
        throws SAXException
    {
        if ( isTable( uri, localName, name ) )
        {
            TableBufferedRegion currentTable =
                new TableBufferedRegion( getCurrentElement(), uri, localName, name, attributes );
            // currentRegion = currentTable;
            // Add table in the stack
            tableStack.push( currentTable );
            return currentTable;

        }
        else if ( isTableRow( uri, localName, name ) )
        {
            // Check if currentRow belong to a table
            if ( tableStack.size() < 1 )
            {
                throw new SAXException( "XML mal formatted. XML Row must be included in a XML Table" );
            }
            currentRow = new RowBufferedRegion( getCurrentElement(), uri, localName, name, attributes );
            return currentRow;
        }
        return super.createElement( parent, uri, localName, name, attributes );
    }

    @Override
    public void onEndEndElement( String uri, String localName, String name )
    {
        if ( isTable( uri, localName, name ) )
        {
            // end of table, remove the last table which was added
            tableStack.pop();
        }
        else if ( isTableRow( uri, localName, name ) )
        {
            // end of row, current region is the last table which was added.
            currentRow = null;
        }
        super.onEndEndElement( uri, localName, name );
    }

    protected abstract boolean isTable( String uri, String localName, String name );

    protected abstract boolean isTableRow( String uri, String localName, String name );

    public TableBufferedRegion getCurrentTable()
    {
        if ( !tableStack.isEmpty() )
        {
            return tableStack.peek();
        }
        return null;
    }

    public RowBufferedRegion getCurrentTableRow()
    {
        return currentRow;
    }
}

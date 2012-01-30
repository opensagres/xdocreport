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
package fr.opensagres.xdocreport.document.odt.textstyling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.document.odt.preprocessor.ODTBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.textstyling.AbstractDocumentHandler;
import fr.opensagres.xdocreport.template.IContext;

public class ODTDocumentHandler
    extends AbstractDocumentHandler
{

    private boolean bolding;

    private boolean italicsing;

    private Stack<Boolean> paragraphsStack;

    private boolean insideHeader = false;

    private int listDepth = 0;

    private List<Boolean> lastItemAlreadyClosed = new ArrayList<Boolean>();

    private static final Logger LOGGER = LogUtils.getLogger( ODTDocumentHandler.class.getName() );

    public ODTDocumentHandler( BufferedElement parent, IContext context )
    {
        super( parent, context );
    }

    public void startDocument()
    {
        this.bolding = false;
        this.italicsing = false;
        this.paragraphsStack = new Stack<Boolean>();
    }

    public void endDocument()
        throws IOException
    {
        endParagraphIfNeeded();
    }

    private void endParagraphIfNeeded()
        throws IOException
    {
        if ( !paragraphsStack.isEmpty() )
        {
            paragraphsStack.size();
            for ( int i = 0; i < paragraphsStack.size(); i++ )
            {
                internalEndParagraph();
            }
            paragraphsStack.clear();
        }
    }

    public void startBold()
    {
        this.bolding = true;
    }

    public void endBold()
    {
        this.bolding = false;
    }

    public void startItalics()
    {
        this.italicsing = true;
    }

    public void endItalics()
    {
        this.italicsing = false;
    }

    @Override
    public void handleString( String content )
        throws IOException
    {
        if ( insideHeader )
        {
            super.write( content );
        }
        else
        {
            super.write( "<text:span" );
            if ( bolding || italicsing )
            {
                super.write( " text:style-name=\"" );
                if ( bolding && italicsing )
                {
                    super.write( ODTBufferedDocumentContentHandler.BOLD_ITALIC_STYLE_NAME );
                }
                else if ( italicsing )
                {
                    super.write( ODTBufferedDocumentContentHandler.ITALIC_STYLE_NAME );
                }
                else if ( bolding )
                {
                    super.write( ODTBufferedDocumentContentHandler.BOLD_STYLE_NAME );
                }
                super.write( "\" " );
            }
            super.write( ">" );
            super.write( content );
            super.write( "</text:span>" );
        }
    }

    public void startParagraph()
        throws IOException
    {
        super.setTextLocation( TextLocation.End );
        internalStartParagraph( false );
    }

    public void endParagraph()
        throws IOException
    {
        internalEndParagraph();
    }

    private void internalStartParagraph( boolean containerIsList )
        throws IOException
    {
        super.write( "<text:p>" );
        paragraphsStack.push( containerIsList );
    }

    private void internalEndParagraph()
        throws IOException
    {
        if ( !paragraphsStack.isEmpty() )
        {
            super.write( "</text:p>" );
            paragraphsStack.pop();
        }
    }

    public void startHeading( int level )
        throws IOException
    {
        endParagraphIfNeeded();
        super.setTextLocation( TextLocation.End );
        super.write( "<text:h text:style-name=\"Heading_20_" + level + "\" text:outline-level=\"" + level + "\">" );
        insideHeader = true;
    }

    public void endHeading( int level )
        throws IOException
    {
        super.write( "</text:h>" );
        insideHeader = false;
        startParagraph();
    }

    @Override
    protected void doStartOrderedList()
        throws IOException
    {
        internalStartList( ODTBufferedDocumentContentHandler.UL_STYLE_NAME );
    }

    @Override
    protected void doEndOrderedList()
        throws IOException
    {
        internalEndList();
    }

    @Override
    protected void doStartUnorderedList()
        throws IOException
    {
        internalStartList( ODTBufferedDocumentContentHandler.UL_STYLE_NAME );
    }

    @Override
    protected void doEndUnorderedList()
        throws IOException
    {
        internalEndList();
    }

    protected String itemStyle = "";

    protected void internalStartList( String style )
        throws IOException
    {
        if ( listDepth == 0 )
        {
            endParagraphIfNeeded();
            lastItemAlreadyClosed.add( listDepth, false );
        }
        else
        {
            // close item for nested lists
            super.write( "</text:p>" );
            lastItemAlreadyClosed.add( listDepth, true );
        }
        if ( style != null )
        {
            super.write( "<text:list text:style-name=\"" + style + "\">" );
            itemStyle = style;
        }
        else
        {
            super.write( "<text:list>" );
        }
        listDepth++;
    }

    protected void internalEndList()
        throws IOException
    {
        super.write( "</text:list>" );
        listDepth--;
        if ( listDepth == 0 )
        {
            startParagraph();
        }
    }

    public void startListItem()
    {
        try
        {
            if ( itemStyle != null )
            {
                super.write( "<text:list-item text:style-name=\"" + itemStyle + "\">" );
                super.write( "<text:p text:style-name=\"" + itemStyle
                    + ODTBufferedDocumentContentHandler.LIST_P_STYLE_NAME_SUFFIX + "\">" );
            }
            else
            {
                super.write( "<text:list-item>" );
                super.write( "<text:p>" );
            }
        }
        catch ( IOException e )
        {
            LOGGER.severe( e.getMessage() );
        }
    }

    public void endListItem()
    {
        try
        {
            if ( lastItemAlreadyClosed.size() > listDepth && lastItemAlreadyClosed.get( listDepth ) )
            {
                lastItemAlreadyClosed.add( listDepth, false );
            }
            else
            {
                super.write( "</text:p>" );
            }
            super.write( "</text:list-item>" );
        }
        catch ( IOException e )
        {
            LOGGER.severe( e.getMessage() );
        }
    }
}

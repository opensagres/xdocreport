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

    protected final IODTStylesGenerator styleGen;

    private boolean paragraphWasInserted;

    public ODTDocumentHandler( BufferedElement parent, IContext context, String entryName )
    {
        super( parent, context, entryName );
        styleGen = ODTStylesGeneratorProvider.getStyleGenerator();
        this.paragraphWasInserted = false;
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
            startParagraphIfNeeded();
            super.write( "<text:span" );
            if ( bolding || italicsing )
            {
                super.write( " text:style-name=\"" );
                if ( bolding && italicsing )
                {
                    super.write( styleGen.getBoldItalicStyleName() );
                }
                else if ( italicsing )
                {
                    super.write( styleGen.getItalicStyleName() );
                }
                else if ( bolding )
                {
                    super.write( styleGen.getBoldStyleName() );
                }
                super.write( "\" " );
            }
            super.write( ">" );
            super.write( content );
            super.write( "</text:span>" );
        }
    }

    private void startParagraphIfNeeded()
        throws IOException
    {

        if ( paragraphWasInserted && paragraphsStack.isEmpty() )
        {
            internalStartParagraph( false );
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
        internalStartParagraph( containerIsList, null );
    }

    private void internalStartParagraph( boolean containerIsList, String styleName )
        throws IOException
    {
        if ( styleName == null )
        {
            super.write( "<text:p>" );
        }
        else
        {
            super.write( "<text:p text:style-name=\"" );
            super.write( styleName );
            super.write( "\">" );

        }
        paragraphWasInserted = true;
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
        super.write( "<text:h text:style-name=\"" + styleGen.getHeaderStyleName( level ) + "\" text:outline-level=\""
            + level + "\">" );
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
        internalStartList( styleGen.getOLStyleName() );
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
        internalStartList( styleGen.getULStyleName() );
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
        super.setTextLocation( TextLocation.End );
        if ( listDepth == 0 )
        {
            endParagraphIfNeeded();
            lastItemAlreadyClosed.add( listDepth, false );
        }
        else
        {
            // close item for nested lists
            //super.write( "</text:p>" );
            endParagraph();
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
            //startParagraph();
        }
    }

    public void startListItem()
        throws IOException
    {
        if ( itemStyle != null )
        {
            super.write( "<text:list-item text:style-name=\"" + itemStyle + "\">" );
            internalStartParagraph( true, itemStyle + styleGen.getListItemParagraphStyleNameSuffix() );
        }
        else
        {
            super.write( "<text:list-item>" );
            internalStartParagraph( true );
        }
    }

    public void endListItem()
        throws IOException
    {
        if ( lastItemAlreadyClosed.size() > listDepth && lastItemAlreadyClosed.get( listDepth ) )
        {
            lastItemAlreadyClosed.add( listDepth, false );
        }
        else
        {
            //endParagraph();
        }
        endParagraphIfNeeded();
        super.write( "</text:list-item>" );
    }

    public void handleReference( String ref, String label )
        throws IOException
    {
        // FIXME: generate text:p only if needed.
        // startParagraph();
        super.write( "<text:a xlink:type=\"simple\" xlink:href=\"" );
        super.write( ref );
        super.write( "\">" );
        super.write( label );
        super.write( "</text:a>" );
        // endParagraph();
    }

    public void handleImage( String ref, String label )
        throws IOException
    {
        // TODO: implements
    }

}

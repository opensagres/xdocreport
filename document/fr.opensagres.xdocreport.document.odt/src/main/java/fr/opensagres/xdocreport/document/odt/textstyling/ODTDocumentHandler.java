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
package fr.opensagres.xdocreport.document.odt.textstyling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.odt.template.ODTContextHelper;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.textstyling.AbstractDocumentHandler;
import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler.TextLocation;
import fr.opensagres.xdocreport.document.textstyling.properties.HeaderProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ListItemProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ListProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ParagraphProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.SpanProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableCellProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableRowProperties;
import fr.opensagres.xdocreport.template.IContext;

public class ODTDocumentHandler
    extends AbstractDocumentHandler
{
    private Stack<Boolean> paragraphsStack;

    private boolean insideHeader = false;

    private Stack<Integer> spanStack;

    private int listDepth = 0;

    private List<Boolean> lastItemAlreadyClosed = new ArrayList<Boolean>();

    protected final IODTStylesGenerator styleGen;

    private boolean paragraphWasInserted;

    private boolean closeHeader;

    public ODTDocumentHandler( BufferedElement parent, IContext context, String entryName )
    {
        super( parent, context, entryName );
        styleGen = ODTContextHelper.getStylesGenerator( context );
        this.paragraphWasInserted = false;
        this.closeHeader = false;
    }

    public void startDocument()
    {
        this.paragraphsStack = new Stack<Boolean>();
        this.spanStack = new Stack<Integer>();
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
        throws IOException
    {
        internalStartSpan( styleGen.getBoldStyleName(), true );
    }

    public void endBold()
        throws IOException
    {
        internalEndSpan();
    }

    public void startItalics()
        throws IOException
    {
        internalStartSpan( styleGen.getItalicStyleName(), true );
    }

    public void endItalics()
        throws IOException
    {
        internalEndSpan();
    }

    public void startUnderline()
        throws IOException
    {
        internalStartSpan( styleGen.getUnderlineStyleName(), true );
    }

    public void endUnderline()
        throws IOException
    {
        internalEndSpan();
    }

    public void startStrike()
        throws IOException
    {
        internalStartSpan( styleGen.getStrikeStyleName(), true );
    }

    public void endStrike()
        throws IOException
    {
        internalEndSpan();
    }

    public void startSubscript()
        throws IOException
    {
        internalStartSpan( styleGen.getSubscriptStyleName(), true );
    }

    public void endSubscript()
        throws IOException
    {
        internalEndSpan();
    }

    public void startSuperscript()
        throws IOException
    {
        internalStartSpan( styleGen.getSuperscriptStyleName(), true );

    }

    public void endSuperscript()
        throws IOException
    {
        internalEndSpan();
    }

    @Override
    public void handleString( String content )
        throws IOException
    {
        // Re-escape ODT special characters, xml parsing removes them.
        content = StringUtils.xmlUnescape( content );
        content = StringUtils.xmlEscape( content );

        if ( insideHeader )
        {
            super.write( content );
        }
        else
        {
            startSpanIfNeeded();

            super.write( content );

            internalEndSpan();
        }
    }

    private void startSpanIfNeeded()
        throws IOException
    {
        boolean spanNeeded = true;

        for ( Integer depth : spanStack )
        {
            if ( depth != 0 )
            {
                spanNeeded = false;
                break;
            }
        }

        if ( spanNeeded )
        {
            internalStartSpan( styleGen.getTextStyleName( null ), true );
        }
        else
        {
            // Push 0 so nothing is closed
            spanStack.push( 0 );
        }
    }

    private void internalStartSpan( String styleName, boolean push )
        throws IOException
    {
        startParagraphIfNeeded();

        super.write( "<text:span" );

        if ( StringUtils.isNotEmpty( styleName ) )
        {
            super.write( " text:style-name=\"" );
            super.write( styleName );
            super.write( "\" " );
        }

        super.write( ">" );

        if ( push )
        {
            spanStack.push( 1 );
        }
    }

    private void internalEndSpan()
        throws IOException
    {
        Integer depth;

        depth = spanStack.pop();

        while ( depth > 0 )
        {
            super.write( "</text:span>" );
            depth--;
        }
    }

    private void startParagraphIfNeeded()
        throws IOException
    {

        if ( ( paragraphWasInserted && paragraphsStack.isEmpty() ) || closeHeader )
        {
            internalStartParagraph( false, (String) null );
        }
    }

    public void startParagraph( ParagraphProperties properties )
        throws IOException
    {
        if ( paragraphsStack.isEmpty() || !paragraphsStack.peek() )
        {
            super.setTextLocation( TextLocation.End );
            internalStartParagraph( false, properties );
        }
    }

    public void endParagraph()
        throws IOException
    {
        // paragraphs inside a list are skipped
        // if ( !paragraphsStack.peek() )
        // {
        internalEndParagraph();
        // }
    }

    private void internalStartParagraph( boolean containerIsList, ParagraphProperties properties )
        throws IOException
    {
        String styleName = null;
        if ( properties != null )
        {

            if ( properties.isPageBreakAfter() )
            {
                styleName = styleGen.getParaBreakAfterStyleName();
            }
            else if ( properties.isPageBreakBefore() )
            {
                styleName = styleGen.getParaBreakBeforeStyleName();
            }
            else
            {
                styleName = styleGen.getTextStyleName( properties );
            }
        }
        internalStartParagraph( containerIsList, styleName );

        // if ( properties != null )
        // {
        // // Remove "span" added by internalStartParagraph
        // // spanStack.pop();
        //
        // // Process properties
        // // startSpan( properties );
        // }
    }

    private void internalStartParagraph( boolean containerIsList, String styleName )
        throws IOException
    {
        closeHeader = false;
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

        // Put a 0 in the stack, endSpan is called when a paragraph is ended
        spanStack.push( 0 );
    }

    private void internalEndParagraph()
        throws IOException
    {
        if ( !paragraphsStack.isEmpty() )
        {
            // Close any spans from paragraph style
            internalEndSpan();

            super.write( "</text:p>" );
            paragraphsStack.pop();
        }
    }

    public void startHeading( int level, HeaderProperties properties )
        throws IOException
    {
        endParagraphIfNeeded();
        super.setTextLocation( TextLocation.End );
        super.write( "<text:h text:style-name=\"" + styleGen.getHeaderStyleName( level ) + "\" text:outline-level=\""
            + level + "\">" );
        insideHeader = true;
        closeHeader = false;
    }

    public void endHeading( int level )
        throws IOException
    {
        super.write( "</text:h>" );
        insideHeader = false;
        closeHeader = true;
        // startParagraph();
    }

    @Override
    protected void doStartOrderedList( ListProperties properties )
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
    protected void doStartUnorderedList( ListProperties properties )
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
        closeHeader = false;
        super.setTextLocation( TextLocation.End );
        if ( listDepth == 0 )
        {
            endParagraphIfNeeded();
            lastItemAlreadyClosed.add( listDepth, false );
        }
        else
        {
            // close item for nested lists
            // super.write( "</text:p>" );
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
            // startParagraph();
        }
    }

    public void startListItem( ListItemProperties properties )
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
            internalStartParagraph( true, (String) null );
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
            // endParagraph();
        }
        endParagraphIfNeeded();
        super.write( "</text:list-item>" );
    }

    public void startSpan( SpanProperties properties )
        throws IOException
    {
        internalStartSpan( styleGen.getTextStyleName( properties ), true );
    }

    public void endSpan()
        throws IOException
    {
        internalEndSpan();
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

    public void handleLineBreak()
        throws IOException
    {
        super.write( "<text:line-break />" );
    }

    protected void doStartTable( TableProperties properties )
        throws IOException
    {
        endParagraphIfNeeded();
        // odf table cannot be generated now, because here we don't know the table column count required to generate
        // table:table-column
        // that's here temp writer is used.
        pushTempWriter();
    }

    public void doEndTable( TableProperties properties )
        throws IOException
    {
        super.setTextLocation( TextLocation.End );
        StringBuilder startTable = new StringBuilder( "<table:table>" );
        startTable.append( "<table:table-column " );
        int count = properties.getColumnCount();
        startTable.append( "table:number-columns-repeated=\"" );
        startTable.append( count );
        startTable.append( "\" >" );
        startTable.append( "</table:table-column>" );
        popTempWriter( startTable.toString() );
        super.write( "</table:table>" );
    }

    protected void doStartTableRow( TableRowProperties properties )
        throws IOException
    {
        super.write( "<table:table-row>" );
    }

    protected void doEndTableRow()
        throws IOException
    {
        super.write( "</table:table-row>" );
    }

    protected void doStartTableCell( TableCellProperties properties )
        throws IOException
    {
        super.write( "<table:table-cell>" );
        internalStartParagraph( false, (String) null );
    }

    public void doEndTableCell()
        throws IOException
    {
        endParagraph();
        super.write( "</table:table-cell>" );
    }
}

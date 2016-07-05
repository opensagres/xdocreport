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
package fr.opensagres.xdocreport.document.docx.textstyling;

import java.io.IOException;
import java.util.Stack;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.hyperlinks.HyperlinkRegistry;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.hyperlinks.HyperlinkUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.numbering.NumberingRegistry;
import fr.opensagres.xdocreport.document.docx.template.DocxContextHelper;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.textstyling.AbstractDocumentHandler;
import fr.opensagres.xdocreport.document.textstyling.properties.ContainerProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.HeaderProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ListItemProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ListProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ParagraphProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.SpanProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableCellProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableRowProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TextAlignment;
import fr.opensagres.xdocreport.template.IContext;

/**
 * Document handler implementation to build docx fragment content.
 */
public class DocxDocumentHandler
    extends AbstractDocumentHandler
{

    private boolean bolding;

    private boolean italicsing;

    private boolean underlining;

    private boolean striking;
    
    private boolean subscripting;

    private boolean superscripting;
    
    private Stack<ContainerProperties> paragraphsStack;

    private Stack<SpanProperties> spansStack;

    private HyperlinkRegistry hyperlinkRegistry;

    protected final IDocxStylesGenerator styleGen;

    private final NumberingRegistry numberingRegistry;

    private DefaultStyle defaultStyle;

    private boolean insideHeader;

    private boolean paragraphWasInserted;

    private int currentNumId;

    private boolean haspPr;

    private int addLineBreak;

    public DocxDocumentHandler( BufferedElement parent, IContext context, String entryName )
    {
        super( parent, context, entryName );
        this.styleGen = DocxContextHelper.getStylesGenerator( context );
        this.defaultStyle = DocxContextHelper.getDefaultStyle( context );
        this.numberingRegistry = getNumberingRegistry( context );
        this.insideHeader = false;
        this.paragraphWasInserted = false;
    }

    public void startDocument()
    {
        this.bolding = false;
        this.italicsing = false;
        this.underlining = false;
        this.striking = false;
        this.subscripting = false;
        this.superscripting = false;
        this.paragraphsStack = new Stack<ContainerProperties>();
        this.spansStack = new Stack<SpanProperties>();
        this.addLineBreak = 0;
    }

    public void endDocument()
        throws IOException
    {
        endParagraphIfNeeded();
        this.spansStack.clear();
    }

    private void endParagraphIfNeeded()
        throws IOException
    {
        if ( !paragraphsStack.isEmpty() )
        {
            paragraphsStack.size();
            for ( int i = 0; i < paragraphsStack.size(); i++ )
            {
                endParagraph();
            }
            paragraphsStack.clear();
        }
    }

    private void closeCurrentParagraph()
        throws IOException
    {
        if ( !paragraphsStack.isEmpty() )
        {
            endParagraph();
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

    public void startUnderline()
        throws IOException
    {
        this.underlining = true;
    }

    public void endUnderline()
        throws IOException
    {
        this.underlining = false;
    }

    public void startStrike()
        throws IOException
    {
        this.striking = true;
    }

    public void endStrike()
        throws IOException
    {
        this.striking = false;
    }

    public void startSubscript()
        throws IOException
    {
    	this.subscripting = true;
    }

    public void endSubscript()
        throws IOException
    {
    	this.subscripting = false;
    }

    public void startSuperscript()
        throws IOException
    {
    	this.superscripting = true;
    }

    public void endSuperscript()
        throws IOException
    {
    	this.superscripting = false;
    }

    @Override
    public void handleString( String content )
        throws IOException
    {
        // Re-escape DOCX special characters, xml parsing removes them.
        content = StringUtils.xmlUnescape( content );
        content = StringUtils.xmlEscape( content );

        if ( insideHeader )
        {
            // Title of the Header.
            super.write( content );
        }
        else
        {
            startParagraphIfNeeded();
            boolean bold = bolding;
            boolean italic = italicsing;
            boolean underline = underlining;
            boolean strike = striking;
            boolean subscript = subscripting;
            boolean superscript = superscripting;
            SpanProperties properties = getCurrentSpanProperties();
            if ( properties != null )
            {
                // ovveride properties declared in the span.
                if ( !bold )
                {
                    bold = properties.isBold();
                }
                if ( !italic )
                {
                    italic = properties.isItalic();
                }
                if ( !underline )
                {
                    underline = properties.isUnderline();
                }
                if ( !strike )
                {
                    strike = properties.isStrike();
                }
                if ( !subscript )
                {
                	subscript = properties.isSubscript();
                }
                if ( !superscript)
                {
                	superscript = properties.isSuperscript();
                }
            }
            super.write( "<w:r>" );
            // w:RP
            processRunProperties( false, bold, italic, underline, strike, subscript, superscript );
            // w:br
            for ( int i = 0; i < addLineBreak; i++ )
            {
                super.write( "<w:br/>" );
            }
            addLineBreak = 0;
            if(!content.isEmpty()) 
            {
	            // w:t
	            super.write( "<w:t xml:space=\"preserve\" >" );
	            super.write( content );
	            super.write( "</w:t>" );
            }
            super.write( "</w:r>" );
        }
    }

    private void processRunProperties( boolean isInsidePPr, boolean bold, boolean italics, boolean underline,
                                       boolean strike, boolean subscript, boolean superscript )
        throws IOException
    {
        if ( bold || italics || underline || strike || subscript || superscript  )
        {
            if ( isInsidePPr )
            {
                startPPrIfNeeded();
            }
            super.write( "<w:rPr>" );
            if ( bold )
            {
                super.write( "<w:b />" );
            }
            if ( italics )
            {
                super.write( "<w:i />" );
            }
            if ( underline )
            {
                super.write( "<w:u w:val=\"single\" />" );
            }
            if ( strike )
            {
                super.write( "<w:strike />" );
            }
            if (subscript) {
            	super.write( "<w:vertAlign w:val=\"subscript\"/>" );
            }
            if (superscript) {
            	super.write( "<w:vertAlign w:val=\"superscript\"/>" );
            }
            super.write( "</w:rPr>" );
        }
    }

    private void startParagraphIfNeeded()
        throws IOException
    {

        if ( paragraphWasInserted && paragraphsStack.isEmpty() )
        {
            internalStartParagraph( null );
        }
    }

    private void internalStartParagraph( ContainerProperties properties, String pStyle, boolean isList )
        throws IOException
    {
        paragraphWasInserted = true;
        super.setTextLocation( TextLocation.End );
        super.write( "<w:p>" );
        processParagraphProperties( properties, pStyle, isList );
        paragraphsStack.push( properties );
    }

    private void internalStartParagraph( ContainerProperties properties )
        throws IOException
    {
        if (properties != null && properties.getStyleName() != null) {
            internalStartParagraph(properties, properties.getStyleName(), false);
        } else {
            internalStartParagraph(properties, null, false);
        }
    }

    public void startParagraph( ParagraphProperties properties )
        throws IOException
    {
        processPageBreakBefore( properties );
        closeCurrentParagraph();
        internalStartParagraph( properties );
    }

    private void processPageBreakBefore( ContainerProperties properties )
        throws IOException
    {
        if ( properties != null )
        {
            if ( properties.isPageBreakBefore() )
            {
                super.setTextLocation( TextLocation.End );
                generatePageBreak();
            }
        }
    }

    private void processPageBreakAfter( ContainerProperties properties )
        throws IOException
    {
        if ( properties != null )
        {
            if ( properties.isPageBreakAfter() )
            {
                super.setTextLocation( TextLocation.End );
                generatePageBreak();
            }
        }
    }

    private void generatePageBreak()
        throws IOException
    {
        super.write( "<w:p>" );
        super.write( "<w:r>" );
        super.write( "<w:br w:type=\"page\" />" );
        super.write( "</w:r>" );
        super.write( "</w:p>" );
    }

    public void endParagraph()
        throws IOException
    {
        super.write( "</w:p>" );
        processPageBreakAfter( paragraphsStack.pop() );
    }

    public void startListItem( ListItemProperties properties )
        throws IOException
    {
        // Close current paragraph
        closeCurrentParagraph();
        internalStartParagraph( properties, null, true );
    }

    /**
     * Generate wpPr docx element.
     * 
     * @param properties
     * @param pStyle
     * @param isList
     * @throws IOException
     */
    private void processParagraphProperties( ContainerProperties properties, String pStyle, boolean isList )
        throws IOException
    {
        haspPr = false;
        if ( pStyle != null )
        {
            startPPrIfNeeded();
            super.write( "<w:pStyle w:val=\"" );
            super.write( pStyle );
            super.write( "\" />" );
        }
        if ( isList )
        {
            startPPrIfNeeded();
            super.write( "<w:numPr>" );

            // <w:ilvl w:val="0" />
            int ilvlVal = super.getCurrentListIndex();
            super.write( "<w:ilvl w:val=\"" );
            super.write( String.valueOf( ilvlVal ) );
            super.write( "\" />" );

            // "<w:numId w:val="1" />"
            int numIdVal = getCurrentNumId();
            super.write( "<w:numId w:val=\"" );
            super.write( String.valueOf( numIdVal ) );
            super.write( "\" />" );

            super.write( "</w:numPr>" );
        }
        //
        if ( properties != null )
        {
            // w:jc
            TextAlignment textAlignment = properties.getTextAlignment();
            if ( textAlignment != null )
            {
                switch ( textAlignment )
                {
                    case Left:
                        startPPrIfNeeded();
                        super.write( "<w:jc w:val=\"left\"/>" );
                        break;
                    case Center:
                        startPPrIfNeeded();
                        super.write( "<w:jc w:val=\"center\"/>" );
                        break;
                    case Right:
                        startPPrIfNeeded();
                        super.write( "<w:jc w:val=\"right\"/>" );
                        break;
                    case Justify:
                        startPPrIfNeeded();
                        super.write( "<w:jc w:val=\"both\"/>" );
                        break;
                }
            }
            // rPPr
            processRunProperties( true, properties.isBold(), properties.isItalic(), properties.isUnderline(),
                                  properties.isStrike(), properties.isSubscript(), properties.isSuperscript() );
        }

        endPPrIfNeeded();
    }

    private void startPPrIfNeeded()
        throws IOException
    {
        if ( !haspPr )
        {
            super.write( "<w:pPr>" );
        }
        haspPr = true;
    }

    private void endPPrIfNeeded()
        throws IOException
    {
        if ( haspPr )
        {
            super.write( "</w:pPr>" );
        }
    }

    public void endListItem()
        throws IOException
    {
        // endParagraph();
    }

    public void startHeading( int level, HeaderProperties properties )
        throws IOException
    {
        // Close current paragraph
        closeCurrentParagraph();

        // In docx, title is a paragraph with a style.

        /**
         * <w:p w:rsidR="00F030AA"s w:rsidRDefault="00285B63" w:rsidP="00285B63"> <w:pPr> <w:pStyle w:val="Titre1" />
         * </w:pPr> <w:r> <w:t>Titre1</w:t> </w:r> </w:p>
         */
        String headingStyleName = styleGen.getHeaderStyleId( level, defaultStyle );
        internalStartParagraph( properties, headingStyleName, false );
        super.write( "<w:r><w:t>" );
        insideHeader = true;
    }

    public void endHeading( int level )
        throws IOException
    {
        super.write( "</w:t></w:r>" );
        endParagraph();
        insideHeader = false;
    }

    public void startSpan( SpanProperties properties )
        throws IOException
    {
        spansStack.push( properties );
        // startParagraphIfNeeded();
        // super.write( "<w:r>" );
        // processRunProperties( false, properties.isBold(), properties.isItalic(), properties.isUnderline(),
        // properties.isStrike() );
        // super.write( "<w:t xml:space=\"preserve\" >" );
        // super.write( content );
        // super.write( "</w:t>" );
        // super.write( "</w:r>" );
    }

    private SpanProperties getCurrentSpanProperties()
    {
        if ( !spansStack.isEmpty() )
        {
            return spansStack.peek();
        }
        return null;
    }

    public void endSpan()
        throws IOException
    {
        spansStack.pop();
    }

    @Override
    protected void doStartOrderedList( ListProperties properties )
        throws IOException
    {
    	if(this.addLineBreak>0) 
    	{
    		handleString("");
    	}
        // if ( numbersStack.isEmpty() )
        // {
        if ( getCurrentListIndex() < 1 )
        {
            int abstractNumId = styleGen.getAbstractNumIdForList( true, defaultStyle );
            int numId = getNumberingRegistry().addNum( abstractNumId, getMaxNumId(), true ).getNumId();
            // numbersStack.push( numId );
            currentNumId = numId;
        }
        // }
    }

    @Override
    protected void doStartUnorderedList( ListProperties properties )
        throws IOException
    {
    	if(this.addLineBreak>0) 
    	{
    		handleString("");
    	}
        // if ( numbersStack.isEmpty() )
        // {
        if ( getCurrentListIndex() < 1 )
        {
            int abstractNumId = styleGen.getAbstractNumIdForList( false, defaultStyle );
            int numId = getNumberingRegistry().addNum( abstractNumId, getMaxNumId(), false ).getNumId();
            // numbersStack.push( numId );
            currentNumId = numId;
        }
        // }
    }

    private Integer getMaxNumId()
    {
        if ( defaultStyle == null )
        {
            return null;
        }
        return defaultStyle.getMaxNumId();
    }

    @Override
    protected void doEndUnorderedList()
        throws IOException
    {
        // Close current paragraph
        closeCurrentParagraph();
    }

    @Override
    protected void doEndOrderedList()
        throws IOException
    {
        // Close current paragraph
        closeCurrentParagraph();
    }

    private int getCurrentNumId()
    {
        return currentNumId;// numbersStack.peek();
    }

    public void handleReference( String ref, String label )
        throws IOException
    {
        if ( ref != null )
        {
            // 1) replace & with &amp;
            // fix issue http://code.google.com/p/xdocreport/issues/detail?id=147
            ref = StringUtils.xmlUnescape( ref );
            ref = StringUtils.xmlEscape( ref );
            // fix issue http://code.google.com/p/xdocreport/issues/detail?id=154
            label = StringUtils.xmlUnescape( label );
            label = StringUtils.xmlEscape( label );

            // 2) Update the hyperlink registry to modifiy the Hyperlink Relationship in the _rels/document.xml.rels
            HyperlinkRegistry registry = getHyperlinkRegistry();
            String rId = registry.registerHyperlink( ref );

            // 3) Generate w:hyperlink
            String hyperlinkStyleName = styleGen.getHyperLinkStyleId( defaultStyle );
            super.write( "<w:hyperlink r:id=\"" );
            super.write( rId );
            super.write( "\" w:history=\"1\"> " );
            super.write( "<w:proofErr w:type=\"spellStart\" />" );
            super.write( "<w:r w:rsidRPr=\"001D30B5\">" );
            super.write( "<w:rPr>" );
            super.write( "<w:rStyle w:val=\"" );
            super.write( hyperlinkStyleName );
            super.write( "\" />" );
            super.write( "</w:rPr>" );
            super.write( "<w:t>" );
            super.write( label != null ? label : ref );
            super.write( "</w:t>" );
            super.write( "</w:r>" );
            super.write( "<w:proofErr w:type=\"spellEnd\" />" );
            super.write( "</w:hyperlink>" );
        }
    }

    public void handleImage( String ref, String label )
        throws IOException
    {

    }

    private NumberingRegistry getNumberingRegistry()
    {
        return numberingRegistry;
    }

    private NumberingRegistry getNumberingRegistry( IContext context )
    {

        if ( context == null )
        {
            return new NumberingRegistry();
        }

        NumberingRegistry registry = DocxContextHelper.getNumberingRegistry( context );
        if ( registry == null )
        {
            registry = new NumberingRegistry();
            DocxContextHelper.putNumberingRegistry( context, registry );
        }
        return registry;

    }

    private HyperlinkRegistry getHyperlinkRegistry()
    {
        if ( hyperlinkRegistry != null )
        {
            return hyperlinkRegistry;
        }

        IContext context = getContext();
        if ( context == null )
        {
            hyperlinkRegistry = new HyperlinkRegistry();
            return hyperlinkRegistry;
        }

        String key = HyperlinkUtils.getHyperlinkRegistryKey( getEntryName() );
        hyperlinkRegistry = (HyperlinkRegistry) context.get( key );
        if ( hyperlinkRegistry == null )
        {
            hyperlinkRegistry = new HyperlinkRegistry();
            context.put( key, hyperlinkRegistry );
        }
        return hyperlinkRegistry;
    }

    public void handleLineBreak()
        throws IOException
    {
        // <w:br />
        this.addLineBreak++;
    }

    protected void doStartTable( TableProperties properties )
        throws IOException
    {
        closeCurrentParagraph();
        // ooxml table cannot be generated now, because here we don't know the table column count required to generate
        // w:tblGrid
        // that's here temp writer is used.
        pushTempWriter();
    }

    public void doEndTable( TableProperties properties )
        throws IOException
    {
        StringBuilder startTable = new StringBuilder( "<w:tbl>" );
        startTable.append( "<w:tblGrid>" );
        int count = properties.getColumnCount();
        for ( int i = 0; i < count; i++ )
        {
            startTable.append( "<w:gridCol w:w=\"2994\" />" );
        }
        startTable.append( "</w:tblGrid>" );
        popTempWriter( startTable.toString() );
        super.write( "</w:tbl>" );
        // if table is inside a table cell, a paragraph is required.
        super.write( "<w:p/>" );
    }

    public void doStartTableRow( TableRowProperties properties )
        throws IOException
    {
        super.write( "<w:tr>" );
    }

    public void doEndTableRow()
        throws IOException
    {
        super.write( "</w:tr>" );
    }

    public void doStartTableCell( TableCellProperties properties )
        throws IOException
    {
        super.write( "<w:tc>" );
        /*
         * super.write( "<w:tcPr>" ); super.write( "<w:tcW w:w=\"0\" w:type=\"auto\" />" ); super.write( "</w:tcPr>" );
         */
        internalStartParagraph( null );

    }

    public void doEndTableCell()
        throws IOException
    {
        endParagraph();
        super.write( "</w:tc>" );
    }

}

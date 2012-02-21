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
package fr.opensagres.xdocreport.document.docx.textstyling;

import java.io.IOException;
import java.util.Stack;

import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;
import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkRegistry;
import fr.opensagres.xdocreport.document.docx.preprocessor.HyperlinkUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.numbering.NumberingRegistry;
import fr.opensagres.xdocreport.document.docx.template.DocxContextHelper;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.textstyling.AbstractDocumentHandler;
import fr.opensagres.xdocreport.template.IContext;

/**
 * Document handler implementation to build docx fragment content.
 */
public class DocxDocumentHandler
    extends AbstractDocumentHandler
{

    private boolean bolding;

    private boolean italicsing;

    private Stack<Boolean> paragraphsStack;

    private HyperlinkRegistry hyperlinkRegistry;

    protected final IDocxStylesGenerator styleGen;

    private final NumberingRegistry numberingRegistry;

    private DefaultStyle defaultStyle;

    private boolean insideHeader;

    private boolean paragraphWasInserted;

    private Stack<Integer> numbersStack;

    public DocxDocumentHandler( BufferedElement parent, IContext context, String entryName )
    {
        super( parent, context, entryName );
        this.styleGen = DocxContextHelper.getStylesGenerator( context );
        this.defaultStyle = DocxContextHelper.getDefaultStyle( context );
        this.numberingRegistry = getNumberingRegistry( context );
        this.insideHeader = false;
        this.paragraphWasInserted = false;
        this.numbersStack = new Stack<Integer>();
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

    @Override
    public void handleString( String content )
        throws IOException
    {
        if ( insideHeader )
        {
            // Title of the Header.
            super.write( content );
        }
        else
        {
            startParagraphIfNeeded();
            super.write( "<w:r>" );
            if ( bolding || italicsing )
            {
                super.write( "<w:rPr>" );
                if ( bolding )
                {
                    super.write( "<w:b />" );
                }
                if ( italicsing )
                {
                    super.write( "<w:i />" );
                }
                super.write( "</w:rPr>" );
            }
            super.write( "<w:t xml:space=\"preserve\" >" );
            super.write( content );
            super.write( "</w:t>" );
            super.write( "</w:r>" );
        }
    }

    private void startParagraphIfNeeded()
        throws IOException
    {

        if ( paragraphWasInserted && paragraphsStack.isEmpty() )
        {
            startParagraph( false );
        }
    }

    private void startParagraph( boolean realParagraph )
        throws IOException
    {
        paragraphWasInserted = true;
        super.setTextLocation( TextLocation.End );
        super.write( "<w:p>" );
        paragraphsStack.push( realParagraph );
    }

    public void startParagraph()
        throws IOException
    {
        // if (!paragraphsStack.isEmpty()) {
        closeCurrentParagraph();
        // }
        startParagraph( true );
    }

    public void endParagraph()
        throws IOException
    {
        super.write( "</w:p>" );
        paragraphsStack.pop();
    }

    public void startListItem()
        throws IOException
    {
        startParagraph( false );
        super.write( "<w:pPr>" );
        // super.write( "<w:pStyle w:val=\"Paragraphedeliste\" />" );
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
        super.write( "</w:pPr>" );

    }

    public void endListItem()
        throws IOException
    {
        endParagraph();
    }

    public void startHeading( int level )
        throws IOException
    {
        // Close current paragraph
        closeCurrentParagraph();
        startParagraph( false );

        // In docx, title is a paragraph with a style.

        /**
         * <w:p w:rsidR="00F030AA"s w:rsidRDefault="00285B63" w:rsidP="00285B63"> <w:pPr> <w:pStyle w:val="Titre1" />
         * </w:pPr> <w:r> <w:t>Titre1</w:t> </w:r> </w:p>
         */
        String headingStyleName = styleGen.getHeaderStyleId( level, defaultStyle );
        super.write( "<w:pPr>" );
        super.write( "<w:pStyle w:val=\"" );
        super.write( headingStyleName );
        super.write( "\" /></w:pPr><w:r><w:t>" );
        insideHeader = true;
    }

    public void endHeading( int level )
        throws IOException
    {
        super.write( "</w:t></w:r>" );
        endParagraph();
        insideHeader = false;
    }

    @Override
    protected void doStartOrderedList()
        throws IOException
    {
        int abstractNumId = styleGen.getAbstractNumIdForList( true, defaultStyle );
        int numId = getNumberingRegistry().addNum( abstractNumId, getMaxNumId() ).getNumId();
        numbersStack.push( numId );
    }

    @Override
    protected void doStartUnorderedList()
        throws IOException
    {
        int abstractNumId = styleGen.getAbstractNumIdForList( false, defaultStyle );
        int numId = getNumberingRegistry().addNum( abstractNumId, getMaxNumId() ).getNumId();
        numbersStack.push( numId );
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
        numbersStack.pop();
    }

    @Override
    protected void doEndOrderedList()
        throws IOException
    {
        numbersStack.pop();
    }

    private int getCurrentNumId()
    {
        return numbersStack.peek();
    }

    public void handleReference( String ref, String label )
        throws IOException
    {
        if ( ref != null )
        {
            // 1) Update the hyperlink registry to modifiy the Hyperlink Relationship in the _rels/document.xml.rels
            HyperlinkRegistry registry = getHyperlinkRegistry();
            String rId = registry.registerHyperlink( ref );

            // 2) Generate w:hyperlink
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
}

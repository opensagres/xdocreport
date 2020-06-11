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
package fr.opensagres.odfdom.converter.pdf.internal;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.element.OdfStylableElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawCustomShapeElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawFrameElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawImageElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawLineElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawTextBoxElement;
import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement;
import org.odftoolkit.odfdom.dom.element.style.StyleFooterElement;
import org.odftoolkit.odfdom.dom.element.style.StyleFooterLeftElement;
import org.odftoolkit.odfdom.dom.element.style.StyleHeaderElement;
import org.odftoolkit.odfdom.dom.element.style.StyleHeaderLeftElement;
import org.odftoolkit.odfdom.dom.element.style.StyleMasterPageElement;
import org.odftoolkit.odfdom.dom.element.svg.SvgDescElement;
import org.odftoolkit.odfdom.dom.element.svg.SvgTitleElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableHeaderRowsElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableRowElement;
import org.odftoolkit.odfdom.dom.element.text.TextAElement;
import org.odftoolkit.odfdom.dom.element.text.TextBookmarkElement;
import org.odftoolkit.odfdom.dom.element.text.TextBookmarkStartElement;
import org.odftoolkit.odfdom.dom.element.text.TextHElement;
import org.odftoolkit.odfdom.dom.element.text.TextIndexBodyElement;
import org.odftoolkit.odfdom.dom.element.text.TextLineBreakElement;
import org.odftoolkit.odfdom.dom.element.text.TextListElement;
import org.odftoolkit.odfdom.dom.element.text.TextListItemElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.dom.element.text.TextPageCountElement;
import org.odftoolkit.odfdom.dom.element.text.TextPageNumberElement;
import org.odftoolkit.odfdom.dom.element.text.TextParagraphElementBase;
import org.odftoolkit.odfdom.dom.element.text.TextSElement;
import org.odftoolkit.odfdom.dom.element.text.TextSectionElement;
import org.odftoolkit.odfdom.dom.element.text.TextSoftPageBreakElement;
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement;
import org.odftoolkit.odfdom.dom.element.text.TextTabElement;
import org.odftoolkit.odfdom.dom.element.text.TextTableOfContentElement;
import org.odftoolkit.odfdom.dom.element.text.TextTableOfContentSourceElement;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.pkg.OdfElement;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Image;

import fr.opensagres.odfdom.converter.core.ElementVisitorConverter;
import fr.opensagres.odfdom.converter.core.ODFConverterException;
import fr.opensagres.odfdom.converter.core.utils.ODFUtils;
import fr.opensagres.odfdom.converter.pdf.PdfOptions;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.IStylableContainer;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.IStylableElement;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableAnchor;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableChunk;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableDocument;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableDocumentSection;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableHeaderFooter;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableHeading;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableImage;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableList;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableListItem;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableMasterPage;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableParagraph;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableParagraphWrapper;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylablePhrase;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableTab;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableTable;
import fr.opensagres.odfdom.converter.pdf.internal.stylable.StylableTableCell;
import fr.opensagres.odfdom.converter.pdf.internal.styles.Style;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTextProperties;
import fr.opensagres.xdocreport.utils.StringUtils;

/**
 * fixes for pdf conversion by Leszek Piotrowicz <leszekp@safe-mail.net>
 */
public class ElementVisitorForIText
    extends ElementVisitorConverter
{
    private final StyleEngineForIText styleEngine;

    // private final PDFViaITextOptions options;

    private IStylableContainer currentContainer;

    private StylableMasterPage currentMasterPage;

    private StylableDocument document;

    private boolean parseOfficeTextElement = false;

    private boolean inTableOfContent; // tabs processing

    private List<Integer> currentHeadingNumbering; // heading processing

    private StylableTable currentTable; // table processing

    private int currentListLevel; // list processing

    private StylableList previousList; // list processing

    private Integer forcedPageCount; // page count processing

    private Integer expectedPageCount; // page count processing
    
    // Image Cache
    private Map<String, Image> imageCache = new HashMap<String, Image>();

    public ElementVisitorForIText( OdfDocument odfDocument, OutputStream out, StyleEngineForIText styleEngine,
                                   PdfOptions options, Integer forcedPageCount )
    {
        super( odfDocument, options.getExtractor(), out, null );
        this.styleEngine = styleEngine;
        this.forcedPageCount = forcedPageCount;
        // this.options = options != null ? options : PDFViaITextOptions.create();
        // Create document
        try
        {
            document = new StylableDocument( out, options.getConfiguration(), styleEngine );
        }
        catch ( DocumentException e )
        {
            throw new ODFConverterException( e );
        }
    }

    public Integer getExpectedPageCount()
    {
        return expectedPageCount;
    }

    public int getActualPageCount()
    {
        if ( document.isOpen() )
        {
            return document.getPageNumber();
        }
        else
        {
            return document.getPageNumber() - 1;
        }
    }

    // ---------------------- visit root
    // styles.xml//office:document-styles/office:master-styles/style:master-page

    /**
     * Generate XHTML page footer + header
     */
    @Override
    public void visit( StyleMasterPageElement ele )
    {
        String name = ele.getStyleNameAttribute();
        String pageLayoutName = ele.getStylePageLayoutNameAttribute();
        String nextStyleName = ele.getStyleNextStyleNameAttribute();
        currentMasterPage = new StylableMasterPage( name, pageLayoutName, nextStyleName );
        document.addMasterPage( currentMasterPage );
        super.visit( ele );
        currentMasterPage = null;
    }

    // ---------------------- visit
    // styles.xml//office:document-styles/office:master-styles/style:master-page/style-header

    @Override
    public void visit( StyleHeaderElement ele )
    {
        StylableHeaderFooter header = document.createHeaderFooter( true );
        Style style = document.getStyleMasterPage( currentMasterPage );
        if ( style != null )
        {
            document.applyStyles( style );
            header.applyStyles( style );
        }
        currentMasterPage.setHeader( header );
        StylableTableCell tableCell = header.getTableCell();
        currentContainer = tableCell;
        super.visit( ele );
        header.flush();
        currentContainer = null;
    }

    @Override
    public void visit( StyleHeaderLeftElement ele )
    {
        // TODO : implement it.
    }

    // ---------------------- visit
    // styles.xml//office:document-styles/office:master-styles/style:master-page/style-footer

    @Override
    public void visit( StyleFooterElement ele )
    {
        StylableHeaderFooter footer = document.createHeaderFooter( false );
        Style style = document.getStyleMasterPage( currentMasterPage );
        if ( style != null )
        {
            document.applyStyles( style );
            footer.applyStyles( style );
        }
        currentMasterPage.setFooter( footer );
        StylableTableCell tableCell = footer.getTableCell();
        currentContainer = tableCell;
        super.visit( ele );
        footer.flush();
        currentContainer = null;

    }

    @Override
    public void visit( StyleFooterLeftElement ele )
    {
        // TODO : implement it.
    }

    // ---------------------- visit root //office-body/office-text

    @Override
    public void visit( OfficeTextElement ele )
    {
        this.parseOfficeTextElement = true;
        currentContainer = document;
        super.visit( ele );
        this.parseOfficeTextElement = false;
    }

    @Override
    public void visit( TextTableOfContentElement ele )
    {
        inTableOfContent = true;
        super.visit( ele );
        inTableOfContent = false;
    }

    @Override
    public void visit( TextTableOfContentSourceElement ele )
    {
        // do not visit child nodes
        // they may contain unnecessary text
    }

    @Override
    public void visit( TextIndexBodyElement ele )
    {
        super.visit( ele );
    }

    @Override
    public void visit( TextSectionElement ele )
    {
        StylableDocumentSection documentSection =
            document.createDocumentSection( currentContainer, !parseOfficeTextElement );
        applyStyles( ele, documentSection );
        addITextContainer( ele, documentSection );
    }

    // ---------------------- visit //text:h

    @Override
    public void visit( TextHElement ele )
    {
        // compute heading numbering (ie 1.3.1)
        int outlineLevel = ele.getTextOutlineLevelAttribute() != null ? ele.getTextOutlineLevelAttribute() : 1;
        if ( currentHeadingNumbering == null )
        {
            currentHeadingNumbering = new ArrayList<Integer>();
        }
        while ( currentHeadingNumbering.size() > outlineLevel )
        {
            currentHeadingNumbering.remove( currentHeadingNumbering.size() - 1 );
        }
        if ( currentHeadingNumbering.size() == outlineLevel )
        {
            currentHeadingNumbering.set( outlineLevel - 1, currentHeadingNumbering.get( outlineLevel - 1 ) + 1 );
        }
        while ( currentHeadingNumbering.size() < outlineLevel )
        {
            currentHeadingNumbering.add( StylableHeading.getFirst( currentContainer.getLastStyleApplied(),
                                                                   currentHeadingNumbering.size() + 1 ) );
        }

        processParagraphOrHeading( ele, new ArrayList<Integer>( currentHeadingNumbering ) );
    }

    // ---------------------- visit //text:p

    @Override
    public void visit( TextPElement ele )
    {
        processParagraphOrHeading( ele, null );
    }

    private void processParagraphOrHeading( TextParagraphElementBase ele, List<Integer> headingNumbering )
    {
        StylableParagraphWrapper paragraphWrapper = createParagraphWrapperAndApplyStyles( ele );
        if ( paragraphWrapper.hasBorders() || paragraphWrapper.hasBackgroundColor() )
        {
            // paragraph has borders or background
            // have to wrap it in a table
            boolean joinWithPrevious = joinParagraphWith( paragraphWrapper, ele.getPreviousSibling() );
            boolean joinWithNext = joinParagraphWith( paragraphWrapper, ele.getNextSibling() );
            // start paragraph wrapper
            if ( joinWithPrevious )
            {
                // add this paragraph to existing wrapper
            }
            else
            {
                // start a new wrapper
                currentContainer = paragraphWrapper;
            }
            // process paragraph
            StylableParagraph paragraph =
                headingNumbering != null ? document.createHeading( currentContainer, headingNumbering )
                                : document.createParagraph( currentContainer );
            applyStyles( ele, paragraph );
            // apply top or bottom margin if necessary
            if ( joinWithNext )
            {
                paragraph.setSpacingAfter( paragraphWrapper );
            }
            if ( joinWithPrevious )
            {
                paragraph.setSpacingBefore( paragraphWrapper );
            }
            addITextContainer( ele, paragraph );
            // end paragraph wrapper
            if ( joinWithNext )
            {
                // some paragraph will be added to current wrapper, do nothing
            }
            else
            {
                // end current wrapper
                IStylableContainer oldContainer = currentContainer.getParent();
                oldContainer.addElement( currentContainer.getElement() );
                currentContainer = oldContainer;
            }
        }
        else
        {
            // paragraph has no border and no background
            // don't have to wrap it in a table
            StylableParagraph paragraph =
                headingNumbering != null ? document.createHeading( currentContainer, headingNumbering )
                                : document.createParagraph( currentContainer );
            applyStyles( ele, paragraph );
            // apply margin (left, right, top, bottom) values
            paragraph.setIndentation( paragraphWrapper );
            paragraph.setSpacingBefore( paragraphWrapper );
            paragraph.setSpacingAfter( paragraphWrapper );
            addITextContainer( ele, paragraph );
        }
    }

    private StylableParagraphWrapper createParagraphWrapperAndApplyStyles( TextParagraphElementBase ele )
    {
        StylableParagraphWrapper paragraphWrapper = new StylableParagraphWrapper( document, currentContainer );
        applyStyles( ele, paragraphWrapper );
        return paragraphWrapper;
    }

    private boolean joinParagraphWith( StylableParagraphWrapper paragraphWrapper1, Node node )
    {
        if ( node instanceof TextParagraphElementBase )
        {
            TextParagraphElementBase ele = (TextParagraphElementBase) node;
            StylableParagraphWrapper paragraphWrapper2 = createParagraphWrapperAndApplyStyles( ele );
            Style style1 = paragraphWrapper1.getLastStyleApplied();
            Style style2 = paragraphWrapper2.getLastStyleApplied();
            if ( style1 != null && style2 != null )
            {
                String styleName1 = style1.getStyleName();
                String styleName2 = style2.getStyleName();
                if ( styleName1 != null && styleName1.equals( styleName2 ) )
                {
                    boolean hasBorders = paragraphWrapper1.hasBorders();
                    boolean joinBorder = paragraphWrapper1.joinBorder();
                    if ( hasBorders && joinBorder )
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // ---------------------- visit //text:span

    @Override
    public void visit( TextSpanElement ele )
    {
        StylablePhrase phrase = document.createPhrase( currentContainer );
        applyStyles( ele, phrase );
        addITextContainer( ele, phrase );
    }

    // ---------------------- visit //text:a

    @Override
    public void visit( TextAElement ele )
    {
        StylableAnchor anchor = document.createAnchor( currentContainer );
        String reference = ele.getXlinkHrefAttribute();
        applyStyles( ele, anchor );

        if ( anchor.getFont().getColor() == null )
        {
            // if no color was applied to the link get the font of the paragraph and set blue color.
            Font linkFont = anchor.getFont();
            Style style = currentContainer.getLastStyleApplied();
            if ( style != null )
            {
                StyleTextProperties textProperties = style.getTextProperties();
                if ( textProperties != null )
                {
                    Font font = textProperties.getFont();
                    if ( font != null )
                    {
                        linkFont = new Font( font );
                        anchor.setFont( linkFont );
                    }
                }
            }
            linkFont.setColor( Color.BLUE );
        }

        // set the link
        if ( reference.endsWith( StylableHeading.IMPLICIT_REFERENCE_SUFFIX ) )
        {
            reference = "#" + StylableHeading.generateImplicitDestination( reference );
        }
        anchor.setReference( reference );
        // Add to current container.
        addITextContainer( ele, anchor );
    }

    @Override
    public void visit( TextBookmarkElement ele )
    {
        // destination for a local anchor
        // chunk with empty text does not work as local anchor
        // so we create chunk with invisible but not empty text content
        // if bookmark is the last chunk in a paragraph something must be added after or it does not work
        createAndAddChunk( ODFUtils.TAB_STR, ele.getTextNameAttribute(), false );
    }

    @Override
    public void visit( TextBookmarkStartElement ele )
    {
        createAndAddChunk( ODFUtils.TAB_STR, ele.getTextNameAttribute(), false );
        super.visit( ele );
    }

    // ---------------------- visit table:table (ex : <table:table
    // table:name="Tableau1" table:style-name="Tableau1">)

    @Override
    public void visit( TableTableElement ele )
    {
        float[] columnWidth = ODFUtils.getColumnWidths( ele, odfDocument );
        StylableTable table = document.createTable( currentContainer, columnWidth.length );
        try
        {
            table.setTotalWidth( columnWidth );
        }
        catch ( DocumentException e )
        {
            // Do nothing
        }
        applyStyles( ele, table );
        StylableTable oldTable = currentTable;
        currentTable = table;
        addITextContainer( ele, table );
        currentTable = oldTable;
    }

    // ---------------------- visit table:table-header-rows

    @Override
    public void visit( TableTableHeaderRowsElement ele )
    {
        // we want to count table rows nested inside table header rows element
        // to determine how many header rows we have in current table
        currentTable.beginTableHeaderRows();
        super.visit( ele );
        currentTable.endTableHeaderRows();
    }

    // ---------------------- visit table:table-row

    @Override
    public void visit( TableTableRowElement ele )
    {
        Style currentRowStyle = getStyle( ele, null );
        if ( currentRowStyle != null )
        {
            currentTable.applyStyles( currentRowStyle );
        }
        currentTable.beginTableRow( currentRowStyle );
        super.visit( ele );
        currentTable.endTableRow();
    }

    // ---------------------- visit table:table-cell

    @Override
    public void visit( TableTableCellElement ele )
    {
        StylableTableCell tableCell = document.createTableCell( currentContainer );

        // table:number-columns-spanned
        Integer colSpan = ele.getTableNumberColumnsSpannedAttribute();
        if ( colSpan != null )
        {
            tableCell.setColspan( colSpan );
        }
        // table:number-rows-spanned
        Integer rowSpan = ele.getTableNumberRowsSpannedAttribute();
        if ( rowSpan != null )
        {
            tableCell.setRowspan( rowSpan );

        }
        // Apply styles coming from table-row
        if ( currentTable.getCurrentRowStyle() != null )
        {
            tableCell.applyStyles( currentTable.getCurrentRowStyle() );
        }
        // Apply styles coming from table-cell
        applyStyles( ele, tableCell );
        addITextContainer( ele, tableCell );
    }

    // ---------------------- visit text:list

    @Override
    public void visit( TextListElement ele )
    {
        currentListLevel++;
        StylableList list = document.createList( currentContainer, currentListLevel );
        applyStyles( ele, list );
        Boolean continueNumbering = ele.getTextContinueNumberingAttribute();
        if (continueNumbering == null){
        	continueNumbering = ele.getTextStyleNameAttribute() != null;
        }        
        if ( Boolean.TRUE.equals( continueNumbering ) && previousList != null
            && previousList.getLastStyleApplied() != null && list.getLastStyleApplied() != null
            && previousList.getLastStyleApplied().getStyleName() != null
            && previousList.getLastStyleApplied().getStyleName().equals( list.getLastStyleApplied().getStyleName() ) )
        {
            list.setFirst( previousList.getIndex() );
        }
        addITextContainer( ele, list );
        currentListLevel--;
        previousList = list;
    }

    // ---------------------- visit text:listitem

    @Override
    public void visit( TextListItemElement ele )
    {
        StylableListItem listItem = document.createListItem( currentContainer );
        addITextContainer( ele, listItem );
    }

    // ---------------------- visit draw:image

    @Override
    protected void visitImage( DrawImageElement ele, String href, byte[] imageStream )
    {    	
    	// add image in the pdf.
		Image imageObj = imageCache.get(href);
		if (imageObj == null) 
		{
			imageObj = StylableImage.getImage(imageStream);
			imageCache.put(href, imageObj);
		} 
		
		if (imageObj != null) 
		{
            DrawFrameElement frame = null;
            Float x = null;
            Float y = null;
            Float width = null;
            Float height = null;
            // set width, height....image
            Node parentNode = ele.getParentNode();
            if ( parentNode instanceof DrawFrameElement )
            {
                frame = (DrawFrameElement) parentNode;
                String svgX = frame.getSvgXAttribute();
                if ( StringUtils.isNotEmpty( svgX ) )
                {
                    x = ODFUtils.getDimensionAsPoint( svgX );
                }
                String svgY = frame.getSvgYAttribute();
                if ( StringUtils.isNotEmpty( svgY ) )
                {
                    y = ODFUtils.getDimensionAsPoint( svgY );
                }
                String svgWidth = frame.getSvgWidthAttribute();
                if ( StringUtils.isNotEmpty( svgWidth ) )
                {
                    width = ODFUtils.getDimensionAsPoint( svgWidth );
                }
                String svgHeight = frame.getSvgHeightAttribute();
                if ( StringUtils.isNotEmpty( svgHeight ) )
                {
                    height = ODFUtils.getDimensionAsPoint( svgHeight );
                }
            }
            StylableImage image = document.createImage( currentContainer, imageObj, x, y, width, height );
            if ( frame != null )
            {
                applyStyles( frame, image );
            }
            addITextElement( image );
        }
    }

    @Override
    protected boolean isNeedImageStream()
    {
        return true;
    }

    @Override
    public void visit( DrawTextBoxElement ele )
    {
        // do not visit child nodes
        // they may contain unnecessary text
    }

    @Override
    public void visit( DrawLineElement ele )
    {
        // do not visit child nodes
        // they may contain unnecessary text
    }

    @Override
    public void visit( DrawCustomShapeElement ele )
    {
        // do not visit child nodes
        // they may contain unnecessary text
    }

    @Override
    public void visit( SvgTitleElement ele )
    {
        // do not visit child nodes
        // they may contain unnecessary text
    }

    @Override
    public void visit( SvgDescElement ele )
    {
        // do not visit child nodes
        // they may contain unnecessary text
    }

    // ---------------------- visit //text:soft-page-break

    @Override
    public void visit( TextSoftPageBreakElement ele )
    {
    }

    // ---------------------- visit //text:tab

    @Override
    public void visit( TextTabElement ele )
    {
        StylableTab tab = document.createTab( currentContainer, inTableOfContent );
        Style style = currentContainer.getLastStyleApplied();
        if ( style != null )
        {
            tab.applyStyles( style );
        }
        addITextElement( tab );
    }

    // ---------------------- visit text:line-break

    @Override
    public void visit( TextLineBreakElement ele )
    {
        createAndAddChunk( "\n", null, false );
    }

    // ---------------------- visit text:s

    @Override
    public void visit( TextSElement ele )
    {
        String spaceStr = " ";
        Integer spaceCount = ele.getTextCAttribute();
        if ( spaceCount != null && spaceCount > 1 )
        {
            for ( int i = 1; i < spaceCount; i++ )
            {
                spaceStr += " ";
            }
        }
        createAndAddChunk( spaceStr, null, false );
    }

    @Override
    public void visit( TextPageNumberElement ele )
    {
        createAndAddChunk( "#", null, true );
    }

    @Override
    public void visit( TextPageCountElement ele )
    {
        if ( forcedPageCount != null )
        {
            createAndAddChunk( forcedPageCount.toString(), null, false );
        }
        else
        {
            String textContent = ele.getTextContent();
            try
            {
                int pageCount = Integer.parseInt( textContent );
                if ( expectedPageCount == null || expectedPageCount == pageCount )
                {
                    expectedPageCount = pageCount;
                }
                else if ( expectedPageCount != pageCount )
                {
                    expectedPageCount = -1;
                }
            }
            catch ( NumberFormatException e )
            {
                expectedPageCount = -1;
            }
            textContent = expectedPageCount != null & expectedPageCount >= 0 ? expectedPageCount.toString() : "#";
            createAndAddChunk( textContent, null, false );
        }
    }

    @Override
    protected void processTextNode( Text node )
    {
        createAndAddChunk( node.getTextContent(), null, false );
    }

    private void createAndAddChunk( String textContent, String localDestinationName, boolean pageNumberChunk )
    {
        // StylableChunk can replace several ODT elements
        // plain text
        // text:bookmark
        // text:line-break
        // text:s
        // text:page-number
        // text:page-count
        List<StylableChunk> chunks = StylableChunk.createChunks( document, currentContainer, textContent );
        for ( StylableChunk chunk : chunks )
        {
            Style style = currentContainer.getLastStyleApplied();
            if ( style != null )
            {
                chunk.applyStyles( style );
            }
            if ( localDestinationName != null )
            {
                chunk.setLocalDestination( localDestinationName );
            }
            if ( pageNumberChunk )
            {
                chunk.setPageNumberChunk( pageNumberChunk );
            }
            addITextElement( chunk );
        }
    }

    @Override
    public void save()
        throws IOException
    {
        if ( document != null )
        {
            document.close();
        }
        super.save();
    }

    private void addITextContainer( OdfElement ele, IStylableContainer newContainer )
    {
        addITextContainer( ele, newContainer, true );
    }

    private void addITextContainer( OdfElement ele, IStylableContainer newContainer, boolean add )
    {
        IStylableContainer oldContainer = currentContainer;
        try
        {
            currentContainer = newContainer;
            super.visit( ele );
            if ( add )
            {
                oldContainer.addElement( newContainer.getElement() );
            }
        }
        finally
        {
            currentContainer = oldContainer;
        }
    }

    private void addITextElement( IStylableElement element )
    {
        currentContainer.addElement( element.getElement() );
    }

    private void applyStyles( OdfElement ele, IStylableElement element )
    {
        Style style = getStyle( ele, element );
        if ( style != null )
        {
            if ( parseOfficeTextElement )
            {
                String masterPageName = style.getMasterPageName();
                if ( StringUtils.isNotEmpty( masterPageName ) )
                {
                    // explicit master page activation
                    StylableMasterPage masterPage = document.getMasterPage( masterPageName );
                    if ( masterPage != null && masterPage != document.getActiveMasterPage() )
                    {
                        document.setActiveMasterPage( masterPage );
                    }
                }
                else if ( document.getActiveMasterPage() == null )
                {
                    // no master page was activated yet
                    // activate default
                    document.setActiveMasterPage( document.getDefaultMasterPage() );
                }
            }
            element.applyStyles( style );
        }
    }

    private Style getStyle( OdfElement e, IStylableElement element )
    {
        Style style = null;
        Style parentElementStyle = element != null ? getParentElementStyle( element ) : null;
        if ( e instanceof OdfStylableElement )
        {
            OdfStylableElement ele = (OdfStylableElement) e;

            String styleName = ele.getStyleName();
            String familyName = ele.getStyleFamily() != null ? ele.getStyleFamily().getName() : null;

            style = styleEngine.getStyle( familyName, styleName, parentElementStyle );
        }
        else if ( e instanceof TextListElement )
        {
            TextListElement ele = (TextListElement) e;

            String styleName = ele.getTextStyleNameAttribute();

            style = styleEngine.getStyle( OdfStyleFamily.List.getName(), styleName, parentElementStyle );
        }
        return style;
    }

    private Style getParentElementStyle( IStylableElement element )
    {
        for ( IStylableContainer c = element.getParent(); c != null; c = c.getParent() )
        {
            Style style = c.getLastStyleApplied();
            if ( style != null )
            {
                return style;
            }
        }
        return null;
    }
}

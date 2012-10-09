/**
 * Copyright (C) 2011 The XDocReport Team <xdocreport@googlegroups.com>
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
package org.apache.poi.xwpf.converter.internal.itext;

import static org.apache.poi.xwpf.converter.internal.DxaUtil.dxa2points;

import java.awt.Color;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.xwpf.converter.internal.XWPFDocumentVisitor;
import org.apache.poi.xwpf.converter.internal.XWPFUtils;
import org.apache.poi.xwpf.converter.internal.itext.stylable.IStylableContainer;
import org.apache.poi.xwpf.converter.internal.itext.stylable.IStylableElement;
import org.apache.poi.xwpf.converter.internal.itext.stylable.StylableDocument;
import org.apache.poi.xwpf.converter.internal.itext.stylable.StylableHeaderFooter;
import org.apache.poi.xwpf.converter.internal.itext.stylable.StylableMasterPage;
import org.apache.poi.xwpf.converter.internal.itext.stylable.StylableParagraph;
import org.apache.poi.xwpf.converter.internal.itext.stylable.StylableTable;
import org.apache.poi.xwpf.converter.internal.itext.stylable.StylableTableCell;
import org.apache.poi.xwpf.converter.internal.itext.styles.Style;
import org.apache.poi.xwpf.converter.itext.PDFViaITextOptions;
import org.apache.poi.xwpf.converter.styles.pargraph.ParagraphIndentationLeftValueProvider;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFAbstractNum;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFNum;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc.Enum;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.draw.VerticalPositionMark;

import fr.opensagres.xdocreport.itext.extension.ExtendedParagraph;
import fr.opensagres.xdocreport.itext.extension.ExtendedPdfPCell;
import fr.opensagres.xdocreport.itext.extension.ExtendedPdfPTable;
import fr.opensagres.xdocreport.itext.extension.IITextContainer;

public class PDFMapper
    extends XWPFDocumentVisitor<IITextContainer, StylableMasterPage>
{

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( PDFMapper.class.getName() );

    // Create instance of PDF document
    private StylableDocument pdfDocument;

    private StyleEngineForIText styleEngine;

    private final PDFViaITextOptions options;

    public PDFMapper( XWPFDocument document, PDFViaITextOptions options )
        throws Exception
    {
        super( document );
        this.options = options != null ? options : PDFViaITextOptions.create();
    }

    @Override
    protected IITextContainer startVisitDocument( OutputStream out )
        throws Exception
    {
        // Create instance of PDF document
        styleEngine = new StyleEngineForIText( document, options );
        this.pdfDocument = new StylableDocument( out, styleEngine );
        this.pdfDocument.setMasterPageManager( getMasterPageManager() );
        return pdfDocument;
    }

    @Override
    protected void endVisitDocument()
        throws Exception
    {
        pdfDocument.close();
    }

    @Override
    protected void visitHeader( XWPFHeader header, CTHdrFtrRef headerRef, CTSectPr sectPr, StylableMasterPage masterPage )
        throws Exception
    {
        StylableHeaderFooter pdfHeader = new StylableHeaderFooter( pdfDocument, true );
        List<IBodyElement> bodyElements = header.getBodyElements();
        StylableTableCell tableCell = getHeaderFooterTableCell( pdfHeader, bodyElements );
        visitBodyElements( bodyElements, tableCell );
        masterPage.setHeader( pdfHeader );
    }

    @Override
    protected void visitFooter( XWPFFooter footer, CTHdrFtrRef footerRef, CTSectPr sectPr, StylableMasterPage masterPage )
        throws Exception
    {
        StylableHeaderFooter pdfFooter = new StylableHeaderFooter( pdfDocument, false );
        List<IBodyElement> bodyElements = footer.getBodyElements();
        StylableTableCell tableCell = getHeaderFooterTableCell( pdfFooter, bodyElements );
        visitBodyElements( footer.getBodyElements(), tableCell );
        masterPage.setFooter( pdfFooter );
    }

    private StylableTableCell getHeaderFooterTableCell( StylableHeaderFooter pdfHeaderFooter,
                                                        List<IBodyElement> bodyElements )
        throws DocumentException
    {
        // XWPFTable table = getFirstTable( bodyElements );
        // if ( table != null )
        // {
        // StylableTable pdfTable = createPDFTable( table );
        // return pdfHeaderFooter.getTableCell( pdfTable );
        // }
        return pdfHeaderFooter.getTableCell();
    }

    private XWPFTable getFirstTable( List<IBodyElement> bodyElements )
    {
        if ( bodyElements.isEmpty() )
        {
            return null;
        }
        IBodyElement firstElement = bodyElements.get( 0 );
        if ( firstElement.getElementType() == BodyElementType.TABLE )
        {
            return (XWPFTable) firstElement;
        }
        return null;
    }

    @Override
    protected IITextContainer startVisitPargraph( XWPFParagraph docxParagraph, IITextContainer parentContainer )
        throws Exception
    {
        if ( docxParagraph.getText().startsWith( "Cette commande client est co" ) )
        {
            System.err.println();
        }
        // 1) Instanciate a pdfParagraph
        StylableParagraph pdfParagraph = pdfDocument.createParagraph( (IStylableContainer) null );
        pdfParagraph.setITextContainer( parentContainer );

        // Paragraph Background color
        Color backgroundColor = XWPFParagraphUtils.getBackgroundColor( docxParagraph );
        if ( backgroundColor != null )
        {
            pdfParagraph.setBackgroundColor( backgroundColor );
        }

        // Paragraph border
        int border = 0;
        Borders borderTop = docxParagraph.getBorderTop();
        if ( hasBorder( borderTop ) )
        {
            border = border | Rectangle.TOP;
        }
        Borders borderBottom = docxParagraph.getBorderBottom();
        if ( hasBorder( borderBottom ) )
        {
            border = border | Rectangle.BOTTOM;
        }
        Borders borderLeft = docxParagraph.getBorderLeft();
        if ( hasBorder( borderLeft ) )
        {
            border = border | Rectangle.LEFT;
        }
        Borders borderRight = docxParagraph.getBorderRight();
        if ( hasBorder( borderRight ) )
        {
            border = border | Rectangle.RIGHT;
        }
        if ( border > 0 )
        {
            // pdfParagraph.getPdfPCell().setBorder( border );
            // pdfParagraph.getPdfPCell().setCellEvent( new CustomPdfPCellEvent() );
        }

        // text-indent
        Float indentationLeft = stylesDocument.getIndentationLeft( docxParagraph );
        if ( indentationLeft != null )
        {
            pdfParagraph.setIndentationLeft( indentationLeft );
        }
        Float indentationRight = stylesDocument.getIndentationRight( docxParagraph );
        if ( indentationRight != null )
        {
            pdfParagraph.setIndentationRight( indentationRight );
        }
        Float indentationFirstLine = stylesDocument.getIndentationFirstLine( docxParagraph );
        if ( indentationFirstLine != null )
        {
            pdfParagraph.setFirstLineIndent( indentationFirstLine );
        }

        // spacing before
        Integer spacingBefore = stylesDocument.getSpacingBefore( docxParagraph );
        if ( spacingBefore != null )
        {
            pdfParagraph.setSpacingBefore( spacingBefore );
        }

        // spacing after
        Integer spacingAfter = stylesDocument.getSpacingAfter( docxParagraph );
        if ( spacingAfter != null )
        {
            pdfParagraph.setSpacingAfter( spacingAfter.intValue() );
        }

        CTPPr ppr = docxParagraph.getCTP().getPPr();
        if ( ppr != null )
        {
            CTSpacing spacing = ppr.getSpacing();
            if ( spacing != null )
            {
                BigInteger line = spacing.getLine();
                if ( line != null )
                {
                    float leading = -1;
                    // see http://officeopenxml.com/WPspacing.php
                    // Note: If the value of the lineRule attribute is atLeast or exactly, then the value of the line
                    // attribute is interpreted as 240th of a point. If the value of lineRule is auto, then the value of
                    // line is interpreted as 240th of a line.
                    LineSpacingRule lineSpacingRule = docxParagraph.getSpacingLineRule();
                    switch ( lineSpacingRule )
                    {
                        case AT_LEAST:
                        case EXACT:
                            // FIXME : is that?
                            leading = line.floatValue() / 240;
                            break;
                        case AUTO:
                            leading = line.floatValue() / 240;
                            break;
                    }
                    pdfParagraph.setMultipliedLeading( leading );
                }
            }

            CTNumPr numPr = ppr.getNumPr();
            if ( numPr != null )
            {
                // - <w:p>
                // - <w:pPr>
                // <w:pStyle w:val="style0" />
                // - <w:numPr>
                // <w:ilvl w:val="0" />
                // <w:numId w:val="2" />
                // </w:numPr>

                CTDecimalNumber ilvl = numPr.getIlvl();

                CTDecimalNumber numID = numPr.getNumId();
                XWPFNum num = document.getNumbering().getNum( numID.getVal() );
                if ( num != null )
                {
                    CTDecimalNumber abstractNumID = num.getCTNum().getAbstractNumId();
                    XWPFAbstractNum abstractNum = document.getNumbering().getAbstractNum( abstractNumID.getVal() );

                    CTLvl lvl = abstractNum.getAbstractNum().getLvlArray( ilvl.getVal().intValue() );
                    CTPPr lvlPPr = lvl.getPPr();
                    if ( lvlPPr != null )
                    {
                        Float indLeft = ParagraphIndentationLeftValueProvider.INSTANCE.getValue( lvlPPr );
                        if ( indLeft != null )
                        {
                            pdfParagraph.setIndentationLeft( indLeft );
                        }

                    }
                }
            }
        }

        // text-align
        ParagraphAlignment alignment = stylesDocument.getParagraphAlignment( docxParagraph );
        if ( alignment != null )
        {
            switch ( alignment )
            {
                case LEFT:
                    pdfParagraph.setAlignment( Element.ALIGN_LEFT );
                    break;
                case RIGHT:
                    pdfParagraph.setAlignment( Element.ALIGN_RIGHT );
                    break;
                case CENTER:
                    pdfParagraph.setAlignment( Element.ALIGN_CENTER );
                    break;
                case BOTH:
                    pdfParagraph.setAlignment( Element.ALIGN_JUSTIFIED );
                    break;
                default:
                    break;
            }
        }
        return pdfParagraph;
    }

    private boolean hasBorder( Borders borders )
    {
        if ( borders == null )
        {
            return false;
        }
        return ( borders.getValue() != Borders.NONE.getValue() && borders.getValue() != Borders.NIL.getValue() );
    }

    @Override
    protected void endVisitPargraph( XWPFParagraph paragraph, IITextContainer parentContainer,
                                     IITextContainer paragraphContainer )
        throws Exception
    {

        // Page Break
        // Cannot use paragraph.isPageBreak() because it throw NPE because
        // pageBreak.getVal() can be null.
        CTPPr ppr = paragraph.getCTP().getPPr();
        if ( ppr.isSetPageBreakBefore() )
        {
            CTOnOff pageBreak = ppr.getPageBreakBefore();
            if ( pageBreak != null
                && ( pageBreak.getVal() == null || pageBreak.getVal().intValue() == STOnOff.INT_TRUE ) )
            {
                pdfDocument.pageBreak();
            }
        }

        // Paragraph
        ExtendedParagraph pdfParagraph = (ExtendedParagraph) paragraphContainer;
        parentContainer.addElement( pdfParagraph.getElement() );
    }

    @Override
    protected void visitEmptyRun( IITextContainer paragraphContainer )
        throws Exception
    {
        // ExtendedParagraph pdfParagraph = (ExtendedParagraph) paragraphContainer;
        // pdfParagraph.addElement( Chunk.NEWLINE );
    }

    @Override
    protected void visitRun( XWPFRun run, IITextContainer pdfContainer )
        throws Exception
    {

        CTR ctr = run.getCTR();

        // <w:lastRenderedPageBreak />
        List<CTEmpty> lastRenderedPageBreakList = ctr.getLastRenderedPageBreakList();
        if ( lastRenderedPageBreakList != null && lastRenderedPageBreakList.size() > 0 )
        {
            for ( CTEmpty lastRenderedPageBreak : lastRenderedPageBreakList )
            {
                pdfDocument.pageBreak();
            }
        }

        // Font family
        String fontFamily = stylesDocument.getFontFamily( run );

        // Get font size
        Integer fontSize = stylesDocument.getFontSize( run );
        if ( fontSize == null )
        {
            fontSize = -1;
        }
        // Get font style

        int fontStyle = Font.NORMAL;
        Boolean bold = stylesDocument.getFontStyleBold( run );
        if ( bold != null && bold )
        {
            fontStyle |= Font.BOLD;
        }
        Boolean italic = stylesDocument.getFontStyleItalic( run );
        if ( italic != null && italic )
        {
            fontStyle |= Font.ITALIC;
        }

        // Process color
        Color fontColor = stylesDocument.getFontColor( run );
        // Get font
        Font font =
            XWPFFontRegistry.getRegistry().getFont( fontFamily, options.getFontEncoding(), fontSize, fontStyle,
                                                    fontColor );

        UnderlinePatterns underlinePatterns = run.getUnderline();

        boolean singleUnderlined = false;
        switch ( underlinePatterns )
        {
            case SINGLE:
                singleUnderlined = true;
                break;

            default:
                break;
        }

        // Loop for each element of <w:run text, tab, image etc
        // to keep the oder of thoses elements.
        XmlCursor c = ctr.newCursor();
        c.selectPath( "./*" );
        while ( c.toNextSelection() )
        {
            XmlObject o = c.getObject();

            if ( o instanceof CTText )
            {
                CTText ctText = (CTText) o;
                String tagName = o.getDomNode().getNodeName();
                // Field Codes (w:instrText, defined in spec sec. 17.16.23)
                // come up as instances of CTText, but we don't want them
                // in the normal text output
                if ( !"w:instrText".equals( tagName ) )
                {
                    Chunk textChunk = new Chunk( ctText.getStringValue(), font );
                    // underlined
                    if ( singleUnderlined )
                    {
                        textChunk.setUnderline( 1, -2 );
                    }
                    // background color
                    Color backgroundColor = XWPFParagraphUtils.getBackgroundColor( run );
                    if ( backgroundColor != null )
                    {
                        textChunk.setBackground( backgroundColor );
                    }
                    pdfContainer.addElement( textChunk );

                }
            }
            else if ( o instanceof CTPTab )
            {
                visitTab( pdfContainer, (CTPTab) o );
            }
            else if ( o instanceof CTBr )
            {
                visitBR( pdfContainer, (CTBr) o );
            }
            else if ( o instanceof CTEmpty )
            {
                // Some inline text elements get returned not as
                // themselves, but as CTEmpty, owing to some odd
                // definitions around line 5642 of the XSDs
                // This bit works around it, and replicates the above
                // rules for that case
                String tagName = o.getDomNode().getNodeName();
                if ( "w:tab".equals( tagName ) )
                {
                    visitTab( pdfContainer, null );
                }
                if ( "w:br".equals( tagName ) )
                {
                    visitBR( pdfContainer, null );
                }
                if ( "w:cr".equals( tagName ) )
                {
                    visitBR( pdfContainer, null );
                }
            }
            else if ( o instanceof CTDrawing )
            {
                visitDrawing( (CTDrawing) o, pdfContainer );
            }
        }
        c.dispose();
    }

    private void visitTab( IITextContainer pdfContainer, CTPTab tab )
    {
        // Chunk pdfTab = new Chunk( new DottedLineSeparator() )
        Chunk pdfTab = new Chunk( new VerticalPositionMark() );
        pdfContainer.addElement( pdfTab );
    }

    private void visitBR( IITextContainer pdfContainer, CTBr br )
    {
        pdfContainer.addElement( Chunk.NEWLINE );
    }

    // Visit table
    protected IITextContainer startVisitTable( XWPFTable table, IITextContainer pdfContainer )
        throws Exception
    {
        styleEngine.startVisitTable( table, pdfContainer );

        StylableTable pdfPTable = createPDFTable( table );
        // 3) Create PDF Table.
        pdfPTable.setITextContainer( pdfContainer );

        pdfPTable.setLockedWidth( true );

        // finally apply the style to the iText paragraph....
        applyStyles( table, pdfPTable );
        return pdfPTable;
    }

    private StylableTable createPDFTable( XWPFTable table )
        throws DocumentException
    {
        // 1) Compute colWidth
        float[] colWidths = XWPFTableUtil.computeColWidths( table );

        // 2) Compute tableWith
        TableWidth tableWidth = XWPFTableUtil.getTableWidth( table );
        StylableTable pdfPTable = pdfDocument.createTable( (IStylableContainer) null, colWidths.length );
        pdfPTable.setTotalWidth( colWidths );
        if ( tableWidth.width > 0 )
        {
            if ( tableWidth.percentUnit )
            {
                pdfPTable.setWidthPercentage( tableWidth.width );
            }
            else
            {
                pdfPTable.setTotalWidth( tableWidth.width );
            }
        }
        return pdfPTable;
    }

    // private StyleBorder createBorder( CTBorder docxBorder, BorderType borderType )
    // {
    // if ( docxBorder == null )
    // {
    // return null;
    // }
    // StyleBorder styleBorder = new StyleBorder( docxBorder.getVal().toString(), borderType );
    // // XXX semi point ?
    // styleBorder.setWidth( docxBorder.getSz() );
    // STHexColor hexColor = docxBorder.xgetColor();
    // Color bc = XWPFUtils.getColor( hexColor.getStringValue() );
    // styleBorder.setColor( bc );
    // return styleBorder;
    // }

    @Override
    protected void endVisitTable( XWPFTable table, IITextContainer parentContainer, IITextContainer tableContainer )
        throws Exception
    {
        parentContainer.addElement( (Element) tableContainer );
    }

    @Override
    protected void visitTableRow( XWPFTableRow row, IITextContainer tableContainer, boolean firstRow, boolean lastRow )
        throws Exception
    {

        StylableTable pdfTable = (StylableTable) tableContainer;
        int nbColumns = pdfTable.getNumberOfColumns();
        // Process cell
        boolean firstCell = false;
        boolean lastCell = false;
        List<XWPFTableCell> cells = row.getTableCells();
        if ( nbColumns > cells.size() )
        {
            // Columns number is not equal to cells number.
            // POI have a bug with
            // <w:tr w:rsidR="00C55C20">
            // <w:tc>
            // <w:tc>...
            // <w:sdt>
            // <w:sdtContent>
            // <w:tc> <= this tc which is a XWPFTableCell is not included in the row.getTableCells();

            firstCell = true;
            CTRow ctRow = row.getCtRow();
            XmlCursor c = ctRow.newCursor();
            c.selectPath( "./*" );
            while ( c.toNextSelection() )
            {
                XmlObject o = c.getObject();
                if ( o instanceof CTTc )
                {
                    CTTc tc = (CTTc) o;
                    XWPFTableCell cell = row.getTableCell( tc );
                    visitCell( cell, tableContainer, firstRow, lastRow, firstCell, lastCell );
                    firstCell = false;
                }
                else if ( o instanceof CTSdtCell )
                {
                    // Fix bug of POI
                    CTSdtCell sdtCell = (CTSdtCell) o;
                    List<CTTc> tcList = sdtCell.getSdtContent().getTcList();
                    for ( CTTc ctTc : tcList )
                    {
                        XWPFTableCell cell = new XWPFTableCell( ctTc, row, row.getTable().getBody() );
                        visitCell( cell, tableContainer, firstRow, lastRow, firstCell, lastCell );
                        firstCell = false;
                    }
                }
            }
            c.dispose();
        }
        else
        {
            // Column number is equal to cells number.
            for ( int i = 0; i < cells.size(); i++ )
            {
                firstCell = ( i == 0 );
                lastCell = ( i == cells.size() - 1 );
                XWPFTableCell cell = cells.get( i );
                visitCell( cell, tableContainer, firstRow, lastRow, firstCell, lastCell );
            }
        }
    }

    @Override
    protected IITextContainer startVisitTableCell( XWPFTableCell cell, IITextContainer tableContainer,
                                                   boolean firstRow, boolean lastRow, boolean firstCell,
                                                   boolean lastCell )
    {
        StylableTable pdfPTable = (StylableTable) tableContainer;
        XWPFTableRow row = cell.getTableRow();
        ExtendedPdfPCell pdfPCell = pdfDocument.createTableCell( pdfPTable );
        pdfPCell.setITextContainer( pdfPTable );
        // pdfPCell.setUseAscender( true );
        // pdfPCell.setUseDescender( true );

        CTTcPr tcPr = cell.getCTTc().getTcPr();
        if ( tcPr != null )
        {

            // Colspan
            Integer colspan = null;
            CTDecimalNumber gridSpan = tcPr.getGridSpan();
            if ( gridSpan != null )
            {
                colspan = gridSpan.getVal().intValue();
            }
            if ( colspan != null )
            {
                pdfPCell.setColspan( colspan );
            }

            // Backround Color
            CTShd shd = tcPr.getShd();
            Color backgroundColor = XWPFUtils.getFillColor( shd );
            if ( backgroundColor != null )
            {
                pdfPCell.setBackgroundColor( backgroundColor );
            }

            // Borders
            // Table Properties on cells

            // overridden locally
            CTTblBorders tableStyleBorders = null;
            XWPFStyle tableStyle = super.getXWPFStyle( cell.getTableRow().getTable().getStyleID() );
            if ( tableStyle != null )
            {
                tableStyleBorders = tableStyle.getCTStyle().getTblPr().getTblBorders();
            }

            CTTblBorders tableBorders = XWPFTableUtil.getTblBorders( cell.getTableRow().getTable() );
            CTTcBorders cellBorders = tcPr.getTcBorders();
            // border-left
            XWPFTableUtil.setBorder( cellBorders, tableBorders, tableStyleBorders, firstRow, lastRow, firstCell,
                                     lastCell, pdfPCell, Rectangle.LEFT );
            // border-right
            XWPFTableUtil.setBorder( cellBorders, tableBorders, tableStyleBorders, firstRow, lastRow, firstCell,
                                     lastCell, pdfPCell, Rectangle.RIGHT );
            // border-top
            XWPFTableUtil.setBorder( cellBorders, tableBorders, tableStyleBorders, firstRow, lastRow, firstCell,
                                     lastCell, pdfPCell, Rectangle.TOP );
            // border-bottom
            XWPFTableUtil.setBorder( cellBorders, tableBorders, tableStyleBorders, firstRow, lastRow, firstCell,
                                     lastCell, pdfPCell, Rectangle.BOTTOM );

            // Text direction <w:textDirection
            CTTextDirection direction = tcPr.getTextDirection();
            if ( direction != null )
            {
                // if (direction.getVal().equals(STTextDirection.FROM_T )) {
                if ( "btLr".equals( direction.getVal().toString() ) )
                {
                    pdfPCell.setRotation( 90 );
                }
                else if ( "tbRl".equals( direction.getVal().toString() ) )
                {
                    pdfPCell.setRotation( 270 );
                }
            }

            // // Vertical aligment
            CTVerticalJc vAlign = tcPr.getVAlign();
            if ( vAlign != null )
            {
                Enum jc = vAlign.getVal();
                if ( jc != null )
                {
                    switch ( jc.intValue() )
                    {
                        case STVerticalJc.INT_BOTTOM:
                            pdfPCell.setVerticalAlignment( Element.ALIGN_BOTTOM );
                            break;
                        case STVerticalJc.INT_CENTER:
                            pdfPCell.setVerticalAlignment( Element.ALIGN_MIDDLE );
                            break;
                        case STVerticalJc.INT_TOP:
                            pdfPCell.setVerticalAlignment( Element.ALIGN_TOP );
                            break;
                    }
                }

            }
        }
        int height = row.getHeight();
        if ( height > 0 )
        {
            pdfPCell.setMinimumHeight( dxa2points( height ) );
        }
        // pdfPCell.setRunDirection( PdfWriter.RUN_DIRECTION_NO_BIDI );
        // pdfPCell.setRotation( 90 );
        // pdfPCell.setVerticalAlignment( pdfPCell.ALIGN_TOP );

        return pdfPCell;
    }

    @Override
    protected void endVisitTableCell( XWPFTableCell cell, IITextContainer tableContainer,
                                      IITextContainer tableCellContainer )
    {
        ExtendedPdfPTable pdfPTable = (ExtendedPdfPTable) tableContainer;
        ExtendedPdfPCell pdfPCell = (ExtendedPdfPCell) tableCellContainer;
        pdfPTable.addCell( pdfPCell );
    }

    @Override
    protected void visitPicture( CTPicture picture, IITextContainer parentContainer )
    {
        CTPositiveSize2D ext = picture.getSpPr().getXfrm().getExt();
        long x = ext.getCx();
        long y = ext.getCy();

        String blipId = picture.getBlipFill().getBlip().getEmbed();

        XWPFPictureData pictureData = super.getPictureDataByID( blipId );
        if ( pictureData != null )
        {
            try
            {
                Image img = Image.getInstance( pictureData.getData() );
                img.scaleAbsolute( dxa2points( x ) / 635, dxa2points( y ) / 635 );

                IITextContainer parentOfParentContainer = parentContainer.getITextContainer();
                if ( parentOfParentContainer != null && parentOfParentContainer instanceof PdfPCell )
                {
                    parentOfParentContainer.addElement( img );
                }
                else
                {
                    parentContainer.addElement( new Chunk( img, 0, 0, false ) );
                }

            }
            catch ( Exception e )
            {
                LOGGER.severe( e.getMessage() );
            }

        }
    }

    private void applyStyles( XWPFParagraph ele, IStylableElement<XWPFParagraph> element )
    {

        Style style = styleEngine.getStyle( ele.getStyleID() );

        element.applyStyles( ele, style );

    }

    private void applyStyles( XWPFTable ele, IStylableElement<XWPFTable> element )
    {

        CTString tblStyle = ele.getCTTbl().getTblPr().getTblStyle();
        Style style;
        if ( tblStyle != null )
        {
            style = styleEngine.getStyle( tblStyle.getVal() );
        }
        else
        {
            style = styleEngine.getDefaultStyle();
        }
        element.applyStyles( ele, style );

    }

    protected XWPFStyle getXWPFStyle( XWPFParagraph paragraph )
    {
        if ( paragraph == null )
        {
            return null;
        }
        return getXWPFStyle( paragraph.getStyleID() );
    }

    protected void setActiveMasterPage( StylableMasterPage masterPage )
    {
        pdfDocument.setActiveMasterPage( masterPage );
    }

    @Override
    protected StylableMasterPage createMasterPage( CTSectPr sectPr )
    {
        return new StylableMasterPage( sectPr );
    }
}

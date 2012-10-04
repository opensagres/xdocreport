package org.apache.poi.xwpf.converter.pdf.internal;

import static org.apache.poi.xwpf.converter.core.utils.DxaUtil.dxa2points;

import java.awt.Color;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.xwpf.converter.core.IXWPFMasterPage;
import org.apache.poi.xwpf.converter.core.XWPFDocumentVisitor;
import org.apache.poi.xwpf.converter.core.styles.pargraph.ParagraphIndentationLeftValueProvider;
import org.apache.poi.xwpf.converter.core.utils.ColorHelper;
import org.apache.poi.xwpf.converter.core.utils.TableWidth;
import org.apache.poi.xwpf.converter.core.utils.XWPFTableUtil;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.converter.pdf.internal.elements.StylableDocument;
import org.apache.poi.xwpf.converter.pdf.internal.elements.StylableHeaderFooter;
import org.apache.poi.xwpf.converter.pdf.internal.elements.StylableMasterPage;
import org.apache.poi.xwpf.converter.pdf.internal.elements.StylableParagraph;
import org.apache.poi.xwpf.converter.pdf.internal.elements.StylableTable;
import org.apache.poi.xwpf.converter.pdf.internal.elements.StylableTableCell;
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
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
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

public class PdfMapper
    extends XWPFDocumentVisitor<IITextContainer, PdfOptions, StylableMasterPage>
{

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( PdfMapper.class.getName() );

    private final OutputStream out;

    // Instance of PDF document
    private StylableDocument pdfDocument;

    private Font currentRunFont;

    private UnderlinePatterns currentRunUnderlinePatterns;

    private Color currentRunBackgroundColor;

    public PdfMapper( XWPFDocument document, OutputStream out, PdfOptions options )
        throws Exception
    {
        super( document, options != null ? options : PdfOptions.create() );
        this.out = out;
    }

    // ------------------------- Document

    @Override
    protected IITextContainer startVisitDocument()
        throws Exception
    {
        // Create instance of PDF document
        this.pdfDocument = new StylableDocument( out );
        this.pdfDocument.setMasterPageManager( getMasterPageManager() );
        return pdfDocument;

    }

    @Override
    protected void endVisitDocument()
        throws Exception
    {
        pdfDocument.close();
        out.close();
    }

    // ------------------------- Header/Footer

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

    @Override
    protected void setActiveMasterPage( StylableMasterPage masterPage )
    {
        pdfDocument.setActiveMasterPage( masterPage );

    }

    @Override
    protected IXWPFMasterPage createMasterPage( CTSectPr sectPr )
    {
        return new StylableMasterPage( sectPr );
    }

    // ------------------------- Paragraph

    @Override
    protected IITextContainer startVisitParagraph( XWPFParagraph docxParagraph, IITextContainer pdfParentContainer )
        throws Exception
    {
        // instanciate a pdfParagraph
        StylableParagraph pdfParagraph = pdfDocument.createParagraph( pdfParentContainer );

        // indentation left
        Float indentationLeft = stylesDocument.getIndentationLeft( docxParagraph );
        if ( indentationLeft != null )
        {
            pdfParagraph.setIndentationLeft( indentationLeft );
        }
        // indentation right
        Float indentationRight = stylesDocument.getIndentationRight( docxParagraph );
        if ( indentationRight != null )
        {
            pdfParagraph.setIndentationRight( indentationRight );
        }
        // indentation first line
        Float indentationFirstLine = stylesDocument.getIndentationFirstLine( docxParagraph );
        if ( indentationFirstLine != null )
        {
            pdfParagraph.setFirstLineIndent( indentationFirstLine );
        }

        // spacing before
        Float spacingBefore = stylesDocument.getSpacingBefore( docxParagraph );
        if ( spacingBefore != null )
        {
            pdfParagraph.setSpacingBefore( spacingBefore );
        }

        // spacing after
        Float spacingAfter = stylesDocument.getSpacingAfter( docxParagraph );
        if ( spacingAfter != null )
        {
            pdfParagraph.setSpacingAfter( spacingAfter );
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
        return pdfParagraph;
    }

    @Override
    protected void endVisitParagraph( XWPFParagraph docxParagraph, IITextContainer pdfParentContainer,
                                      IITextContainer pdfParagraphContainer )
        throws Exception
    {
        // add the iText paragraph in the current parent container.
        ExtendedParagraph pdfParagraph = (ExtendedParagraph) pdfParagraphContainer;
        pdfParentContainer.addElement( pdfParagraph.getContainer() );
    }

    @Override
    protected void visitEmptyRun( IITextContainer pdfParagraphContainer )
        throws Exception
    {

    }

    @Override
    protected void visitRun( XWPFRun docxRun, IITextContainer pdfParagraphContainer )
        throws Exception
    {
        // Font family
        String fontFamily = stylesDocument.getFontFamily( docxRun );

        // Get font size
        Integer fontSize = stylesDocument.getFontSize( docxRun );
        if ( fontSize == null )
        {
            fontSize = -1;
        }
        // Get font style

        int fontStyle = Font.NORMAL;
        Boolean bold = stylesDocument.getFontStyleBold( docxRun );
        if ( bold != null && bold )
        {
            fontStyle |= Font.BOLD;
        }
        Boolean italic = stylesDocument.getFontStyleItalic( docxRun );
        if ( italic != null && italic )
        {
            fontStyle |= Font.ITALIC;
        }

        // Process color
        Color fontColor = stylesDocument.getFontColor( docxRun );

        // Font
        this.currentRunFont =
            ITextFontRegistry.getRegistry().getFont( fontFamily, options.getFontEncoding(), fontSize, fontStyle,
                                                     fontColor );
        // Underline patterns
        this.currentRunUnderlinePatterns = docxRun.getUnderline();
        this.currentRunBackgroundColor = stylesDocument.getBackgroundColor( docxRun ); // XWPFParagraphUtils.getBackgroundColor(
        // run );

        super.visitRun( docxRun, pdfParagraphContainer );

        this.currentRunFont = null;
        this.currentRunUnderlinePatterns = null;
        this.currentRunBackgroundColor = null;
    }

    @Override
    protected void visitText( CTText docxText, IITextContainer pdfParagraphContainer )
        throws Exception
    {
        Chunk textChunk = new Chunk( docxText.getStringValue(), currentRunFont );

        if ( currentRunUnderlinePatterns != null )
        {
            // underlined
            boolean singleUnderlined = false;
            switch ( currentRunUnderlinePatterns )
            {
                case SINGLE:
                    singleUnderlined = true;
                    break;

                default:
                    break;
            }
            if ( singleUnderlined )
            {
                textChunk.setUnderline( 1, -2 );
            }
        }
        // background color
        if ( currentRunBackgroundColor != null )
        {
            textChunk.setBackground( currentRunBackgroundColor );
        }
        pdfParagraphContainer.addElement( textChunk );
    }

    @Override
    protected void visitTab( CTPTab tab, IITextContainer pdfParagraphContainer )
        throws Exception
    {
        Chunk pdfTab = new Chunk( new VerticalPositionMark() );
        pdfParagraphContainer.addElement( pdfTab );
    }

    @Override
    protected void visitBR( CTBr br, IITextContainer pdfParagraphContainer )
        throws Exception
    {
        pdfParagraphContainer.addElement( Chunk.NEWLINE );

    }

    @Override
    protected void pageBreak()
        throws Exception
    {
        pdfDocument.pageBreak();
    }

    // ----------------- Table

    @Override
    protected IITextContainer startVisitTable( XWPFTable table, float[] colWidths, IITextContainer pdfParentContainer )
        throws Exception
    {
        StylableTable pdfPTable = createPDFTable( table, colWidths, pdfParentContainer );
        pdfPTable.setLockedWidth( true );
        return pdfPTable;
    }

    private StylableTable createPDFTable( XWPFTable table, float[] colWidths, IITextContainer pdfParentContainer )
        throws DocumentException
    {
        // 2) Compute tableWith
        TableWidth tableWidth = XWPFTableUtil.getTableWidth( table );
        StylableTable pdfPTable = pdfDocument.createTable( pdfParentContainer, colWidths.length );
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

    @Override
    protected void endVisitTable( XWPFTable table, IITextContainer pdfParentContainer, IITextContainer pdfTableContainer )
        throws Exception
    {
        pdfParentContainer.addElement( (Element) pdfTableContainer );

    }

    @Override
    protected IITextContainer startVisitTableCell( XWPFTableCell cell, IITextContainer pdfTableContainer,
                                                   boolean firstRow, boolean lastRow, boolean firstCell,
                                                   boolean lastCell )
        throws Exception
    {
        StylableTable pdfPTable = (StylableTable) pdfTableContainer;
        XWPFTableRow row = cell.getTableRow();
        StylableTableCell pdfPCell = pdfDocument.createTableCell( pdfPTable );
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
            Color backgroundColor = ColorHelper.getFillColor( shd );
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
            pdfPCell.setBorder( cellBorders, tableBorders, tableStyleBorders, firstRow, lastRow, firstCell, lastCell,
                                Rectangle.LEFT );
            // border-right
            pdfPCell.setBorder( cellBorders, tableBorders, tableStyleBorders, firstRow, lastRow, firstCell, lastCell,
                                Rectangle.RIGHT );
            // border-top
            pdfPCell.setBorder( cellBorders, tableBorders, tableStyleBorders, firstRow, lastRow, firstCell, lastCell,
                                Rectangle.TOP );
            // border-bottom
            pdfPCell.setBorder( cellBorders, tableBorders, tableStyleBorders, firstRow, lastRow, firstCell, lastCell,
                                Rectangle.BOTTOM );

            // Text direction <w:textDirection
            CTTextDirection direction = tcPr.getTextDirection();
            if ( direction != null )
            {
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
    protected void visitPicture( CTPicture picture, IITextContainer pdfParentContainer )
        throws Exception
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

                IITextContainer parentOfParentContainer = pdfParentContainer.getITextContainer();
                if ( parentOfParentContainer != null && parentOfParentContainer instanceof PdfPCell )
                {
                    parentOfParentContainer.addElement( img );
                }
                else
                {
                    pdfParentContainer.addElement( new Chunk( img, 0, 0, false ) );
                }

            }
            catch ( Exception e )
            {
                LOGGER.severe( e.getMessage() );
            }

        }
    }

}

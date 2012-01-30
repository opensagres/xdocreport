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
package org.apache.poi.xwpf.converter.internal.itext;

import static org.apache.poi.xwpf.converter.internal.DxaUtil.dxa2points;
import static org.apache.poi.xwpf.converter.internal.XWPFRunUtils.getFontColor;
import static org.apache.poi.xwpf.converter.internal.XWPFRunUtils.getFontFamily;
import static org.apache.poi.xwpf.converter.internal.XWPFRunUtils.getRStyle;
import static org.apache.poi.xwpf.converter.internal.XWPFRunUtils.isBold;
import static org.apache.poi.xwpf.converter.internal.XWPFRunUtils.isItalic;
import static org.apache.poi.xwpf.converter.internal.XWPFUtils.getRPr;
import static org.apache.poi.xwpf.converter.internal.itext.XWPFTableUtil.setBorder;

import java.awt.Color;
import java.io.OutputStream;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

import org.apache.poi.xwpf.converter.internal.XWPFElementVisitor;
import org.apache.poi.xwpf.converter.internal.itext.stylable.IStylableContainer;
import org.apache.poi.xwpf.converter.internal.itext.stylable.IStylableElement;
import org.apache.poi.xwpf.converter.internal.itext.stylable.StylableDocument;
import org.apache.poi.xwpf.converter.internal.itext.stylable.StylableParagraph;
import org.apache.poi.xwpf.converter.internal.itext.stylable.StylableTable;
import org.apache.poi.xwpf.converter.internal.itext.styles.Style;
import org.apache.poi.xwpf.converter.internal.itext.styles.StyleBorder;
import org.apache.poi.xwpf.converter.itext.PDFViaITextOptions;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;

import fr.opensagres.xdocreport.itext.extension.ExtendedParagraph;
import fr.opensagres.xdocreport.itext.extension.ExtendedPdfPCell;
import fr.opensagres.xdocreport.itext.extension.ExtendedPdfPTable;
import fr.opensagres.xdocreport.itext.extension.IITextContainer;
import fr.opensagres.xdocreport.itext.extension.MasterPage;
import fr.opensagres.xdocreport.itext.extension.MasterPageHeaderFooter;
import fr.opensagres.xdocreport.itext.extension.PageOrientation;
import fr.opensagres.xdocreport.utils.BorderType;
import fr.opensagres.xdocreport.utils.StringUtils;

public class PDFMapper
    extends XWPFElementVisitor<IITextContainer>
{

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( XWPFElementVisitor.class.getName() );

    // Create instance of PDF document
    private StylableDocument pdfDocument;

    private Stack<CTSectPr> sectPrStack = null;

    private StyleEngineForIText styleEngine;

    private final PDFViaITextOptions options;

    public PDFMapper( XWPFDocument document, PDFViaITextOptions options )
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
        pdfDocument = new StylableDocument( out, styleEngine );
        CTSectPr sectPr = document.getDocument().getBody().getSectPr();
        applySectPr( sectPr );

        return pdfDocument;
    }

    private void applySectPr( CTSectPr sectPr )
    {
        if ( sectPr == null )
        {
            return;
        }
        // Set page size
        CTPageSz pageSize = sectPr.getPgSz();
        Rectangle pdfPageSize = new Rectangle( dxa2points( pageSize.getW() ), dxa2points( pageSize.getH() ) );
        pdfDocument.setPageSize( pdfPageSize );

        // Orientation
        org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation.Enum orientation =
            pageSize.getOrient();
        if ( orientation != null )
        {
            if ( org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation.LANDSCAPE.equals( orientation ) )
            {
                pdfDocument.setOrientation( PageOrientation.Landscape );
            }
            else
            {
                pdfDocument.setOrientation( PageOrientation.Portrait );
            }
        }

        // Set page margin
        CTPageMar pageMar = sectPr.getPgMar();
        if ( pageMar != null )
        {
            pdfDocument.setOriginalMargins( dxa2points( pageMar.getLeft() ), dxa2points( pageMar.getRight() ),
                                            dxa2points( pageMar.getTop() ), dxa2points( pageMar.getBottom() ) );
        }
    }

    @Override
    protected void endVisitDocument()
        throws Exception
    {
        pdfDocument.close();
    }

    @Override
    protected void visitHeader( CTHdrFtrRef headerRef )
        throws Exception
    {
        STHdrFtr.Enum type = headerRef.getType();
        MasterPage masterPage = getOrCreateMasterPage( type );

        MasterPageHeaderFooter pdfHeader = new MasterPageHeaderFooter();
        XWPFHeader hdr = getXWPFHeader( headerRef );
        visitBodyElements( hdr.getBodyElements(), (ExtendedPdfPCell) pdfHeader.getTableCell() );
        pdfHeader.flush();
        masterPage.setHeader( pdfHeader );

    }

    @Override
    protected void visitFooter( CTHdrFtrRef footerRef )
        throws Exception
    {
        STHdrFtr.Enum type = footerRef.getType();
        MasterPage masterPage = getOrCreateMasterPage( type );

        MasterPageHeaderFooter pdfFooter = new MasterPageHeaderFooter();
        XWPFFooter hdr = getXWPFFooter( footerRef );
        visitBodyElements( hdr.getBodyElements(), (ExtendedPdfPCell) pdfFooter.getTableCell() );
        pdfFooter.flush();
        masterPage.setFooter( pdfFooter );

    }

    private MasterPage getOrCreateMasterPage( STHdrFtr.Enum type )
    {
        String masterPageName = type.toString();
        MasterPage masterPage = pdfDocument.getMasterPage( masterPageName );
        if ( masterPage == null )
        {
            masterPage = new MasterPage( masterPageName );
            pdfDocument.addMasterPage( masterPage );
        }
        return masterPage;
    }

    private Stack<CTSectPr> getSectPrStack()
    {
        if ( sectPrStack != null )
        {
            return sectPrStack;
        }
        sectPrStack = new Stack<CTSectPr>();
        for ( IBodyElement bodyElement : document.getBodyElements() )
        {
            if ( bodyElement.getElementType() == BodyElementType.PARAGRAPH )
            {
                CTPPr ppr = ( (XWPFParagraph) bodyElement ).getCTP().getPPr();
                if ( ppr != null )
                {
                    CTSectPr sectPr = ppr.getSectPr();
                    if ( sectPr != null )
                    {
                        sectPrStack.push( sectPr );
                    }
                }
            }
        }
        return sectPrStack;
    }

    @Override
    protected IITextContainer startVisitPargraph( XWPFParagraph docxParagraph, IITextContainer parentContainer )
        throws Exception
    {

        // 1) Instanciate a pdfParagraph
        StylableParagraph pdfParagraph = pdfDocument.createParagraph( (IStylableContainer) null );
        // apply style for the title font, color, bold style...
        // 2) Create style instance of the paragraph if needed
        styleEngine.startVisitPargraph( docxParagraph, pdfParagraph );
        pdfParagraph.setITextContainer( parentContainer );

        // TODO

        String backgroundColor = XWPFParagraphUtils.getBackgroundColor( docxParagraph );
        if ( StringUtils.isNotEmpty( backgroundColor ) )
        {
            pdfParagraph.getPdfPCell().setBackgroundColor( ColorRegistry.getInstance().getColor( "0x" + backgroundColor ) );
        }
        // finally apply the style to the iText paragraph....
        applyStyles( docxParagraph, pdfParagraph );
        return pdfParagraph;
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
                pdfDocument.newPage();
            }
        }

        // Paragraph
        ExtendedParagraph pdfParagraph = (ExtendedParagraph) paragraphContainer;
        parentContainer.addElement( pdfParagraph.getContainer() );
    }

    @Override
    protected void visitEmptyRun( IITextContainer paragraphContainer )
        throws Exception
    {
        ExtendedParagraph pdfParagraph = (ExtendedParagraph) paragraphContainer;
        pdfParagraph.add( Chunk.NEWLINE );
    }

    @Override
    protected void visitRun( XWPFRun run, IITextContainer pdfContainer )
        throws Exception
    {
        CTR ctr = run.getCTR();
        // Get family name
        // Get CTRPr from style+defaults
        CTString rStyle = getRStyle( run );
        CTRPr runRprStyle = getRPr( super.getXWPFStyle( rStyle != null ? rStyle.getVal() : null ) );
        CTRPr rprStyle = getRPr( super.getXWPFStyle( run.getParagraph().getStyleID() ) );
        CTRPr rprDefault = getRPr( defaults );

        // Font family
        String fontFamily = getFontFamily( run, rprStyle, rprDefault );

        // Get font size
        float fontSize = run.getFontSize();

        // Get font style
        int fontStyle = Font.NORMAL;
        if ( isBold( run, runRprStyle, rprStyle, rprDefault ) )
        {
            fontStyle |= Font.BOLD;
        }
        if ( isItalic( run, runRprStyle, rprStyle, rprDefault ) )
        {
            fontStyle |= Font.ITALIC;
        }

        // Process color
        Color fontColor = null;
        String hexColor = getFontColor( run, runRprStyle, rprStyle, rprDefault );
        if ( StringUtils.isNotEmpty( hexColor ) )
        {
            if ( hexColor != null && !"auto".equals( hexColor ) )
            {
                fontColor = ColorRegistry.getInstance().getColor( "0x" + hexColor );
            }
        }
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

        List<CTBr> brs = ctr.getBrList();
        for ( @SuppressWarnings( "unused" )
        CTBr br : brs )
        {
            pdfContainer.addElement( Chunk.NEWLINE );
        }

        List<CTText> texts = run.getCTR().getTList();
        for ( CTText ctText : texts )
        {

            Chunk aChunk = new Chunk( ctText.getStringValue(), font );
            if ( singleUnderlined )
                aChunk.setUnderline( 1, -2 );

            pdfContainer.addElement( aChunk );
        }

        super.visitPictures( run, pdfContainer );

        // <w:lastRenderedPageBreak />
        List<CTEmpty> lastRenderedPageBreakList = ctr.getLastRenderedPageBreakList();
        if ( lastRenderedPageBreakList != null && lastRenderedPageBreakList.size() > 0 )
        {
            // IText Document#newPage must be called to generate page break.
            // But before that, CTSectPr must be getted to compute pageSize,
            // margins...
            // The CTSectPr <w:pPr><w:sectPr w:rsidR="00AA33F7"
            // w:rsidSect="00607077"><w:pgSz w:w="16838" w:h="11906"
            // w:orient="landscape" />...
            Stack<CTSectPr> sectPrStack = getSectPrStack();
            if ( sectPrStack != null && !sectPrStack.isEmpty() )
            {
                CTSectPr sectPr = sectPrStack.pop();
                applySectPr( sectPr );
            }
            for ( CTEmpty lastRenderedPageBreak : lastRenderedPageBreakList )
            {
                pdfDocument.newPage();
            }
        }
    }

    // Visit table
    protected IITextContainer startVisitTable( XWPFTable table, IITextContainer pdfContainer )
        throws Exception
    {
        styleEngine.startVisitTable( table, pdfContainer );

        // 1) Compute colWidth
        float[] colWidths = XWPFTableUtil.computeColWidths( table );

        // 2) Compute tableWith
        TableWidth tableWidth = XWPFTableUtil.getTableWidth( table );

        StylableTable pdfPTable = pdfDocument.createTable( (IStylableContainer) null, colWidths.length );
        // 3) Create PDF Table.
        // ExtendedPdfPTable pdfPTable = new
        // ExtendedPdfPTable(colWidths.length);
        pdfPTable.setITextContainer( pdfContainer );

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

        if ( table.getCTTbl() != null )
        {
            if ( table.getCTTbl().getTblPr().getTblBorders() != null )
            {
                CTBorder bottom = table.getCTTbl().getTblPr().getTblBorders().getBottom();
                if ( bottom != null )
                {
                    pdfPTable.setBorderBottom( createBorder( bottom, BorderType.BOTTOM ) );
                }
                CTBorder left = table.getCTTbl().getTblPr().getTblBorders().getLeft();
                if ( left != null )
                {
                    pdfPTable.setBorderLeft( createBorder( left, BorderType.LEFT ) );
                }
                CTBorder top = table.getCTTbl().getTblPr().getTblBorders().getTop();
                if ( top != null )
                {
                    pdfPTable.setBorderTop( createBorder( top, BorderType.TOP ) );
                }
                CTBorder right = table.getCTTbl().getTblPr().getTblBorders().getRight();
                if ( right != null )
                {
                    pdfPTable.setBorderRight( createBorder( right, BorderType.RIGHT ) );
                }
            }
        }

        pdfPTable.setLockedWidth( true );
        // finally apply the style to the iText paragraph....
        applyStyles( table, pdfPTable );
        return pdfPTable;
    }

    private StyleBorder createBorder( CTBorder docxBorder, BorderType borderType )
    {
        if ( docxBorder == null )
        {
            return null;
        }
        StyleBorder styleBorder = new StyleBorder( docxBorder.getVal().toString(), borderType );
        // XXX semi point ?
        styleBorder.setWidth( docxBorder.getSz() );
        STHexColor hexColor = docxBorder.xgetColor();
        Color bc = ColorRegistry.getInstance().getColor( "0x" + hexColor.getStringValue() );
        styleBorder.setColor( bc );
        return styleBorder;
    }

    @Override
    protected void endVisitTable( XWPFTable table, IITextContainer parentContainer, IITextContainer tableContainer )
        throws Exception
    {
        parentContainer.addElement( (Element) tableContainer );
    }

    @Override
    protected IITextContainer startVisitTableCell( XWPFTableCell cell, IITextContainer tableContainer )
    {
        StylableTable pdfPTable = (StylableTable) tableContainer;

        XWPFTableRow row = cell.getTableRow();
        ExtendedPdfPCell pdfPCell = new ExtendedPdfPCell();
        pdfPCell.setITextContainer( pdfPTable );

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
            String hexColor = null;
            if ( shd != null )
            {
                hexColor = shd.xgetFill().getStringValue();
            }
            if ( hexColor != null && !"auto".equals( hexColor ) )
            {
                pdfPCell.setBackgroundColor( ColorRegistry.getInstance().getColor( "0x" + hexColor ) );
            }

            // Borders
            // Table Properties on cells

            // overridden locally
            CTTcBorders borders = tcPr.getTcBorders();
            if ( borders != null )
            {
                // border-left
                setBorder( borders.getLeft(), pdfPCell, Rectangle.LEFT );
                // border-right
                setBorder( borders.getRight(), pdfPCell, Rectangle.RIGHT );
                // border-top
                setBorder( borders.getTop(), pdfPCell, Rectangle.TOP );
                // border-bottom
                setBorder( borders.getBottom(), pdfPCell, Rectangle.BOTTOM );
            }
        }
        int height = row.getHeight();
        pdfPCell.setMinimumHeight( dxa2points( height ) );

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
    protected void visitPicture( XWPFPicture picture, IITextContainer parentContainer )
        throws Exception
    {
        CTPositiveSize2D ext = picture.getCTPicture().getSpPr().getXfrm().getExt();
        long x = ext.getCx();
        long y = ext.getCy();

        CTPicture ctPic = picture.getCTPicture();
        String blipId = ctPic.getBlipFill().getBlip().getEmbed();

        XWPFPictureData pictureData = XWPFPictureUtil.getPictureData( document, blipId );

        if ( pictureData != null )
        {
            try
            {
                Image img = Image.getInstance( pictureData.getData() );
                img.scaleAbsolute( dxa2points( x ) / 635, dxa2points( y ) / 635 );

                IITextContainer parentOfParentContainer = parentContainer.getITextContainer();
                if ( parentOfParentContainer != null && parentOfParentContainer instanceof PdfPCell )
                {
                    ( (PdfPCell) parentOfParentContainer ).setImage( img );
                }
                else
                {
                    parentContainer.addElement( img );
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
}

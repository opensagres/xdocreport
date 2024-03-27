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
package fr.opensagres.poi.xwpf.converter.pdf.internal;

import static fr.opensagres.poi.xwpf.converter.core.utils.DxaUtil.emu2points;

import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFSDT;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromH;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapText;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrBase;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextDirection;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextDirection;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc.Enum;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.draw.DottedLineSeparator;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lowagie.text.pdf.draw.VerticalPositionMark;
import com.microsoft.schemas.vml.CTImageData;
import com.microsoft.schemas.vml.CTShape;

import fr.opensagres.poi.xwpf.converter.core.BorderSide;
import fr.opensagres.poi.xwpf.converter.core.Color;
import fr.opensagres.poi.xwpf.converter.core.ImageShapeStyle;
import fr.opensagres.poi.xwpf.converter.core.ListItemContext;
import fr.opensagres.poi.xwpf.converter.core.MultiValueTriplet;
import fr.opensagres.poi.xwpf.converter.core.ParagraphLineSpacing;
import fr.opensagres.poi.xwpf.converter.core.TableCellBorder;
import fr.opensagres.poi.xwpf.converter.core.TableHeight;
import fr.opensagres.poi.xwpf.converter.core.TableWidth;
import fr.opensagres.poi.xwpf.converter.core.XWPFDocumentVisitor;
import fr.opensagres.poi.xwpf.converter.core.styles.paragraph.ParagraphIndentationHangingValueProvider;
import fr.opensagres.poi.xwpf.converter.core.styles.paragraph.ParagraphIndentationLeftValueProvider;
import fr.opensagres.poi.xwpf.converter.core.utils.DxaUtil;
import fr.opensagres.poi.xwpf.converter.core.utils.StringUtils;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.poi.xwpf.converter.pdf.internal.elements.StylableAnchor;
import fr.opensagres.poi.xwpf.converter.pdf.internal.elements.StylableDocument;
import fr.opensagres.poi.xwpf.converter.pdf.internal.elements.StylableHeaderFooter;
import fr.opensagres.poi.xwpf.converter.pdf.internal.elements.StylableMasterPage;
import fr.opensagres.poi.xwpf.converter.pdf.internal.elements.StylableParagraph;
import fr.opensagres.poi.xwpf.converter.pdf.internal.elements.StylableTable;
import fr.opensagres.poi.xwpf.converter.pdf.internal.elements.StylableTableCell;
import fr.opensagres.xdocreport.itext.extension.ExtendedChunk;
import fr.opensagres.xdocreport.itext.extension.ExtendedImage;
import fr.opensagres.xdocreport.itext.extension.ExtendedParagraph;
import fr.opensagres.xdocreport.itext.extension.ExtendedPdfPCell;
import fr.opensagres.xdocreport.itext.extension.ExtendedPdfPTable;
import fr.opensagres.xdocreport.itext.extension.IITextContainer;
import fr.opensagres.xdocreport.itext.extension.font.FontGroup;

public class PdfMapper
    extends XWPFDocumentVisitor<IITextContainer, PdfOptions, StylableMasterPage>
{

    private static final String TAB = "\t";

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( PdfMapper.class.getName() );

    private final OutputStream out;

    // Instance of PDF document
    private StylableDocument pdfDocument;

    private Font currentRunFontAscii;

    private Font currentRunFontEastAsia;

    private Font currentRunFontHAnsi;

    private UnderlinePatterns currentRunUnderlinePatterns;

    private Color currentRunBackgroundColor;

    private Float currentRunX;

    private Float currentPageWidth;

    private StylableHeaderFooter pdfHeader;

    private StylableHeaderFooter pdfFooter;

    private Integer expectedPageCount;

    private VerticalAlign currentRunVerticalAlign = VerticalAlign.BASELINE;

    private Map<MultiValueTriplet<XWPFPictureData, Long, Long>, Image> imageCache;

    public PdfMapper( XWPFDocument document, OutputStream out, PdfOptions options, Integer expectedPageCount )
        throws Exception
    {
        super( document, options != null ? options : PdfOptions.getDefault() );
        this.out = out;
        this.expectedPageCount = expectedPageCount;
    }

    // ------------------------- Document

    @Override
    protected IITextContainer startVisitDocument()
        throws Exception
    {
        // Create instance of PDF document
        this.pdfDocument = new StylableDocument( out, options.getConfiguration() );
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

    @Override
    protected IITextContainer startVisitSDT( XWPFSDT contents, IITextContainer container )
    {
        return null;
    }

    @Override
    protected void endVisitSDT( XWPFSDT contents, IITextContainer container, IITextContainer sdtContainer )
    {

    }

    @Override
    protected void visitSDTBody( XWPFSDT contents, IITextContainer sdtContainer )
        throws Exception
    {
    }
    // ------------------------- Header/Footer

    @Override
    protected void visitHeader( XWPFHeader header, CTHdrFtrRef headerRef, CTSectPr sectPr,
                                StylableMasterPage masterPage )
        throws Exception
    {
    	STTwipsMeasure headerY = sectPr.getPgMar() != null ? sectPr.getPgMar().xgetHeader() : null;
        this.currentPageWidth = sectPr.getPgMar() != null ? DxaUtil.dxa2points( sectPr.getPgSz().xgetW() ) : null;
        this.pdfHeader = new StylableHeaderFooter( pdfDocument, headerY, true );
        // List<IBodyElement> bodyElements = header.getBodyElements();
        List<IBodyElement> bodyElements = super.getBodyElements( header );
        StylableTableCell tableCell = getHeaderFooterTableCell( pdfHeader, bodyElements );
        visitBodyElements( bodyElements, tableCell );
        masterPage.setHeader( pdfHeader );
        this.currentPageWidth = null;
        this.pdfHeader = null;
    }

    @Override
    protected void visitFooter( XWPFFooter footer, CTHdrFtrRef footerRef, CTSectPr sectPr,
                                StylableMasterPage masterPage )
        throws Exception
    {
    	STTwipsMeasure footerY = sectPr.getPgMar() != null ? sectPr.getPgMar().xgetFooter() : null;
        this.currentPageWidth = sectPr.getPgMar() != null ? DxaUtil.dxa2points( sectPr.getPgSz().xgetW() ) : null;
        this.pdfFooter = new StylableHeaderFooter( pdfDocument, footerY, false );
        List<IBodyElement> bodyElements = super.getBodyElements( footer );
        StylableTableCell tableCell = getHeaderFooterTableCell( pdfFooter, bodyElements );
        visitBodyElements( bodyElements, tableCell );
        masterPage.setFooter( pdfFooter );
        this.currentPageWidth = null;
        this.pdfFooter = null;
    }

    private StylableTableCell getHeaderFooterTableCell( StylableHeaderFooter pdfHeaderFooter,
                                                        List<IBodyElement> bodyElements )
        throws DocumentException
    {
        return pdfHeaderFooter.getTableCell();
    }

    @Override
    public void setActiveMasterPage( StylableMasterPage masterPage )
    {
        pdfDocument.setActiveMasterPage( masterPage );

    }

    @Override
    public StylableMasterPage createMasterPage( CTSectPr sectPr )
    {
        return new StylableMasterPage( sectPr );
    }

    // ------------------------- Paragraph

    @Override
    protected IITextContainer startVisitParagraph( XWPFParagraph docxParagraph, ListItemContext itemContext,
                                                   IITextContainer pdfParentContainer )
        throws Exception
    {
        this.currentRunX = null;

        // create PDF paragraph
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
        // indentation hanging (remove first line)
        Float indentationHanging = stylesDocument.getIndentationHanging( docxParagraph );
        if ( indentationHanging != null )
        {
            pdfParagraph.setFirstLineIndent( -indentationHanging );
        }

        // // spacing before
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

        ParagraphLineSpacing lineSpacing = stylesDocument.getParagraphSpacing( docxParagraph );
        if ( lineSpacing != null )
        {
            if ( lineSpacing.getLeading() != null && lineSpacing.getMultipleLeading() != null )
            {
                pdfParagraph.setLeading( lineSpacing.getLeading(), lineSpacing.getMultipleLeading() );
            }
            else
            {
                if ( lineSpacing.getLeading() != null )
                {
                    pdfParagraph.setLeading( lineSpacing.getLeading() );
                }
                if ( lineSpacing.getMultipleLeading() != null )
                {
                    pdfParagraph.setMultipliedLeading( lineSpacing.getMultipleLeading() );
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

        // background-color
        Color backgroundColor = stylesDocument.getBackgroundColor( docxParagraph );
        if ( backgroundColor != null )
        {
            pdfParagraph.setBackgroundColor( Converter.toAwtColor( backgroundColor ) );
        }

        // border
        CTBorder borderTop = stylesDocument.getBorderTop( docxParagraph );
        pdfParagraph.setBorder( borderTop, Rectangle.TOP );

        CTBorder borderBottom = stylesDocument.getBorderBottom( docxParagraph );
        pdfParagraph.setBorder( borderBottom, Rectangle.BOTTOM );

        CTBorder borderLeft = stylesDocument.getBorderLeft( docxParagraph );
        pdfParagraph.setBorder( borderLeft, Rectangle.LEFT );

        CTBorder borderRight = stylesDocument.getBorderRight( docxParagraph );
        pdfParagraph.setBorder( borderRight, Rectangle.RIGHT );

        if ( itemContext != null )
        {
            CTLvl lvl = itemContext.getLvl();
            CTPPrBase lvlPPr = lvl.getPPr();
            if ( lvlPPr != null )
            {
                if ( ParagraphIndentationLeftValueProvider.INSTANCE.getValue( docxParagraph.getCTP().getPPr() ) == null )
                {

                    // search the indentation from the level properties only if
                    // paragraph has not override it
                    // see
                    // https://code.google.com/p/xdocreport/issues/detail?id=239
                    Float indLeft = ParagraphIndentationLeftValueProvider.INSTANCE.getValue( lvlPPr );
                    if ( indLeft != null )
                    {
                        pdfParagraph.setIndentationLeft( indLeft );
                    }
                }
                if ( ParagraphIndentationHangingValueProvider.INSTANCE.getValue( docxParagraph.getCTP().getPPr() ) == null )
                {
                    // search the hanging from the level properties only if
                    // paragraph has not override it
                    // see
                    // https://code.google.com/p/xdocreport/issues/detail?id=239
                    Float hanging = stylesDocument.getIndentationHanging( lvlPPr );
                    if ( hanging != null )
                    {
                        pdfParagraph.setFirstLineIndent( -hanging );
                    }
                }
            }
            CTRPr lvlRPr = lvl.getRPr();
            if ( lvlRPr != null )
            {
                // Font family
                String listItemFontFamily = stylesDocument.getFontFamilyAscii( lvlRPr );

                // Get font size
                Float listItemFontSize = stylesDocument.getFontSize( lvlRPr );

                // Get font style
                int listItemFontStyle = Font.NORMAL;
                Boolean bold = stylesDocument.getFontStyleBold( lvlRPr );
                if ( bold != null && bold )
                {
                    listItemFontStyle |= Font.BOLD;
                }
                Boolean italic = stylesDocument.getFontStyleItalic( lvlRPr );
                if ( italic != null && italic )
                {
                    listItemFontStyle |= Font.ITALIC;
                }
                Boolean strike = stylesDocument.getFontStyleStrike( lvlRPr );
                if ( strike != null && strike )
                {
                    listItemFontStyle |= Font.STRIKETHRU;
                }

                // Font color
                Color listItemFontColor = stylesDocument.getFontColor( lvlRPr );

                pdfParagraph.setListItemFontFamily( listItemFontFamily );
                pdfParagraph.setListItemFontSize( listItemFontSize );
                pdfParagraph.setListItemFontStyle( listItemFontStyle );
                pdfParagraph.setListItemFontColor( Converter.toAwtColor( listItemFontColor ) );

            }
            pdfParagraph.setListItemText( itemContext.getText() );
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
        pdfParentContainer.addElement( pdfParagraph.getElement() );

        this.currentRunX = null;
    }

    // ------------------------- Run

    @Override
    protected void visitEmptyRun( IITextContainer pdfParagraphContainer )
        throws Exception
    {
        StylableParagraph paragraph = (StylableParagraph) pdfParagraphContainer;
        IITextContainer parent = paragraph.getParent();
        if ( parent instanceof StylableTableCell )
        {
            StylableTableCell cell = (StylableTableCell) parent;
            if ( cell.getRotation() > 0 )
            {
                // Run paragraph belongs to Cell which has rotation, ignore the
                // empty run.
                return;
            }
        }
        // Add new PDF line
        pdfParagraphContainer.addElement( Chunk.NEWLINE );
    }

    @Override
    protected void visitRun( XWPFRun docxRun, boolean pageNumber, String url, IITextContainer pdfParagraphContainer )
        throws Exception
    {
        // Font family
        String fontFamilyAscii = stylesDocument.getFontFamilyAscii( docxRun );
        String fontFamilyEastAsia = stylesDocument.getFontFamilyEastAsia( docxRun );
        String fontFamilyHAnsi = stylesDocument.getFontFamilyHAnsi( docxRun );

        // Get font size
        Float fontSize = stylesDocument.getFontSize( docxRun );
        if ( fontSize == null )
        {
            fontSize = -1f;
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
        Boolean strike = stylesDocument.getFontStyleStrike( docxRun );
        if ( strike != null && strike )
        {
            fontStyle |= Font.STRIKETHRU;
        }

        // Font color
        Color fontColor = stylesDocument.getFontColor( docxRun );

        // superscript or subscript
        this.currentRunVerticalAlign = stylesDocument.getVerticalAlign( docxRun );

        // to make the text more pleasing to the eye, use a smaller font in case of superscript or subscript
        switch ( currentRunVerticalAlign )
        {
            case SUBSCRIPT:
            case SUPERSCRIPT:
                fontSize = fontSize * stylesDocument.getVerticalAlignFontFactor();
                break;
        }

        // Font
        this.currentRunFontAscii = getFont( fontFamilyAscii, fontSize, fontStyle, fontColor );
        this.currentRunFontEastAsia = getFont( fontFamilyEastAsia, fontSize, fontStyle, fontColor );
        this.currentRunFontHAnsi = getFont( fontFamilyHAnsi, fontSize, fontStyle, fontColor );

        // Underline patterns
        this.currentRunUnderlinePatterns = stylesDocument.getUnderline( docxRun );

        // background color
        this.currentRunBackgroundColor = stylesDocument.getBackgroundColor( docxRun );

        // highlight
        if ( currentRunBackgroundColor == null )
        {
            this.currentRunBackgroundColor = stylesDocument.getTextHighlighting( docxRun );
        }

        StylableParagraph pdfParagraph = (StylableParagraph) pdfParagraphContainer;
        pdfParagraph.adjustMultipliedLeading( currentRunFontAscii );
        pdfParagraph.adjustLeading(currentRunFontAscii);

        // addd symbol list item chunk if needed.
        String listItemText = pdfParagraph.getListItemText();
        if ( StringUtils.isNotEmpty( listItemText ) )
        {
            // FIXME: add some space after the list item
            listItemText += "    ";

            String listItemFontFamily = pdfParagraph.getListItemFontFamily();
            Float listItemFontSize = pdfParagraph.getListItemFontSize();
            int listItemFontStyle = pdfParagraph.getListItemFontStyle();
            java.awt.Color listItemFontColor = pdfParagraph.getListItemFontColor();
            Font listItemFont =
                options.getFontProvider().getFont( listItemFontFamily != null ? listItemFontFamily : fontFamilyAscii,
                                                   options.getFontEncoding(),
                                                   listItemFontSize != null ? listItemFontSize : fontSize,
                                                   listItemFontStyle != Font.NORMAL ? listItemFontStyle : fontStyle,
                                                   listItemFontColor != null ? listItemFontColor
                                                                   : Converter.toAwtColor( fontColor ) );
            Chunk symbol = createTextChunk( listItemText, false, listItemFont, currentRunUnderlinePatterns,
                                            currentRunBackgroundColor );
            pdfParagraph.add( symbol );
            pdfParagraph.setListItemText( null );
        }

        IITextContainer container = pdfParagraphContainer;
        if ( url != null )
        {
            // URL is not null, generate a PDF hyperlink.
            StylableAnchor pdfAnchor = new StylableAnchor();
            pdfAnchor.setReference( url );
            pdfAnchor.setITextContainer( container );
            container = pdfAnchor;
        }
        super.visitRun( docxRun, pageNumber, url, container );

        if ( url != null )
        {
            // URL is not null, add the PDF hyperlink in the PDF paragraph
            pdfParagraphContainer.addElement( (StylableAnchor) container );
        }

        this.currentRunFontAscii = null;
        this.currentRunFontEastAsia = null;
        this.currentRunFontHAnsi = null;
        this.currentRunUnderlinePatterns = null;
        this.currentRunBackgroundColor = null;
        this.currentRunVerticalAlign = VerticalAlign.BASELINE;
    }

    /**
     * Strike-through is part of the {@link Font} definition. Vertical align is handled by this class. Therefore, they
     * do not need any special treatment and we exclude them by overriding this method of the superclass.
     */
    @Override
    protected boolean hasTextStyles( CTRPr rPr )
    {
        return rPr != null && ( rPr.sizeOfHighlightArray() > 0 || rPr.sizeOfDstrikeArray() > 0 );
    }

    private Font getFont( String fontFamily, Float fontSize, int fontStyle, Color fontColor )
    {

        String fontToUse = stylesDocument.getFontNameToUse( fontFamily );
        if ( StringUtils.isNotEmpty( fontToUse ) )
        {
            return options.getFontProvider().getFont( fontToUse, options.getFontEncoding(), fontSize, fontStyle,
                                                      Converter.toAwtColor( fontColor ) );
        }

        Font font = options.getFontProvider().getFont( fontFamily, options.getFontEncoding(), fontSize, fontStyle,
                                                       Converter.toAwtColor( fontColor ) );
        if ( !isFontExists( font ) )
        {
            // font is not found
            try
            {
                List<String> altNames = stylesDocument.getFontsAltName( fontFamily );
                if ( altNames != null )
                {
                    // Loop for each alternative names font (from the
                    // fontTable.xml) to find the well font.
                    for ( String altName : altNames )
                    {
                        // check if the current font name is not the same that
                        // original (o avoid StackOverFlow : see
                        // https://code.google.com/p/xdocreport/issues/detail?id=393)
                        if ( !fontFamily.equals( altName ) )
                        {
                            font = getFont( altName, fontSize, fontStyle, fontColor );
                            if ( isFontExists( font ) )
                            {
                                stylesDocument.setFontNameToUse( fontFamily, altName );
                                return font;
                            }
                        }
                    }
                }
            }
            catch ( Exception e )
            {
                LOGGER.severe( e.getMessage() );
            }
        }
        return font;
    }

    /**
     * Returns true if the iText font exists and false otherwise.
     *
     * @param font
     * @return
     */
    private boolean isFontExists( Font font )
    {
        // FIXME : is it like this to test that font exists?
        return font != null && font.getBaseFont() != null;
    }

    @Override
    protected void visitStyleText(XWPFRun run, String text, IITextContainer paragraphContainer, boolean pageNumber) throws Exception {
        createAndAddChunks( paragraphContainer, text, currentRunUnderlinePatterns, currentRunBackgroundColor,
                pageNumber, currentRunFontAscii, currentRunFontEastAsia, currentRunFontHAnsi );
    }

    @Override
    protected void visitText( CTText docxText, boolean pageNumber, IITextContainer pdfParagraphContainer )
        throws Exception
    {
        Font font = currentRunFontAscii;
        Font fontAsian = currentRunFontEastAsia;
        Font fontComplex = currentRunFontHAnsi;
        createAndAddChunks( pdfParagraphContainer, docxText.getStringValue(), currentRunUnderlinePatterns,
                            currentRunBackgroundColor, pageNumber, font, fontAsian, fontComplex );
    }

    private Chunk createTextChunk( String text, boolean pageNumber, Font currentRunFont,
                                   UnderlinePatterns currentRunUnderlinePatterns, Color currentRunBackgroundColor )
    {
        // Chunk textChunk =
        // pageNumber ? new ExtendedChunk( pdfDocument, true, currentRunFont ) :
        // new Chunk( text, currentRunFont );

        Chunk textChunk = null;
        if ( processingTotalPageCountField && expectedPageCount != null )
        {
            textChunk = new Chunk( String.valueOf( expectedPageCount ), currentRunFont );
        }
        else
        {
            textChunk =
                pageNumber ? new ExtendedChunk( pdfDocument, true, currentRunFont ) : new Chunk( text, currentRunFont );
        }
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
                textChunk.setUnderline( 0.1F, -2 );
            }
        }

        // background color
        if ( currentRunBackgroundColor != null )
        {
            textChunk.setBackground( Converter.toAwtColor( currentRunBackgroundColor ) );
        }
        if ( currentRunX != null )
        {
            this.currentRunX += textChunk.getWidthPoint();
        }

        switch ( currentRunVerticalAlign )
        {
            case SUBSCRIPT:
                textChunk.setTextRise( -currentRunFont.getSize() * stylesDocument.getVerticalAlignOffset() );
                break;
            case SUPERSCRIPT:
                textChunk.setTextRise( currentRunFont.getSize() * stylesDocument.getVerticalAlignOffset() );
                break;
        }

        return textChunk;
    }

    private void createAndAddChunks( IITextContainer parent, String textContent, UnderlinePatterns underlinePatterns,
                                     Color backgroundColor, boolean pageNumber, Font font, Font fontAsian,
                                     Font fontComplex )
    {
        StringBuilder sbuf = new StringBuilder();
        FontGroup currentGroup = FontGroup.WESTERN;
        for ( int i = 0; i < textContent.length(); i++ )
        {
            char ch = textContent.charAt( i );
            FontGroup group = FontGroup.getUnicodeGroup( ch, font, fontAsian, fontComplex );
            if ( sbuf.length() == 0 || currentGroup.equals( group ) )
            {
                // continue current chunk
                sbuf.append( ch );
            }
            else
            {
                // end chunk
                Font chunkFont = getFont( font, fontAsian, fontComplex, currentGroup );
                Chunk chunk =
                    createTextChunk( sbuf.toString(), pageNumber, chunkFont, underlinePatterns, backgroundColor );
                parent.addElement( chunk );
                // start new chunk
                sbuf.setLength( 0 );
                sbuf.append( ch );
            }
            currentGroup = group;
        }
        // end chunk
        Font chunkFont = getFont( font, fontAsian, fontComplex, currentGroup );
        Chunk chunk = createTextChunk( sbuf.toString(), pageNumber, chunkFont,
                                       underlinePatterns, backgroundColor );
        parent.addElement( chunk );
    }

    private Font getFont( Font font, Font fontAsian, Font fontComplex, FontGroup group )
    {
        switch ( group )
        {
            case WESTERN:
                return font;
            case ASIAN:
                return fontAsian;
            case COMPLEX:
                return fontComplex;
        }
        return font;
    }

    @Override
    protected void visitTab( CTPTab tab, IITextContainer pdfParagraphContainer )
        throws Exception
    {
        // TODO manage this case.
        //
        // if ( tab == null )
        // {
        // float defaultTabStop = stylesDocument.getDefaultTabStop();
        // Chunk pdfTab = new Chunk( new VerticalPositionMark(), defaultTabStop,
        // false );
        // pdfParagraphContainer.addElement( pdfTab );
        // }
        // else
        // {
        // Chunk pdfTab = new Chunk( new VerticalPositionMark() );
        // pdfParagraphContainer.addElement( pdfTab );
        // }
    }

    @Override
    protected void visitTabs( CTTabs tabs, IITextContainer pdfParagraphContainer )
        throws Exception
    {
        if ( currentRunX == null )
        {
            Paragraph paragraph = null;
            if ( pdfParagraphContainer instanceof Paragraph )
            {
                paragraph = (Paragraph) pdfParagraphContainer;
            }
            else
            {
                paragraph = (Paragraph) ( (StylableAnchor) pdfParagraphContainer ).getITextContainer();
            }
            currentRunX = paragraph.getFirstLineIndent();
            List<Chunk> chunks = paragraph.getChunks();
            for ( Chunk chunk : chunks )
            {
                currentRunX += chunk.getWidthPoint();
            }
        }
        else
        {
            if ( currentRunX >= pdfDocument.getPageWidth() )
            {
                currentRunX = 0f;
            }
        }

        Float tabPosition = null;
        STTabTlc.Enum tabLeader = null;
        STTabJc.Enum tabVal = null;
        boolean useDefaultTabStop = false;
        if ( tabs != null )
        {
            List<CTTabStop> tabList = tabs.getTabList();

            CTTabStop tabStop = getTabStop( tabList );
            if ( tabStop != null )
            {

                float lastX = DxaUtil.dxa2points( tabStop.xgetPos() );
                if ( lastX > currentRunX )
                {
                    tabPosition = lastX;
                    tabLeader = tabStop.getLeader();
                    tabVal = tabStop.getVal();
                }
                else
                {
                    useDefaultTabStop = true;
                }
            }
        }
        if ( tabs == null || useDefaultTabStop )
        {
            // default tab
            float defaultTabStop = stylesDocument.getDefaultTabStop();
            float pageWidth = pdfDocument.getPageWidth();
            int nbInterval = (int) ( pageWidth / defaultTabStop );
            Float lastX = getTabStopPosition( currentRunX, defaultTabStop, nbInterval );
            if ( lastX != null )
            {
                tabPosition = lastX;
            }
        }

        if ( tabPosition != null )
        {
            currentRunX = tabPosition;
            // tab leader : Specifies the character which shall be used to fill
            // in the space created by a tab
            // which
            // ends
            // at this custom tab stop. This character shall be repeated as
            // required to completely fill the
            // tab spacing generated by the tab character.
            VerticalPositionMark mark = createVerticalPositionMark( tabLeader );
            Chunk pdfTab = null;
            if ( STTabJc.RIGHT.equals( tabVal ) )
            {
                pdfTab = new Chunk( mark );
            }
            else
            {
                pdfTab = new Chunk( mark, currentRunX );
            }
            pdfParagraphContainer.addElement( pdfTab );
        }
    }

    private Float getTabStopPosition( float currentPosition, float interval, int nbInterval )
    {
        Float nextPosition = null;
        float newPosition = 0f;
        for ( int i = 1; i < nbInterval; i++ )
        {
            newPosition = interval * i;
            if ( currentPosition < newPosition )
            {
                nextPosition = newPosition;
                break;
            }
        }
        return nextPosition;
    }

    private VerticalPositionMark createVerticalPositionMark( org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc.Enum leader )
    {
        if ( leader != null )
        {
            if ( leader == STTabTlc.DOT )
            {
                return new DottedLineSeparator();
            }
            else if ( leader == STTabTlc.UNDERSCORE )
            {
                return new LineSeparator();
            }
        }
        return new VerticalPositionMark();
    }

    private CTTabStop getTabStop( List<CTTabStop> tabList )
    {
        if ( tabList.size() == 1 )
        {
            CTTabStop tabStop = tabList.get( 0 );
            if ( isClearTab( tabStop ) )
            {
                return null;
            }
            return tabStop;

        }
        CTTabStop selectedTabStop = null;
        for ( CTTabStop tabStop : tabList )
        {
            if ( isClearTab( tabStop ) )
            {
                continue;
            }

            if ( canApplyTabStop( tabStop ) )
            {
                return tabStop;
            }
            //
            // if ( selectedTabStop == null )
            // {
            // selectedTabStop = tabStop;
            // }
            // else
            // {
            // if ( tabStop.getPos().floatValue() >
            // selectedTabStop.getPos().floatValue() )
            // {
            // selectedTabStop = tabStop;
            // }
            // }
        }
        // TODO : retrieve the well tab stop according the current width of the
        // line.
        return null;
    }

    private boolean canApplyTabStop( CTTabStop tabStop )
    {
        if ( tabStop.getVal().equals( STTabJc.LEFT ) )
        {

            if ( currentRunX < DxaUtil.dxa2points( tabStop.xgetPos() ) )
            {
                return true;
            }
        }
        else if ( tabStop.getVal().equals( STTabJc.RIGHT ) )
        {
            if ( isWordDocumentPartParsing() )
            {
                if ( pdfDocument.getWidthLimit()
                    - ( currentRunX + DxaUtil.dxa2points( tabStop.xgetPos() ) ) <= 0 )
                {
                    return true;
                }
            }
            else
            {
                if ( currentPageWidth == null )
                {
                    return true;
                }
                if ( currentPageWidth.floatValue()
                    - ( currentRunX + DxaUtil.dxa2points( tabStop.xgetPos() ) ) <= 0 )
                {
                    return true;
                }
            }

        }
        else if ( tabStop.getVal().equals( STTabJc.CENTER ) )
        {

        }
        return false;
    }

    private boolean isClearTab( CTTabStop tabStop )
    {
        org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc.Enum tabVal = tabStop.getVal();
        if ( tabVal != null )
        {
            if ( tabVal.equals( STTabJc.CLEAR ) )
            {
                // Specifies that the current tab stop is cleared and shall be
                // removed and ignored when processing
                // the contents of this document
                return true;
            }
        }
        return false;
    }

    @Override
    protected void addNewLine( CTBr br, IITextContainer pdfParagraphContainer )
        throws Exception
    {
        pdfParagraphContainer.addElement( Chunk.NEWLINE );
    }

    @Override
    protected void visitBR( CTBr br, IITextContainer paragraphContainer )
        throws Exception
    {
        currentRunX = 0f;
        super.visitBR( br, paragraphContainer );
    }

    @Override
    protected void pageBreak()
        throws Exception
    {
        pdfDocument.pageBreak();
    }

    @Override
    protected void visitBookmark( CTBookmark bookmark, XWPFParagraph paragraph, IITextContainer paragraphContainer )
        throws Exception
    {
        // destination for a local anchor
        // chunk with empty text does not work as local anchor
        // so we create chunk with invisible but not empty text content
        // if bookmark is the last chunk in a paragraph something must be added
        // after or it does not work
        Chunk chunk = new Chunk( TAB );
        chunk.setLocalDestination( bookmark.getName() );
        paragraphContainer.addElement( chunk );
    }

    // ----------------- Table

    @Override
    protected IITextContainer startVisitTable( XWPFTable table, float[] colWidths, IITextContainer pdfParentContainer )
        throws Exception
    {
        StylableTable pdfPTable = createPDFTable( table, colWidths, pdfParentContainer );
        return pdfPTable;
    }

    private StylableTable createPDFTable( XWPFTable table, float[] colWidths, IITextContainer pdfParentContainer )
        throws DocumentException
    {
        // 2) Compute tableWith
        TableWidth tableWidth = stylesDocument.getTableWidth( table );
        StylableTable pdfPTable = pdfDocument.createTable( pdfParentContainer, colWidths.length );
        pdfPTable.setTotalWidth( colWidths );
        if ( tableWidth != null && tableWidth.width > 0 )
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
        pdfPTable.setLockedWidth( true );

        // Table alignment
        ParagraphAlignment alignment = stylesDocument.getTableAlignment( table );
        if ( alignment != null )
        {
            switch ( alignment )
            {
                case LEFT:
                    pdfPTable.setHorizontalAlignment( Element.ALIGN_LEFT );
                    break;
                case RIGHT:
                    pdfPTable.setHorizontalAlignment( Element.ALIGN_RIGHT );
                    break;
                case CENTER:
                    pdfPTable.setHorizontalAlignment( Element.ALIGN_CENTER );
                    break;
                case BOTH:
                    pdfPTable.setHorizontalAlignment( Element.ALIGN_JUSTIFIED );
                    break;
                default:
                    break;
            }
        }

        // Table indentation
        Float indentation = stylesDocument.getTableIndentation( table );
        if ( indentation != null )
        {
            pdfPTable.setPaddingLeft( indentation );
        }
        return pdfPTable;
    }

    @Override
    protected void endVisitTable( XWPFTable table, IITextContainer pdfParentContainer,
                                  IITextContainer pdfTableContainer )
        throws Exception
    {
        pdfParentContainer.addElement( ( (ExtendedPdfPTable) pdfTableContainer ).getElement() );

    }

    // ------------------------- Table Row

    @Override
    protected void startVisitTableRow( XWPFTableRow row, IITextContainer tableContainer, int rowIndex,
                                       boolean headerRow )
        throws Exception
    {
        if ( headerRow )
        {
            PdfPTable table = (PdfPTable) tableContainer;
            table.setHeaderRows( table.getHeaderRows() + 1 );
        }
        super.startVisitTableRow( row, tableContainer, rowIndex, headerRow );
    }


    @Override
    protected void endVisitTableRow(XWPFTableRow row, IITextContainer tableContainer, boolean firstRow, boolean lastRow,
                                    boolean headerRow ) throws Exception {
        ExtendedPdfPTable pdfPTable = (ExtendedPdfPTable) tableContainer;
        pdfPTable.finishRow();
    }

    // ------------------------- Table Cell

    @Override
    protected IITextContainer startVisitTableCell( final XWPFTableCell cell, IITextContainer pdfTableContainer,
                                                   boolean firstRow, boolean lastRow, boolean firstCol, boolean lastCol,
                                                   List<XWPFTableCell> vMergeCells )
        throws Exception
    {
        XWPFTableRow row = cell.getTableRow();
        XWPFTable table = row.getTable();

        // 1) store table cell info
        stylesDocument.getTableInfo( table ).addCellInfo( cell, firstRow, lastRow, firstCol, lastCol );

        // 2) create PDF cell
        StylableTable pdfPTable = (StylableTable) pdfTableContainer;

        StylableTableCell pdfPCell = pdfDocument.createTableCell( pdfPTable );
        // pdfPCell.setUseAscender( true );
        // pdfPCell.setUseDescender( true );

        XWPFTableCell lastVMergedCell = null;
        if ( vMergeCells != null )
        {
            pdfPCell.setRowspan( vMergeCells.size() );
            lastVMergedCell = vMergeCells.get( vMergeCells.size() - 1 );
            stylesDocument.getTableInfo( table ).addCellInfo( lastVMergedCell, false, lastRow, firstCol, lastCol );
        }

        // border-bottom

        XWPFTableCell theCell = lastVMergedCell != null ? lastVMergedCell : cell;
        pdfPCell.setBorderTop( stylesDocument.getTableCellBorderWithConflicts( cell, BorderSide.TOP ),
                               stylesDocument.isBorderInside( theCell, BorderSide.TOP ) );
        TableCellBorder borderBottom = stylesDocument.getTableCellBorderWithConflicts( theCell, BorderSide.BOTTOM );
        pdfPCell.setBorderBottom( borderBottom, stylesDocument.isBorderInside( theCell, BorderSide.BOTTOM ) );

        // border-left
        TableCellBorder borderLeft = stylesDocument.getTableCellBorderWithConflicts( cell, BorderSide.LEFT );
        pdfPCell.setBorderLeft( borderLeft, stylesDocument.isBorderInside( cell, BorderSide.LEFT ) );

        // border-right
        TableCellBorder borderRight = stylesDocument.getTableCellBorderWithConflicts( cell, BorderSide.RIGHT );
        pdfPCell.setBorderRight( borderRight, stylesDocument.isBorderInside( cell, BorderSide.RIGHT ) );

        // Text direction <w:textDirection
        CTTextDirection direction = stylesDocument.getTextDirection( cell );
        if ( direction != null )
        {
            int dir = direction.getVal().intValue();
            switch ( dir )
            {
                case STTextDirection.INT_BT_LR:
                    pdfPCell.setRotation( 90 );
                    break;
                case STTextDirection.INT_TB_RL:
                    pdfPCell.setRotation( 270 );
                    break;
            }
        }

        // Colspan
        BigInteger gridSpan = stylesDocument.getTableCellGridSpan( cell );
        if ( gridSpan != null )
        {
            pdfPCell.setColspan( gridSpan.intValue() );
        }

        // Background Color
        Color backgroundColor = stylesDocument.getTableCellBackgroundColor( cell );
        if ( backgroundColor != null )
        {
            pdfPCell.setBackgroundColor( Converter.toAwtColor( backgroundColor ) );
        }

        // Vertical aligment
        Enum jc = stylesDocument.getTableCellVerticalAlignment( cell );
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
        // Cell margin
        Float marginTop = stylesDocument.getTableCellMarginTop( cell );
        if ( marginTop == null )
        {
            marginTop = stylesDocument.getTableRowMarginTop( row );
            if ( marginTop == null )
            {
                marginTop = stylesDocument.getTableMarginTop( table );
            }
        }
        if ( marginTop != null )
        {
            pdfPCell.setPaddingTop( marginTop );
        }
        Float marginBottom = stylesDocument.getTableCellMarginBottom( cell );
        if ( marginBottom == null )
        {
            marginBottom = stylesDocument.getTableRowMarginBottom( row );
            if ( marginBottom == null )
            {
                marginBottom = stylesDocument.getTableMarginBottom( table );
            }
        }
        if ( marginBottom != null )
        {
            pdfPCell.setPaddingBottom( marginBottom );
        }
        Float marginLeft = stylesDocument.getTableCellMarginLeft( cell );
        if ( marginLeft == null )
        {
            marginLeft = stylesDocument.getTableRowMarginLeft( row );
            if ( marginLeft == null )
            {
                marginLeft = stylesDocument.getTableMarginLeft( table );
            }
        }
        if ( marginLeft != null )
        {
            pdfPCell.setPaddingLeft( marginLeft );
        }
        Float marginRight = stylesDocument.getTableCellMarginRight( cell );
        if ( marginRight == null )
        {
            marginRight = stylesDocument.getTableRowMarginRight( row );
            if ( marginRight == null )
            {
                marginRight = stylesDocument.getTableMarginRight( table );
            }
        }
        if ( marginRight != null )
        {
            pdfPCell.setPaddingRight( marginRight );
        }

        // Row height
        TableHeight tableHeight = stylesDocument.getTableRowHeight( row );
        if ( tableHeight != null )
        {
            if ( tableHeight.minimum )
            {
                pdfPCell.setMinimumHeight( tableHeight.height );
            }
            else
            {
                pdfPCell.setFixedHeight( tableHeight.height );
            }
        }
        // No wrap
        Boolean noWrap = stylesDocument.getTableCellNoWrap( cell );
        if ( noWrap != null )
        {
            pdfPCell.setNoWrap( noWrap );
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

    protected void visitVmlPicture(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture picture,
                                   IITextContainer pdfParentContainer)
            throws Exception
    {
        XmlCursor pictureCur = picture.newCursor();
        pictureCur.selectPath("./*");
        while (pictureCur.toNextSelection()) {
            XmlObject obj = pictureCur.getObject();
            if (obj instanceof CTShape) {
                CTShape shape = (CTShape) obj;

                List<CTImageData> imagedataList = shape.getImagedataList();
                for (CTImageData imageData : imagedataList) {
                    XWPFPictureData pictureData = getPictureDataByID(imageData.getId2());
                    visitVmlPicture(pictureData, shape.getStyle(), pdfParentContainer);
                }
            }
        }
        pictureCur.dispose();
    }

    protected void visitVmlPicture(
            XWPFPictureData pictureData, String style, IITextContainer pdfParentContainer)
    {
        if (pictureData == null) {
            return;
        }

        try {
            Image img = Image.getInstance(pictureData.getData());
            ImageShapeStyle imageStyle = ImageShapeStyle.parse(style);
            img.scaleAbsolute(imageStyle.getWidth(), imageStyle.getHeight());
            IITextContainer parentOfParentContainer = pdfParentContainer.getITextContainer();
            if (parentOfParentContainer != null && parentOfParentContainer instanceof PdfPCell) {
                pdfParentContainer.addElement(img);
            } else {
                float chunkOffsetX = 0.0F;
                float chunkOffsetY = 0.0F;
                if (pdfParentContainer instanceof Paragraph) {
                    Paragraph paragraph = (Paragraph) pdfParentContainer;
                    paragraph.setSpacingBefore(paragraph.getSpacingBefore() + 5.0F);
                }

                pdfParentContainer.addElement(new Chunk(img, chunkOffsetX, chunkOffsetY, false));
            }
        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    @Override
    protected void visitPicture( CTPicture picture, Float offsetX,
                                 org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromH.Enum relativeFromH,
                                 Float offsetY,
                                 org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV.Enum relativeFromV,
                                 STWrapText.Enum wrapText, IITextContainer pdfParentContainer )
        throws Exception
    {

        CTPositiveSize2D ext = picture.getSpPr().getXfrm().getExt();
        long x = ext.getCx();
        long y = ext.getCy();

        XWPFPictureData pictureData = super.getPictureData( picture );
        if ( pictureData != null )
        {
            try
            {
                if ( imageCache == null )
                {
                    imageCache = new HashMap<MultiValueTriplet<XWPFPictureData, Long, Long>, Image>();
                }
                MultiValueTriplet<XWPFPictureData, Long, Long> key =
                    new MultiValueTriplet<XWPFPictureData, Long, Long>( pictureData, x, y );
                Image img = imageCache.get( key );
                if ( img == null )
                {
                    imageCache.put( key, img = Image.getInstance( pictureData.getData() ) );
                    img.scaleAbsolute( emu2points( x ), emu2points( y ) );
                }
                IITextContainer parentOfParentContainer = pdfParentContainer.getITextContainer();
                if ( parentOfParentContainer != null && parentOfParentContainer instanceof PdfPCell )
                {
                    parentOfParentContainer.addElement( img );
                }
                else
                {
                    float chunkOffsetX = 0;
                    if ( offsetX != null )
                    {
                        if ( STRelFromH.CHARACTER.equals( relativeFromH ) )
                        {
                            chunkOffsetX = offsetX;
                        }
                        else if ( STRelFromH.COLUMN.equals( relativeFromH ) )
                        {
                            chunkOffsetX = offsetX;
                        }
                        else if ( STRelFromH.INSIDE_MARGIN.equals( relativeFromH ) )
                        {
                            chunkOffsetX = offsetX;
                        }
                        else if ( STRelFromH.LEFT_MARGIN.equals( relativeFromH ) )
                        {
                            chunkOffsetX = offsetX;
                        }
                        else if ( STRelFromH.MARGIN.equals( relativeFromH ) )
                        {
                            chunkOffsetX = pdfDocument.left() + offsetX;
                        }
                        else if ( STRelFromH.OUTSIDE_MARGIN.equals( relativeFromH ) )
                        {
                            chunkOffsetX = offsetX;
                        }
                        else if ( STRelFromH.PAGE.equals( relativeFromH ) )
                        {
                            chunkOffsetX = offsetX - pdfDocument.left();
                        }
                    }

                    float chunkOffsetY = 0;
                    boolean useExtendedImage = false;
                    if ( STRelFromV.PARAGRAPH.equals( relativeFromV ) )
                    {
                        useExtendedImage = true;
                    }

                    if ( useExtendedImage )
                    {
                        ExtendedImage extImg = new ExtendedImage( img, -offsetY );

                        if ( STRelFromV.PARAGRAPH.equals( relativeFromV ) )
                        {
                            chunkOffsetY = -extImg.getScaledHeight();
                        }

                        Chunk chunk = new Chunk( extImg, chunkOffsetX, chunkOffsetY, false );
                        pdfParentContainer.addElement( chunk );
                    }
                    /*
                     * float chunkOffsetY = 0; if ( wrapText != null ) { chunkOffsetY = -img.getScaledHeight(); }
                     * boolean useExtendedImage = offsetY != null; // if ( STRelFromV.PARAGRAPH.equals( relativeFromV )
                     * ) // { // useExtendedImage = true; // } // if ( useExtendedImage ) { float imgY = -offsetY; if (
                     * pdfHeader != null ) { float headerY = pdfHeader.getY() != null ? pdfHeader.getY() : 0; imgY += -
                     * img.getScaledHeight() + headerY; } ExtendedImage extImg = new ExtendedImage( img, imgY ); // if (
                     * STRelFromV.PARAGRAPH.equals( relativeFromV ) ) // { // chunkOffsetY = -extImg.getScaledHeight();
                     * // } Chunk chunk = new Chunk( extImg, chunkOffsetX, chunkOffsetY, false );
                     * pdfParentContainer.addElement( chunk ); }
                     */
                    else
                    {
                        if ( pdfParentContainer instanceof Paragraph )
                        {
                            // I don't know why but we need add some spacing
                            // before in the paragraph
                            // otherwise the image cut the text of the below
                            // paragraph (see FormattingTests JUnit)?
                            Paragraph paragraph = (Paragraph) pdfParentContainer;
                            paragraph.setSpacingBefore( paragraph.getSpacingBefore() + 5f );
                        }
                        pdfParentContainer.addElement( new Chunk( img, chunkOffsetX, chunkOffsetY, false ) );
                    }
                }

            }
            catch ( Exception e )
            {
                LOGGER.severe( e.getMessage() );
            }

        }
    }

    public int getPageCount()
    {
        if ( pdfDocument.isOpen() )
        {
            return pdfDocument.getPageNumber();
        }
        else
        {
            return pdfDocument.getPageNumber() - 1;
        }
    }

    public boolean useTotalPageField()
    {
        return totalPageFieldUsed;
    }

}

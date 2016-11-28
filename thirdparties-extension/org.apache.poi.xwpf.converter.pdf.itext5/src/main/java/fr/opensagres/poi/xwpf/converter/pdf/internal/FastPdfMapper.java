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
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromH;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import fr.opensagres.poi.xwpf.converter.core.Color;
import fr.opensagres.poi.xwpf.converter.core.ListItemContext;
import fr.opensagres.poi.xwpf.converter.core.ParagraphLineSpacing;
import fr.opensagres.poi.xwpf.converter.core.TableWidth;
import fr.opensagres.poi.xwpf.converter.core.openxmlformats.IOpenXMLFormatsPartProvider;
import fr.opensagres.poi.xwpf.converter.core.openxmlformats.OpenXMlFormatsVisitor;
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

public class FastPdfMapper
    extends OpenXMlFormatsVisitor<IITextContainer, PdfOptions, StylableMasterPage>
{

    private static final String TAB = "\t";

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( FastPdfMapper.class.getName() );

    private final OutputStream out;

    // Instance of PDF document
    private StylableDocument pdfDocument;

    private Font currentRunFontAscii;

    private Font currentRunFontEastAsia;

    private Font currentRunFontHAnsi;

    private UnderlinePatterns currentRunUnderlinePatterns;

    private BaseColor currentRunBackgroundColor;

    private Float currentRunX;

    private Float currentPageWidth;

    public FastPdfMapper( IOpenXMLFormatsPartProvider provider, OutputStream out, PdfOptions options )
        throws Exception
    {
        super( provider, options != null ? options : PdfOptions.getDefault() );
        this.out = out;
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

    public void setActiveMasterPage( StylableMasterPage masterPage )
    {
        pdfDocument.setActiveMasterPage( masterPage );
    }

    public StylableMasterPage createMasterPage( CTSectPr sectPr )
    {
        return new StylableMasterPage( sectPr );
    }

    @Override
    protected void pageBreak()
        throws Exception
    {
        pdfDocument.pageBreak();
    }

    @Override
    protected IITextContainer startVisitParagraph( CTP paragraph, ListItemContext itemContext,
                                                   IITextContainer pdfParentContainer )
        throws Exception
    {
        // create PDF paragraph
        StylableParagraph pdfParagraph = pdfDocument.createParagraph( pdfParentContainer );

        CTTbl parentTable = super.getParentTable();
        // indentation left
        Float indentationLeft = stylesDocument.getIndentationLeft( paragraph, parentTable );
        if ( indentationLeft != null )
        {
            pdfParagraph.setIndentationLeft( indentationLeft );
        }
        // indentation right
        Float indentationRight = stylesDocument.getIndentationRight( paragraph, parentTable );
        if ( indentationRight != null )
        {
            pdfParagraph.setIndentationRight( indentationRight );
        }
        // indentation first line
        Float indentationFirstLine = stylesDocument.getIndentationFirstLine( paragraph, parentTable );
        if ( indentationFirstLine != null )
        {
            pdfParagraph.setFirstLineIndent( indentationFirstLine );
        }
        // indentation hanging (remove first line)
        Float indentationHanging = stylesDocument.getIndentationHanging( paragraph, parentTable );
        if ( indentationHanging != null )
        {
            pdfParagraph.setFirstLineIndent( -indentationHanging );
        }

        // // spacing before
        Float spacingBefore = stylesDocument.getSpacingBefore( paragraph, parentTable );
        if ( spacingBefore != null )
        {
            pdfParagraph.setSpacingBefore( spacingBefore );
        }

        // spacing after
        // one more pargraph, spacing after should be applied.
        Float spacingAfter = stylesDocument.getSpacingAfter( paragraph, parentTable );
        if ( spacingAfter != null )
        {
            pdfParagraph.setSpacingAfter( spacingAfter );
        }

        ParagraphLineSpacing lineSpacing = stylesDocument.getParagraphSpacing( paragraph, parentTable );
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
        ParagraphAlignment alignment = stylesDocument.getParagraphAlignment( paragraph, parentTable );
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

    @Override
    protected void endVisitParagraph( CTP paragraph, IITextContainer pdfParentContainer,
                                      IITextContainer pdfParagraphContainer )
        throws Exception
    {
        // add the iText paragraph in the current parent container.
        ExtendedParagraph pdfParagraph = (ExtendedParagraph) pdfParagraphContainer;
        pdfParentContainer.addElement( pdfParagraph.getElement() );
    }

    @Override
    protected void visitRun( CTR run, CTP paragraph, boolean pageNumber, String url,
                             IITextContainer pdfParagraphContainer )
        throws Exception
    {
        // Font family
        String fontFamilyAscii = stylesDocument.getFontFamilyAscii( run, paragraph );
        String fontFamilyEastAsia = stylesDocument.getFontFamilyEastAsia( run, paragraph );
        String fontFamilyHAnsi = stylesDocument.getFontFamilyHAnsi( run, paragraph );

        // Get font size
        Float fontSize = stylesDocument.getFontSize( run, paragraph );
        if ( fontSize == null )
        {
            fontSize = -1f;
        }

        // Get font style
        int fontStyle = Font.NORMAL;
        Boolean bold = stylesDocument.getFontStyleBold( run, paragraph );
        if ( bold != null && bold )
        {
            fontStyle |= Font.BOLD;
        }
        Boolean italic = stylesDocument.getFontStyleItalic( run, paragraph );
        if ( italic != null && italic )
        {
            fontStyle |= Font.ITALIC;
        }
        Boolean strike = stylesDocument.getFontStyleStrike( run, paragraph );
        if ( strike != null && strike )
        {
            fontStyle |= Font.STRIKETHRU;
        }

        // Font color
        BaseColor fontColor = Converter.toBaseColor(stylesDocument.getFontColor( run, paragraph ));
        // Font
        this.currentRunFontAscii = getFont( fontFamilyAscii, fontSize, fontStyle, fontColor );
        this.currentRunFontEastAsia = getFont( fontFamilyEastAsia, fontSize, fontStyle, fontColor );
        this.currentRunFontHAnsi = getFont( fontFamilyHAnsi, fontSize, fontStyle, fontColor );

        // Underline patterns
        this.currentRunUnderlinePatterns = stylesDocument.getUnderline( run, paragraph );

        // background color
        Color awtRunBackgroundColor = stylesDocument.getBackgroundColor( run, paragraph );

        // highlight
        if ( awtRunBackgroundColor == null )
        {
        	awtRunBackgroundColor = stylesDocument.getTextHighlighting( run, paragraph );
        	if(awtRunBackgroundColor != null) {
        		this.currentRunBackgroundColor = new BaseColor(awtRunBackgroundColor.getRed(),awtRunBackgroundColor.getGreen(),awtRunBackgroundColor.getGreen());
        	}
        } else {
        	this.currentRunBackgroundColor = new BaseColor(awtRunBackgroundColor.getRed(),awtRunBackgroundColor.getGreen(),awtRunBackgroundColor.getGreen());
        }

        StylableParagraph pdfParagraph = (StylableParagraph) pdfParagraphContainer;
        pdfParagraph.adjustMultipliedLeading( currentRunFontAscii );

        // addd symbol list item chunk if needed.
        String listItemText = pdfParagraph.getListItemText();
        if ( StringUtils.isNotEmpty( listItemText ) )
        {
            // FIXME: add some space after the list item
            listItemText += "    ";

            String listItemFontFamily = pdfParagraph.getListItemFontFamily();
            Float listItemFontSize = pdfParagraph.getListItemFontSize();
            int listItemFontStyle = pdfParagraph.getListItemFontStyle();
            BaseColor listItemFontColor = pdfParagraph.getListItemFontColor();
            Font listItemFont =
                options.getFontProvider().getFont( listItemFontFamily != null ? listItemFontFamily : fontFamilyAscii,
                                                   options.getFontEncoding(),
                                                   listItemFontSize != null ? listItemFontSize : fontSize,
                                                   listItemFontStyle != Font.NORMAL ? listItemFontStyle : fontStyle,
                                                   listItemFontColor != null ? listItemFontColor : fontColor );
            
            BaseColor baseColor = new BaseColor(currentRunBackgroundColor.getRed(), currentRunBackgroundColor.getGreen(), currentRunBackgroundColor.getBlue());
            Chunk symbol =
                createTextChunk( listItemText, false, listItemFont, currentRunUnderlinePatterns, baseColor );
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
        super.visitRun( run, paragraph, pageNumber, url, pdfParagraphContainer );

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
    }

    

	private Font getFont( String fontFamily, Float fontSize, int fontStyle, BaseColor fontColor )
    {

        String fontToUse = stylesDocument.getFontNameToUse( fontFamily );
        if ( StringUtils.isNotEmpty( fontToUse ) )
        {
            return options.getFontProvider().getFont( fontToUse, options.getFontEncoding(), fontSize, fontStyle,
                                                      fontColor );
        }
        Font font =
            options.getFontProvider().getFont( fontFamily, options.getFontEncoding(), fontSize, fontStyle, fontColor );
        if ( !isFontExists( font ) )
        {
            // font is not found
            try
            {
                List<String> altNames = stylesDocument.getFontsAltName( fontFamily );
                if ( altNames != null )
                {
                    // Loop for each alternative names font (from the fontTable.xml) to find the well font.
                    for ( String altName : altNames )
                    {
                        // check if the current font name is not the same that original (o avoid StackOverFlow : see
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
                                   UnderlinePatterns currentRunUnderlinePatterns, BaseColor currentRunBackgroundColor )
    {
        Chunk textChunk =
            pageNumber ? new ExtendedChunk( pdfDocument, true, currentRunFont ) : new Chunk( text, currentRunFont );

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
        if ( currentRunX != null )
        {
            this.currentRunX += textChunk.getWidthPoint();
        }
        return textChunk;
    }

    private void createAndAddChunks( IITextContainer parent, String textContent, UnderlinePatterns underlinePatterns,
                                     BaseColor backgroundColor, boolean pageNumber, Font font, Font fontAsian,
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
        Chunk chunk = createTextChunk( sbuf.toString(), pageNumber, chunkFont, underlinePatterns, backgroundColor );
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
    protected void visitTab( CTPTab o, IITextContainer paragraphContainer )
        throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void visitTabs( CTTabs tabs, IITextContainer paragraphContainer )
        throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void visitBookmark( CTBookmark bookmark, CTP paragraph, IITextContainer paragraphContainer )
        throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void addNewLine( CTBr br, IITextContainer paragraphContainer )
        throws Exception
    {
        paragraphContainer.addElement( Chunk.NEWLINE );
    }

    @Override
    protected void visitHeader( CTHdrFtr currentHeader, CTHdrFtrRef headerRef, CTSectPr sectPr,
                                StylableMasterPage masterPage )
        throws Exception
    {
        BigInteger headerY = sectPr.getPgMar() != null ? sectPr.getPgMar().getHeader() : null;
        this.currentPageWidth = sectPr.getPgMar() != null ? DxaUtil.dxa2points( sectPr.getPgSz().getW() ) : null;
        StylableHeaderFooter pdfHeader = new StylableHeaderFooter( pdfDocument, headerY, false );
        StylableTableCell tableCell = pdfHeader.getTableCell();
        visitBodyElements( currentHeader, tableCell );
        masterPage.setHeader( pdfHeader );
        this.currentPageWidth = null;
    }

    @Override
    protected void visitFooter( CTHdrFtr currentFooter, CTHdrFtrRef footerRef, CTSectPr sectPr,
                                StylableMasterPage masterPage )
        throws Exception
    {
        BigInteger footerY = sectPr.getPgMar() != null ? sectPr.getPgMar().getFooter() : null;
        this.currentPageWidth = sectPr.getPgMar() != null ? DxaUtil.dxa2points( sectPr.getPgSz().getW() ) : null;
        StylableHeaderFooter pdfFooter = new StylableHeaderFooter( pdfDocument, footerY, false );
        StylableTableCell tableCell = pdfFooter.getTableCell();
        visitBodyElements( currentFooter, tableCell );
        masterPage.setFooter( pdfFooter );
        this.currentPageWidth = null;

    }

    // ----------------- Table

    @Override
    protected IITextContainer startVisitTable( CTTbl table, float[] colWidths, IITextContainer pdfParentContainer )
        throws Exception
    {
        StylableTable pdfPTable = createPDFTable( table, colWidths, pdfParentContainer );
        return pdfPTable;
    }

    private StylableTable createPDFTable( CTTbl table, float[] colWidths, IITextContainer pdfParentContainer )
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
    protected void endVisitTable( CTTbl table, IITextContainer parentContainer, IITextContainer tableContainer )
        throws Exception
    {

        parentContainer.addElement( ( (ExtendedPdfPTable) tableContainer ).getElement() );

    }

    // ------------------------- Table Row

    @Override
    protected void startVisitTableRow( CTRow row, IITextContainer tableContainer, boolean headerRow )
        throws Exception
    {
        if ( headerRow )
        {
            PdfPTable table = (PdfPTable) tableContainer;
            table.setHeaderRows( table.getHeaderRows() + 1 );
        }
    }

    @Override
    protected IITextContainer startVisitTableCell( CTTc cell, IITextContainer tableContainer )
        throws Exception
    {
        StylableTable pdfPTable = (StylableTable) tableContainer;
        StylableTableCell pdfPCell = pdfDocument.createTableCell( pdfPTable );

        // Background Color
        Color awtColor = stylesDocument.getTableCellBackgroundColor( cell );
        if ( awtColor != null )
        {
        	BaseColor backgroundColor = new BaseColor(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
            pdfPCell.setBackgroundColor( backgroundColor );
        }

        return pdfPCell;
    }

    @Override
    protected void endVisitTableCell( CTTc cell, IITextContainer tableContainer, IITextContainer tableCellContainer )
        throws Exception
    {
        ExtendedPdfPTable pdfPTable = (ExtendedPdfPTable) tableContainer;
        ExtendedPdfPCell pdfPCell = (ExtendedPdfPCell) tableCellContainer;
        pdfPTable.addCell( pdfPCell );
    }

    // ------------------------- Image

    @Override
    protected void visitPicture( CTPicture picture,
                                 Float offsetX,
                                 org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromH.Enum relativeFromH,
                                 Float offsetY,
                                 org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV.Enum relativeFromV,
                                 STWrapText.Enum wrapText, IITextContainer pdfParentContainer )
        throws Exception
    {

        CTPositiveSize2D ext = picture.getSpPr().getXfrm().getExt();
        long x = ext.getCx();
        long y = ext.getCy();

        byte[] pictureData = super.getPictureBytes( picture );
        if ( pictureData != null )
        {
            try
            {
                Image img = Image.getInstance( pictureData );
                img.scaleAbsolute( emu2points( x ), emu2points( y ) );

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
                            // I don't know why but we need add some spacing before in the paragraph
                            // otherwise the image cut the text of the below paragraph (see FormattingTests JUnit)?
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
}

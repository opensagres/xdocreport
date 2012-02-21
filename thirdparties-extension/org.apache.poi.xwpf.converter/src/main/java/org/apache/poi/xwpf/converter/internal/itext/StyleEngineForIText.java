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

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.poi.xwpf.converter.internal.AbstractStyleEngine;
import org.apache.poi.xwpf.converter.internal.itext.stylable.StylableParagraph;
import org.apache.poi.xwpf.converter.internal.itext.styles.FontInfos;
import org.apache.poi.xwpf.converter.internal.itext.styles.Style;
import org.apache.poi.xwpf.converter.internal.itext.styles.StyleBorder;
import org.apache.poi.xwpf.converter.internal.itext.styles.StyleParagraphProperties;
import org.apache.poi.xwpf.converter.internal.itext.styles.StyleTableProperties;
import org.apache.poi.xwpf.converter.itext.PDFViaITextOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextAlignment;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextAlignment;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline;

import com.lowagie.text.Element;
import com.lowagie.text.Font;

import fr.opensagres.xdocreport.itext.extension.IITextContainer;
import fr.opensagres.xdocreport.utils.BorderType;

public class StyleEngineForIText
    extends AbstractStyleEngine
{

    private static final String DEFAULT_STYLE = "default";

    protected static final String BOLD = "bold";

    protected static final String ITALIC = "italic";

    private final PDFViaITextOptions options;

    private final Map<String, Style> stylesMap = new HashMap<String, Style>();

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = Logger.getLogger( StyleEngineForIText.class.getName() );

    public StyleEngineForIText( XWPFDocument document, PDFViaITextOptions options )
    {
        super( document );
        this.options = options != null ? options : PDFViaITextOptions.create();
        buildDefault();
    }

    private void buildDefault()
    {
        try
        {
            CTDocDefaults defaults = document.getStyle().getDocDefaults();
            Style aStyle = new Style( DEFAULT_STYLE );
            if ( defaults != null )
            {
                if ( defaults.getPPrDefault().getPPr() != null )
                {
                    StyleParagraphProperties paragraphProperties =
                        mapStyleParagraphProperties( defaults.getPPrDefault().getPPr() );
                    aStyle.setParagraphProperties( paragraphProperties );
                    FontInfos fontInfos = processRPR( defaults.getRPrDefault().getRPr() );
                    paragraphProperties.setFontInfos( fontInfos );
                }
            }
            stylesMap.put( DEFAULT_STYLE, aStyle );
        }
        catch ( XmlException e )
        {
            LOGGER.severe( e.getMessage() );
        }
        catch ( IOException e )
        {
            LOGGER.severe( e.getMessage() );
        }

    }

    public void visit( Object ele )
    {
        // TODO Auto-generated method stub

    }

    public void visit( StylableParagraph ele )
    {

    }

    private StyleParagraphProperties mapStyleParagraphProperties( CTPPr xwpfParagraphProperties )
    {
        StyleParagraphProperties paragraphProperties = new StyleParagraphProperties();
        CTSpacing spacing = xwpfParagraphProperties.getSpacing();
        if ( spacing != null )
        {

            BigInteger spacingBefore = spacing.getBefore();

            if ( spacingBefore != null )
            {
                paragraphProperties.setSpacingBefore( spacingBefore.intValue() );
            }
            BigInteger spacingAfter = spacing.getAfter();
            if ( spacingAfter != null )
            {
                paragraphProperties.setSpacingAfter( spacingAfter.intValue() );
            }
        }

        // TODO : text Alignement...

        CTTextAlignment alignment = xwpfParagraphProperties.getTextAlignment();

        if ( alignment != null )
        {
            STTextAlignment textAlignment = alignment.xgetVal();

            if ( STTextAlignment.BASELINE.equals( textAlignment ) )
            {
                paragraphProperties.setAlignment( Element.ALIGN_BASELINE );
            }
            else if ( STTextAlignment.BOTTOM.equals( textAlignment ) )
            {
                paragraphProperties.setAlignment( Element.ALIGN_BOTTOM );
            }
            else if ( STTextAlignment.CENTER.equals( textAlignment ) )
            {
                paragraphProperties.setAlignment( Element.ALIGN_CENTER );
            }
            else if ( STTextAlignment.TOP.equals( textAlignment ) )
            {
                paragraphProperties.setAlignment( Element.ALIGN_TOP );
            }

        }
        CTInd ctInd = xwpfParagraphProperties.getInd();
        if ( ctInd != null )
        {
            processIndent( paragraphProperties, ctInd );

        }

        // CTParaRPr ctParaRPr = xwpfParagraphProperties.getRPr();
        // if (ctParaRPr != null) {
        //
        // }
        return paragraphProperties;
    }

    private FontInfos processRPR( CTRPr ctParaRPr )
    {

        FontInfos fontInfos = new FontInfos();
        CTFonts fonts = ctParaRPr.getRFonts();
        if ( fonts != null && fonts.getAscii() != null )
        {

            // font familly
            fontInfos.setFontFamily( fonts.getAscii() );
        }

        boolean bold = ctParaRPr.getB() != null && STOnOff.TRUE.equals( ctParaRPr.getB().xgetVal() );
        boolean italic = ctParaRPr.getI() != null && STOnOff.TRUE.equals( ctParaRPr.getI().xgetVal() );

        if ( bold && italic )
        {
            fontInfos.setFontStyle( Font.BOLDITALIC );
        }
        else if ( bold )
        {
            fontInfos.setFontStyle( Font.BOLD );
        }
        else if ( italic )
        {
            fontInfos.setFontStyle( Font.ITALIC );

        }
        // font size
        CTHpsMeasure hpsMeasure = ctParaRPr.getSz();
        if ( hpsMeasure != null )
        {

            STHpsMeasure measure = hpsMeasure.xgetVal();
            float size = measure.getBigDecimalValue().floatValue();
            // cf. http://www.schemacentral.com/sc/ooxml/t-w_ST_HpsMeasure.html
            fontInfos.setFontSize( size / 2 );
        }

        CTUnderline underline = ctParaRPr.getU();

        int style = fontInfos.getFontStyle();
        if ( underline != null )
        {

            STUnderline uu = underline.xgetVal();

            if ( STUnderline.NONE != uu.enumValue() )
            {
                style = style | Font.UNDERLINE;
                fontInfos.setFontStyle( style );
            }
        }

        // font color...
        CTColor ctColor = ctParaRPr.getColor();
        if ( ctColor != null )
        {

            STHexColor hexColor = ctColor.xgetVal();
            String strText = hexColor.getStringValue();

            if ( !"auto".equals( strText ) )
            {

                Color color = ColorRegistry.getInstance().getColor( "0x" + strText );
                fontInfos.setFontColor( color );
            }
        }

        // font encoding
        fontInfos.setFontEncoding( options.getFontEncoding() );
        return fontInfos;
    }

    private void processIndent( StyleParagraphProperties paragraphProperties, CTInd ctInd )
    {
        BigInteger firstLine = ctInd.getFirstLine();
        if ( firstLine != null )
            paragraphProperties.setIndentationFirstLine( firstLine.intValue() );

        BigInteger left = ctInd.getLeft();
        if ( left != null )
            paragraphProperties.setIndentationLeft( left.intValue() );
        BigInteger right = ctInd.getLeft();
        if ( right != null )
            paragraphProperties.setIndentationRight( right );
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

    protected Style buildStyle( String styleID )
    {
        if ( styleID == null )
            return stylesMap.get( DEFAULT_STYLE );

        // else

        Style aStyle = stylesMap.get( styleID );
        if ( aStyle == null )
        {

            XWPFStyle style = document.getStyles().getStyle( styleID );

            aStyle = new Style( styleID );
            CTPPr xwpfParagraphProperties = style.getCTStyle().getPPr();
            if ( xwpfParagraphProperties != null )
            {
                StyleParagraphProperties paragraphProperties = mapStyleParagraphProperties( xwpfParagraphProperties );
                aStyle.setParagraphProperties( paragraphProperties );
                if ( style.getCTStyle().getRPr() != null )
                {
                    FontInfos fontInfos = processRPR( style.getCTStyle().getRPr() );
                    paragraphProperties.setFontInfos( fontInfos );
                }

                // borders...
                CTPBdr borders = xwpfParagraphProperties.getPBdr();
                if ( borders != null )
                {
                    paragraphProperties.setBorderBottom( createBorder( borders.getBottom(), BorderType.BOTTOM ) );
                    paragraphProperties.setBorderLeft( createBorder( borders.getLeft(), BorderType.LEFT ) );
                    paragraphProperties.setBorderRight( createBorder( borders.getRight(), BorderType.RIGHT ) );
                    paragraphProperties.setBorderTop( createBorder( borders.getTop(), BorderType.TOP ) );
                    // XXX:
                    // paragraphProperties.setBorderBetween(createBorder(borders.getBetween()));
                }
            }
            // TODO :
            CTTblPrBase xwpfTableProperties = style.getCTStyle().getTblPr();
            if ( xwpfTableProperties != null )
            {
                StyleTableProperties tableProperties = mapStyleTableProperties( xwpfTableProperties );

            }

            stylesMap.put( styleID, aStyle );
        }
        return aStyle;
    }

    private StyleTableProperties mapStyleTableProperties( CTTblPrBase xwpfTablePropertiesBase )
    {
        StyleTableProperties tableProperties = new StyleTableProperties();
        // CTSpacing spacing = xwpfParagraphProperties.getSpacing();
        CTTblWidth width = xwpfTablePropertiesBase.getTblW();
        if ( width != null )
        {
            tableProperties.setWidth( width.getW().floatValue() );
        }

        CTTblBorders xwpfBorders = xwpfTablePropertiesBase.getTblBorders();
        // TODO:
        // xwpfBorders.getBottom();

        if ( xwpfBorders != null )
        {
            tableProperties.setBorderBottom( createBorder( xwpfBorders.getBottom(), BorderType.BOTTOM ) );
            tableProperties.setBorderLeft( createBorder( xwpfBorders.getLeft(), BorderType.LEFT ) );
            tableProperties.setBorderRight( createBorder( xwpfBorders.getRight(), BorderType.RIGHT ) );
            tableProperties.setBorderTop( createBorder( xwpfBorders.getTop(), BorderType.TOP ) );
            // XXX:
            // paragraphProperties.setBorderBetween(createBorder(borders.getBetween()));
        }

        return tableProperties;
    }

    @Override
    protected IITextContainer startVisitDocument( OutputStream out )
        throws Exception
    {
        // TODO parse default style here ?
        CTStyles styles = document.getStyle();
        styles.getDocDefaults().getPPrDefault().getPPr();
        return null;
    }

    @Override
    protected IITextContainer startVisitPargraph( XWPFParagraph xwpfParagraph, IITextContainer pdfParagraph )
        throws Exception
    {
        String styleID = xwpfParagraph.getStyleID();
        if ( styleID != null )
        {
            Style style = stylesMap.get( styleID );
            if ( style == null )
            {
                style = buildStyle( styleID );
            }
        }
        return pdfParagraph;
    }

    @Override
    protected void endVisitPargraph( XWPFParagraph paragraph, IITextContainer parentContainer,
                                     IITextContainer paragraphContainer )
        throws Exception
    {

    }

    @Override
    protected void visitEmptyRun( IITextContainer paragraphContainer )
        throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void visitRun( XWPFRun run, IITextContainer paragraphContainer )
        throws Exception
    {

    }

    @Override
    protected IITextContainer startVisitTable( XWPFTable table, IITextContainer tableContainer )
        throws Exception
    {

        CTString str = table.getCTTbl().getTblPr().getTblStyle();
        if ( str != null )
        {
            String styleID = str.getVal();
            if ( styleID != null )
            {
                Style style = stylesMap.get( styleID );
                if ( style == null )
                {
                    style = buildStyle( styleID );
                }
            }
        }
        return tableContainer;
    }

    @Override
    protected void endVisitTable( XWPFTable table, IITextContainer parentContainer, IITextContainer tableContainer )
        throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected IITextContainer startVisitTableCell( XWPFTableCell cell, IITextContainer tableContainer )
    {

        return null;
    }

    @Override
    protected void endVisitTableCell( XWPFTableCell cell, IITextContainer tableContainer,
                                      IITextContainer tableCellContainer )
    {
        //System.out.println( cell );

    }

    @Override
    protected void visitPicture( XWPFPicture picture, IITextContainer parentContainer )
        throws Exception
    {

    }

    @Override
    protected void endVisitDocument()
        throws Exception
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void visitHeader( CTHdrFtrRef headerRef )
        throws Exception
    {
        // TODO Auto-generated method stub
    }

    @Override
    protected void visitFooter( CTHdrFtrRef footerRef )
        throws Exception
    {
        // TODO Auto-generated method stub
    }

    protected void visitParagraph( XWPFParagraph ele )
        throws Exception
    {

    }

    public Style getDefaultStyle()
    {
        return stylesMap.get( DEFAULT_STYLE );
    }

    protected Style getStyle( String styleID )
    {
        if ( styleID == null )

            return getDefaultStyle();
        else if ( !document.getStyles().styleExist( styleID ) )
            return getDefaultStyle();
        // else
        return stylesMap.get( styleID );
    }
}
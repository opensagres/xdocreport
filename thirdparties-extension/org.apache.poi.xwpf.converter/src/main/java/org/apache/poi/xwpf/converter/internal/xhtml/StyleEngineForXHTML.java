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
package org.apache.poi.xwpf.converter.internal.xhtml;

import static org.apache.poi.xwpf.converter.internal.DxaUtil.dxa2points;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.converter.IURIResolver;
import org.apache.poi.xwpf.converter.internal.AbstractStyleEngine;
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
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextAlignment;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextAlignment;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnderline;

import fr.opensagres.xdocreport.itext.extension.IITextContainer;
import fr.opensagres.xdocreport.utils.StringUtils;
import fr.opensagres.xdocreport.xhtml.extension.CSSStylePropertyConstants;
import fr.opensagres.xdocreport.xhtml.extension.CSSStyleSheet;
import fr.opensagres.xdocreport.xhtml.extension.XHTMLConstants;
import fr.opensagres.xdocreport.xhtml.extension.XHTMLPageContentBuffer;

public class StyleEngineForXHTML
    extends AbstractStyleEngine
    implements XHTMLConstants, CSSStylePropertyConstants
{

    private static final String STYLE_NAME_HEADER = "Header";

    private static final String STYLE_NAME_FOOTER = "Footer";

    private CSSStyleSheet cssStyleSheet;

    private final boolean generateCSSComments;

    private static final String DEFAULT_STYLE = "default";

    // private final Map<String, String> classNamesMap = new HashMap<String,
    // String>();
    private final IURIResolver resolver;

    // private final Map<String, Style> stylesMap = new HashMap<String,
    // Style>();
    private final List<String> stylesMap = new ArrayList<String>();

    public StyleEngineForXHTML( XWPFDocument document, boolean generateCSSComments, int indent, IURIResolver resolver )
    {
        super( document );
        this.generateCSSComments = generateCSSComments;
        this.resolver = resolver;
        cssStyleSheet = new CSSStyleSheet( indent );
        buildDefault();
    }

    private void maptStyleParagraphProperties( CTPPr xwpfParagraphProperties )
    {

        CTSpacing spacing = xwpfParagraphProperties.getSpacing();
        if ( spacing != null )
        {

            BigInteger spacingBefore = spacing.getBefore();

            if ( spacingBefore != null )
            {
                cssStyleSheet.setCSSProperty( MARGIN_TOP, dxa2points( spacingBefore ) + "pt" );
                // paragraphProperties.setSpacingBefore(spacingBefore.intValue());
            }
            BigInteger spacingAfter = spacing.getAfter();
            if ( spacingAfter != null )
            {
                cssStyleSheet.setCSSProperty( MARGIN_BOTTOM, dxa2points( spacingAfter ) + "pt" );

            }
        }

        // TODO : text Alignement...

        CTTextAlignment alignment = xwpfParagraphProperties.getTextAlignment();

        if ( alignment != null )
        {
            STTextAlignment textAlignment = alignment.xgetVal();
            // vertical-align
            cssStyleSheet.setCSSProperty( VERTICAL_ALIGN, textAlignment.getStringValue() );

        }
        CTInd ctInd = xwpfParagraphProperties.getInd();
        if ( ctInd != null )
        {
            BigInteger firstLine = ctInd.getFirstLine();
            if ( firstLine != null )
                cssStyleSheet.setCSSProperty( TEXT_INDENT, dxa2points( firstLine ) + "pt" );

            BigInteger left = ctInd.getLeft();
            if ( left != null )
                cssStyleSheet.setCSSProperty( MARGIN_LEFT, dxa2points( left ) + "pt" );

            BigInteger right = ctInd.getLeft();
            if ( right != null )
                cssStyleSheet.setCSSProperty( MARGIN_RIGHT, dxa2points( right ) + "pt" );

        }

    }

    private void processRPR( CTRPr ctParaRPr )
    {
        if ( ctParaRPr != null )
        {

            CTFonts fonts = ctParaRPr.getRFonts();
            if ( fonts != null )
            {

                // font-family
                String fontFamily = fonts.getAscii();
                if ( StringUtils.isNotEmpty( fontFamily ) )
                {
                    cssStyleSheet.setCSSProperty( FONT_FAMILY, "\"" + fontFamily + "\"" );
                }
            }
            /*
             * String fontName = ele.getStyleFontNameAttribute(); if (StringUtils.isNotEmpty(fontName)) {
             * cssStyleSheet.setCSSProperty(FONT_STYLE, "\"" + fontName + "\""); }
             */

            else if ( ctParaRPr.getB() != null && STOnOff.TRUE.equals( ctParaRPr.getB().xgetVal() ) )
            {
                // font-weight
                cssStyleSheet.setCSSProperty( FONT_WEIGHT, "bold" );
            }
            else if ( ctParaRPr.getI() != null && STOnOff.TRUE.equals( ctParaRPr.getI().xgetVal() ) )
            {
                // font-style
                cssStyleSheet.setCSSProperty( FONT_STYLE, "italic" );

            }

            // font size
            CTHpsMeasure hpsMeasure = ctParaRPr.getSz();
            if ( hpsMeasure != null )
            {

                STHpsMeasure measure = hpsMeasure.xgetVal();
                float size = measure.getBigDecimalValue().floatValue();
                cssStyleSheet.setCSSProperty( FONT_SIZE, String.valueOf( size ) );
            }
            CTUnderline underline = ctParaRPr.getU();
            // TODO underline:
            if ( underline != null )
            {

                STUnderline uu = underline.xgetVal();

                if ( STUnderline.NONE != uu.enumValue() )
                {
                    cssStyleSheet.setCSSProperty( TEXT_DECORATION, TEXT_DECORATION_UNDERLINE );

                }
            }

            // XXX It's difficult but necessary to test

            CTColor ctColor = ctParaRPr.getColor();
            if ( ctColor != null )
            {

                STHexColor hexColor = ctColor.xgetVal();
                String strText = hexColor.getStringValue();

                if ( !"auto".equals( strText ) )
                    cssStyleSheet.setCSSProperty( COLOR, "#" + strText );
            }
        }
    }

    private void buildDefault()
    {
        try
        {
            CTDocDefaults defaults = document.getStyle().getDocDefaults();
            // Style aStyle = new Style(DEFAULT_STYLE);

            stylesMap.add( DEFAULT_STYLE );

            cssStyleSheet.startCSSStyleDeclaration( DEFAULT_STYLE );
            if ( defaults != null )
            {
                if ( defaults.getPPrDefault().getPPr() != null )
                {
                    maptStyleParagraphProperties( defaults.getPPrDefault().getPPr() );

                }

            }
            cssStyleSheet.endCSSStyleDeclaration();

            // stylesMap.put(DEFAULT_STYLE, aStyle);
        }
        catch ( XmlException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // private void processIndent(StyleParagraphProperties paragraphProperties,
    // CTInd ctInd) {
    // BigInteger firstLine = ctInd.getFirstLine();
    // if (firstLine != null)
    // paragraphProperties.setIndentationFirstLine(firstLine.intValue());
    //
    // BigInteger left = ctInd.getLeft();
    // if (left != null)
    // paragraphProperties.setIndentationLeft(left.intValue());
    // BigInteger right = ctInd.getLeft();
    // if (right != null)
    // paragraphProperties.setIndentationRight(right.intValue());
    // }

    protected String buildStyle( String styleID )
    {
        if ( styleID == null )
            return DEFAULT_STYLE;

        // else

        // Style aStyle = stylesMap.get(styleID);

        if ( !stylesMap.contains( styleID ) )
        {

            XWPFStyle style = document.getStyles().getStyle( styleID );
            // if (style == null) {
            // System.out.println(styleID);
            // return null;
            // }

            // StyleParagraphProperties paragraphProperties = new
            // StyleParagraphProperties();
            // aStyle.setParagraphProperties(paragraphProperties);
            CTPPr xwpfParagraphProperties = style.getCTStyle().getPPr();
            if ( xwpfParagraphProperties != null )
            {
                cssStyleSheet.startCSSStyleDeclaration( styleID );
                maptStyleParagraphProperties( xwpfParagraphProperties );

                CTStyle ctStyle = style.getCTStyle();
                CTRPr rpr = ctStyle.getRPr();
                processRPR( rpr );

                // borders...
                CTPBdr borders = xwpfParagraphProperties.getPBdr();
                if ( borders != null )
                {
                    CTBorder bottom = borders.getBottom();

                    if ( bottom != null )
                    {

                        STBorder border = bottom.xgetVal();
                        System.err.println( "bottom " + border );
                        if ( !STBorder.NONE.equals( border ) && !STBorder.NIL.equals( border ) )
                        {
                            cssStyleSheet.setCSSProperty( BORDER_BOTTOM, border.getStringValue() );
                            // XXX semi point ?
                            cssStyleSheet.setCSSProperty( BORDER_BOTTOM_WIDTH, bottom.getSz().floatValue() / 2 + "pt" );
                            cssStyleSheet.setCSSProperty( BORDER_BOTTOM_COLOR, "#"
                                + bottom.xgetColor().getStringValue() );
                        }
                    }
                    CTBorder left = borders.getBottom();
                    if ( left != null )
                    {

                        STBorder border = left.xgetVal();
                        System.err.println( "left " + border.getStringValue() );
                        if ( !STBorder.NONE.equals( border ) && !STBorder.NIL.equals( border ) )
                        {
                            cssStyleSheet.setCSSProperty( BORDER_LEFT, border.getStringValue() );
                            // XXX semi point ?
                            cssStyleSheet.setCSSProperty( BORDER_LEFT_WIDTH, left.getSz().floatValue() / 2 + "pt" );
                            cssStyleSheet.setCSSProperty( BORDER_LEFT_COLOR, "#" + left.xgetColor().getStringValue() );
                        }
                    }

                    CTBorder top = borders.getBottom();
                    if ( top != null )
                    {

                        STBorder border = left.xgetVal();
                        System.err.println( "top " + border.getStringValue() );
                        if ( !STBorder.NONE.equals( border ) && !STBorder.NIL.equals( border ) )
                        {
                            cssStyleSheet.setCSSProperty( BORDER_TOP, border.getStringValue() );
                            // XXX semi point ?
                            cssStyleSheet.setCSSProperty( BORDER_TOP_WIDTH, top.getSz().floatValue() / 2 + "pt" );
                            cssStyleSheet.setCSSProperty( BORDER_TOP_COLOR, "#" + top.xgetColor().getStringValue() );
                        }
                    }

                    CTBorder right = borders.getBottom();
                    if ( right != null )
                    {

                        STBorder border = right.xgetVal();
                        System.err.println( "right " + border.getStringValue() );
                        if ( !STBorder.NONE.equals( border ) && !STBorder.NIL.equals( border ) )
                        {
                            cssStyleSheet.setCSSProperty( BORDER_RIGHT, border.getStringValue() );
                            // XXX semi point ?
                            cssStyleSheet.setCSSProperty( BORDER_RIGHT_WIDTH, right.getSz().floatValue() / 2 + "pt" );
                            cssStyleSheet.setCSSProperty( BORDER_RIGHT_COLOR, "#" + right.xgetColor().getStringValue() );
                        }
                    }
                }
                cssStyleSheet.endCSSStyleDeclaration();
            }

            stylesMap.add( styleID );
        }
        return styleID;
    }

    @Override
    protected IITextContainer startVisitDocument( OutputStream out )
        throws Exception
    {
        // TODO parse default style here ?
        CTStyles styles = document.getStyle();
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "office:styles begin" );
        }
        styles.getDocDefaults().getPPrDefault().getPPr();
        if ( generateCSSComments )
        {
            cssStyleSheet.setComment( "office:styles end" );
        }
        return null;
    }

    @Override
    protected IITextContainer startVisitPargraph( XWPFParagraph xwpfParagraph, IITextContainer pdfParagraph )
        throws Exception
    {
        String styleID = xwpfParagraph.getStyleID();
        if ( styleID != null )
        {
            // FIXME : bad if styleID==null
            if ( !stylesMap.contains( styleID ) )
            {
                buildStyle( styleID );
            }
            // if (currentStyle == null)
            // currentStyle = style;
            // StyleParagraphProperties paragraphProperties =
            // style.getParagraphProperties();
        }
        // TODO: elsewhere...
        // spacingAfter

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
        // TODO Auto-generated method stub
        return null;
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

    public String getClassName( String familyName, String styleName )
    {
        StringBuilder className = new StringBuilder();
        // style:family
        className.append( familyName );
        // style:name
        if ( styleName != null )
        {
            className.append( '_' );
            className.append( StringUtils.replaceAll( styleName, ".", "_" ) );
        }
        return className.toString();

    }

    // -------------------------- Apply styles ---------------

    public CSSStyleSheet getCSSStyleSheet()
    {
        return cssStyleSheet;
    }

    public String getMasterPageHeaderStyleName( String masterPageLayoutName )
    {
        return masterPageLayoutName + STYLE_NAME_HEADER;
    }

    public String getMasterPageFooterStyleName( String masterPageLayoutName )
    {
        return masterPageLayoutName + STYLE_NAME_FOOTER;
    }

    public void applyStyles( String styleName, XHTMLPageContentBuffer xhtml )
    {
        // String className = classNamesMap
        // .get(getClassName(familyName, styleName));
        if ( StringUtils.isNotEmpty( styleName ) )
        {
            xhtml.setAttribute( CLASS_ATTR, styleName );
        }
    }

    public IURIResolver getURIResolver()
    {
        return resolver;
    }

    public void visit( Object ele )
    {
        // TODO Auto-generated method stub

    }
}

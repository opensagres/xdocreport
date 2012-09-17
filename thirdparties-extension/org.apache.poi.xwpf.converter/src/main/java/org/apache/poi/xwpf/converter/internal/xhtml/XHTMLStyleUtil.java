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
package org.apache.poi.xwpf.converter.internal.xhtml;

import static org.apache.poi.xwpf.converter.internal.DxaUtil.dxa2points;
import static org.apache.poi.xwpf.converter.internal.XWPFRunUtils.getFontColor;
import static org.apache.poi.xwpf.converter.internal.XWPFRunUtils.getFontFamily;
import static org.apache.poi.xwpf.converter.internal.XWPFRunUtils.isBold;
import static org.apache.poi.xwpf.converter.internal.XWPFRunUtils.isItalic;
import static org.apache.poi.xwpf.converter.internal.XWPFUtils.getRPr;

import java.awt.Color;
import java.math.BigInteger;

import org.apache.poi.xwpf.converter.internal.XWPFUtils;
import org.apache.poi.xwpf.converter.internal.itext.TableWidth;
import org.apache.poi.xwpf.converter.internal.itext.XWPFParagraphUtils;
import org.apache.poi.xwpf.converter.internal.itext.XWPFTableUtil;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;

import fr.opensagres.xdocreport.utils.BorderType;
import fr.opensagres.xdocreport.utils.StringUtils;
import fr.opensagres.xdocreport.xhtml.extension.CSSStylePropertyConstants;
import fr.opensagres.xdocreport.xhtml.extension.XHTMLUtil;

public class XHTMLStyleUtil
    implements CSSStylePropertyConstants
{

    public static StringBuilder getStyle( XWPFDocument document, CTDocDefaults defaults )
    {
        StringBuilder htmlStyle = new StringBuilder();
        CTSectPr sectPr = document.getDocument().getBody().getSectPr();
        CTPageSz pageSize = sectPr.getPgSz();

        if ( pageSize != null )
        {
            // Width
            BigInteger width = pageSize.getW();
            float widthPt = dxa2points( width );
            XHTMLUtil.addHTMLStyle( htmlStyle, WIDTH, widthPt + "pt" );
        }

        CTPageMar pageMargin = sectPr.getPgMar();
        if ( pageMargin != null )
        {

            // margin bottom
            BigInteger marginBottom = pageMargin.getBottom();
            if ( marginBottom != null )
            {
                float marginBottomPt = dxa2points( marginBottom );
                XHTMLUtil.addHTMLStyle( htmlStyle, MARGIN_BOTTOM, marginBottomPt + "pt" );
            }

            // margin top
            BigInteger marginTop = pageMargin.getTop();
            if ( marginTop != null )
            {
                float marginTopPt = dxa2points( marginTop );
                XHTMLUtil.addHTMLStyle( htmlStyle, MARGIN_TOP, marginTopPt + "pt" );
            }

            // margin right
            BigInteger marginRight = pageMargin.getRight();
            if ( marginRight != null )
            {
                float marginRightPt = dxa2points( marginRight );
                XHTMLUtil.addHTMLStyle( htmlStyle, MARGIN_RIGHT, marginRightPt + "pt" );
            }

            // margin left
            BigInteger marginLeft = pageMargin.getLeft();
            if ( marginLeft != null )
            {
                float marginLeftPt = dxa2points( marginLeft );
                XHTMLUtil.addHTMLStyle( htmlStyle, MARGIN_LEFT, marginLeftPt + "pt" );
            }

        }
        return htmlStyle;
    }

    public static StringBuilder getStyle( XWPFParagraph paragraph, XWPFStyle style, CTDocDefaults defaults )
    {
        StringBuilder htmlStyle = new StringBuilder();

        float indentationLeft = -1;
        float indentationRight = -1;
        float firstLineIndent = -1;
        float spacingBefore = -1;
        float spacingAfter = -1;

        // // 1) From style
        // CTPPr ppr = getPPr(style);
        // if (ppr != null) {
        // // Indentation
        // CTInd ind = ppr.getInd();
        // if (ind != null) {
        //
        // // Left Indentation
        // BigInteger left = ind.getLeft();
        // if (left != null) {
        // indentationLeft = dxa2points(left);
        // }
        //
        // // Right Indentation
        // BigInteger right = ind.getRight();
        // if (right != null) {
        // indentationRight = dxa2points(right);
        // }
        //
        // // First line Indentation
        // BigInteger firstLine = ind.getFirstLine();
        // if (firstLine != null) {
        // firstLineIndent = dxa2points(firstLine);
        // }
        // }
        //
        // CTSpacing spacing = ppr.getSpacing();
        // if (spacing != null) {
        //
        // // Spacing before
        // BigInteger before = spacing.getBefore();
        // if (before != null) {
        // spacingBefore = dxa2points(before);
        // }
        //
        // // Spacing after
        // BigInteger after = spacing.getAfter();
        // if (after != null) {
        // spacingAfter = dxa2points(after);
        // }
        // }
        //
        // }

        // 2) From paragraph
        if ( indentationLeft == -1 && paragraph.getIndentationLeft() != -1 )
        {
            indentationLeft = dxa2points( paragraph.getIndentationLeft() );
        }
        if ( indentationRight == -1 && paragraph.getIndentationRight() != -1 )
        {
            indentationRight = dxa2points( paragraph.getIndentationRight() );
        }
        if ( firstLineIndent == -1 && paragraph.getIndentationFirstLine() != -1 )
        {
            firstLineIndent = dxa2points( paragraph.getIndentationFirstLine() );
        }
        if ( spacingBefore == -1 && paragraph.getSpacingBefore() != -1 )
        {
            spacingBefore = dxa2points( paragraph.getSpacingBefore() );
        }
        if ( spacingAfter == -1 && paragraph.getSpacingAfter() != -1 )
        {
            spacingAfter = dxa2points( paragraph.getSpacingAfter() );
        }

        // 3) From default
        // TODO

        // Apply
        if ( indentationLeft != -1 )
        {
            XHTMLUtil.addHTMLStyle( htmlStyle, TEXT_ALIGN, "left" );
            XHTMLUtil.addHTMLStyle( htmlStyle, TEXT_INDENT, indentationLeft + "pt" );
        }
        if ( indentationRight != -1 )
        {
            XHTMLUtil.addHTMLStyle( htmlStyle, TEXT_ALIGN, "right" );
            XHTMLUtil.addHTMLStyle( htmlStyle, TEXT_INDENT, indentationRight + "pt" );
        }
        if ( firstLineIndent != -1 )
        {
            XHTMLUtil.addHTMLStyle( htmlStyle, TEXT_INDENT, firstLineIndent + "pt" );
        }

        // Aligment
        ParagraphAlignment alignment = paragraph.getAlignment();
        switch ( alignment )
        {
            case LEFT:
                XHTMLUtil.addHTMLStyle( htmlStyle, TEXT_ALIGN, TEXT_ALIGN_LEFT );
                break;
            case RIGHT:
                XHTMLUtil.addHTMLStyle( htmlStyle, TEXT_ALIGN, TEXT_ALIGN_RIGHT );
                break;

            case CENTER:
                XHTMLUtil.addHTMLStyle( htmlStyle, TEXT_ALIGN, TEXT_ALIGN_CENTER );
                break;

            case BOTH:
                XHTMLUtil.addHTMLStyle( htmlStyle, TEXT_ALIGN, TEXT_ALIGN_JUSTIFIED );
                break;
        }

        // Margin bottom/top
        if ( spacingBefore != -1 )
        {
            XHTMLUtil.addHTMLStyle( htmlStyle, MARGIN_TOP, spacingBefore + "pt" );
        }
        else
        {
            // By default p element are no margin top/bottom
            // XHTMLUtil.addHTMLStyle(htmlStyle, MARGIN_TOP, "0");
            XHTMLUtil.addHTMLStyle( htmlStyle, MARGIN_TOP, "0" );
        }
        if ( spacingAfter != -1 )
        {
            XHTMLUtil.addHTMLStyle( htmlStyle, MARGIN_BOTTOM, spacingAfter + "pt" );
        }
        else
        {
            // By default p element are no margin top/bottom
            // XHTMLUtil.addHTMLStyle(htmlStyle, MARGIN_TOP, "0");
            XHTMLUtil.addHTMLStyle( htmlStyle, MARGIN_BOTTOM, "0" );
        }

        // Background-color
        Color backgroundColor = XWPFParagraphUtils.getBackgroundColor( paragraph );
        if ( backgroundColor != null )
        {
            XHTMLUtil.addHTMLStyle( htmlStyle, BACKGROUND_COLOR, XWPFUtils.toHexString( backgroundColor ) );

        }
        return htmlStyle;
    }

    public static StringBuilder getStyle( XWPFRun run, XWPFStyle runStyle, XWPFStyle style, CTDocDefaults defaults )
    {
        StringBuilder htmlStyle = new StringBuilder();

        // Get CTRPr from style+defaults
        CTRPr runRprStyle = getRPr( runStyle );
        CTRPr rprStyle = getRPr( style );
        CTRPr rprDefault = getRPr( defaults );

        // Font family
        String fontFamily = getFontFamily( run, rprStyle, rprDefault );
        if ( StringUtils.isNotEmpty( fontFamily ) )
        {
            XHTMLUtil.addHTMLStyle( htmlStyle, FONT_FAMILY, "'" + fontFamily + "'" );
        }

        // 2) Font size
        float fontSize = run.getFontSize();
        if ( fontSize == -1 )
        {
            XHTMLUtil.addHTMLStyle( htmlStyle, FONT_SIZE, fontSize + "pt" );
        }

        // Font Bold
        if ( isBold( run, runRprStyle, rprStyle, rprDefault ) )
        {
            XHTMLUtil.addHTMLStyle( htmlStyle, FONT_WEIGHT, "bold" );
        }
        // Font Italic
        if ( isItalic( run, runRprStyle, rprStyle, rprDefault ) )
        {
            XHTMLUtil.addHTMLStyle( htmlStyle, FONT_STYLE, "italic" );
        }

        // Font color
        Color fontColor = getFontColor( run, runRprStyle, rprStyle, rprDefault );
        if ( fontColor != null )
        {
            XHTMLUtil.addHTMLStyle( htmlStyle, COLOR, XWPFUtils.toHexString( fontColor ) );
        }

        UnderlinePatterns underlinePatterns = run.getUnderline();

        switch ( underlinePatterns )
        {
            case SINGLE:
                XHTMLUtil.addHTMLStyle( htmlStyle, TEXT_DECORATION, TEXT_DECORATION_UNDERLINE );
                break;
            default:
                break;
        }

        // Background-color
        Color backgroundColor = XWPFParagraphUtils.getBackgroundColor( run );
        if ( backgroundColor != null )
        {
            XHTMLUtil.addHTMLStyle( htmlStyle, BACKGROUND_COLOR, XWPFUtils.toHexString( backgroundColor ) );

        }
        return htmlStyle;
    }

    // public static ComputedBorder computeBorder(XWPFTable table, XWPFStyle tableStyle,
    // CTDocDefaults defaults) {
    //
    // ComputedBorder computedBorder = new ComputedBorder();
    // CTTblBorders localTblBorders = null;
    // CTTblBorders styleTblBorders = null;
    //
    // if (tableStyle != null) {
    // styleTblBorders = tableStyle.getCTStyle().getTblPr()
    // .getTblBorders();
    // }
    //
    // localTblBorders = table.getCTTbl().getTblPr().getTblBorders();
    // computeBorder(localTblBorders, styleTblBorders, computedBorder);
    // return computedBorder;
    // }

    // private static void computeBorder(CTTblBorders localTblBorders,
    // CTTblBorders styleTblBorders, ComputedBorder computedBorder) {
    // // TODO Auto-generated method stub
    //
    // }

    private static void setBorders( CTTblBorders localTblBorders, CTTblBorders styleTblBorders, StringBuilder htmlStyle )
    {
        setBorder( localTblBorders != null ? localTblBorders.getTop() : null,
                   styleTblBorders != null ? styleTblBorders.getTop() : null, htmlStyle, BorderType.TOP );
    }

    private static void setBorder( CTBorder localBorder, CTBorder styleBorder, StringBuilder htmlStyle,
                                   BorderType borderType )
    {
        boolean noBorder = false;
        float borderSize = -1;
        Color borderColor = null;
        if ( localBorder != null )
        {
            noBorder = ( STBorder.NONE == localBorder.getVal() );
            if ( noBorder )
            {
                XHTMLUtil.addHTMLStyle( htmlStyle, BORDER, "none" );
                return;
            }

            BigInteger size = localBorder.getSz();
            if ( size != null )
            {
                borderSize = dxa2points( size );
            }

            borderColor = XWPFTableUtil.getBorderColor( localBorder );
        }

        if ( styleBorder != null )
        {
            noBorder = ( STBorder.NONE == styleBorder.getVal() );
            if ( noBorder )
            {
                XHTMLUtil.addHTMLStyle( htmlStyle, BORDER, "none" );
                return;
            }

            if ( borderSize == -1 )
            {
                BigInteger size = styleBorder.getSz();
                if ( size != null )
                {
                    borderSize = dxa2points( size );
                }
            }

            if ( borderColor == null )
            {
                borderColor = XWPFTableUtil.getBorderColor( styleBorder );
            }
        }

        if ( borderSize != -1 )
        {
            switch ( borderType )
            {
                case TOP:
                    XHTMLUtil.addHTMLStyle( htmlStyle, "border-top-width", borderSize + "pt" );
                    break;
            }
        }

        if ( borderColor != null )
        {
            switch ( borderType )
            {
                case TOP:
                    XHTMLUtil.addHTMLStyle( htmlStyle, "border-top-color", "#" + XWPFUtils.toHexString( borderColor ) );
                    break;
            }
        }
    }

    public static StringBuilder getStyle( XWPFTableCell tableCell, CTDocDefaults defaults )
    {
        StringBuilder htmlStyle = new StringBuilder();

        CTTcPr tcPr = tableCell.getCTTc().getTcPr();

        // Width
        CTTblWidth tblWidth = tcPr.getTcW();
        if ( tblWidth != null )
        {
            TableWidth tableWidth = XWPFTableUtil.getTableWidth( tableCell );
            boolean percentUnit = tableWidth.percentUnit;
            if ( percentUnit )
            {
                XHTMLUtil.addHTMLStyle( htmlStyle, WIDTH, tableWidth.width + "%" );
            }
            else
            {
                XHTMLUtil.addHTMLStyle( htmlStyle, WIDTH, tableWidth.width + "pt" );
            }
        }

        // Background Color
        CTShd shd = tcPr.getShd();
        Color backgroundColor = XWPFUtils.getFillColor( shd );
        if ( backgroundColor != null )
        {
            XHTMLUtil.addHTMLStyle( htmlStyle, BACKGROUND_COLOR, "#" + backgroundColor );
        }

        return htmlStyle;
    }

    public static CTPPr getPPr( XWPFStyle style )
    {
        if ( style == null )
        {
            return null;
        }

        CTStyle ctStyle = style.getCTStyle();
        if ( ctStyle == null )
        {
            return null;
        }
        return ctStyle.getPPr();
    }

    public static StringBuilder getStyle( CTPicture picture )
    {
        StringBuilder htmlStyle = new StringBuilder();

        // Position
        CTPositiveSize2D ext = picture.getSpPr().getXfrm().getExt();
        long x = ext.getCx();
        long y = ext.getCy();
        float width = dxa2points( x ) / 635;
        float height = dxa2points( y ) / 635;

        XHTMLUtil.addHTMLStyle( htmlStyle, WIDTH, width + "pt" );
        XHTMLUtil.addHTMLStyle( htmlStyle, HEIGHT, height + "pt" );

        return htmlStyle;
    }

}

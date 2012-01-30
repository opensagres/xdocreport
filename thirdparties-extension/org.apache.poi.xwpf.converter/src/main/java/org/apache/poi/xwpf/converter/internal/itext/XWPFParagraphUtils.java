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

import java.math.BigInteger;
import java.util.List;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextAlignment;
import org.w3c.dom.Attr;

import com.lowagie.text.Paragraph;

public class XWPFParagraphUtils
{

    public static void processLayout( XWPFParagraph paragraph, Paragraph pdfParagraph, XWPFStyle style,
                                      CTDocDefaults defaults )
    {

        float indentationLeft = -1;
        float indentationRight = -1;
        float firstLineIndent = -1;
        float spacingBefore = -1;
        float spacingAfter = -1;

        // 1) From style
        CTPPr ppr = getPPr( style );
        if ( ppr != null )
        {

            // Indentation
            CTInd ind = ppr.getInd();
            if ( ind != null )
            {

                // Left Indentation
                BigInteger left = ind.getLeft();
                if ( left != null )
                {
                    indentationLeft = dxa2points( left );
                }

                // Right Indentation
                BigInteger right = ind.getRight();
                if ( right != null )
                {
                    indentationRight = dxa2points( right );
                }

                // First line Indentation
                BigInteger firstLine = ind.getFirstLine();
                if ( firstLine != null )
                {
                    firstLineIndent = dxa2points( firstLine );
                }
            }

            // Spacing
            CTSpacing spacing = ppr.getSpacing();
            if ( spacing != null )
            {

                // Spacing before
                BigInteger before = spacing.getBefore();
                if ( before != null )
                {
                    spacingBefore = dxa2points( before );
                }

                // Spacing after
                BigInteger after = spacing.getAfter();
                if ( after != null )
                {
                    spacingAfter = dxa2points( after );
                }
            }

            // Text aligment
            CTTextAlignment textAligment = ppr.getTextAlignment();
            if ( textAligment != null )
            {
                // TODO
            }

        }

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
            pdfParagraph.setIndentationLeft( indentationLeft );
        }
        if ( indentationRight != -1 )
        {
            pdfParagraph.setIndentationRight( indentationRight );
        }
        if ( firstLineIndent != -1 )
        {
            pdfParagraph.setFirstLineIndent( firstLineIndent );
        }
        if ( spacingBefore != -1 )
        {
            pdfParagraph.setSpacingBefore( spacingBefore );
        }
        if ( spacingAfter != -1 )
        {
            pdfParagraph.setSpacingAfter( spacingAfter );
        }

        // Aligment
        ParagraphAlignment alignment = paragraph.getAlignment();
        switch ( alignment )
        {
            case LEFT:
                pdfParagraph.setAlignment( Paragraph.ALIGN_LEFT );
                break;
            case RIGHT:
                pdfParagraph.setAlignment( Paragraph.ALIGN_RIGHT );
                break;

            case CENTER:
                pdfParagraph.setAlignment( Paragraph.ALIGN_CENTER );
                break;

            case BOTH:
                pdfParagraph.setAlignment( Paragraph.ALIGN_JUSTIFIED );
                break;
        }
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

    public static String getBackgroundColor( XWPFParagraph paragraph )
    {
        List<XWPFRun> runs = paragraph.getRuns();
        if ( runs.isEmpty() )
        {
            return null;
        }
        return getBackgroundColor( runs.get( 0 ) );
    }

    public static String getBackgroundColor( XWPFRun run )
    {
        CTR ctr = run.getCTR();
        if ( ctr == null )
        {
            return null;
        }
        CTRPr ctrPr = ctr.getRPr();
        if ( ctrPr == null )
        {
            return null;
        }
        CTShd ctShd = ctrPr.getShd();
        if ( ctShd == null )
        {
            return null;
        }

        Attr attr =
            (Attr) ctShd.getDomNode().getAttributes().getNamedItemNS( "http://schemas.openxmlformats.org/wordprocessingml/2006/main",
                                                                      "fill" );
        if ( attr != null )
        {
            return attr.getValue();
        }
        return null;
    }

}

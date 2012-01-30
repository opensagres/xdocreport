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
package org.apache.poi.xwpf.converter.internal.itext.stylable;

import static org.apache.poi.xwpf.converter.internal.DxaUtil.dxa2points;

import org.apache.poi.xwpf.converter.internal.itext.XWPFFontRegistry;
import org.apache.poi.xwpf.converter.internal.itext.styles.FontInfos;
import org.apache.poi.xwpf.converter.internal.itext.styles.Style;
import org.apache.poi.xwpf.converter.internal.itext.styles.StyleParagraphProperties;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;

import fr.opensagres.xdocreport.itext.extension.ExtendedParagraph;

//TODO: color, underline, verticalAlignement...
public class StylableParagraph
    extends ExtendedParagraph
    implements IStylableContainer<XWPFParagraph>
{

    private static final long serialVersionUID = 664309269352903329L;

    private final StylableDocument ownerDocument;

    private IStylableContainer<XWPFParagraph> parent;

    private Style lastStyleApplied = null;

    public StylableParagraph( StylableDocument ownerDocument, IStylableContainer<XWPFParagraph> parent )
    {
        super();
        this.ownerDocument = ownerDocument;
        this.parent = parent;
    }

    public StylableParagraph( StylableDocument ownerDocument, Paragraph title, IStylableContainer<XWPFParagraph> parent )
    {
        super( title );
        this.ownerDocument = ownerDocument;
        this.parent = parent;
    }

    public void addElement( Element element )
    {
        super.add( element );
    }

    public void applyStyles( XWPFParagraph p, Style style )
    {

        if ( style != null )
        {
            // first process values from "style"
            // will be overridden by in-line values if available...
            StyleParagraphProperties paragraphProperties = style.getParagraphProperties();

            if ( paragraphProperties != null )
            {
                FontInfos fontInfos = paragraphProperties.getFontInfos();
                if ( fontInfos != null )
                {
                    Font font =
                        XWPFFontRegistry.getRegistry().getFont( fontInfos.getFontFamily(), fontInfos.getFontEncoding(),
                                                                fontInfos.getFontSize(), fontInfos.getFontStyle(),
                                                                fontInfos.getFontColor() );
                    setFont( font );
                }
                // Alignment
                int alignment = paragraphProperties.getAlignment();
                if ( alignment != Element.ALIGN_UNDEFINED )
                {
                    setAlignment( alignment );
                }

                Float lineHeight = paragraphProperties.getLineHeight();
                if ( lineHeight != null )
                {
                    // super.getPdfPCell().setMinimumHeight(lineHeight);
                    // FIXME : Is it correct???
                    setLeading( lineHeight * super.getTotalLeading() );
                }
                // System.err.println("IndentationRight "+paragraphProperties.getIndentationRight());
                setIndentationRight( paragraphProperties.getIndentationRight() );
                setIndentationLeft( paragraphProperties.getIndentationLeft() );
                setFirstLineIndent( paragraphProperties.getIndentationFirstLine() );
                // StyleBorder borderBottom= paragraphProperties.getBorderBottom();

            }

        }

        ParagraphAlignment paragraphAlignment = p.getAlignment();

        // text-align

        if ( paragraphAlignment != null )
        {
            int alignment = Element.ALIGN_UNDEFINED;
            switch ( paragraphAlignment )
            {
                case LEFT:
                    alignment = Element.ALIGN_LEFT;
                    break;
                case RIGHT:
                    alignment = Element.ALIGN_RIGHT;
                    break;
                case CENTER:
                    alignment = Element.ALIGN_CENTER;
                    break;
                case BOTH:
                    alignment = Element.ALIGN_JUSTIFIED;
                    break;
                default:
                    break;
            }

            setAlignment( alignment );
        }

        int indentationLeft = p.getIndentationLeft();
        if ( indentationLeft > 0 )
        {
            setIndentationLeft( indentationLeft );
        }

        // text-indent
        int indentationFirstLine = p.getIndentationFirstLine();
        if ( indentationFirstLine > 0 )
        {
            setFirstLineIndent( indentationFirstLine );
        }

        int indentationRight = p.getIndentationRight();

        if ( indentationRight > 0 )
        {
            setIndentationRight( indentationRight );
        }
        // verticalAlignment DOES not exists in StyleParagraphProperties iText
        // TextAlignment textAlignment = p.getVerticalAlignment();

        int left = p.getIndentationLeft();
        int right = p.getIndentationRight();

        if ( right > 0 )
        {
            setIndentationRight( dxa2points( right ) );
        }

        if ( left > 0 )
        {
            setIndentationLeft( dxa2points( left ) );
        }

        int firstLineIndent = p.getIndentationFirstLine();
        if ( firstLineIndent > 0 )
        {
            setFirstLineIndent( dxa2points( firstLineIndent ) );
        }

        int spacingBefore = p.getSpacingBefore();
        if ( spacingBefore > 0 )
        {
            setSpacingBefore( dxa2points( spacingBefore ) );
        }
        if ( p.getSpacingAfter() >= 0 )
        {
            setSpacingAfter( dxa2points( p.getSpacingAfter() ) );

        }
        else
        {
            // XXX Seems to be a default :
            setSpacingAfter( 10f );
        }

        if ( p.getCTP().getPPr() != null )
        {
            if ( p.getCTP().getPPr().getSpacing() != null )
            {

                if ( p.getCTP().getPPr().getSpacing().getLine() != null )
                {

                    // XXX PLQ : why 240 ???
                    float leading = ( p.getCTP().getPPr().getSpacing().getLine().floatValue() / 240 );
                    setMultipliedLeading( leading );
                }

            }
        }

        ParagraphAlignment alignment = p.getAlignment();
        switch ( alignment )
        {
            case LEFT:
                setAlignment( Element.ALIGN_LEFT );
                break;
            case RIGHT:
                setAlignment( Element.ALIGN_RIGHT );
                break;

            case CENTER:
                setAlignment( Element.ALIGN_CENTER );
                break;

            case BOTH:
                setAlignment( Element.ALIGN_JUSTIFIED );
                break;

            default:
                break;
        }

    }

    // FIXME check with Angelo the purpose of this method....
    public Style getLastStyleApplied()
    {
        return lastStyleApplied;
    }

    // FIXME check with Angelo the purpose of this method....
    public IStylableContainer getParent()
    {
        return parent;
    }

    public StylableDocument getOwnerDocument()
    {
        return ownerDocument;
    }

    public Element getElement()
    {
        return this;// super.getContainer();
    }
}

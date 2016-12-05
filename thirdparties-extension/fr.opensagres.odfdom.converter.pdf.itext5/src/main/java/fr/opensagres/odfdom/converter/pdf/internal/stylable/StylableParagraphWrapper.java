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
package fr.opensagres.odfdom.converter.pdf.internal.stylable;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;

import fr.opensagres.odfdom.converter.pdf.internal.styles.Style;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleBorder;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleParagraphProperties;
import fr.opensagres.xdocreport.itext.extension.ExtendedParagraph;

public class StylableParagraphWrapper
    extends ExtendedParagraph
    implements IStylableContainer
{
    private static final long serialVersionUID = 664309269352903329L;

    private IStylableContainer parent;

    private Style lastStyleApplied = null;

    private boolean joinBorder = true;

    PdfPTable wrapperTable = null;

    public StylableParagraphWrapper( StylableDocument ownerDocument, IStylableContainer parent )
    {
        super();
        this.parent = parent;
    }

    public boolean joinBorder()
    {
        return joinBorder;
    }

    public void addElement( Element element )
    {
        getWrapperCell().addElement( element );
    }

    public void applyStyles( Style style )
    {
        this.lastStyleApplied = style;

        StyleParagraphProperties paragraphProperties = style.getParagraphProperties();
        if ( paragraphProperties != null )
        {
            // margins
            Float margin = paragraphProperties.getMargin();
            if ( margin != null && margin > 0.0f )
            {
                super.setIndentationLeft( margin );
                super.setIndentationRight( margin );
                super.setSpacingBefore( margin );
                super.setSpacingAfter( margin );
            }
            Float marginLeft = paragraphProperties.getMarginLeft();
            if ( marginLeft != null && marginLeft > 0.0f )
            {
                super.setIndentationLeft( marginLeft );
            }
            Float marginRight = paragraphProperties.getMarginRight();
            if ( marginRight != null && marginRight > 0.0f )
            {
                super.setIndentationRight( marginRight );
            }
            Float marginTop = paragraphProperties.getMarginTop();
            if ( marginTop != null && marginTop > 0.0f )
            {
                super.setSpacingBefore( marginTop );
            }
            Float marginBottom = paragraphProperties.getMarginBottom();
            if ( marginBottom != null && marginBottom > 0.0f )
            {
                super.setSpacingAfter( marginBottom );
            }

            // background color
            BaseColor backgroundColor = paragraphProperties.getBackgroundColor();
            if ( backgroundColor != null && !TRANSPARENT_COLOR.equals( backgroundColor ) )
            {
                getWrapperCell().setBackgroundColor( backgroundColor );
            }

            // borders
            StyleBorder border = paragraphProperties.getBorder();
            if ( border != null && !border.isNoBorder() )
            {
                StyleUtils.applyStyles( border, getWrapperCell() );
            }
            StyleBorder borderLeft = paragraphProperties.getBorderLeft();
            if ( borderLeft != null && !borderLeft.isNoBorder() )
            {
                StyleUtils.applyStyles( borderLeft, getWrapperCell() );
            }
            StyleBorder borderRight = paragraphProperties.getBorderRight();
            if ( borderRight != null && !borderRight.isNoBorder() )
            {
                StyleUtils.applyStyles( borderRight, getWrapperCell() );
            }
            StyleBorder borderTop = paragraphProperties.getBorderTop();
            if ( borderTop != null && !borderTop.isNoBorder() )
            {
                StyleUtils.applyStyles( borderTop, getWrapperCell() );
            }
            StyleBorder borderBottom = paragraphProperties.getBorderBottom();
            if ( borderBottom != null && !borderBottom.isNoBorder() )
            {
                StyleUtils.applyStyles( borderBottom, getWrapperCell() );
            }

            // padding
            Float padding = paragraphProperties.getPadding();
            if ( padding != null && padding > 0.0f )
            {
                if ( getWrapperCell().hasBorder( Rectangle.LEFT ) )
                {
                    getWrapperCell().setPaddingLeft( padding );
                }
                if ( getWrapperCell().hasBorder( Rectangle.RIGHT ) )
                {
                    getWrapperCell().setPaddingRight( padding );
                }
                if ( getWrapperCell().hasBorder( Rectangle.TOP ) )
                {
                    getWrapperCell().setPaddingTop( padding );
                }
                if ( getWrapperCell().hasBorder( Rectangle.BOTTOM ) )
                {
                    getWrapperCell().setPaddingBottom( padding );
                }
            }
            Float paddingLeft = paragraphProperties.getPaddingLeft();
            if ( paddingLeft != null && paddingLeft > 0.0f )
            {
                if ( getWrapperCell().hasBorder( Rectangle.LEFT ) )
                {
                    getWrapperCell().setPaddingLeft( paddingLeft );
                }
            }
            Float paddingRight = paragraphProperties.getPaddingRight();
            if ( paddingRight != null && paddingRight > 0.0f )
            {
                if ( getWrapperCell().hasBorder( Rectangle.RIGHT ) )
                {
                    getWrapperCell().setPaddingRight( paddingRight );
                }
            }
            Float paddingTop = paragraphProperties.getPaddingTop();
            if ( paddingTop != null && paddingTop > 0.0f )
            {
                if ( getWrapperCell().hasBorder( Rectangle.TOP ) )
                {
                    getWrapperCell().setPaddingTop( paddingTop );
                }
            }
            Float paddingBottom = paragraphProperties.getPaddingBottom();
            if ( paddingBottom != null && paddingBottom > 0.0f )
            {
                if ( getWrapperCell().hasBorder( Rectangle.BOTTOM ) )
                {
                    getWrapperCell().setPaddingBottom( paddingBottom );
                }
            }

            // join border
            Boolean joinBorder = paragraphProperties.getJoinBorder();
            if ( joinBorder != null )
            {
                this.joinBorder = joinBorder;
            }

            // keep together on the same page
            Boolean keepTogether = paragraphProperties.getKeepTogether();
            if ( keepTogether != null )
            {
                super.setKeepTogether( keepTogether );
            }
        }
    }

    public Style getLastStyleApplied()
    {
        return lastStyleApplied;
    }

    public IStylableContainer getParent()
    {
        return parent;
    }

    public Element getElement()
    {
        if ( wrapperTable == null )
        {
            wrapperTable = createWrapperTable( getWrapperCell(), false );
        }
        return wrapperTable;
    }
}

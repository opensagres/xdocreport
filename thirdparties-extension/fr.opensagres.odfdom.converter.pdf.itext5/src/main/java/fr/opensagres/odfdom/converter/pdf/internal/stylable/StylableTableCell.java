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
import com.itextpdf.text.pdf.PdfPCell;

import fr.opensagres.odfdom.converter.pdf.internal.styles.Style;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleBorder;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTableCellProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTableRowProperties;

/**
 * fixes for pdf conversion by Leszek Piotrowicz <leszekp@safe-mail.net>
 */
public class StylableTableCell
    extends PdfPCell
    implements IStylableContainer, IBoundsLimitContainer
{
    private final StylableDocument ownerDocument;

    private final IStylableContainer parent;

    private Style lastStyleApplied = null;

    public StylableTableCell( StylableDocument ownerDocument, IStylableContainer parent )
    {
        this.ownerDocument = ownerDocument;
        this.parent = parent;
        // make iText first line alignment compatible with open office
        getColumn().setAdjustFirstLine( false );
        // set no border as default
        setBorder( NO_BORDER );
        // make a room for borders
        setUseBorderPadding( true );
        // set no padding as default
        setPadding( 0.0f );
    }

    public void addElement( Element element )
    {
        super.addElement( element );
    }

    public void applyStyles( Style style )
    {
        this.lastStyleApplied = style;

        StyleTableRowProperties tableRowProperties = style.getTableRowProperties();
        if ( tableRowProperties != null )
        {
            Float minRowHeight = tableRowProperties.getMinRowHeight();
            if ( minRowHeight != null )
            {
                super.setMinimumHeight( minRowHeight );
            }

            Float rowHeight = tableRowProperties.getRowHeight();
            if ( rowHeight != null )
            {
                super.setFixedHeight( rowHeight );
            }
        }
        StyleTableCellProperties tableCellProperties = style.getTableCellProperties();
        if ( tableCellProperties != null )
        {
            // background-color
        	BaseColor backgroundColor = tableCellProperties.getBackgroundColor();
            if ( backgroundColor != null )
            {
                super.setBackgroundColor( backgroundColor );
            }

            // border
            StyleBorder border = tableCellProperties.getBorder();
            StyleUtils.applyStyles( border, this );

            // border-top
            StyleBorder borderTop = tableCellProperties.getBorderTop();
            StyleUtils.applyStyles( borderTop, this );

            // border-bottom
            StyleBorder borderBottom = tableCellProperties.getBorderBottom();
            StyleUtils.applyStyles( borderBottom, this );

            // border-left
            StyleBorder borderLeft = tableCellProperties.getBorderLeft();
            StyleUtils.applyStyles( borderLeft, this );

            // border-right
            StyleBorder borderRight = tableCellProperties.getBorderRight();
            StyleUtils.applyStyles( borderRight, this );

            // padding
            Float padding = tableCellProperties.getPadding();
            if ( padding != null )
            {
                super.setPadding( padding );
            }

            // padding-top
            Float paddingTop = tableCellProperties.getPaddingTop();
            if ( paddingTop != null )
            {
                super.setPaddingTop( paddingTop );
            }

            // padding-bottom
            Float paddingBottom = tableCellProperties.getPaddingBottom();
            if ( paddingBottom != null )
            {
                super.setPaddingBottom( paddingBottom );
            }

            // padding-left
            Float paddingLeft = tableCellProperties.getPaddingLeft();
            if ( paddingLeft != null )
            {
                super.setPaddingLeft( paddingLeft );
            }

            // padding-right
            Float paddingRight = tableCellProperties.getPaddingRight();
            if ( paddingRight != null )
            {
                super.setPaddingRight( paddingRight );
            }

            // vertical-alignment
            int verticalAlignment = tableCellProperties.getVerticalAlignment();
            if ( verticalAlignment != Element.ALIGN_UNDEFINED )
            {
                super.setVerticalAlignment( verticalAlignment );
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
        return this;
    }

    public float getWidthLimit()
    {
        float width = 0.0f;
        if ( parent != null && parent instanceof StylableTable )
        {
            StylableTable table = (StylableTable) parent;
            // we have to determine this cell index in parent table
            // we assume that this cell is not added yet
            // and it will be added at parent table current column index
            int colIdx = table.getColIdx();
            float[] widths = table.getAbsoluteWidths();
            for ( int i = 0; i < getColspan(); i++ )
            {
                width += widths[colIdx + i];
            }
        }
        return width > 0.0f ? width : ownerDocument.getPageWidth();
    }

    public float getHeightLimit()
    {
        // no height limit
        return -1.0f;
    }
}

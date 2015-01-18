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
package fr.opensagres.xdocreport.itext.extension;

import java.awt.Color;

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class ExtendedParagraph
    extends Paragraph
    implements IITextContainer
{
    private static final long serialVersionUID = 664309269352903329L;

    public static final Color TRANSPARENT_COLOR = Color.WHITE;

    private IITextContainer container;

    protected PdfPCell wrapperCell;

    protected PdfPTable wrapperTable;

    public ExtendedParagraph()
    {
    }

    public ExtendedParagraph( Paragraph paragraph )
    {
        super( paragraph );
    }

    public void addElement( Element element )
    {
        // in function add(Element element) chunks are cloned
        // it is not correct for chunks with dynamic content (ie page number)
        // use function add(int index, Element element) because in this function chunks are added without cloning
        super.add( size(), element );
    }

    public IITextContainer getITextContainer()
    {
        return container;
    }

    public void setITextContainer( IITextContainer container )
    {
        this.container = container;
    }

    private PdfPCell createCell()
    {
        PdfPCell cell = new PdfPCell();
        cell.setBorder( Table.NO_BORDER );
        cell.setPadding( 0.0f );
        cell.setUseBorderPadding( true );
        cell.getColumn().setAdjustFirstLine( false );
        return cell;
    }

    private PdfPTable createTable( PdfPCell cell )
    {
        PdfPTable table = new PdfPTable( 1 );
        table.setWidthPercentage( 100.0f );
        table.setSplitLate( false );
        table.addCell( cell );
        return table;
    }

    protected PdfPCell getWrapperCell()
    {
        if ( wrapperCell == null )
        {
            wrapperCell = createCell();
        }
        return wrapperCell;
    }

    protected PdfPTable createWrapperTable( PdfPCell wrapperCell, boolean addParagraph )
    {
        PdfPTable wrapperTable = null;
        // wrap this paragraph into a table if necessary
        if ( wrapperCell != null )
        {
            // background color or borders were set
            if ( addParagraph )
            {
                wrapperCell.addElement( this );
            }
            wrapperTable = createTable( wrapperCell );
            if ( getSpacingBefore() > 0.0f || getSpacingAfter() > 0.0f )
            {
                // top or bottom margin was set, promote it to enclosing table
                wrapperTable.setSpacingBefore( getSpacingBefore() );
                setSpacingBefore( 0.0f );
                wrapperTable.setSpacingAfter( getSpacingAfter() );
                setSpacingAfter( 0.0f );
            }
            if ( getIndentationLeft() > 0.0f || getIndentationRight() > 0.0f )
            {
                // left or right margin was set, have to wrap the cell again
                PdfPCell outerCell = createCell();
                outerCell.setPaddingLeft( getIndentationLeft() );
                setIndentationLeft( 0.0f );
                outerCell.setPaddingRight( getIndentationRight() );
                setIndentationRight( 0.0f );
                outerCell.addElement( wrapperTable );
                wrapperTable = createTable( outerCell );
            }
        }
        return wrapperTable;
    }

    public Element getElement()
    {
        if ( wrapperCell != null )
        {
            if ( wrapperTable == null )
            {
                wrapperTable = createWrapperTable( wrapperCell, true );
            }
        }
        return wrapperTable != null ? wrapperTable : this;
    }

    public boolean hasBorders()
    {
        return wrapperCell != null && wrapperCell.hasBorders();
    }

    public boolean hasBackgroundColor()
    {
        return wrapperCell != null && wrapperCell.getBackgroundColor() != null
            && !TRANSPARENT_COLOR.equals( wrapperCell.getBackgroundColor() );
    }

    public void setSpacingBefore( Paragraph paragraph )
    {
        setSpacingBefore( paragraph.getSpacingBefore() );
    }

    public void setSpacingAfter( Paragraph paragraph )
    {
        setSpacingAfter( paragraph.getSpacingAfter() );
    }

    public void setIndentation( Paragraph paragraph )
    {
        setIndentationLeft( paragraph.getIndentationLeft() );
        setIndentationRight( paragraph.getIndentationRight() );
    }

    public void setBackgroundColor( Color backgroundColor )
    {
        getWrapperCell().setBackgroundColor( backgroundColor );
    }

    /**
     * Sets the width of the Top border.
     *
     * @param borderWidthTop a width
     */
    public void setBorderWidthTop( float borderWidthTop )
    {
        getWrapperCell().setBorderWidthTop( borderWidthTop );
    }

    /**
     * Sets the color of the top border.
     *
     * @param borderColorTop a <CODE>Color</CODE>
     */
    public void setBorderColorTop( Color borderColorTop )
    {
        getWrapperCell().setBorderColorTop( borderColorTop );
    }

    /**
     * Sets the width of the bottom border.
     *
     * @param borderWidthBottom a width
     */
    public void setBorderWidthBottom( float borderWidthBottom )
    {
        getWrapperCell().setBorderWidthBottom( borderWidthBottom );
    }

    /**
     * Sets the color of the Bottom border.
     *
     * @param borderColorBottom a <CODE>Color</CODE>
     */
    public void setBorderColorBottom( Color borderColorBottom )
    {
        getWrapperCell().setBorderColorBottom( borderColorBottom );
    }

    /**
     * Sets the width of the Left border.
     *
     * @param borderWidthLeft a width
     */
    public void setBorderWidthLeft( float borderWidthLeft )
    {
        getWrapperCell().setBorderWidthLeft( borderWidthLeft );
    }

    /**
     * Sets the color of the Left border.
     *
     * @param borderColorLeft a <CODE>Color</CODE>
     */
    public void setBorderColorLeft( Color borderColorLeft )
    {
        getWrapperCell().setBorderColorLeft( borderColorLeft );
    }

    /**
     * Sets the width of the Right border.
     *
     * @param borderWidthRight a width
     */
    public void setBorderWidthRight( float borderWidthRight )
    {
        getWrapperCell().setBorderWidthRight( borderWidthRight );
    }

    /**
     * Sets the color of the Right border.
     *
     * @param borderColorRight a <CODE>Color</CODE>
     */
    public void setBorderColorRight( Color borderColorRight )
    {
        getWrapperCell().setBorderColorRight( borderColorRight );
    }

    /**
     * Sets the padding of the contents in the cell (space between content and border).
     *
     * @param padding
     */
    public void setBorderPadding( float padding )
    {
        getWrapperCell().setPadding( padding );
    }

    /**
     * Setter for property paddingLeft.
     *
     * @param paddingLeft New value of property paddingLeft.
     */
    public void setBorderPaddingLeft( float paddingLeft )
    {
        getWrapperCell().setPaddingLeft( paddingLeft );
    }

    /**
     * Setter for property paddingRight.
     *
     * @param paddingRight New value of property paddingRight.
     */
    public void setBorderPaddingRight( float paddingRight )
    {
        getWrapperCell().setPaddingRight( paddingRight );
    }

    /**
     * Setter for property paddingBottom.
     *
     * @param paddingBottom New value of property paddingBottom.
     */
    public void setBorderPaddingBottom( float paddingBottom )
    {
        getWrapperCell().setPaddingBottom( paddingBottom );
    }

    /**
     * Setter for property paddingTop.
     *
     * @param paddingTop New value of property paddingTop.
     */
    public void setBorderPaddingTop( float paddingTop )
    {
        getWrapperCell().setPaddingTop( paddingTop );
    }

}

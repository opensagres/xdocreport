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
package fr.opensagres.xdocreport.openpdf.extension;

import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPRow;
import com.lowagie.text.pdf.PdfPTable;

public class ExtendedPdfPTable
    extends PdfPTable
    implements IITextContainer
{
    private IITextContainer container;

    private boolean empty;

    private PdfPCell wrapperCell;

    private PdfPTable wrapperTable;

    public ExtendedPdfPTable( int numColumns )
    {
        super( numColumns );
        super.setSpacingBefore( 0f );
        this.empty = true;
        super.setLockedWidth( true );
        super.setHorizontalAlignment( Element.ALIGN_LEFT );
    }

    public void addElement( Element element )
    {
        if ( element instanceof PdfPCell )
        {
            addCell( (PdfPCell) element );
        }
    }

    public IITextContainer getITextContainer()
    {
        return container;
    }

    public void setITextContainer( IITextContainer container )
    {
        this.container = container;
    }

    public float getRowHeight( int idx )
    {
        return getRowHeight( idx, false );
    }

    /**
     * Gets the height of a particular row.
     *
     * @param idx the row index (starts at 0)
     * @param firsttime is this the first time the row heigh is calculated?
     * @return the height of a particular row
     * @since 3.0.0
     */
    public float getRowHeight( int idx, boolean firsttime )
    {
        if ( totalWidth <= 0 || idx < 0 || idx >= rows.size() )
            return 0;
        PdfPRow row = (PdfPRow) rows.get( idx );
        if ( row == null )
            return 0;
        if ( firsttime )
            row.setWidths( absoluteWidths );
        float height = row.getMaxHeights();
        PdfPCell cell;
        PdfPRow tmprow;
        for ( int i = 0; i < relativeWidths.length; i++ )
        {
            if ( !rowSpanAbove( idx, i ) )
                continue;
            int rs = 1;
            while ( rowSpanAbove( idx - rs, i ) )
            {
                rs++;
            }
            tmprow = (PdfPRow) rows.get( idx - rs );
            cell = tmprow.getCells()[i];
            float tmp = 0;
            // AZERR patch : sometimes cell is null????
            // LP : cell may be null if colspan/rowspan occurs
            // create a dummy cell to avoid NullPointerException
            if ( cell == null )
            {
                cell = new PdfPCell();
                tmprow.getCells()[i] = cell;
            }
            if ( cell.getRowspan() == rs + 1 )
            {
                tmp = cell.getMaxHeight();
                while ( rs > 0 )
                {
                    tmp -= getRowHeight( idx - rs );
                    rs--;
                }
            }
            if ( tmp > height )
                height = tmp;
        }
        row.setMaxHeights( height );
        return height;
    }

    /**
     * Checks if there are rows above belonging to a rowspan.
     *
     * @param currRow the current row to check
     * @param currCol the current column to check
     * @return true if there's a cell above that belongs to a rowspan
     * @since 2.1.6
     */
    @SuppressWarnings( "all" )
    boolean rowSpanAbove( int currRow, int currCol )
    {
        if ( ( currCol >= getNumberOfColumns() ) || ( currCol < 0 ) || ( currRow == 0 ) )
            return false;

        int row = currRow - 1;
        PdfPRow aboveRow = (PdfPRow) rows.get( row );
        if ( aboveRow == null )
            return false;
        PdfPCell aboveCell = (PdfPCell) aboveRow.getCells()[currCol];
        while ( ( aboveCell == null ) && ( row > 0 ) )
        {
            aboveRow = (PdfPRow) rows.get( --row );
            if ( aboveRow == null )
                return false;
            aboveCell = (PdfPCell) aboveRow.getCells()[currCol];
        }

        int distance = currRow - row;

        if ( aboveCell == null )
        {
            int col = currCol - 1;
            aboveCell = (PdfPCell) aboveRow.getCells()[col];
            while ( ( aboveCell == null ) && ( row > 0 ) )
                aboveCell = (PdfPCell) aboveRow.getCells()[--col];
            return aboveCell != null && aboveCell.getRowspan() > distance;
        }

        if ( ( aboveCell.getRowspan() == 1 ) && ( distance > 1 ) )
        {
            int col = currCol - 1;
            aboveRow = (PdfPRow) rows.get( row + 1 );
            distance--;
            aboveCell = (PdfPCell) aboveRow.getCells()[col];
            while ( ( aboveCell == null ) && ( col > 0 ) )
                aboveCell = (PdfPCell) aboveRow.getCells()[--col];
        }

        return aboveCell != null && aboveCell.getRowspan() > distance;
    }

    @Override
    public PdfPCell addCell( Image image )
    {
        this.empty = false;
        return super.addCell( image );
    }

    @Override
    public PdfPCell addCell( PdfPCell cell )
    {
        this.empty = false;
        return super.addCell( cell );
    }

    @Override
    public PdfPCell addCell( PdfPTable table )
    {
        this.empty = false;
        return super.addCell( table );
    }

    @Override
    public PdfPCell addCell( Phrase phrase )
    {
        this.empty = false;
        return super.addCell( phrase );
    }

    @Override
    public PdfPCell addCell( String text )
    {
        this.empty = false;
        return super.addCell( text );
    }

    public boolean isEmpty()
    {
        return empty;
    }

    /**
     * Sets the padding of the contents in the cell (space between content and border).
     *
     * @param padding
     */
    public void setPadding( float padding )
    {
        getWrapperCell().setPadding( padding );
    }

    /**
     * Setter for property paddingLeft.
     *
     * @param paddingLeft New value of property paddingLeft.
     */
    public void setPaddingLeft( float paddingLeft )
    {
        getWrapperCell().setPaddingLeft( paddingLeft );
    }

    /**
     * Setter for property paddingRight.
     *
     * @param paddingRight New value of property paddingRight.
     */
    public void setPaddingRight( float paddingRight )
    {
        getWrapperCell().setPaddingRight( paddingRight );
    }

    /**
     * Setter for property paddingBottom.
     *
     * @param paddingBottom New value of property paddingBottom.
     */
    public void setPaddingBottom( float paddingBottom )
    {
        getWrapperCell().setPaddingBottom( paddingBottom );
    }

    /**
     * Setter for property paddingTop.
     *
     * @param paddingTop New value of property paddingTop.
     */
    public void setPaddingTop( float paddingTop )
    {
        getWrapperCell().setPaddingTop( paddingTop );
    }

    private PdfPCell createCell()
    {
        PdfPCell cell = new PdfPCell();
        cell.setBorder( Table.NO_BORDER );
        cell.setPadding( 0.0f );
        cell.setUseBorderPadding( true );
        return cell;
    }

    private PdfPTable createTable( PdfPCell cell )
    {
        PdfPTable table = new PdfPTable( 1 );
        table.setSpacingBefore( this.spacingBefore() );
        table.setSpacingAfter( this.spacingAfter() );
        table.setHorizontalAlignment( this.getHorizontalAlignment() );
        table.setTotalWidth( cell.getPaddingLeft() + this.getTotalWidth() + cell.getPaddingRight() );
        table.setLockedWidth( true );
        table.setSplitLate( false );
        table.addCell( cell );
        return table;
    }

    private PdfPCell getWrapperCell()
    {
        if ( wrapperCell == null )
        {
            wrapperCell = createCell();
        }
        return wrapperCell;
    }

    public Element getElement()
    {
        if ( wrapperTable != null )
        {
            return wrapperTable;
        }
        else if ( wrapperCell != null )
        {
            wrapperCell.addElement( this );
            wrapperTable = createTable( wrapperCell );
            return wrapperTable;
        }
        return this;
    }
}

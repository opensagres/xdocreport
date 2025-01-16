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

import com.lowagie.text.Element;

import com.lowagie.text.pdf.PdfPCell;
import fr.opensagres.odfdom.converter.pdf.internal.styles.Style;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTableProperties;
import fr.opensagres.odfdom.converter.pdf.internal.styles.StyleTableRowProperties;
import fr.opensagres.xdocreport.openpdf.extension.ExtendedPdfPTable;

import java.util.Arrays;
import java.util.stream.IntStream;

public class StylableTable
    extends ExtendedPdfPTable
    implements IStylableContainer
{
    private final StylableDocument ownerDocument;

    private IStylableContainer parent;

    private Style lastStyleApplied = null;

    private boolean inTableHeaderRows;

    private boolean inTableRow;

    // keeps an indication of how many rows are already occupied (by row- or col-spans from the rows above)
    private int[] spansFromAbove;
    private int spansFromAboveIdx;

    private Style currentRowStyle;

    public StylableTable( StylableDocument ownerDocument, IStylableContainer parent, int numColumns )
    {
        super( numColumns );
        // cancel ExtendedPdfPTable settings
        // we raise text in StylableParagraph so extra spacing here is unnecessary
        super.setSpacingBefore( 0.0f );
        // default is split rows early
        super.setSplitLate( false );
        this.ownerDocument = ownerDocument;
        this.parent = parent;

        this.spansFromAbove = new int[numColumns];
        Arrays.fill(spansFromAbove, 0);
        this.spansFromAboveIdx = 0;
    }

    public Style getCurrentRowStyle()
    {
        return currentRowStyle;
    }

    public void beginTableHeaderRows()
    {
        inTableHeaderRows = true;
    }

    public void endTableHeaderRows()
    {
        inTableHeaderRows = false;
    }

    private void fitCellIntoSpansFromAbove(PdfPCell cell) {
        int rowSpan = cell.getRowspan();
        int colSpan = cell.getColspan();

        // skip all the cell places already spanned from above
        do {
            if (spansFromAboveIdx < getNumberOfColumns() && spansFromAbove[spansFromAboveIdx] > 0) {
                spansFromAbove[spansFromAboveIdx]--;
                spansFromAboveIdx++;
            }
            if (spansFromAboveIdx >= getNumberOfColumns()) {
                // we have no more place in the current row; finish it up, and start a new one
                endTableRow();
                beginTableRow(currentRowStyle);
            }
        }
        while (spansFromAbove[spansFromAboveIdx] > 0);

        for (int col = 0; col < colSpan; col++){
            if (spansFromAbove[spansFromAboveIdx] > 0){
                break; // this is an error situation. cell spans over a cell that already has been spanned from above
            }
            else {
                spansFromAbove[spansFromAboveIdx] = rowSpan - 1;
            }
            spansFromAboveIdx++;
        }
    }

    private boolean reachedEndOfRow(){
        return spansFromAboveIdx == getNumberOfColumns()
            // we might reach end of row by not yet reaching getNumberOfColumns() with spansFromAboveIdx, but in that
            // case all the remaining columns in the row have to be spanned from above
            ||  IntStream.range(spansFromAboveIdx, getNumberOfColumns()).noneMatch(idx -> spansFromAbove[idx] == 0);
    }

    public void beginTableRow( Style currentRowStyle )
    {
        // beginTableRow/addElement/endTableRow protects before too many/too less cells in a row than declared
        inTableRow = true;
        spansFromAboveIdx = 0;
        this.currentRowStyle = currentRowStyle;
        if ( inTableHeaderRows )
        {
            // increment header rows count
            setHeaderRows( getHeaderRows() + 1 );
        }
    }

    public void endTableRow()
    {
        // fill row with empty cells if necessary
        while ( ! reachedEndOfRow()  )
        {
            StylableTableCell cell = new StylableTableCell( ownerDocument, this );
            if ( currentRowStyle != null )
            {
                cell.applyStyles( currentRowStyle );
            }
            addElement( cell );
        }
        inTableRow = false;
        currentRowStyle = null;
    }

    @Override
    public void addElement( Element element )
    {
        if ( inTableRow )
        {
            if (element instanceof PdfPCell){
                fitCellIntoSpansFromAbove ((PdfPCell) element);
            }
            super.addElement( element );
        }
        if ( reachedEndOfRow() )
        {
            // row fully filled, end row
            endTableRow();
        }
    }

    public void applyStyles( Style style )
    {
        this.lastStyleApplied = style;

        StyleTableProperties tableProperties = style.getTableProperties();
        if ( tableProperties != null )
        {
            // width
            if ( tableProperties.getWidth() != null )
            {
                super.setTotalWidth( tableProperties.getWidth() );
            }

            // alignment
            int alignment = tableProperties.getAlignment();
            if ( alignment != Element.ALIGN_UNDEFINED )
            {
                super.setHorizontalAlignment( alignment );
            }

            // margins
            Float margin = tableProperties.getMargin();
            if ( margin != null && margin > 0.0f )
            {
                super.setPadding( margin );
            }
            Float marginLeft = tableProperties.getMarginLeft();
            if ( marginLeft != null && marginLeft > 0.0f )
            {
                super.setPaddingLeft( marginLeft );
            }
            Float marginRight = tableProperties.getMarginRight();
            if ( marginRight != null && marginRight > 0.0f )
            {
                super.setPaddingRight( marginRight );
            }
            Float marginTop = tableProperties.getMarginTop();
            if ( marginTop != null && marginTop > 0.0f )
            {
                super.setPaddingTop( marginTop );
            }
            Float marginBottom = tableProperties.getMarginBottom();
            if ( marginBottom != null && marginBottom > 0.0f )
            {
                super.setPaddingBottom( marginBottom );
            }

            // table splitting
            Boolean mayBreakBetweenRows = tableProperties.getMayBreakBetweenRows();
            if ( mayBreakBetweenRows != null )
            {
                super.setKeepTogether( !mayBreakBetweenRows );
            }
        }
        StyleTableRowProperties tableRowProperties = style.getTableRowProperties();
        if ( tableRowProperties != null )
        {
            // keep together
            Boolean keepTogether = tableRowProperties.getKeepTogether();
            if ( keepTogether != null )
            {
                // keep together is table row property in open office
                // but it is table property in iText
                // so we set keep together = true if any of table rows has this property set to true
                if ( super.isSplitLate() == false && keepTogether == true )
                {
                    super.setSplitLate( true );
                }
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

    public StylableDocument getOwnerDocument()
    {
        return ownerDocument;
    }

    public int getColIdx()
    {
        return currentRowIdx;
    }
}

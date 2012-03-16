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
package org.odftoolkit.odfdom.converter.internal.itext.stylable;

import java.util.ArrayList;
import java.util.List;

import org.odftoolkit.odfdom.converter.ODFConverterException;
import org.odftoolkit.odfdom.converter.internal.itext.styles.Style;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleColumnProperties;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleColumnsProperties;
import org.odftoolkit.odfdom.converter.internal.itext.styles.StyleSectionProperties;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import fr.opensagres.xdocreport.itext.extension.IITextContainer;

//
// this amazing and awesome algorithm
// which lay out content in columns and do content balancing
// was written by Leszek Piotrowicz <leszekp@safe-mail.net>
//
public class StylableDocumentSection
    implements IStylableContainer
{
    private final StylableDocument ownerDocument;

    private IStylableContainer parent;

    private IStylableContainer ultimateParent;

    private boolean inHeaderFooter;

    private Style lastStyleApplied = null;

    private PdfPTable layoutTable;

    private boolean balanceText = true;

    private List<ColumnText> texts;

    private int colIdx;

    public StylableDocumentSection( StylableDocument ownerDocument, IStylableContainer parent, boolean inHeaderFooter )
    {
        super();
        this.ownerDocument = ownerDocument;
        this.parent = parent;
        this.inHeaderFooter = inHeaderFooter;
        // find ultimate parent which may be
        // StylableDocument or StylableHeaderFooter table cell
        if ( inHeaderFooter )
        {
            this.ultimateParent = parent;
            while ( this.ultimateParent != null )
            {
                this.ultimateParent = this.ultimateParent.getParent();
            }
        }
        else
        {
            this.ultimateParent = ownerDocument;
        }
    }

    public void applyStyles( Style style )
    {
        this.lastStyleApplied = style;

        if ( !inHeaderFooter )
        {
            // flush parent section to update vertical position on page
            StylableDocumentSection parentSection = getStylableDocumentSection( parent );
            if ( parentSection != null )
            {
                parentSection.flushTable();
            }
        }

        StyleSectionProperties sectionProperties = style.getSectionProperties();
        if ( sectionProperties != null )
        {
            Boolean dontBalanceTextColumns = sectionProperties.getDontBalanceTextColumns();
            if ( Boolean.TRUE.equals( dontBalanceTextColumns ) )
            {
                balanceText = false;
            }
        }

        // initialize layout table
        float width = getUltimateParentAvailableWidth();
        float height = getUltimateParentParentAvailableHeight();
        layoutTable = createLayoutTable( width, height, style );

        // initialize simulated text
        texts = new ArrayList<ColumnText>();
        setColIdx( 0 );
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
        // force calculate height because it may be zero
        // and nothing will be flushed
        layoutTable.calculateHeights( true );
        return layoutTable;
    }

    public IITextContainer getITextContainer()
    {
        return null;
    }

    public void setITextContainer( IITextContainer container )
    {
    }

    public void addElement( Element element )
    {
        if ( element instanceof SectionPdfPTable )
        {
            // section is a specific container
            // it is not added to direct parent container
            // but to ultimate parent container which may be
            // StylableDocument or StylableHeaderFooter table cell
            ultimateParent.addElement( element );
        }
        else
        {
            // ordinary element
            texts.get( colIdx ).addElement( element );
            balanceText();
        }
    }

    public void newColumn()
    {
        if ( inHeaderFooter )
        {
            // may result in more column breaks than available columns
            // but we cannot break to a new page in header/footer
            // don't know what to do in such situation
            // anyway it is logical error made by document creator
            setColIdx( colIdx + 1 );
        }
        else
        {
            if ( colIdx + 1 < layoutTable.getNumberOfColumns() )
            {
                setColIdx( colIdx + 1 );
            }
            else
            {
                newPage();
            }
        }
    }

    public void newPage()
    {
        if ( inHeaderFooter )
        {
            // should not happen because
            // page break is forbidden in header/footer
        }
        else
        {
            // cancel balancing if page break
            fitsHeight( getUltimateParentParentAvailableHeight() );
            // flush
            flushTable();
            ownerDocument.newColumn();
        }
    }

    public void flushTable()
    {
        // force calculate height because it may be zero
        // and nothing will be flushed
        layoutTable.calculateHeights( true );
        ultimateParent.addElement( layoutTable );

        // initialize layout table
        layoutTable = cloneAndClearTable( layoutTable );

        // initialize simulated text
        texts = new ArrayList<ColumnText>();
        setColIdx( 0 );
    }

    private void balanceText()
    {
        float minHeight = 0f;
        float maxHeight = getUltimateParentParentAvailableHeight();
        //
        List<ColumnText> splittedTexts = fitsHeight( maxHeight );
        if ( splittedTexts == null )
        {
            // fits in current column on page
            if ( balanceText )
            {
                float currHeight = 0f;
                while ( Math.abs( maxHeight - minHeight ) > 0.1f )
                {
                    currHeight = ( minHeight + maxHeight ) / 2;
                    if ( fitsHeight( currHeight ) == null )
                    {
                        // try to lower height
                        maxHeight = currHeight;
                    }
                    else
                    {
                        // have to raise height
                        minHeight = currHeight;
                    }
                }
                // populate table with last height
                if ( currHeight != maxHeight )
                {
                    fitsHeight( maxHeight );
                }
            }
        }
        else
        {
            // overflow
            if ( !inHeaderFooter )
            {
                flushTable();
                ownerDocument.newColumn();
                //
                texts = splittedTexts;
                colIdx = texts.size() - 1;
                balanceText();
            }
        }
    }

    private List<ColumnText> fitsHeight( float height )
    {
        // copy text for simulation
        List<ColumnText> tt = null;
        if ( inHeaderFooter && colIdx >= layoutTable.getNumberOfColumns() )
        {
            // more column breaks than available column
            // we try not to lose content
            // but results may be different than in open office
            // anyway it is logical error made by document creator
            tt = new ArrayList<ColumnText>();
            ColumnText t = createColumnText();
            tt.add( t );
            for ( int i = 0; i < texts.size(); i++ )
            {
                PdfPTable table = new PdfPTable( 1 );
                table.setWidthPercentage( 100.0f );
                PdfPCell cell = new PdfPCell();
                cell.setBorder( Table.NO_BORDER );
                cell.setPadding( 0.0f );
                cell.setColumn( ColumnText.duplicate( texts.get( i ) ) );
                table.addCell( cell );
                t.addElement( table );
            }
        }
        else
        {
            tt = new ArrayList<ColumnText>( texts );
            for ( int i = 0; i < tt.size(); i++ )
            {
                tt.set( i, ColumnText.duplicate( tt.get( i ) ) );
            }
        }
        // clear layout table
        clearTable( layoutTable );
        setWidthIfNecessary();

        // try to fill cells with text
        ColumnText t = tt.get( 0 );
        for ( PdfPCell cell : layoutTable.getRow( 0 ).getCells() )
        {
            cell.setFixedHeight( height );
            cell.setColumn( ColumnText.duplicate( t ) );
            //
            t.setSimpleColumn( cell.getLeft() + cell.getPaddingLeft(), -height,
                               cell.getRight() - cell.getPaddingRight(), 0 );
            int res = 0;
            try
            {
                res = t.go( true );
            }
            catch ( DocumentException e )
            {
                throw new ODFConverterException( e );
            }
            if ( !ColumnText.hasMoreText( res ) )
            {
                // no overflow in current column
                if ( tt.size() == 1 )
                {
                    // no more text
                    return null;
                }
                else
                {
                    // some text waiting for new column
                    tt.remove( 0 );
                    t = tt.get( 0 );
                }
            }
        }
        return tt;
    }

    private void setColIdx( int idx )
    {
        colIdx = idx;
        for ( int i = texts.size(); i <= idx; i++ )
        {
            ColumnText text = createColumnText();
            texts.add( text );
        }
    }

    private void setWidthIfNecessary()
    {
        if ( layoutTable.getTotalWidth() != getUltimateParentAvailableWidth() )
        {
            layoutTable.setTotalWidth( getUltimateParentAvailableWidth() );
        }
    }

    private float getUltimateParentAvailableWidth()
    {
        return inHeaderFooter ? ownerDocument.getPageWidth() : ownerDocument.getCurrentColumnWidth();
    }

    private float getUltimateParentParentAvailableHeight()
    {
        return inHeaderFooter ? ownerDocument.getAdjustedPageHeight() : ownerDocument.getCurrentColumnAvailableHeight();
    }

    //
    // static helper functions
    //

    public static StylableDocumentSection getStylableDocumentSection( IStylableContainer c )
    {
        while ( c != null )
        {
            if ( c instanceof StylableDocumentSection )
            {
                return (StylableDocumentSection) c;
            }
            c = c.getParent();
        }
        return null;
    }

    public static SectionPdfPTable createLayoutTable( float width, float height, Style style )
    {
        // create one row table which will layout section text
        if ( style != null )
        {
            List<StyleColumnProperties> columnPropertiesList = style.getColumnPropertiesList();
            StyleColumnsProperties columnsProperties = style.getColumnsProperties();
            if ( columnPropertiesList != null && !columnPropertiesList.isEmpty() )
            {
                // explicit column list
                return createLayoutTable( width, height, columnPropertiesList );
            }
            else if ( columnsProperties != null )
            {
                // we have columns properties
                // make table with columns of equal width
                columnPropertiesList = new ArrayList<StyleColumnProperties>();

                Integer columnCount = columnsProperties.getColumnCount();
                int colCount = columnCount != null ? columnCount : 1;

                Float columnGap = columnsProperties.getColumnGap();
                float halfGap = columnGap != null ? columnGap / 2.0f : 0.0f;

                for ( int i = 0; i < colCount; i++ )
                {
                    StyleColumnProperties columnProperties = new StyleColumnProperties();
                    columnProperties.setRelWidth( 100 );
                    if ( halfGap > 0.0f )
                    {
                        columnProperties.setStartIndent( i == 0 ? null : halfGap );
                        columnProperties.setEndIndent( i == colCount - 1 ? null : halfGap );
                    }
                    columnPropertiesList.add( columnProperties );
                }
                return createLayoutTable( width, height, columnPropertiesList );
            }
        }
        // default is one column and no gap
        List<StyleColumnProperties> columnPropertiesList = new ArrayList<StyleColumnProperties>();
        StyleColumnProperties columnProperties = new StyleColumnProperties();
        columnProperties.setRelWidth( 100 );
        columnPropertiesList.add( columnProperties );
        return createLayoutTable( width, height, columnPropertiesList );
    }

    public static SectionPdfPTable createLayoutTable( float width, float height,
                                                      List<StyleColumnProperties> columnPropertiesList )
    {
        // create one row table which will layout section text
        int colCount = columnPropertiesList.size();
        int relativeWidths[] = new int[colCount];
        SectionPdfPTable table = new SectionPdfPTable( colCount );
        // add cells
        for ( int i = 0; i < colCount; i++ )
        {
            PdfPCell cell = new PdfPCell();
            cell.setBorder( Table.NO_BORDER );
            cell.setPadding( 0.0f );
            cell.setColumn( createColumnText() );
            cell.setFixedHeight( height );
            // apply styles to cell
            StyleColumnProperties columnProperties = columnPropertiesList.get( i );
            relativeWidths[i] = columnProperties.getRelWidth();
            cell.setPaddingLeft( columnProperties.getStartIndent() != null ? columnProperties.getStartIndent() : 0.0f );
            cell.setPaddingRight( columnProperties.getEndIndent() != null ? columnProperties.getEndIndent() : 0.0f );
            table.addCell( cell );
        }
        replaceTableCells( table );
        // set width
        try
        {
            table.setWidths( relativeWidths );
        }
        catch ( DocumentException e )
        {
            throw new ODFConverterException( e );
        }
        table.setTotalWidth( width );
        table.setLockedWidth( true );
        return table;
    }

    public static ColumnText createColumnText()
    {
        ColumnText text = new ColumnText( null );
        // make iText first line alignment compatible with open office
        text.setAdjustFirstLine( false );
        return text;
    }

    public static void replaceTableCells( PdfPTable table )
    {
        // replace default table cells
        // by fixed height cells
        PdfPCell[] cells = table.getRow( 0 ).getCells();
        for ( int i = 0; i < cells.length; i++ )
        {
            cells[i] = new FixedHeightPdfPCell( cells[i] );
        }
    }

    public static PdfPCell getCell( PdfPTable table, int idx )
    {
        return table.getRow( 0 ).getCells()[idx];
    }

    public static SectionPdfPTable cloneAndClearTable( PdfPTable table )
    {
        SectionPdfPTable clonedTable = new SectionPdfPTable( table );
        clearTable( clonedTable );
        replaceTableCells( clonedTable );
        return clonedTable;
    }

    public static void clearTable( PdfPTable table )
    {
        for ( PdfPCell cell : table.getRow( 0 ).getCells() )
        {
            cell.setColumn( createColumnText() );
        }
    }

    public static class SectionPdfPTable
        extends PdfPTable
    {
        public SectionPdfPTable( int numColumns )
        {
            super( numColumns );
        }

        public SectionPdfPTable( PdfPTable table )
        {
            super( table );
        }
    }

    public static class FixedHeightPdfPCell
        extends PdfPCell
    {

        public FixedHeightPdfPCell( PdfPCell cell )
        {
            super( cell );
        }

        @Override
        public float getMaxHeight()
        {
            // sometimes max height of a table cell is higher than desired height
            // such a situation occurs if paragraphs have space before or after
            // it is hard to say if it is a bug or a feature in iText
            // we want fixed height to avoid breaking table to a new page
            return getFixedHeight() != 0.0f ? getFixedHeight() : super.getMaxHeight();
        }
    }
}

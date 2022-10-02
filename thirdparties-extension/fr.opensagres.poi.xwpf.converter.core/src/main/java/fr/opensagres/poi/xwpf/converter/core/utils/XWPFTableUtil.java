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
package fr.opensagres.poi.xwpf.converter.core.utils;

import static fr.opensagres.poi.xwpf.converter.core.utils.DxaUtil.dxa2points;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblLook;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import fr.opensagres.poi.xwpf.converter.core.Color;
import fr.opensagres.poi.xwpf.converter.core.TableCellBorder;
import fr.opensagres.poi.xwpf.converter.core.TableWidth;

public class XWPFTableUtil
{

    private static final String MAIN_NAMESPACE = "http://schemas.openxmlformats.org/wordprocessingml/2006/main";

    // Hexa Bitmask for tblLook.

    public static final int DEFAULT_TBLLOOK = 0x0000;

    public static final int APPLY_FIRST_ROW_CONDITIONNAL_FORMATTING = 0x0020; // Apply first row conditional formatting

    public static final int APPLY_LAST_ROW_CONDITIONNAL_FORMATTING = 0x0040; // Apply last row conditional formatting

    public static final int APPLY_FIRST_COLUMN_CONDITIONNAL_FORMATTING = 0x0080; // Apply first column conditional
                                                                                 // formatting

    public static final int APPLY_LAST_COLUMN_CONDITIONNAL_FORMATTING = 0x0100; // Apply last column conditional
                                                                                // formatting

    public static final int DO_NOT_APPLY_ROW_BANDING_CONDITIONNAL_FORMATTING = 0x0200; // Do not apply row banding
                                                                                       // conditional formatting

    public static final int DO_NOT_APPLY_COLUMN_BANDING_CONDITIONNAL_FORMATTING = 0x0400; // Do not apply column banding
                                                                                          // conditional formatting

    public static float[] computeColWidths( CTTbl table )
    {
        CTTblGrid grid = table.getTblGrid();
        List<CTTblGridCol> cols = getGridColList( grid );
        int nbColumns = cols.size();
        float[] colWidths = new float[nbColumns];
        int nbColumnsToIgnoreBefore = 0;
        for ( int i = nbColumnsToIgnoreBefore; i < colWidths.length; i++ )
        {
            CTTblGridCol tblGridCol = cols.get( i );
            colWidths[i] = dxa2points( tblGridCol.xgetW() );
        }
        return colWidths;
    }

    /**
     * Compute column widths of the XWPF table.
     * 
     * @param table
     * @return
     */
    public static float[] computeColWidths( XWPFTable table )
    {

        XWPFTableRow firstRow = getFirstRow( table );
        float[] colWidths;
        // Get first row to know if there is cell which have gridSpan to compute
        // columns number.
        int nbCols = getNumberOfColumns( firstRow );

        // Compare nbCols computed with number of grid colList
        CTTblGrid grid = table.getCTTbl().getTblGrid();
        List<CTTblGridCol> cols = getGridColList( grid );
        if ( nbCols > cols.size() )
        {
            Collection<Float> maxColWidths = null;
            Collection<Float> currentColWidths = null;

            // nbCols computed is not equals to number of grid colList
            // columns width must be computed by looping for each row/cells
            List<XWPFTableRow> rows = table.getRows();
            for ( XWPFTableRow row : rows )
            {
                currentColWidths = computeColWidths( row );
                if ( maxColWidths == null )
                {
                    maxColWidths = currentColWidths;
                }
                else
                {
                    if ( currentColWidths.size() > maxColWidths.size() )
                    {
                        maxColWidths = currentColWidths;
                    }
                }
            }

            colWidths = new float[maxColWidths.size()];
            int i = 0;
            for ( Float colWidth : maxColWidths )
            {
                colWidths[i++] = colWidth;
            }
            return colWidths;

        }
        else
        {
            // If w:gridAfter is defined, ignore the last columns defined on the gridColumn
            int nbColumnsToIgnoreBefore = getNbColumnsToIgnore( firstRow, true );
            int nbColumnsToIgnoreAfter = getNbColumnsToIgnore( firstRow, false );
            int nbColumns = cols.size() - nbColumnsToIgnoreBefore - nbColumnsToIgnoreAfter;

            // nbCols computed is equals to number of grid colList
            // columns width can be computed by using the grid colList
            colWidths = new float[nbColumns];
            for ( int i = nbColumnsToIgnoreBefore; i < colWidths.length; i++ )
            {
                CTTblGridCol tblGridCol = cols.get( i );
                colWidths[i] = dxa2points( tblGridCol.xgetW() );
            }
        }
        return colWidths;
    }

    /**
     * <w:gridCol list should be filtered to ignore negative value.
     * <p>
     * Ex : <w:gridCol w:w="-54" /> should be ignored. See https://code.google.com/p/xdocreport/issues/detail?id=315
     * </p>
     * 
     * @param grid
     * @return
     */
    private static List<CTTblGridCol> getGridColList( CTTblGrid grid )
    {
        List<CTTblGridCol> newCols = new ArrayList<CTTblGridCol>();
        List<CTTblGridCol> cols = grid.getGridColList();
        for ( CTTblGridCol col : cols )
        {
            if ( XWPFUtils.floatValue(col.xgetW()) >= 0 )
            {
                newCols.add( col );
            }
        }
        return newCols;
    }

    private static int getNbColumnsToIgnore( XWPFTableRow row, boolean before )
    {
        CTTrPr trPr = row.getCtRow().getTrPr();
        if ( trPr == null )
        {
            return 0;
        }

        List<CTDecimalNumber> gridBeforeAfters = before ? trPr.getGridBeforeList() : trPr.getGridAfterList();
        if ( gridBeforeAfters == null || gridBeforeAfters.size() < 1 )
        {
            return 0;
        }
        int nbColumns = 0;
        BigInteger val = null;
        for ( CTDecimalNumber gridBeforeAfter : gridBeforeAfters )
        {
            val = gridBeforeAfter.getVal();
            if ( val != null )
            {
                nbColumns += val.intValue();
            }
        }

        return nbColumns;
    }

    /**
     * Returns number of column if the XWPF table by using the declared cell (which can declare gridSpan) from the first
     * row.
     * 
     * @param table
     * @return
     */
    public static int getNumberOfColumns( XWPFTableRow row )
    {
        if ( row == null )
        {
            return 0;
        }
        // Get first row to know if there is cell which have gridSpan to compute
        // columns number.
        int nbCols = 0;
        List<XWPFTableCell> tableCellsOffFirstRow = row.getTableCells();
        for ( XWPFTableCell tableCellOffFirstRow : tableCellsOffFirstRow )
        {
            CTDecimalNumber gridSpan = getGridSpan( tableCellOffFirstRow );
            if ( gridSpan != null )
            {
                nbCols += gridSpan.getVal().intValue();
            }
            else
            {
                nbCols += 1;
            }
        }
        return nbCols;
    }

    public static XWPFTableRow getFirstRow( XWPFTable table )
    {
        int numberOfRows = table.getNumberOfRows();
        if ( numberOfRows > 0 )
        {
            return table.getRow( 0 );
        }
        return null;
    }

    public static CTDecimalNumber getGridSpan( XWPFTableCell cell )
    {
        if ( cell.getCTTc().getTcPr() != null )
            return cell.getCTTc().getTcPr().getGridSpan();

        return null;
    }

    public static CTTblWidth getWidth( XWPFTableCell cell )
    {
        CTTcPr tcPr = cell.getCTTc().getTcPr();
        if (tcPr == null) {
            return null;
        }
        return tcPr.getTcW();
    }

    private static Collection<Float> computeColWidths( XWPFTableRow row )
    {
        List<Float> colWidths = new ArrayList<Float>();
        List<XWPFTableCell> cells = row.getTableCells();
        for ( XWPFTableCell cell : cells )
        {

            // Width
            CTTblWidth width = getWidth( cell );
            if ( width != null )
            {
                int nb = 1;
                CTDecimalNumber gridSpan = getGridSpan( cell );
                TableWidth tableCellWidth = getTableWidth( cell );
                if ( gridSpan != null )
                {
                    nb = gridSpan.getVal().intValue();
                }
                for ( int i = 0; i < nb; i++ )
                {
                    colWidths.add( tableCellWidth.width / nb );
                }
            }
        }
        return colWidths;
    }

    /**
     * Returns table width of teh XWPF table.
     * 
     * @param table
     * @return
     */
    public static TableWidth getTableWidth( XWPFTable table )
    {
        float width = 0;
        boolean percentUnit = false;
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        if ( tblPr.isSetTblW() )
        {
            CTTblWidth tblWidth = tblPr.getTblW();
            return getTableWidth( tblWidth );
        }
        return new TableWidth( width, percentUnit );
    }

    public static TableWidth getTableWidth( XWPFTableCell cell )
    {
        float width = 0;
        boolean percentUnit = false;
        CTTcPr tcPr = cell.getCTTc().getTcPr();
        if ( tcPr != null && tcPr.isSetTcW() )
        {
            CTTblWidth tblWidth = tcPr.getTcW();
            return getTableWidth( tblWidth );
        }
        return new TableWidth( width, percentUnit );
    }

    public static TableWidth getTableWidth( CTTblWidth tblWidth )
    {
        if ( tblWidth == null )
        {
            return null;
        }
        Float width = getTblWidthW( tblWidth );
        if ( width == null )
        {
            return null;
        }
        boolean percentUnit = ( STTblWidth.INT_PCT == tblWidth.getType().intValue() );
        if ( percentUnit )
        {
            width = width / 100f;
        }
        else
        {
            width = dxa2points( width );
        }
        return new TableWidth( width, percentUnit );
    }

    /**
     * Returns the float value of <w:tblW w:w="9288.0" w:type="dxa" />
     * 
     * @param tblWidth
     * @return
     */
    public static Float getTblWidthW( CTTblWidth tblWidth )
    {
        try
        {
            return XWPFUtils.floatValue(tblWidth.xgetW());
        }
        catch ( Throwable e )
        {
            // Sometimes w:w is a float value.Ex : <w:tblW w:w="9288.0" w:type="dxa" />
            // see https://code.google.com/p/xdocreport/issues/detail?id=315
            Attr attr =
                (Attr) tblWidth.getDomNode().getAttributes().getNamedItemNS( "http://schemas.openxmlformats.org/wordprocessingml/2006/main",
                                                                             "w" );
            if ( attr != null )
            {
                return Float.valueOf( attr.getValue() );
            }
        }
        return null;
    }

    public static CTTblPr getTblPr( XWPFTable table )
    {
        CTTbl tbl = table.getCTTbl();
        if ( tbl != null )
        {
            return tbl.getTblPr();
        }
        return null;
    }

    public static CTTblBorders getTblBorders( XWPFTable table )
    {
        CTTblPr tblPr = getTblPr( table );
        if ( tblPr != null )
        {
            return tblPr.getTblBorders();
        }
        return null;
    }

    public static CTTblCellMar getTblCellMar( XWPFTable table )
    {
        CTTblPr tblPr = getTblPr( table );
        if ( tblPr != null )
        {
            return tblPr.getTblCellMar();
        }
        return null;
    }

    public static TableCellBorder getTableCellBorder( CTBorder border, boolean fromTableCell )
    {
        if ( border != null )
        {
            if ( STBorder.NONE == border.getVal() && fromTableCell ) {
                return null;
            }
            boolean noBorder = ( STBorder.NIL == border.getVal() );
            if ( noBorder )
            {
                return new TableCellBorder( false, fromTableCell );
            }
            Float borderSize = null;
            BigInteger size = border.getSz();
            if ( size != null )
            {
                // http://officeopenxml.com/WPtableBorders.php
                // if w:sz="4" => 1/4 points
                borderSize = size.floatValue() / 8f;
            }
            else
            {
                // if no border size is set, use 1/4 pt
                borderSize = .25f;
            }
            Color borderColor = ColorHelper.getBorderColor( border );
            return new TableCellBorder( borderSize, borderColor, fromTableCell );
        }
        return null;
    }

    public static Color getBorderColor( CTBorder border )
    {
        if ( border == null )
        {
            return null;
        }
        // border.getColor returns object???, use attribute w:color to get
        // the color.
        Node colorAttr = border.getDomNode().getAttributes().getNamedItemNS( MAIN_NAMESPACE, "color" );
        if ( colorAttr != null )
        {
            Object val = border.getVal();
            return ColorHelper.getColor( ( (Attr) colorAttr ).getValue(), val, false );
        }
        return null;
    }

    public static CTTblLook getTblLook( XWPFTable table )
    {
        CTTblPr tblPr = getTblPr( table );
        if ( tblPr != null )
        {
            return tblPr.getTblLook();
        }
        return null;

    }

    public static int getTblLookVal( XWPFTable table )
    {
        int tblLook = DEFAULT_TBLLOOK;
        CTTblLook hexNumber = getTblLook( table );
        if ( hexNumber != null && !hexNumber.isNil() )
        {
            // CTShortHexNumber#getVal() returns byte[] and not byte, use attr value ???
            Attr attr = (Attr) hexNumber.getDomNode().getAttributes().getNamedItemNS( MAIN_NAMESPACE, "val" );
            if ( attr != null )
            {
                String value = attr.getValue();
                try
                {
                    tblLook = Integer.parseInt( value, 16 );
                }
                catch ( Throwable e )
                {
                    e.printStackTrace();
                }
            }
        }
        return tblLook;
    }

    public static boolean canApplyFirstRow( int tblLookVal )
    {
        int mask = APPLY_FIRST_ROW_CONDITIONNAL_FORMATTING;
        return ( tblLookVal & mask ) == mask;
    }

    public static boolean canApplyLastRow( int tblLookVal )
    {
        int mask = APPLY_LAST_ROW_CONDITIONNAL_FORMATTING;
        return ( tblLookVal & mask ) == mask;
    }

    public static boolean canApplyFirstCol( int tblLookVal )
    {
        int mask = APPLY_FIRST_COLUMN_CONDITIONNAL_FORMATTING;
        return ( tblLookVal & mask ) == mask;
    }

    public static boolean canApplyLastCol( int tblLookVal )
    {
        int mask = APPLY_LAST_COLUMN_CONDITIONNAL_FORMATTING;
        return ( tblLookVal & mask ) == mask;
    }

}

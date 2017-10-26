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
package fr.opensagres.poi.xwpf.converter.core.styles;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;

import fr.opensagres.poi.xwpf.converter.core.utils.XWPFTableUtil;

public class TableInfo
{

    private final XWPFTable table;

    private final XWPFStylesDocument stylesDocument;

    private final Map<XWPFTableCell, TableCellInfo> cellInfos;

    private Integer nbColumns;

    private final boolean canApplyFirstRow;

    private final boolean canApplyLastRow;

    private boolean canApplyFirstCol;

    private final boolean canApplyLastCol;

    public TableInfo( XWPFTable table, XWPFStylesDocument stylesDocument )
    {
        this.table = table;
        this.cellInfos = new HashMap<XWPFTableCell, TableCellInfo>();
        this.stylesDocument = stylesDocument;
        this.nbColumns = null;

        // Compute value apply lastRow etc from the tblLook
        int tblLookVal = XWPFTableUtil.getTblLookVal( table );
        this.canApplyFirstRow = XWPFTableUtil.canApplyFirstRow( tblLookVal );
        this.canApplyLastRow = XWPFTableUtil.canApplyLastRow( tblLookVal );
        this.canApplyFirstCol = XWPFTableUtil.canApplyFirstCol( tblLookVal );
        this.canApplyLastCol = XWPFTableUtil.canApplyLastCol( tblLookVal );

    }

    public TableCellInfo getCellInfo( XWPFTableCell cell )
    {
        TableCellInfo cellInfo = cellInfos.get( cell );
        if ( cellInfo != null )
        {
            return cellInfo;
        }
        // Compute it
        computeCellInfos( cell.getTableRow() );
        return cellInfos.get( cell );

    }

    private void computeCellInfos( XWPFTableRow row )
    {
        if ( nbColumns == null )
        {
            nbColumns = XWPFTableUtil.computeColWidths( row.getTable() ).length;
        }

        int rowIndex = table.getRows().indexOf( row );
        boolean firstRow = rowIndex == 0;
        boolean lastRow = rowIndex == ( table.getRows().size() - 1 );

        boolean firstCol = true;
        boolean lastCol = false;
        int cellIndex = 0;
        CTRow ctRow = row.getCtRow();
        XmlCursor c = ctRow.newCursor();
        c.selectPath( "./*" );
        while ( c.toNextSelection() )
        {
            XmlObject o = c.getObject();
            if ( o instanceof CTTc )
            {
                CTTc tc = (CTTc) o;
                XWPFTableCell cell = row.getTableCell( tc );
                cellIndex = getCellIndex( cellIndex, cell );
                lastCol = ( cellIndex == nbColumns - 1 );
                addCellInfo( cell, firstRow, lastRow, firstCol, lastCol );
                firstCol = false;
            }
            else if ( o instanceof CTSdtCell )
            {
                // Fix bug of POI
                CTSdtCell sdtCell = (CTSdtCell) o;
                List<CTTc> tcList = sdtCell.getSdtContent().getTcList();
                for ( CTTc ctTc : tcList )
                {
                    XWPFTableCell cell = new XWPFTableCell( ctTc, row, row.getTable().getBody() );
                    cellIndex = getCellIndex( cellIndex, cell );
                    lastCol = ( cellIndex == nbColumns - 1 );
                    addCellInfo( row.getTableCell( ctTc ), firstRow, lastRow, firstCol, lastCol );
                    firstCol = false;
                }
            }
        }
        c.dispose();
    }

    public void addCellInfo( XWPFTableCell cell, boolean firstRow, boolean lastRow, boolean firstCol, boolean lastCol )
    {
        TableCellInfo cellInfo = new TableCellInfo( this, firstRow, lastRow, firstCol, lastCol );
        cellInfos.put( cell, cellInfo );
    }

    private int getCellIndex( int cellIndex, XWPFTableCell cell )
    {
        BigInteger gridSpan = stylesDocument.getTableCellGridSpan( cell.getCTTc().getTcPr() );
        if ( gridSpan != null )
        {
            cellIndex = cellIndex + gridSpan.intValue();
        }
        else
        {
            cellIndex++;
        }
        return cellIndex;
    }

    public boolean canApplyFirstRow()
    {
        return canApplyFirstRow;
    }

    public boolean canApplyLastRow()
    {
        return canApplyLastRow;
    }

    public boolean canApplyFirstCol()
    {
        return canApplyFirstCol;
    }

    public boolean canApplyLastCol()
    {
        return canApplyLastCol;
    }
}

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

import org.apache.poi.xwpf.usermodel.XWPFTableCell;

import fr.opensagres.poi.xwpf.converter.core.BorderSide;

public class TableCellInfo
{

    private final boolean firstRow;

    private final boolean lastRow;

    private final boolean firstCol;

    private final boolean lastCol;

    private final TableInfo tableInfo;

    public TableCellInfo( TableInfo tableInfo, boolean firstRow, boolean lastRow, boolean firstCol, boolean lastCol )
    {
        this.tableInfo = tableInfo;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.firstCol = firstCol;
        this.lastCol = lastCol;
    }

    public boolean isFirstRow()
    {
        return firstRow;
    }

    public boolean isLastRow()
    {
        return lastRow;
    }

    public boolean isFirstCol()
    {
        return firstCol;
    }

    public boolean isLastCol()
    {
        return lastCol;
    }

    public boolean canApplyFirstRow()
    {
        return isFirstRow() && tableInfo.canApplyFirstRow();
    }

    public boolean canApplyLastRow()
    {
        return isLastRow() && tableInfo.canApplyLastRow();
    }

    public boolean canApplyFirstCol()
    {
        return isFirstCol() && tableInfo.canApplyFirstCol();
    }

    public boolean canApplyLastCol()
    {
        return isLastCol() && tableInfo.canApplyLastCol();
    }

    public boolean isInside( BorderSide borderSide )
    {
        return isInside( firstRow, lastRow, firstCol, lastCol, borderSide );
    }

    private boolean isInside( boolean firstRow, boolean lastRow, boolean firstCol, boolean lastCol,
                              BorderSide borderSide )
    {
        switch ( borderSide )
        {
            case TOP:
                return !firstRow;
            case BOTTOM:
                return !lastRow;
            case LEFT:
                return !firstCol;
            case RIGHT:
                return !lastCol;
        }
        return false;
    }

    public void getTableCellBorder( XWPFTableCell cell, BorderSide borderSide )
    {
        // TODO Auto-generated method stub
        
    }
}

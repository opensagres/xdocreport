package org.apache.poi.xwpf.converter.core.styles;

import org.apache.poi.xwpf.converter.core.BorderSide;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;

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

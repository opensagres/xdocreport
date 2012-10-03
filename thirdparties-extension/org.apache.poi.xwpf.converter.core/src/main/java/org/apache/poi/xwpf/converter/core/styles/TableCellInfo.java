package org.apache.poi.xwpf.converter.core.styles;

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
}

package org.apache.poi.xwpf.converter.core.styles.table.row;

import java.util.List;

import org.apache.poi.xwpf.converter.core.utils.XWPFUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;

public class TableRowHeaderValueProvider
    extends AbstractTableRowValueProvider<Boolean>
{
    public static final TableRowHeaderValueProvider INSTANCE = new TableRowHeaderValueProvider();

    @Override
    public Boolean getValue( CTTrPr trPr )
    {
        if (trPr == null) {
            return false;
        }
        List<CTOnOff> headers = trPr.getTblHeaderList();
        if ( headers != null && headers.size() > 0 )
        {
            return XWPFUtils.isCTOnOff( headers.get( 0 ) );
        }
        return false;
    }
}

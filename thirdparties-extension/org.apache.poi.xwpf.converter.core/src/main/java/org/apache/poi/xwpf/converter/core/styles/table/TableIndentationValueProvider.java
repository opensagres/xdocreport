package org.apache.poi.xwpf.converter.core.styles.table;

import java.math.BigInteger;

import org.apache.poi.xwpf.converter.core.utils.DxaUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;

public class TableIndentationValueProvider
    extends AbstractTableValueProvider<Float>
{

    public static final TableIndentationValueProvider INSTANCE = new TableIndentationValueProvider();

    @Override
    public Float getValue( CTTblPr tblPr )
    {
        return getTableAlignment( tblPr );
    }

    @Override
    public Float getValue( CTTblPrBase tblPr )
    {
        return getTableAlignment( tblPr );
    }

    private Float getTableAlignment( CTTblPrBase tblPr )
    {
        if ( tblPr == null )
        {
            return null;
        }
        CTTblWidth tblWidth = tblPr.getTblInd();
        if ( tblWidth != null )
        {
            // TODO manage percent
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth.Enum type = tblWidth.getType();
            if ( type != null )
            {

            }
            BigInteger width = tblWidth.getW();
            if ( width != null )
            {
                return DxaUtil.dxa2points( width );
            }
        }
        return null;
    }

}

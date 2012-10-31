package org.apache.poi.xwpf.converter.core.styles.table.row;

import org.apache.poi.xwpf.converter.core.utils.DxaUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrEx;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;

public abstract class AbstractTablelRowMarginValueProvider
    extends AbstractTableRowExValueProvider<Float>
{

    @Override
    public Float getValue( CTTblPrEx tblPrEx )
    {
        return getMarginValue( tblPrEx );
    }

    private Float getMarginValue( CTTblPrEx tblPrEx )
    {
        if ( tblPrEx == null )
        {
            return null;
        }
        CTTblCellMar margin = tblPrEx.getTblCellMar();
        if ( margin != null )
        {
            CTTblWidth tblWidth = getValue( margin );
            if ( tblWidth == null )
            {
                return null;
            }
            return DxaUtil.dxa2points( tblWidth.getW() );
        }
        return null;
    }

    public abstract CTTblWidth getValue( CTTblCellMar margin );

}

package org.apache.poi.xwpf.converter.core.styles.table;

import org.apache.poi.xwpf.converter.core.utils.DxaUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblCellMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;

public abstract class AbstractTablelMarginValueProvider
    extends AbstractTableValueProvider<Float>
{

    @Override
    public Float getValue( CTTblPr tblPr )
    {
        return getMarginValue( tblPr );
    }

    private Float getMarginValue( CTTblPrBase tblPr )
    {
        if ( tblPr == null )
        {
            return null;
        }
        CTTblCellMar margin = tblPr.getTblCellMar();
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

    @Override
    public Float getValue( CTTblPrBase tblPr )
    {
        return getMarginValue( tblPr );
    }

    public abstract CTTblWidth getValue( CTTblCellMar margin );

}

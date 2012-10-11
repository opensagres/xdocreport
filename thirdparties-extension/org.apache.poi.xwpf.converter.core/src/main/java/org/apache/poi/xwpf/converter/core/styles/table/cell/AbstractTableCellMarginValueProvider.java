package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.apache.poi.xwpf.converter.core.utils.DxaUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

public abstract class AbstractTableCellMarginValueProvider
    extends AbstractTableCellValueProvider<Float>
{

    @Override
    public Float getValue( CTTcPr tcPr )
    {
        if ( tcPr == null )
        {
            return null;
        }
        CTTcMar margin = tcPr.getTcMar();
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

    public abstract CTTblWidth getValue( CTTcMar margin );

}

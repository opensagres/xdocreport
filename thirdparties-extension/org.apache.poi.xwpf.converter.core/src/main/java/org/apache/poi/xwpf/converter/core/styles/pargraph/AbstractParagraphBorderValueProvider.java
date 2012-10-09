package org.apache.poi.xwpf.converter.core.styles.pargraph;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;

public abstract class AbstractParagraphBorderValueProvider
    extends AbstractParagraphValueProvider<CTBorder>
{

    @Override
    public CTBorder getValue( CTPPr ppr )
    {
        if ( ppr == null )
        {
            return null;
        }
        CTPBdr border = ppr.getPBdr();
        if ( border != null )
        {
            return getBorder( border );
        }
        return null;
    }

    protected abstract CTBorder getBorder( CTPBdr border );
}

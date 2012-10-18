package org.apache.poi.xwpf.converter.core.styles.run;

import org.apache.poi.xwpf.converter.core.utils.XWPFUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

public class RunFontStyleStrikeValueProvider
    extends AbstractRunValueProvider<Boolean>
{

    public static RunFontStyleStrikeValueProvider INSTANCE = new RunFontStyleStrikeValueProvider();

    @Override
    public Boolean getValue( CTRPr rpr )
    {
        return isStrike( rpr );
    }

    private static Boolean isStrike( CTRPr pr )
    {
        if ( pr == null || !pr.isSetStrike() )
        {
            return null;
        }
        return XWPFUtils.isCTOnOff( pr.getStrike() );
    }

}

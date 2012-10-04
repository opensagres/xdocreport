package org.apache.poi.xwpf.converter.core.styles.run;

import org.apache.poi.xwpf.converter.core.utils.XWPFUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

public class RunFontStyleBoldValueProvider
    extends AbstractRunValueProvider<Boolean>
{

    public static RunFontStyleBoldValueProvider INSTANCE = new RunFontStyleBoldValueProvider();

    @Override
    public Boolean getValue( CTRPr rpr )
    {
        return isBold( rpr );
    }

    private static Boolean isBold( CTRPr pr )
    {
        if ( pr == null || !pr.isSetB() )
        {
            return null;
        }
        return XWPFUtils.isCTOnOff( pr.getB() );
    }

}

package org.apache.poi.xwpf.converter.styles.run;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;

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
        return isCTOnOff( pr.getB() );
    }

    /**
     * For isBold, isItalic etc
     */
    private static boolean isCTOnOff( CTOnOff onoff )
    {
        if ( !onoff.isSetVal() )
            return true;
        if ( onoff.getVal() == STOnOff.ON )
            return true;
        if ( onoff.getVal() == STOnOff.TRUE )
            return true;
        return false;
    }

}

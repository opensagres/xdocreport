package org.apache.poi.xwpf.converter.styles.run;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;

public class RunFontStyleItalicValueProvider
    extends AbstractRunValueProvider<Boolean>
{

    public static RunFontStyleItalicValueProvider INSTANCE = new RunFontStyleItalicValueProvider();

    @Override
    public Boolean getValue( CTRPr rpr )
    {
        return isItalic( rpr );
    }

    private static Boolean isItalic( CTRPr pr )
    {
        if ( pr == null || !pr.isSetI() )
        {
            return null;
        }
        return isCTOnOff( pr.getI() );
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

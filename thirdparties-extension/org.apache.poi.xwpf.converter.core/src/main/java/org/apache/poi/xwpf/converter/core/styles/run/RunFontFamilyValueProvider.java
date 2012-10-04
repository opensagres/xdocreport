package org.apache.poi.xwpf.converter.core.styles.run;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

public class RunFontFamilyValueProvider
    extends AbstractRunValueProvider<String>
{

    public static RunFontFamilyValueProvider INSTANCE = new RunFontFamilyValueProvider();

    @Override
    public String getValue( CTRPr ppr )
    {
        if ( ppr == null )
        {
            return null;
        }
        CTFonts fonts = ppr.getRFonts();
        if ( fonts != null )
        {
            return fonts.getAscii();
        }
        return null;
    }

}

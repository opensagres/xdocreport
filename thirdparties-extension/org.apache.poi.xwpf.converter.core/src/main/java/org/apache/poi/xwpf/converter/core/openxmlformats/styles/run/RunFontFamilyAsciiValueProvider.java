package org.apache.poi.xwpf.converter.core.openxmlformats.styles.run;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme;

public class RunFontFamilyAsciiValueProvider
    extends RunFontFamilyValueProvider
{

    public static RunFontFamilyAsciiValueProvider INSTANCE = new RunFontFamilyAsciiValueProvider();

    @Override
    protected STTheme.Enum getTheme( CTFonts fonts )
    {
        return fonts.getAsciiTheme();
    }

    @Override
    protected String getFamily( CTFonts fonts )
    {
        return fonts.getAscii();
    }

}

package org.apache.poi.xwpf.converter.core.openxmlformats.styles.run;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme;

public class RunFontFamilyHAnsiValueProvider
    extends RunFontFamilyValueProvider
{

    public static RunFontFamilyHAnsiValueProvider INSTANCE = new RunFontFamilyHAnsiValueProvider();

    @Override
    protected STTheme.Enum getTheme( CTFonts fonts )
    {
        return fonts.getHAnsiTheme();
    }

    @Override
    protected String getFamily( CTFonts fonts )
    {
        return fonts.getHAnsi();
    }

}

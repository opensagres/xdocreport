package org.apache.poi.xwpf.converter.core.styles.run;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme;

public class RunFontFamilyEastAsiaValueProvider
    extends RunFontFamilyValueProvider
{

    public static RunFontFamilyEastAsiaValueProvider INSTANCE = new RunFontFamilyEastAsiaValueProvider();

    @Override
    protected STTheme.Enum getTheme( CTFonts fonts )
    {
        return fonts.getEastAsiaTheme();
    }

    @Override
    protected String getFamily( CTFonts fonts )
    {
        return fonts.getEastAsia();
    }

}

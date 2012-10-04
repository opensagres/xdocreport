package org.apache.poi.xwpf.converter.core.styles.run;

import java.awt.Color;

import org.apache.poi.xwpf.converter.core.utils.ColorHelper;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

public class RunFontColorValueProvider
    extends AbstractRunValueProvider<Color>
{

    public static RunFontColorValueProvider INSTANCE = new RunFontColorValueProvider();

    @Override
    public Color getValue( CTRPr rPr )
    {
        return ColorHelper.getColor( rPr );
    }
}

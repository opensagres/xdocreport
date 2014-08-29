package org.apache.poi.xwpf.converter.core.openxmlformats.styles.run;

import org.apache.poi.xwpf.converter.core.Color;
import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.converter.core.utils.ColorHelper;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

public class RunFontColorValueProvider
    extends AbstractRunValueProvider<Color>
{

    public static final RunFontColorValueProvider INSTANCE = new RunFontColorValueProvider();

    @Override
    public Color getValue( CTRPr pr, XWPFStylesDocument document )
    {
        return ColorHelper.getColor( pr );
    }

    @Override
    public Color getValue( CTParaRPr pr, XWPFStylesDocument document )
    {
        return ColorHelper.getColor( pr );
    }
}

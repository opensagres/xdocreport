package org.apache.poi.xwpf.converter.core.styles.run;

import java.awt.Color;

import org.apache.poi.xwpf.converter.core.utils.ColorHelper;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

public class RunBackgroundColorValueProvider
    extends AbstractRunValueProvider<Color>
{

    public static RunBackgroundColorValueProvider INSTANCE = new RunBackgroundColorValueProvider();

    @Override
    public Color getValue( CTRPr rPr )
    {
        if ( rPr == null )
        {
            return null;
        }
        return ColorHelper.getFillColor( rPr.getShd() );
    }
}

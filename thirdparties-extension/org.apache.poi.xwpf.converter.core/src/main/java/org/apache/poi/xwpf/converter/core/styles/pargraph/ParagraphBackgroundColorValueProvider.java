package org.apache.poi.xwpf.converter.core.styles.pargraph;

import java.awt.Color;

import org.apache.poi.xwpf.converter.core.utils.ColorHelper;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;

public class ParagraphBackgroundColorValueProvider
    extends AbstractParagraphValueProvider<Color>
{

    public static final ParagraphBackgroundColorValueProvider INSTANCE = new ParagraphBackgroundColorValueProvider();

    @Override
    public Color getValue( CTPPr ppr )
    {
        if ( ppr == null )
        {
            return null;
        }
        return ColorHelper.getFillColor( ppr.getShd() );
    }
}

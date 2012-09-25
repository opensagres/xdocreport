package org.apache.poi.xwpf.converters.core.styles.pargraph;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

public class ParagraphFontFamilyValueProvider
    extends AbstractParagraphRunValueProvider<String>
{

    public static ParagraphFontFamilyValueProvider INSTANCE = new ParagraphFontFamilyValueProvider();

    @Override
    public String getValue( CTParaRPr ppr )
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

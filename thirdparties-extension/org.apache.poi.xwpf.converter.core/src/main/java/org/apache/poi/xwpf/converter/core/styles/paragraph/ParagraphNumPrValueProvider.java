package org.apache.poi.xwpf.converter.core.styles.paragraph;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;

public class ParagraphNumPrValueProvider
    extends AbstractParagraphValueProvider<CTNumPr>
{

    public static final ParagraphNumPrValueProvider INSTANCE = new ParagraphNumPrValueProvider();

    @Override
    public CTNumPr getValue( CTPPr ppr )
    {
        if ( ppr == null )
        {
            return null;
        }
        return ppr.getNumPr();
    }
}

package org.apache.poi.xwpf.converter.core.styles.paragraph;

import org.apache.poi.xwpf.converter.core.utils.XWPFUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;

public class ParagraphKeepNextValueProvider
    extends AbstractParagraphValueProvider<Boolean>
{

    public static final ParagraphKeepNextValueProvider INSTANCE = new ParagraphKeepNextValueProvider();

    @Override
    public Boolean getValue( CTPPr ppr )
    {
        if ( ppr == null )
        {
            return false;
        }
        return XWPFUtils.isCTOnOff( ppr.getKeepNext() );
    }
}

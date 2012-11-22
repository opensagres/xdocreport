package org.apache.poi.xwpf.converter.core.styles.paragraph;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs;

public class ParagraphTabsValueProvider
    extends AbstractParagraphValueProvider<CTTabs>
{

    public static final ParagraphTabsValueProvider INSTANCE = new ParagraphTabsValueProvider();

    @Override
    public CTTabs getValue( CTPPr ppr )
    {
        if ( ppr == null )
        {
            return null;
        }
        return ppr.getTabs();
    }
}

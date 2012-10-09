package org.apache.poi.xwpf.converter.core.styles.pargraph;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr;

public class ParagraphBorderTopValueProvider
    extends AbstractParagraphBorderValueProvider
{

    public static final ParagraphBorderTopValueProvider INSTANCE = new ParagraphBorderTopValueProvider();

    protected CTBorder getBorder( CTPBdr border )
    {
        return border.getTop();
    }
}

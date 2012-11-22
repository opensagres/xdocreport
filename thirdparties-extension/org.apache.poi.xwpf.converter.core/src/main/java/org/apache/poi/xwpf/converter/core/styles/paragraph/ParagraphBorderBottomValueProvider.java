package org.apache.poi.xwpf.converter.core.styles.paragraph;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr;

public class ParagraphBorderBottomValueProvider
    extends AbstractParagraphBorderValueProvider
{

    public static final ParagraphBorderBottomValueProvider INSTANCE = new ParagraphBorderBottomValueProvider();

    protected CTBorder getBorder( CTPBdr border )
    {
        return border.getBottom();
    }
}

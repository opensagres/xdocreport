package org.apache.poi.xwpf.converter.core.styles.paragraph;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr;

public class ParagraphBorderRightValueProvider
    extends AbstractParagraphBorderValueProvider
{

    public static final ParagraphBorderRightValueProvider INSTANCE = new ParagraphBorderRightValueProvider();

    protected CTBorder getBorder( CTPBdr border )
    {
        return border.getRight();
    }
}

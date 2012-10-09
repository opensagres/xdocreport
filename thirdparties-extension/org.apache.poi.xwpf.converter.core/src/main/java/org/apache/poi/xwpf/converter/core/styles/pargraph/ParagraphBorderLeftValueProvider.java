package org.apache.poi.xwpf.converter.core.styles.pargraph;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPBdr;

public class ParagraphBorderLeftValueProvider
    extends AbstractParagraphBorderValueProvider
{

    public static final ParagraphBorderLeftValueProvider INSTANCE = new ParagraphBorderLeftValueProvider();

    protected CTBorder getBorder( CTPBdr border )
    {
        return border.getLeft();
    }
}

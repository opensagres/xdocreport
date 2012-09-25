package org.apache.poi.xwpf.converter.styles.pargraph;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;

public class ParagraphAlignmentValueProvider
    extends AbstractParagraphValueProvider<ParagraphAlignment>
{

    public static final ParagraphAlignmentValueProvider INSTANCE = new ParagraphAlignmentValueProvider();

    @Override
    public ParagraphAlignment getValue( CTPPr ppr )
    {
        return ppr == null || !ppr.isSetJc() ? null : ParagraphAlignment.valueOf( ppr.getJc().getVal().intValue() );
    }

}

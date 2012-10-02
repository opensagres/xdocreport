package org.apache.poi.xwpf.converter.core.styles.table;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;

public class TableAlignmentValueProvider
    extends AbstractTableValueProvider<ParagraphAlignment>
{

    public static final TableAlignmentValueProvider INSTANCE = new TableAlignmentValueProvider();

    @Override
    public ParagraphAlignment getValue( CTTblPr tblPr )
    {
        return getTableAlignment( tblPr );
    }

    @Override
    public ParagraphAlignment getValue( CTTblPrBase tblPr )
    {
        return getTableAlignment( tblPr );
    }

    private ParagraphAlignment getTableAlignment( CTTblPrBase tblPr )
    {
        return tblPr == null || !tblPr.isSetJc() ? null
                        : ParagraphAlignment.valueOf( tblPr.getJc().getVal().intValue() );
    }

}

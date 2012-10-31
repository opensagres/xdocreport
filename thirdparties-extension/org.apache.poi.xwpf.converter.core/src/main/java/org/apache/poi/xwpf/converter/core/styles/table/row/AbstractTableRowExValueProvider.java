package org.apache.poi.xwpf.converter.core.styles.table.row;

import org.apache.poi.xwpf.converter.core.styles.AbstractValueProvider;
import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrEx;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr;

public abstract class AbstractTableRowExValueProvider<Value>
    extends AbstractValueProvider<Value, XWPFTableRow>
{

    public CTTblPrEx getTblPrEx( XWPFTableRow row )
    {
        return row.getCtRow().getTblPrEx();
    }

    @Override
    public Value getValueFromElement( XWPFTableRow row )
    {

        return getValue( getTblPrEx( row ) );
    }

    @Override
    protected Value getValueFromStyle( CTStyle style )
    {
        return null;
    }

    @Override
    protected Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults, XWPFStylesDocument stylesDocument )
    {
        return null;
    }

    public abstract Value getValue( CTTblPrEx ctTblPrEx );

    @Override
    protected XWPFTableCell getParentTableCell( XWPFTableRow element )
    {
        return null;
    }

    @Override
    protected Value getValueFromTableStyle( CTTblStylePr tblStylePr )
    {
        return null;
    }

    @Override
    protected String[] getStyleID( XWPFTableRow element )
    {
        return null;
    }

    @Override
    protected CTStyle getDefaultStyle( XWPFTableRow element, XWPFStylesDocument stylesDocument )
    {
        return null;
    }
}

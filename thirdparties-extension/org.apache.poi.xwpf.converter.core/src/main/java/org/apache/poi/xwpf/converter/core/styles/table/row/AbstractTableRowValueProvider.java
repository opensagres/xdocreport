package org.apache.poi.xwpf.converter.core.styles.table.row;

import org.apache.poi.xwpf.converter.core.styles.AbstractValueProvider;
import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;

public abstract class AbstractTableRowValueProvider<Value>
    extends AbstractValueProvider<Value, XWPFTableRow>
{

    public CTTrPr getTrPr( XWPFTableRow row )
    {
        return row.getCtRow().getTrPr();
    }

    public CTTrPr getTrPr( CTStyle style )
    {
        return style.getTrPr();
    }

    public CTTrPr getTrPr( CTDocDefaults docDefaults )
    {
        return null;
    }

    @Override
    public Value getValueFromElement( XWPFTableRow row )
    {

        return getValue( getTrPr( row ) );
    }

    @Override
    protected Value getValueFromStyle( CTStyle style )
    {
        return getValue( getTrPr( style ) );
    }

    @Override
    protected Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults )
    {
        return getValue( getTrPr( docDefaults ) );
    }

    public abstract Value getValue( CTTrPr trPr );

    @Override
    protected String[] getStyleID( XWPFTableRow row )
    {
        return new String[] { row.getTable().getStyleID() };
    }

    @Override
    protected CTStyle getDefaultStyle( XWPFTableRow row, XWPFStylesDocument stylesDocument )
    {
        return stylesDocument.getDefaultTableStyle();
    }

    @Override
    protected XWPFTableCell getEmbeddedTableCell( XWPFTableRow element )
    {
        return null;
    }
}

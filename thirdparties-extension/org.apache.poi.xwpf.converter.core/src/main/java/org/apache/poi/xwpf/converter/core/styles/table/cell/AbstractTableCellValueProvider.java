package org.apache.poi.xwpf.converter.core.styles.table.cell;

import org.apache.poi.xwpf.converter.core.styles.AbstractValueProvider;
import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

public abstract class AbstractTableCellValueProvider<Value>
    extends AbstractValueProvider<Value, XWPFTableCell>
{

    public CTTcPr getTcPr( XWPFTableCell cell )
    {
        return cell.getCTTc().getTcPr();
    }

    public CTTcPr getTcPr( CTStyle style )
    {
        return style.getTcPr();
    }

    public CTTcPr getTcPr( CTTblStylePr tblStylePr )
    {
        return tblStylePr.getTcPr();
    }

    public CTTcPr getTcPr( CTDocDefaults docDefaults )
    {
        return null;
    }

    @Override
    public Value getValueFromElement( XWPFTableCell cell )
    {
        return getValue( getTcPr( cell ) );
    }

    @Override
    protected Value getValueFromStyle( CTStyle style )
    {
        return getValue( getTcPr( style ) );
    }

    @Override
    protected Value getValueFromTableStyle( CTTblStylePr tblStylePr )
    {
        return getValue( getTcPr( tblStylePr ) );
    }

    @Override
    protected Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults, XWPFStylesDocument stylesDocument )
    {
        return getValue( getTcPr( docDefaults ) );
    }

    public abstract Value getValue( CTTcPr tcPr );

    @Override
    protected String[] getStyleID( XWPFTableCell cell )
    {
        return null;
    }

    @Override
    protected CTStyle getDefaultStyle( XWPFTableCell cell, XWPFStylesDocument stylesDocument )
    {
        return stylesDocument.getDefaultTableStyle();
    }

    @Override
    protected XWPFTableCell getParentTableCell( XWPFTableCell cell )
    {
        return cell;
    }

}

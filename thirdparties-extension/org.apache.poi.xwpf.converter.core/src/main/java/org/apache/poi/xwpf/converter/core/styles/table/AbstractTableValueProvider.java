package org.apache.poi.xwpf.converter.core.styles.table;

import org.apache.poi.xwpf.converter.core.styles.AbstractValueProvider;
import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;

public abstract class AbstractTableValueProvider<Value>
    extends AbstractValueProvider<Value, XWPFTable>
{

    public CTTblPr getTblPr( XWPFTable table )
    {
        return table.getCTTbl().getTblPr();
    }

    public CTTblPrBase getTblPr( CTStyle style )
    {
        return style.getTblPr();
    }

    public CTTblPr getTblPr( CTDocDefaults docDefaults )
    {
        return null;
    }

    @Override
    public Value getValueFromElement( XWPFTable table )
    {
        return getValue( getTblPr( table ) );
    }

    @Override
    protected Value getValueFromStyle( CTStyle style )
    {
        return getValue( getTblPr( style ) );
    }

    @Override
    protected Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults )
    {
        return getValue( getTblPr( docDefaults ) );
    }

    public abstract Value getValue( CTTblPr tblPr );

    public abstract Value getValue( CTTblPrBase tblPr );

    @Override
    protected String[] getStyleID( XWPFTable table )
    {
        return new String[] { table.getStyleID() };
    }

    @Override
    protected CTStyle getDefaultStyle( XWPFTable table, XWPFStylesDocument stylesDocument )
    {
        return stylesDocument.getDefaultTableStyle();
    }
    
    @Override
    protected XWPFTableCell getEmbeddedTableCell( XWPFTable element )
    {     
        return null;
    }
}

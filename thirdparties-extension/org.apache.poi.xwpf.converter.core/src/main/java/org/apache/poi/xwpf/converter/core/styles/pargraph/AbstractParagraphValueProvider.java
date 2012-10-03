package org.apache.poi.xwpf.converter.core.styles.pargraph;

import java.util.List;

import org.apache.poi.xwpf.converter.core.styles.AbstractValueProvider;
import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.apache.poi.xwpf.converter.core.utils.StylesHelper;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr;

public abstract class AbstractParagraphValueProvider<Value>
    extends AbstractValueProvider<Value, XWPFParagraph>
{

    public CTPPr getCTPPr( XWPFParagraph paragraph )
    {
        return paragraph.getCTP().getPPr();
    }

    public CTPPr getCTPPr( CTStyle style )
    {
        return style.getPPr();
    }

    public CTPPr getCTPPr( CTTblStylePr tblStylePr )
    {
        return tblStylePr.getPPr();
    }

    public CTPPr getCTPPr( CTDocDefaults docDefaults )
    {
        CTPPrDefault prDefault = docDefaults.getPPrDefault();
        if ( prDefault == null )
        {
            return null;
        }
        return prDefault.getPPr();
    }

    @Override
    public Value getValueFromElement( XWPFParagraph paragraph )
    {
        return getValue( getCTPPr( paragraph ) );
    }

    @Override
    protected Value getValueFromStyle( CTStyle style )
    {
        return getValue( getCTPPr( style ) );
    }

    @Override
    protected Value getValueFromTableStyle( CTTblStylePr tblStylePr )
    {
        return getValue( getCTPPr( tblStylePr ) );
    }

    @Override
    protected Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults )
    {
        return getValue( getCTPPr( docDefaults ) );
    }

    public abstract Value getValue( CTPPr ppr );

    @Override
    protected String[] getStyleID( XWPFParagraph paragraph )
    {
        List<String> styleIDs = StylesHelper.getStyleIDs( paragraph );
        if ( styleIDs != null )
        {
            return styleIDs.toArray( StringUtils.EMPTY_STRING_ARRAY );
        }
        return null;
    }

    @Override
    protected CTStyle getDefaultStyle( XWPFParagraph element, XWPFStylesDocument styleManager )
    {
        return styleManager.getDefaultParagraphStyle();
    }

    @Override
    protected XWPFTableCell getParentTableCell( XWPFParagraph paragraph )
    {
        return StylesHelper.getEmbeddedTableCell( paragraph );
    }
}

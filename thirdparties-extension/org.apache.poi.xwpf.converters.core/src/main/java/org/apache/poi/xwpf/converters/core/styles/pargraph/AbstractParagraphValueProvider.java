package org.apache.poi.xwpf.converters.core.styles.pargraph;

import org.apache.poi.xwpf.converters.core.styles.AbstractValueProvider;
import org.apache.poi.xwpf.converters.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

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
    protected Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults )
    {
        return getValue( getCTPPr( docDefaults ) );
    }

    public abstract Value getValue( CTPPr ppr );

    @Override
    protected String[] getStyleID( XWPFParagraph paragraph )
    {
        return new String[] { paragraph.getStyleID() };
    }

    @Override
    protected CTStyle getDefaultStyle( XWPFParagraph element, XWPFStylesDocument styleManager )
    {
        return styleManager.getDefaultParagraphStyle();
    }
}

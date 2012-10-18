package org.apache.poi.xwpf.converter.core.styles.pargraph;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.converter.core.styles.AbstractValueProvider;
import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.apache.poi.xwpf.converter.core.utils.StylesHelper;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

public abstract class AbstractParagraphRunValueProvider<Value>
    extends AbstractValueProvider<Value, XWPFParagraph>
{

    public CTParaRPr getCTParaRPr( XWPFParagraph paragraph )
    {
        return paragraph.getCTP().getPPr().getRPr();
    }

    public CTRPr getRPr( CTStyle style )
    {
        return style.getRPr();
    }

    public CTRPr getRPr( CTDocDefaults docDefaults )
    {
        CTRPrDefault prDefault = docDefaults.getRPrDefault();
        if ( prDefault == null )
        {
            return null;
        }
        return prDefault.getRPr();
    }

    @Override
    public Value getValueFromElement( XWPFParagraph paragraph )
    {
        return getValue( getCTParaRPr( paragraph ) );
    }

    @Override
    protected Value getValueFromStyle( CTStyle style )
    {
        return getValue( getRPr( style ) );
    }

    @Override
    protected Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults, XWPFStylesDocument stylesDocument)
    {
        return getValue( getRPr( docDefaults ) );
    }

    public abstract Value getValue( CTParaRPr ppr );

    public abstract Value getValue( CTRPr ppr );

    @Override
    protected String[] getStyleID( XWPFParagraph paragraph )
    {
        List<String> styleIDs = StylesHelper.getStyleIDs( paragraph );
        CTParaRPr rPr = getCTParaRPr( paragraph );
        if ( rPr != null )
        {
            CTString style = rPr.getRStyle();
            if ( style != null )
            {
                if ( styleIDs == null )
                {
                    styleIDs = new ArrayList<String>();
                }
                styleIDs.add( 0, style.getVal() );
            }
        }
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
}

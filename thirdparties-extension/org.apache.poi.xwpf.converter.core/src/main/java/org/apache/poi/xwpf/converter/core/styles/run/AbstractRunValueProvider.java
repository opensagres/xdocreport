package org.apache.poi.xwpf.converter.core.styles.run;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.converter.core.styles.AbstractValueProvider;
import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.apache.poi.xwpf.converter.core.utils.StylesHelper;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

public abstract class AbstractRunValueProvider<Value>
    extends AbstractValueProvider<Value, XWPFRun>
{

    public CTRPr getRPr( XWPFRun run )
    {
        return run.getCTR().getRPr();
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
    public Value getValueFromElement( XWPFRun run )
    {
        return getValue( getRPr( run ) );
    }

    @Override
    protected Value getValueFromStyle( CTStyle style )
    {
        return getValue( getRPr( style ) );
    }

    @Override
    protected Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults )
    {
        return getValue( getRPr( docDefaults ) );
    }

    public abstract Value getValue( CTRPr ppr );

    @Override
    protected String[] getStyleID( XWPFRun run )
    {
        XWPFParagraph paragraph = run.getParagraph();
        List<String> styleIDs = StylesHelper.getStyleIDs( paragraph );
        CTRPr rPr = getRPr( run );
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
    protected CTStyle getDefaultStyle( XWPFRun element, XWPFStylesDocument styleManager )
    {
        return styleManager.getDefaultParagraphStyle();
    }

    @Override
    protected XWPFTableCell getEmbeddedTableCell( XWPFRun run )
    {
        return StylesHelper.getEmbeddedTableCell( run.getParagraph() );
    }
}

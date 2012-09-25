package org.apache.poi.xwpf.converter.styles.run;

import org.apache.poi.xwpf.converter.styles.AbstractValueProvider;
import org.apache.poi.xwpf.converter.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
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
        CTRPr rPr = getRPr( run );
        if ( rPr != null )
        {
            CTString style = rPr.getRStyle();
            if ( style != null )
            {
                return new String[] { style.getVal(), run.getParagraph().getStyleID() };
            }
        }
        return new String[] { run.getParagraph().getStyleID() };
    }

    @Override
    protected CTStyle getDefaultStyle( XWPFRun element, XWPFStylesDocument styleManager )
    {
        return styleManager.getDefaultParagraphStyle();
    }
}

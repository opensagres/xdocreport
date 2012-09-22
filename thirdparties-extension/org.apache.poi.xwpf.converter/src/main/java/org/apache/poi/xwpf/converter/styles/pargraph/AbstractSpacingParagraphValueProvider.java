package org.apache.poi.xwpf.converter.styles.pargraph;

import org.apache.poi.xwpf.converter.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

public abstract class AbstractSpacingParagraphValueProvider<Value>
    extends AbstractParagraphValueProvider<Value>
{
    @Override
    public Value getValueFromElement( XWPFParagraph paragraph )
    {
        CTSpacing spacing = getCTSpacing( paragraph );
        if ( spacing == null )
        {
            return null;
        }
        return getValue( spacing );
    }

    public CTSpacing getCTSpacing( XWPFParagraph paragraph )
    {
        CTPPr pr = getCTPPr( paragraph );
        if ( pr == null )
        {
            return null;
        }
        return pr.getSpacing() == null ? null : pr.getSpacing();
    }

    @Override
    protected Value getValueFromStyle( CTStyle style )
    {
        CTSpacing spacing = getCTSpacing( style );
        if ( spacing == null )
        {
            return null;
        }
        return getValue( spacing );
    }

    public CTSpacing getCTSpacing( CTStyle style )
    {
        CTPPr pr = getCTPPr( style );
        if ( pr == null )
        {
            return null;
        }
        return pr.getSpacing() == null ? null : pr.getSpacing();
    }

    protected abstract Value getValue( CTSpacing spacing );


}

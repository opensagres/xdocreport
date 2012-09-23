package org.apache.poi.xwpf.converter.styles.pargraph;

import org.apache.poi.xwpf.converter.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.BodyType;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
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

    @Override
    protected Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults )
    {
        CTSpacing spacing = getCTSpacing( docDefaults );
        if ( spacing == null )
        {
            return null;
        }
        return getValue( spacing );
    }

    public CTSpacing getCTSpacing( CTDocDefaults docDefaults )
    {
        CTPPr pr = getCTPPr( docDefaults );
        if ( pr == null )
        {
            return null;
        }
        return pr.getSpacing() == null ? null : pr.getSpacing();
    }

    @Override
    protected String getKey( XWPFParagraph element, XWPFStylesDocument stylesDocument, String styleId )
    {
        if ( element.getPartType() == BodyType.TABLECELL )
        {
            if ( styleId != null && styleId.length() > 0 )
            {
                return new StringBuilder( this.getClass().getName() ).append( "_" ).append( styleId ).append( "_cell" ).toString();
            }
            return new StringBuilder( this.getClass().getName() ).append( "_cell" ).toString();
        }
        return super.getKey( element, stylesDocument, styleId );
    }

    protected abstract Value getValue( CTSpacing spacing );

}

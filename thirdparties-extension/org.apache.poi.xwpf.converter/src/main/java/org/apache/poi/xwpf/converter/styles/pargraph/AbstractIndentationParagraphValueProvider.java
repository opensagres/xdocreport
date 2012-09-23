package org.apache.poi.xwpf.converter.styles.pargraph;

import org.apache.poi.xwpf.converter.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.BodyType;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

public abstract class AbstractIndentationParagraphValueProvider<Value>
    extends AbstractParagraphValueProvider<Value>
{
    @Override
    public Value getValueFromElement( XWPFParagraph paragraph )
    {
        return internalGetValue( getInd( getCTPPr( paragraph ) ) );
    }

    @Override
    protected Value getValueFromStyle( CTStyle style )
    {
        return internalGetValue( getInd( getCTPPr( style ) ) );
    }

    @Override
    protected Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults )
    {
        return internalGetValue( getInd( getCTPPr( docDefaults ) ) );
    }

    private Value internalGetValue( CTInd ind )
    {
        if ( ind == null )
        {
            return null;
        }
        return getValue( ind );
    }

    public CTInd getInd( CTPPr pr )
    {
        if ( pr == null )
        {
            return null;
        }
        return pr.getInd() == null ? null : pr.getInd();
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

    protected abstract Value getValue( CTInd ind );

}

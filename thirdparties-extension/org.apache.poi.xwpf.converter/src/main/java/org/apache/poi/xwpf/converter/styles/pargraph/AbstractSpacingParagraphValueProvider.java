package org.apache.poi.xwpf.converter.styles.pargraph;

import org.apache.poi.xwpf.converter.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.BodyType;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;

public abstract class AbstractSpacingParagraphValueProvider<Value>
    extends AbstractParagraphValueProvider<Value>
{

    @Override
    public Value getValue( CTPPr ppr )
    {
        CTSpacing spacing = getSpacing( ppr );
        if ( spacing == null )
        {
            return null;
        }
        return getValue( spacing );
    }

    public CTSpacing getSpacing( CTPPr ppr )
    {
        if ( ppr == null )
        {
            return null;
        }
        return ppr.getSpacing() == null ? null : ppr.getSpacing();
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

    protected abstract Value getValue( CTSpacing ind );

}

package org.apache.poi.xwpf.converter.core.styles.pargraph;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.BodyType;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType.Enum;

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
        return ppr.getSpacing();
    }

    @Override
    protected StringBuilder getKeyBuffer( XWPFParagraph element, XWPFStylesDocument stylesDocument, String styleId,
                                          Enum type )
    {
        if ( element.getPartType() == BodyType.TABLECELL )
        {
            return super.getKeyBuffer( element, stylesDocument, styleId, type ).append( "_cell" );
        }
        return super.getKeyBuffer( element, stylesDocument, styleId, type );
    }
    
    protected abstract Value getValue( CTSpacing ind );

}

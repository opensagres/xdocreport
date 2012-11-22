package org.apache.poi.xwpf.converter.core.styles.paragraph;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.usermodel.BodyType;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType.Enum;

public abstract class AbstractIndentationParagraphValueProvider<Value>
    extends AbstractParagraphValueProvider<Value>
{
    @Override
    public Value getValue( CTPPr ppr )
    {
        CTInd ind = getInd( ppr );
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
    protected StringBuilder getKeyBuffer( XWPFParagraph element, XWPFStylesDocument stylesDocument, String styleId,
                                          Enum type )
    {
        if ( element.getPartType() == BodyType.TABLECELL )
        {
            return super.getKeyBuffer( element, stylesDocument, styleId, type ).append( "_cell" );
        }
        return super.getKeyBuffer( element, stylesDocument, styleId, type );
    }

    public abstract Value getValue( CTInd ind );

}

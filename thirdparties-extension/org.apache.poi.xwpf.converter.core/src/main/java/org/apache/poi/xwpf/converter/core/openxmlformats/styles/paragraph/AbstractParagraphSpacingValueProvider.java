package org.apache.poi.xwpf.converter.core.openxmlformats.styles.paragraph;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;

public abstract class AbstractParagraphSpacingValueProvider<Value>
    extends AbstractParagraphValueProvider<Value>
{
    @Override
    public Value getValue( CTPPr ppr, XWPFStylesDocument document )
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
    
    protected abstract Value getValue( CTSpacing ind );
}

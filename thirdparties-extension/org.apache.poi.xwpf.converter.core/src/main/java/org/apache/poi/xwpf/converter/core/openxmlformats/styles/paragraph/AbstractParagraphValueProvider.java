package org.apache.poi.xwpf.converter.core.openxmlformats.styles.paragraph;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;

public abstract class AbstractParagraphValueProvider<Value>
{

    public Value getValue( CTP paragraph, XWPFStylesDocument document )
    {
        Value value = null;
        // from paragraph
        CTPPr pPr = paragraph.getPPr();
        if ( pPr != null )
        {
            // from paragraph inline
            value = getValue( pPr );
            if ( value != null )
            {
                return value;
            }
            // from paragraph style
            value = getValueFromStyle( pPr.getPStyle(), document );
            if ( value != null )
            {
                return value;
            }

        }
        return null;
    }

    private Value getValueFromStyle( CTString styleId, XWPFStylesDocument document )
    {
        // Get the style
        CTStyle style = document.getStyle( styleId );
        if ( style != null )
        {
            return getValue( style.getPPr() );
        }
        return null;
    }

    public abstract Value getValue( CTPPr pPr );
}

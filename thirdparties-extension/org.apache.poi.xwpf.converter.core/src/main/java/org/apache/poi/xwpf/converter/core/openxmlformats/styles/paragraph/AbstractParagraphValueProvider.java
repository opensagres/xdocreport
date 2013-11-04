package org.apache.poi.xwpf.converter.core.openxmlformats.styles.paragraph;

import org.apache.poi.xwpf.converter.core.openxmlformats.styles.ValueProviderHelper;
import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;

public abstract class AbstractParagraphValueProvider<Value>
{

    public Value getValue( CTP paragraph, CTTbl parentTable, XWPFStylesDocument document )
    {
        Value value = null;
        // from paragraph
        CTPPr pPr = paragraph.getPPr();
        if ( pPr != null )
        {
            // from paragraph inline
            value = getValue( pPr, document );
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

        // from parent table style
        if ( parentTable != null )
        {
            CTTblPr tblPr = parentTable.getTblPr();
            if ( tblPr != null )
            {
                // from table style
                value = getValueFromStyle( tblPr.getTblStyle(), document );
                if ( value != null )
                {
                    return value;
                }
            }
        }

        // from paragraph default style
        value = getValueFromStyle( document.getDefaultParagraphStyle(), document );
        if ( value != null )
        {
            return value;
        }

        // from defaults docs paragraph
        CTDocDefaults docDefaults = document.getDocDefaults();
        if ( docDefaults == null )
        {
            return null;
        }
        value = getValueFromDocDefaultsStyle( docDefaults, document );
        if ( value != null )
        {
            return value;
        }
        return null;
    }

    private Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults, XWPFStylesDocument document )
    {
        return getValue( getCTPPr( docDefaults ), document );
    }

    public CTPPr getCTPPr( CTDocDefaults docDefaults )
    {
        CTPPrDefault prDefault = docDefaults.getPPrDefault();
        if ( prDefault == null )
        {
            return null;
        }
        return prDefault.getPPr();
    }

    private Value getValueFromStyle( CTString styleId, XWPFStylesDocument document )
    {
        if ( styleId == null )
        {
            return null;
        }
        String key = ValueProviderHelper.getKey( this.getClass(), document, styleId.getVal(), null );
        Object v = document.getValue( key );
        if ( v != null && v != XWPFStylesDocument.EMPTY_VALUE )
        {
            return (Value) v;
        }

        // Get the style
        CTStyle style = document.getStyle( styleId );
        Value value = getValueFromStyle( style, document );
        if ( value == null )
        {
            document.setValue( key, XWPFStylesDocument.EMPTY_VALUE );
            return null;
        }
        document.setValue( key, value );
        if ( value.equals( XWPFStylesDocument.EMPTY_VALUE ) )
        {
            return null;
        }
        return value;
    }

    private Value getValueFromStyle( CTStyle style, XWPFStylesDocument document )
    {
        if ( style != null )
        {
            Value value = getValue( style.getPPr(), document );
            if ( value != null )
            {
                return value;
            }
            return getValueFromStyle( style.getBasedOn(), document );
        }
        return null;
    }

    public abstract Value getValue( CTPPr pPr, XWPFStylesDocument document );
}

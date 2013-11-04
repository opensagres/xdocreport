package org.apache.poi.xwpf.converter.core.openxmlformats.styles;

import org.apache.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType;

public class ValueProviderHelper
{

    public static String getKey( Class valueProvider, XWPFStylesDocument stylesDocument, String styleId,
                                 STTblStyleOverrideType.Enum type )
    {
        return getKeyBuffer( valueProvider, stylesDocument, styleId, type ).toString();
    }

    public static StringBuilder getKeyBuffer( Class valueProvider, XWPFStylesDocument stylesDocument, String styleId,
                                              STTblStyleOverrideType.Enum type )
    {
        StringBuilder key = new StringBuilder( valueProvider.getName() );
        if ( StringUtils.isNotEmpty( styleId ) )
        {
            key.append( "_" ).append( styleId ).toString();
        }
        if ( type != null )
        {
            key.append( "_table" );
            key.append( type.intValue() );
        }
        return key;
    }
}

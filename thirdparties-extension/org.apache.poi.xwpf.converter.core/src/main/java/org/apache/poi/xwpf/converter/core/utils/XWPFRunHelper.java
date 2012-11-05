package org.apache.poi.xwpf.converter.core.utils;

import java.util.List;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFldChar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType;

public class XWPFRunHelper
{

    public static STFldCharType.Enum getFldCharType( CTR r )
    {
        CTFldChar fldChar = getFldChar( r );
        if ( fldChar == null )
        {
            return null;
        }
        return fldChar.getFldCharType();
    }

    public static CTFldChar getFldChar( CTR r )
    {
        List<CTFldChar> fldChars = r.getFldCharList();
        if ( fldChars == null || fldChars.size() < 1 )
        {
            return null;
        }
        return fldChars.get( 0 );
    }

    public static String getInstrText( CTR r )
    {
        List<CTText> instrTextList = r.getInstrTextList();
        if ( instrTextList == null || instrTextList.size() < 1 )
        {
            return null;
        }
        if ( instrTextList.size() == 1 )
        {
            return instrTextList.get( 0 ).getStringValue();
        }

        StringBuilder instrText = new StringBuilder();
        for ( CTText ctText : instrTextList )
        {
            instrText.append( ctText.getStringValue() );
        }
        return instrText.toString();
    }
}

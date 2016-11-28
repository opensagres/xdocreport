/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.poi.xwpf.converter.core.openxmlformats.styles;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType;

import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;
import fr.opensagres.poi.xwpf.converter.core.utils.StringUtils;

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

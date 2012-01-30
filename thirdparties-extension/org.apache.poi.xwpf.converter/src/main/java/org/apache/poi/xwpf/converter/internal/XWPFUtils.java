/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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
package org.apache.poi.xwpf.converter.internal;

import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor;

public class XWPFUtils
{

    /**
     * Returns CTRPr from default.
     * 
     * @param defaults
     * @return
     */
    public static CTRPr getRPr( CTDocDefaults defaults )
    {
        if ( defaults == null )
        {
            return null;
        }
        CTRPrDefault rprDefault = defaults.getRPrDefault();
        if ( rprDefault == null )
        {
            return null;
        }
        return rprDefault.getRPr();
    }

    /**
     * Returns CTRPr from style.
     * 
     * @param style
     * @return
     */
    public static CTRPr getRPr( XWPFStyle style )
    {
        if ( style == null )
        {
            return null;
        }

        CTStyle ctStyle = style.getCTStyle();
        if ( ctStyle == null )
        {
            return null;
        }
        return ctStyle.getRPr();
    }

    public static String getColor( STHexColor color )
    {
        if ( color != null )
        {
            if ( !"auto".equals( color.getStringValue() ) )
            {
                return color.getStringValue();
            }
        }
        return null;
    }

}

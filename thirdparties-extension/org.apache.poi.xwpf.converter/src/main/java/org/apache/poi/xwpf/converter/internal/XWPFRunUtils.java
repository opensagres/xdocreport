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

import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;

public class XWPFRunUtils
{

    /**
     * Returns font family.
     * 
     * @param run
     * @param rprStyle
     * @param rprDefault
     * @return
     */
    public static String getFontFamily( XWPFRun run, CTRPr rprStyle, CTRPr rprDefault )
    {
        // Font family name
        String fontFamily = run.getFontFamily();
        if ( fontFamily == null )
        {
            // Search from style
            if ( rprStyle != null )
            {
                CTFonts fonts = rprStyle.getRFonts();
                if ( fonts != null )
                {
                    fontFamily = fonts.getAscii();
                }
            }
            if ( fontFamily == null )
            {
                // Search from default
                if ( rprDefault != null )
                {
                    CTFonts fonts = rprDefault.getRFonts();
                    if ( fonts != null )
                    {
                        fontFamily = fonts.getAscii();
                    }
                }
            }
        }
        return fontFamily;
    }

    public static String getFontColor( XWPFRun run, CTRPr runRprStyle, CTRPr rprStyle, CTRPr rprDefault )
    {
        CTRPr rPr = run.getCTR().getRPr();
        String fontColor = getColor( rPr );
        if ( fontColor == null )
        {
            fontColor = getColor( runRprStyle );
            if ( fontColor == null )
            {
                fontColor = getColor( rprStyle );
                if ( fontColor == null )
                {
                    fontColor = getColor( rprDefault );
                }
            }
        }
        return fontColor;
    }

    public static String getColor( CTRPr rPr )
    {
        if ( rPr != null )
        {
            CTColor ctColor = rPr.getColor();
            if ( ctColor != null )
            {
                STHexColor color = ctColor.xgetVal();
                return XWPFUtils.getColor( color );
            }
        }
        return null;
    }

    public static boolean isBold( XWPFRun run, CTRPr runRprStyle, CTRPr rprStyle, CTRPr rprDefault )
    {
        Boolean bold = isBold( run.getCTR().getRPr() );
        if ( bold != null )
        {
            return bold;
        }
        bold = isBold( runRprStyle );
        if ( bold != null )
        {
            return bold;
        }
        bold = isBold( rprStyle );
        if ( bold != null )
        {
            return bold;
        }
        bold = isBold( rprDefault );
        if ( bold != null )
        {
            return bold;
        }
        return run.isBold();
    }

    private static Boolean isBold( CTRPr pr )
    {
        if ( pr == null || !pr.isSetB() )
        {
            return null;
        }
        return isCTOnOff( pr.getB() );
    }

    public static boolean isItalic( XWPFRun run, CTRPr runRprStyle, CTRPr rprStyle, CTRPr rprDefault )
    {
        Boolean italic = isItalic( run.getCTR().getRPr() );
        if ( italic != null )
        {
            return italic;
        }
        italic = isItalic( runRprStyle );
        if ( italic != null )
        {
            return italic;
        }
        italic = isItalic( rprStyle );
        if ( italic != null )
        {
            return italic;
        }
        italic = isItalic( rprDefault );
        if ( italic != null )
        {
            return italic;
        }
        return run.isItalic();
    }

    private static Boolean isItalic( CTRPr pr )
    {
        if ( pr == null || !pr.isSetI() )
        {
            return null;
        }
        return isCTOnOff( pr.getI() );
    }

    /**
     * For isBold, isItalic etc
     */
    private static boolean isCTOnOff( CTOnOff onoff )
    {
        if ( !onoff.isSetVal() )
            return true;
        if ( onoff.getVal() == STOnOff.ON )
            return true;
        if ( onoff.getVal() == STOnOff.TRUE )
            return true;
        return false;
    }

    public static CTString getRStyle( XWPFRun run )
    {
        if ( run == null )
        {
            return null;
        }
        CTR ctr = run.getCTR();
        if ( ctr == null )
        {
            return null;
        }
        CTRPr rpr = ctr.getRPr();
        if ( rpr == null )
        {
            return null;
        }
        return rpr.getRStyle();
    }
}

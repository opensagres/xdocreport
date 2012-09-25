/**
 * Copyright (C) 2011 The XDocReport Team <xdocreport@googlegroups.com>
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

import java.awt.Color;

import org.apache.poi.xwpf.converter.internal.itext.ColorRegistry;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrDefault;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHexColor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STSectionMark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;

import fr.opensagres.xdocreport.itext.extension.PageOrientation;

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

    public static PageOrientation getPageOrientation( org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation.Enum orientation )
    {
        if ( orientation != null )
        {
            if ( org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation.LANDSCAPE.equals( orientation ) )
            {
                return PageOrientation.Landscape;
            }
            else
            {
                return PageOrientation.Portrait;
            }
        }
        return null;
    }

    public static boolean isContinuousSection( CTSectPr sectPr )
    {
        if ( sectPr == null )
        {
            return false;
        }
        CTSectType sectType = sectPr.getType();
        if ( sectType == null )
        {
            return false;
        }

        return sectType.getVal() == STSectionMark.CONTINUOUS;
    }

    public static Color getFillColor( CTShd shd )
    {
        if ( shd == null )
        {
            return null;
        }
        STHexColor hexColor = shd.xgetFill();
        Object val = shd.xgetVal();
        return getColor( hexColor, val, true );
    }

    public static Color getColor( CTShd shd )
    {
        if ( shd == null )
        {
            return null;
        }
        STHexColor hexColor = shd.xgetColor();
        Object val = shd.xgetVal();
        return getColor( hexColor, val, false );
    }

    public static Color getColor( STHexColor hexColor, Object val, boolean background )
    {
        if ( hexColor == null )
        {
            return null;
        }
        return getColor( hexColor.getStringValue(), val, background );
    }

    public static Color getColor( String hexColor, Object val, boolean background )
    {
        if ( hexColor != null )
        {
            if ( "auto".equals( hexColor ) )
            {
                Color autoColor = background ? Color.WHITE : Color.BLACK;
                if ( val != null )
                {
                    String s = getStringVal( val );
                    if ( s.startsWith( "pct" ) )
                    {
                        s = s.substring( "pct".length(), s.length() );
                        try
                        {
                            float percent = Float.parseFloat( s ) / 100;
                            if ( background )
                            {
                                return darken( autoColor.getRed(), autoColor.getGreen(), autoColor.getBlue(), percent );
                            }
                            return lighten( autoColor.getRed(), autoColor.getGreen(), autoColor.getBlue(), percent );
                        }
                        catch ( Throwable e )
                        {
                            e.printStackTrace();
                        }
                    }
                }
                return autoColor;
            }
            return ColorRegistry.getInstance().getColor( "0x" + hexColor );
        }
        return null;
    }

    private static String getStringVal( Object val )
    {
        if ( val instanceof STShd )
        {
            STShd shd = (STShd) val;
            return shd.getStringValue();
        }
        return val.toString();
    }

    public static String toHexString( Color color )
    {
        String hexaWith8Digits = Integer.toHexString( color.getRGB() );
        return new StringBuilder( "#" ).append( hexaWith8Digits.substring( 2, hexaWith8Digits.length() ) ).toString();
    }

    public static Color darken( int r, int g, int b, double percent )
        throws IllegalArgumentException
    {
        return new Color( Math.max( (int) ( r * ( 1 - percent ) ), 0 ), Math.max( (int) ( g * ( 1 - percent ) ), 0 ),
                          Math.max( (int) ( b * ( 1 - percent ) ), 0 ) );
    }

    public static Color lighten( int r, int g, int b, double percent )
        throws IllegalArgumentException
    {
        int r2, g2, b2;
        r2 = r + (int) ( ( 255 - r ) * percent );
        g2 = g + (int) ( ( 255 - g ) * percent );
        b2 = b + (int) ( ( 255 - b ) * percent );
        return new Color( r2, g2, b2 );
    }

    /**
     * For isBold, isItalic etc
     */
    public static boolean isCTOnOff( CTOnOff onoff )
    {
        if ( onoff == null )
        {
            return false;
        }
        if ( !onoff.isSetVal() )
            return true;
        if ( onoff.getVal() == STOnOff.ON )
            return true;
        if ( onoff.getVal() == STOnOff.TRUE )
            return true;
        return false;
    }
}

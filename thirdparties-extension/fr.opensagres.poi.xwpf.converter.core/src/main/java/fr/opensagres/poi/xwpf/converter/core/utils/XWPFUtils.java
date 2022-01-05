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
package fr.opensagres.poi.xwpf.converter.core.utils;


import java.math.BigInteger;

import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff1;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMeasurementOrPercent;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STSectionMark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure;

import fr.opensagres.poi.xwpf.converter.core.Color;
import fr.opensagres.poi.xwpf.converter.core.PageOrientation;

public class XWPFUtils
{

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
        if ( !onoff.isSetVal() ) {
            return true;
        }
        return isOn(onoff.xgetVal());
    }   

    public static boolean isOn( STOnOff onoff )
    {
        if ( onoff == null || onoff.getObjectValue() == null ) {
            return false;
        }
        Object value = onoff.getObjectValue();
        if (value instanceof Boolean) {
        	return ((Boolean) value).booleanValue();
        } else {
        	return STOnOff1.INT_ON == ((STOnOff1) onoff).getEnumValue().intValue();
        }
    }

    public static float floatValue( STTwipsMeasure dxa )
    {
    	return ((SimpleValue) dxa).getFloatValue();
    }

    public static float floatValue( STSignedTwipsMeasure dxa )
    {
    	return ((SimpleValue) dxa).getFloatValue();        
    }

    public static float floatValue( STMeasurementOrPercent dxa )
    {
    	return ((SimpleValue) dxa).getFloatValue();        
    }

    public static BigInteger bigIntegerValue( STHpsMeasure dxa )
    {
    	return ((SimpleValue) dxa).getBigIntegerValue();        
    }
    
}

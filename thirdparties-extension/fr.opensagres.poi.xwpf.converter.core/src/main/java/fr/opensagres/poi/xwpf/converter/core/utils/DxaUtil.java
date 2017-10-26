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

/**
 * @author pascalleclercq cf.
 *         http://startbigthinksmall.wordpress.com/2010/01/04/points-inches-and-emus-measuring-units-in-office-open-xml/
 */
public class DxaUtil
{

    public static float dxa2mm( float dxa )
    {
        return (float) ( dxa2inch( dxa ) * 25.4 );
    }

    public static float dxa2mm( BigInteger dxa )
    {
        return (float) ( dxa2inch( dxa ) * 25.4 );
    }

    public static float emu2points( long emu )
    {
        return dxa2points( emu ) / 635;
    }

    public static float dxa2points( float dxa )
    {
        return dxa / 20;
    }

    public static int dxa2points( int dxa )
    {
        return dxa / 20;
    }

    public static float dxa2points( BigInteger dxa )
    {
        return (float) (dxa.doubleValue() / 20);
    }

    public static float dxa2inch( float dxa )
    {
        return dxa2points( dxa ) / 72;
    }

    public static float dxa2inch( BigInteger dxa )
    {
        return dxa2points( dxa ) / 72;
    }
}

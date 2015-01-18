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
package fr.opensagres.xdocreport.core.utils;

import static fr.opensagres.xdocreport.core.utils.XMLUtils.prettyPrint;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class XMLUtilsTest
{

    private static final String LINE_SEPARATOR = System.getProperty( "line.separator" );

    @Test
    public void testPrettyPrint()
    {
        String xml = "<a><b>c</b></a>";

        System.out.println( prettyPrint( xml ) );

        long start = System.currentTimeMillis();
        char[] padding = new char[4];
        Arrays.fill( padding, ' ' );
        assertEquals( "<a>" + LINE_SEPARATOR + String.copyValueOf( padding ) + "<b>c</b>" + LINE_SEPARATOR + "</a>"
            + LINE_SEPARATOR, prettyPrint( xml ) );

        System.out.println( "time spent " + ( System.currentTimeMillis() - start ) );
    }

    @Test
    public void testPrettyPrint2Indent()
    {
        int ident = 2;
        String xml = "<a><b>c</b></a>";

        System.out.println( prettyPrint( xml, 2 ) );

        long start = System.currentTimeMillis();

        char[] padding = new char[ident];
        Arrays.fill( padding, ' ' );
        assertEquals( "<a>" + LINE_SEPARATOR + String.copyValueOf( padding ) + "<b>c</b>" + LINE_SEPARATOR + "</a>"
            + LINE_SEPARATOR, prettyPrint( xml, ident ) );

        System.out.println( "time spent " + ( System.currentTimeMillis() - start ) );
    }
}

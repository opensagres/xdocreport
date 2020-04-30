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
package fr.opensagres.xdocreport.document.textstyling.html;

import fr.opensagres.xdocreport.document.textstyling.properties.Color;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HTMLColorParserTest
{

	@Test
	public void parseMinMax()
	{
		assertEquals( new Color( 0, 15, 255 ), HTMLColorParser.parse( "rgb(0,15,255)" ) );
		assertEquals( new Color( 0, 15, 255 ), HTMLColorParser.parse( "#000FFF" ) );
		assertEquals( new Color( 0, 15, 255 ), HTMLColorParser.parse( "#000fff" ) );
	}

	@Test
	public void parseInvalidArgumentReturnsNull()
	{
		assertNull( HTMLColorParser.parse( "rgb(0,15,256)" ) );
		assertNull( HTMLColorParser.parse( "#XXFFAA" ) );
	}
}
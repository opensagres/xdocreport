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
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
		assertEquals( new Color( 0, 17, 255 ), HTMLColorParser.parse( "hsl(236,100%,50%)" ) );
	}

	@Test
	public void parseColornames()
	{
		assertEquals( new Color( 0, 255, 255 ), HTMLColorParser.parse( "aqua" ) );
		assertEquals( new Color( 0, 255, 255 ), HTMLColorParser.parse( "AQUA" ) );
	}

	@Test
	public void parseInvalidArgumentReturnsNull()
	{
		assertNull( HTMLColorParser.parse( "rgb(0,15,256)" ) );
		assertNull( HTMLColorParser.parse( "#XXFFAA" ) );
		assertNull( HTMLColorParser.parse( "hsl(0,101%,50%)" ) );
		assertNull( HTMLColorParser.parse( "nocolorname" ) );
	}

	@Test
	public void parseUnsupportedArgumentReturnsNull()
	{
		// Transparency not support yet
		assertNull( HTMLColorParser.parse( "rgba(0,15,255,0.3)" ) );
		assertNull( HTMLColorParser.parse( "#11FFAA22" ) );
		assertNull( HTMLColorParser.parse( "hsla(0,101%,50%,0.3)" ) );
	}

	@Test
	public void hslToRgb() {
		assertThat(HTMLColorParser.hslToRgb(new int[] {0, 100, 50}), is(rgb(255, 0, 0)));
		assertThat(HTMLColorParser.hslToRgb(new int[] {36, 100, 50}), is(rgb(255, 153, 0)));
		assertThat(HTMLColorParser.hslToRgb(new int[] {36, 0, 50}), is(rgb(127, 127, 127)));
		assertThat(HTMLColorParser.hslToRgb(new int[] {36, 0, 33}), is(rgb(84, 84, 84)));
	}

	private static Matcher<int[]> rgb(final int r, final int g, final int b) {
		return new TypeSafeDiagnosingMatcher<int[]>() {
			@Override
			protected boolean matchesSafely(final int[] rgb, final Description description) {
				description.appendText(String.format("r = %d, g = %d, b = %s", rgb[0], rgb[1], rgb[2]));
				return Arrays.equals(rgb, new int[] { r, g, b });
			}

			@Override
			public void describeTo(final Description description) {
				description.appendText(String.format("r = %d, g = %d, b = %s", r, g, b));
			}
		};
	}

}
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
package fr.opensagres.odfdom.converter.core;


public class Color {

	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color BLUE = new Color(0, 0, 255);
	public static final Color CYAN = new Color(0, 255, 255);
	public static final Color DARK_GRAY = new Color(64, 64, 64);
	public static final Color GREEN = new Color(0, 255, 0);
	public static final Color LIGHT_GRAY = new Color(192, 192, 192);
	public static final Color MAGENTA = new Color(255, 0, 255);
	public static final Color RED = new Color(255, 0, 0);
	public static final Color YELLOW = new Color(255, 255, 0);

	private int argb;

	public Color(final int value) {
		this.argb = value;
	}

	public Color(int r, int g, int b) {
		this(r, g, b, 255);
	}

	public Color(final int red, final int green, final int blue, final int alpha) {
		update(red, green, blue, alpha);
	}

	private void update(final int red, final int green, final int blue,
			final int alpha) {
		isValid(red, "red");
		isValid(green, "green");
		isValid(blue, "blue");
		isValid(alpha, "alpha");
		argb = ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16)
				| ((green & 0xFF) << 8) | ((blue & 0xFF) << 0);
	}

	private void isValid(int value, String name) {
		if (value < 0 || value > 255)
			throw new IllegalArgumentException(name
					+ " is invalid, must be between 0 and 255");

	}

	public int getRGB() {
		return argb;
	}

	public int getRed() {
		return (getRGB() >> 16) & 0xFF;
	}

	public int getGreen() {
		return (getRGB() >> 8) & 0xFF;
	}

	public int getBlue() {
		return (getRGB() >> 0) & 0xFF;
	}

	public int getAlpha() {
		return (getRGB() >> 24) & 0xff;
	}

	public static Color decode(String hexColor) {
		Integer intval = Integer.decode(hexColor);
        int i = intval.intValue();
        return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
	}
}	
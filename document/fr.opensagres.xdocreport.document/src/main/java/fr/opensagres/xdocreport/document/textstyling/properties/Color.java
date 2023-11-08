/**
 * Copyright (C) 2011-2020 The XDocReport Team <xdocreport@googlegroups.com>
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
package fr.opensagres.xdocreport.document.textstyling.properties;

/**
 * Representation of a text color. Only supports RGB.
 */
public class Color
{
    private final int red;

    private final int green;

    private final int blue;

    public Color(final int red, final int green, final int blue )
    {
        this.red = checkColor( red );
        this.green = checkColor( green );
        this.blue = checkColor( blue );
    }

    public int getRed()
    {
        return red;
    }

    public int getGreen()
    {
        return green;
    }

    public int getBlue()
    {
        return blue;
    }

    private int checkColor(final int color )
    {
        if ( color >= 0 && color < 256 )
        {
            return color;
        }
        throw new IllegalArgumentException( "rgb must be between 0 and 255" );
    }

    @Override
    public boolean equals(final Object o )
    {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;

        final Color color = (Color) o;

        if ( getRed() != color.getRed() )
            return false;
        if ( getGreen() != color.getGreen() )
            return false;
        if ( getBlue() != color.getBlue() )
            return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = getRed();
        result = 31 * result + getGreen();
        result = 31 * result + getBlue();
        return result;
    }

    @Override
    public String toString() {
        return "Color{"
                + "red=" + red
                + ", green=" + green
                + ", blue=" + blue
                + '}';
    }
}

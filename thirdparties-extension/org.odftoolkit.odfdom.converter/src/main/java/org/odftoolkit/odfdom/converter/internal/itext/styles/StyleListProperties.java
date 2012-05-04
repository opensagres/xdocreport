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
package org.odftoolkit.odfdom.converter.internal.itext.styles;

import com.lowagie.text.Image;

/**
 * fixes for pdf conversion by Leszek Piotrowicz <leszekp@safe-mail.net>
 */
public class StyleListProperties
{
    private String bulletChar; // label before list item is a char

    private Float height; // height of an image

    private Image image; // label before list item is an image

    private Float marginLeft; // list item text indent

    private StyleNumFormat numFormat; // number format

    private String numPrefix; // label prefix

    private String numSuffix; // label suffix

    private Integer startValue; // numbering start value

    private Float textIndent; // label indent relative to text indent, negative value

    private Float width; // width of an image

    public StyleListProperties()
    {
    }

    public String getBulletChar()
    {
        return bulletChar;
    }

    public void setBulletChar( String bulletChar )
    {
        this.bulletChar = bulletChar;
    }

    public Float getHeight()
    {
        return height;
    }

    public void setHeight( Float height )
    {
        this.height = height;
    }

    public Image getImage()
    {
        return image;
    }

    public void setImage( Image image )
    {
        this.image = image;
    }

    public Float getMarginLeft()
    {
        return marginLeft;
    }

    public void setMarginLeft( Float marginLeft )
    {
        this.marginLeft = marginLeft;
    }

    public StyleNumFormat getNumFormat()
    {
        return numFormat;
    }

    public void setNumFormat( StyleNumFormat numFormat )
    {
        this.numFormat = numFormat;
    }

    public String getNumPrefix()
    {
        return numPrefix;
    }

    public void setNumPrefix( String numPrefix )
    {
        this.numPrefix = numPrefix;
    }

    public String getNumSuffix()
    {
        return numSuffix;
    }

    public void setNumSuffix( String numSuffix )
    {
        this.numSuffix = numSuffix;
    }

    public Integer getStartValue()
    {
        return startValue;
    }

    public void setStartValue( Integer startValue )
    {
        this.startValue = startValue;
    }

    public Float getTextIndent()
    {
        return textIndent;
    }

    public void setTextIndent( Float textIndent )
    {
        this.textIndent = textIndent;
    }

    public Float getWidth()
    {
        return width;
    }

    public void setWidth( Float width )
    {
        this.width = width;
    }
}

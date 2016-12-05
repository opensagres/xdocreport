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
package fr.opensagres.odfdom.converter.pdf.internal.styles;

import java.awt.Color;

import fr.opensagres.odfdom.converter.core.utils.ODFUtils;
import fr.opensagres.odfdom.converter.pdf.internal.ColorRegistry;
import fr.opensagres.xdocreport.utils.BorderType;

public class StyleBorder
{

    private final BorderType borderType;

    private boolean noBorder = false;

    private Color color;

    private Float width;

    public StyleBorder( String border, BorderType borderType )
    {
        this.borderType = borderType;
        noBorder = "none".equals( border );
        if ( noBorder )
        {
            return;
        }

        // ex : 0.035cm, solid, #ff0000
        String[] s = border.split( " " );
        for ( int i = 0; i < s.length; i++ )
        {
            switch ( i )
            {
                case 0:
                    width = ODFUtils.getDimensionAsPoint( s[i] );
                    break;
                case 2:
                    color = ColorRegistry.getInstance().getColor( s[i] );
                    break;
            }
        }
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor( Color color )
    {
        this.color = color;
    }

    public Float getWidth()
    {
        return width;
    }

    public void setWidth( Float width )
    {
        this.width = width;
    }

    public boolean isNoBorder()
    {
        return noBorder;
    }

    public BorderType getBorderType()
    {
        return borderType;
    }
}

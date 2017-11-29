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
package fr.opensagres.poi.xwpf.converter.core;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat;

import fr.opensagres.poi.xwpf.converter.core.utils.RomanAlphabetFactory;
import fr.opensagres.poi.xwpf.converter.core.utils.RomanNumberFactory;
import fr.opensagres.poi.xwpf.converter.core.utils.StringUtils;

public class ListItemContext
{

    private int startIndex = 0;

    private int nb = 0;

    private final int number;

    private final CTLvl lvl;

    private final ListItemContext parent;

    private final String numberText;

    public ListItemContext( CTLvl lvl, int number, ListItemContext parent )
    {
        this.lvl = lvl;
        this.parent = parent;
        this.startIndex = 0;
        if ( lvl != null )
        {
            CTDecimalNumber start = lvl.getStart();
            if ( start != null )
            {
                BigInteger val = start.getVal();
                if ( val != null )
                {
                    this.startIndex = val.intValue();
                }
            }
        }
        this.nb = 0;
        this.number = number + startIndex;
        if ( lvl != null )
        {
            CTNumFmt numFmt = lvl.getNumFmt();
            this.numberText = computeNumberText( this.number, numFmt != null ? numFmt.getVal() : null);
        }
        else
        {
            this.numberText = null;
        }
    }

    public ListItemContext getParent()
    {
        return parent;
    }

    public CTLvl getLvl()
    {
        return lvl;
    }

    public int getNumber()
    {
        return number;
    }

    public String getNumberText()
    {
        return numberText;
    }

    private static String computeNumberText( int number, STNumberFormat.Enum numFmt )
    {
        if ( STNumberFormat.LOWER_LETTER.equals( numFmt ) )
        {
            return RomanAlphabetFactory.getLowerCaseString( number );
        }
        else if ( STNumberFormat.UPPER_LETTER.equals( numFmt ) )
        {
            return RomanAlphabetFactory.getUpperCaseString( number );
        }
        else if ( STNumberFormat.LOWER_ROMAN.equals( numFmt ) )
        {
            return RomanNumberFactory.getLowerCaseString( number );
        }
        else if ( STNumberFormat.UPPER_ROMAN.equals( numFmt ) )
        {
            return RomanNumberFactory.getUpperCaseString( number );
        }
        return String.valueOf( number );
    }

    public ListItemContext createAndAddItem( CTLvl lvl )
    {
        return new ListItemContext( lvl, nb++, this );
    }

    public boolean isRoot()
    {
        return false;
    }

    public String getText()
    {
        String text = lvl.getLvlText().getVal();

        CTNumFmt numFmt = lvl.getNumFmt();
        if ( STNumberFormat.BULLET.equals( numFmt ) )
        {

        }
        else
        {
            List<String> numbers = new ArrayList<String>();
            ListItemContext item = this;
            while ( !item.isRoot() )
            {
                numbers.add( 0, item.getNumberText() );
                item = item.getParent();
            }
            String number = null;
            for ( int i = 0; i < numbers.size(); i++ )
            {
                number = numbers.get( i );
                text = StringUtils.replaceAll( text, "%" + ( i + 1 ), number );
            }
        }
        return text;
    }
}

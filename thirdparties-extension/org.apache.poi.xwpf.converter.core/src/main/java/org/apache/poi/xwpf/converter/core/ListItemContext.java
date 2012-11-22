package org.apache.poi.xwpf.converter.core;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.converter.core.utils.RomanAlphabetFactory;
import org.apache.poi.xwpf.converter.core.utils.RomanNumberFactory;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLvl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat;

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
                text = StringUtils.replaceAll( text, "%" + ( i + startIndex ), number );
            }
        }
        return text;
    }
}

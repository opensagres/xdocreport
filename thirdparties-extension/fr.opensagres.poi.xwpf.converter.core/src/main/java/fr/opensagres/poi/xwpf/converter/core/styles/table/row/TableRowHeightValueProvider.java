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
package fr.opensagres.poi.xwpf.converter.core.styles.table.row;

import static fr.opensagres.poi.xwpf.converter.core.utils.DxaUtil.dxa2points;

import java.math.BigInteger;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import fr.opensagres.poi.xwpf.converter.core.TableHeight;
import fr.opensagres.poi.xwpf.converter.core.TableWidth;
import fr.opensagres.poi.xwpf.converter.core.utils.DxaUtil;

public class TableRowHeightValueProvider
    extends AbstractTableRowValueProvider<TableHeight>
{

    public static final TableRowHeightValueProvider INSTANCE = new TableRowHeightValueProvider();

    @Override
    public TableHeight getValue( CTTrPr trPr )
    {
        if ( trPr == null )
        {
            return null;
        }
        if ( trPr.sizeOfTrHeightArray() == 0 )
        {
            return null;
        }
        // see http://officeopenxml.com/WPtableRowProperties.php
        CTHeight trHeight = trPr.getTrHeightArray( 0 );
        org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule.Enum hRule = trHeight.getHRule();
        boolean minimum = true;
        // hRule -- Specifies the meaning of the height. Possible values :
        if ( hRule != null )
        {
            switch ( hRule.intValue() )
            {
                case STHeightRule.INT_AT_LEAST:
                    // are atLeast (height should be at leasat the
                    // value specified)
                    minimum = true;
                    break;
                case STHeightRule.INT_EXACT:
                    // exact (height should be exactly the value specified)
                    minimum = false;
                    break;
                case STHeightRule.INT_AUTO:
                    // auto (default value--height is determined based on the height of the contents, so the value is
                    // ignored)
                    return null;
            }
        }
        // val -- Specifies the row's height, in twentieths of a point.
        BigInteger value = trHeight.getVal();
        float height = DxaUtil.dxa2points( value );
        return new TableHeight( height, minimum );
    }

}

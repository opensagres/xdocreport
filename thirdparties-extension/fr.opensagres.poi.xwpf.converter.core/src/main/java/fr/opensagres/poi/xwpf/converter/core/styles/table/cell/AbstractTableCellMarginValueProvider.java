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
package fr.opensagres.poi.xwpf.converter.core.styles.table.cell;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import fr.opensagres.poi.xwpf.converter.core.utils.DxaUtil;
import fr.opensagres.poi.xwpf.converter.core.utils.XWPFTableUtil;

public abstract class AbstractTableCellMarginValueProvider
    extends AbstractTableCellValueProvider<Float>
{

    @Override
    public Float getValue( CTTcPr tcPr )
    {
        if ( tcPr == null )
        {
            return null;
        }
        // see http://officeopenxml.com/WPtableCellMargins.php
        CTTcMar margin = tcPr.getTcMar();
        if ( margin != null )
        {
            CTTblWidth tblWidth = getValue( margin );
            if ( tblWidth == null )
            {
                return null;
            }
            // type:
            // => dxa - Specifies that the value is in twentieths of a point (1/1440 of an inch).
            // => nil - Specifies a value of zero
            STTblWidth.Enum type = tblWidth.getType();
            if ( type != null && type.equals( STTblWidth.NIL ) )
            {
                return 0f;
            }
            return DxaUtil.dxa2points( XWPFTableUtil.getTblWidthW( tblWidth ) );
        }
        return null;
    }

    public abstract CTTblWidth getValue( CTTcMar margin );

}

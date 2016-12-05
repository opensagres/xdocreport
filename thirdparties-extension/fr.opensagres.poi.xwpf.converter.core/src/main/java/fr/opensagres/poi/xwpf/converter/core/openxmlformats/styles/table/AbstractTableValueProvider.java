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
package fr.opensagres.poi.xwpf.converter.core.openxmlformats.styles.table;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;

import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;

public abstract class AbstractTableValueProvider<Value>
{

    public Value getValue( CTTbl table, XWPFStylesDocument document )
    {
        Value value = null;
        // from table
        CTTblPr tblPr = table.getTblPr();
        if ( tblPr != null )
        {
            // from table inline
            value = getValue( tblPr, document );
            if ( value != null )
            {
                return value;
            }
            // from table style
            value = getValueFromStyle( tblPr.getTblStyle(), document );
            if ( value != null )
            {
                return value;
            }

        }
        return null;
    }

    private Value getValueFromStyle( CTString styleId, XWPFStylesDocument document )
    {
        // Get the style
        CTStyle style = document.getStyle( styleId );
        return getValueFromStyle( style, document );
    }

    private Value getValueFromStyle( CTStyle style, XWPFStylesDocument document )
    {
        if ( style != null )
        {
            Value value = getValue( style.getTblPr(), document );
            if ( value != null )
            {
                return value;
            }
            return getValueFromStyle( style.getBasedOn(), document );
        }
        return null;
    }

    public abstract Value getValue( CTTblPr tblPr, XWPFStylesDocument document );

    public abstract Value getValue( CTTblPrBase tblPr, XWPFStylesDocument document );
}

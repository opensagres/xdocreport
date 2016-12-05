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
package fr.opensagres.poi.xwpf.converter.core.styles.table;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;

import fr.opensagres.poi.xwpf.converter.core.TableCellBorder;
import fr.opensagres.poi.xwpf.converter.core.utils.XWPFTableUtil;

public abstract class AbstractTableBorderValueProvider
    extends AbstractTableValueProvider<TableCellBorder>
{

    @Override
    public TableCellBorder getValue( CTTblPr tblPr )
    {
        return getTableCellBorder( tblPr );
    }

    @Override
    public TableCellBorder getValue( CTTblPrBase tblPr )
    {
        return getTableCellBorder( tblPr );
    }

    public TableCellBorder getTableCellBorder( CTTblPrBase tblPr )
    {
        if ( tblPr == null )
        {
            return null;
        }
        CTTblBorders borders = tblPr.getTblBorders();
        return getTableCellBorder( borders );
    }

    private TableCellBorder getTableCellBorder( CTTblBorders borders )
    {
        if ( borders != null )
        {
            CTBorder border = getBorder( borders );
            return XWPFTableUtil.getTableCellBorder( border, false );
        }
        return null;
    }

    public abstract CTBorder getBorder( CTTblBorders borders );

}

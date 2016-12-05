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

import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocDefaults;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPrBase;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblStylePr;

import fr.opensagres.poi.xwpf.converter.core.styles.AbstractValueProvider;
import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;

public abstract class AbstractTableValueProvider<Value>
    extends AbstractValueProvider<Value, XWPFTable>
{

    public CTTblPr getTblPr( XWPFTable table )
    {
        return table.getCTTbl().getTblPr();
    }

    public CTTblPrBase getTblPr( CTStyle style )
    {
        return style.getTblPr();
    }

    public CTTblPr getTblPr( CTDocDefaults docDefaults )
    {
        return null;
    }

    @Override
    public Value getValueFromElement( XWPFTable table, XWPFStylesDocument stylesDocument )
    {
        return getValue( getTblPr( table ) );
    }

    @Override
    protected Value getValueFromStyle( CTStyle style, XWPFStylesDocument stylesDocument )
    {
        return getValue( getTblPr( style ) );
    }

    @Override
    protected Value getValueFromTableStyle( CTTblStylePr tblStylePr, XWPFStylesDocument stylesDocument )
    {
        return null;
    }

    @Override
    protected Value getValueFromDocDefaultsStyle( CTDocDefaults docDefaults, XWPFStylesDocument stylesDocument )
    {
        return getValue( getTblPr( docDefaults ) );
    }

    public abstract Value getValue( CTTblPr tblPr );

    public abstract Value getValue( CTTblPrBase tblPr );

    @Override
    protected String[] getStyleID( XWPFTable table )
    {
        return new String[] { table.getStyleID() };
    }

    @Override
    protected CTStyle getDefaultStyle( XWPFTable table, XWPFStylesDocument stylesDocument )
    {
        return stylesDocument.getDefaultTableStyle();
    }

    @Override
    protected XWPFTableCell getParentTableCell( XWPFTable element )
    {
        return null;
    }
}

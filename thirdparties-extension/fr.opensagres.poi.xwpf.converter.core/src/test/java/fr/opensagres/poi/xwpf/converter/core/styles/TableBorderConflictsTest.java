/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 * <p>
 * All rights reserved.
 * <p>
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 * <p>
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 * <p>
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.poi.xwpf.converter.core.styles;

import fr.opensagres.poi.xwpf.converter.core.BorderSide;
import fr.opensagres.poi.xwpf.converter.core.TableCellBorder;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TableBorderConflictsTest {

    @Test
    public void testBorderConflicts()
            throws Exception {
        // 1) Load docx with Apache POI
        XWPFDocument document = new XWPFDocument(Data.class.getResourceAsStream("TableBordersTest.docx"));

        // Create styles engine
        XWPFStylesDocument stylesDocument = new XWPFStylesDocument(document);

        // Check cells of last row
        List<IBodyElement> elements = document.getBodyElements();
        XWPFTable table = (XWPFTable) elements.get(0);
        stylesDocument.isBorderInside(table.getRow(0).getCell(3), BorderSide.RIGHT);
        int rowIndex = 3;
        {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(0);
            TableCellBorder tableCellBorder = stylesDocument.getTableCellBorderWithConflicts(cell, BorderSide.BOTTOM);
            Assert.assertTrue("Cell border must be inherited from table", tableCellBorder.hasBorder());
        }
        {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(1);
            TableCellBorder tableCellBorder = stylesDocument.getTableCellBorderWithConflicts(cell, BorderSide.BOTTOM);
            Assert.assertFalse("NIL value in cell must override table border", tableCellBorder.hasBorder());
        }
        {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(3);
            TableCellBorder tableCellBorder = stylesDocument.getTableCellBorderWithConflicts(cell, BorderSide.BOTTOM);
            Assert.assertTrue("NONE value in cell must be overridden by table border", tableCellBorder.hasBorder());
        }
    }
}

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
package fr.opensagres.poi.xwpf.converter.core.styles;

import java.util.List;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.junit.Assert;
import org.junit.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc.Enum;

import fr.opensagres.poi.xwpf.converter.core.styles.XWPFStylesDocument;

public class TableCellVerticalAlignmentTestCase
{

    @Test
    public void testParagraphStyles()
        throws Exception
    {
        // 1) Load docx with Apache POI
        XWPFDocument document = new XWPFDocument( Data.class.getResourceAsStream( "TableCellVerticalAlignment.docx" ) );

        // Create styles engine
        XWPFStylesDocument stylesDocument = new XWPFStylesDocument( document );

        // Loop for each paragraph
        List<IBodyElement> elements = document.getBodyElements();
        for ( IBodyElement element : elements )
        {
            if ( element.getElementType() == BodyElementType.TABLE )
            {
                testTable( (XWPFTable) element, stylesDocument );
            }
        }
    }

    private void testTable( XWPFTable table, XWPFStylesDocument stylesDocument )
    {
        List<XWPFTableRow> rows = table.getRows();
        for ( XWPFTableRow row : rows )
        {
            testTableRow( row, stylesDocument );
        }
    }

    private void testTableRow( XWPFTableRow row, XWPFStylesDocument stylesDocument )
    {
        List<XWPFTableCell> cells = row.getTableCells();
        for ( XWPFTableCell cell : cells )
        {
            testTableCell( cell, stylesDocument );
        }
    }

    private void testTableCell( XWPFTableCell cell, XWPFStylesDocument stylesDocument )
    {
        XWPFParagraph paragraph = cell.getParagraphs().get( 0 );
        if ( "A".equals( paragraph.getText() ) )
        {
            testsA( paragraph, stylesDocument );
        }
        else if ( "B".equals( paragraph.getText() ) )
        {
            testsB( paragraph, stylesDocument );
        }
        else if ( "C".equals( paragraph.getText() ) )
        {
            testsC( paragraph, stylesDocument );
        }
        else if ( "D".equals( paragraph.getText() ) )
        {
            testsD( paragraph, stylesDocument );
        }
        else if ( "E".equals( paragraph.getText() ) )
        {
            testsE( paragraph, stylesDocument );
        }
        else if ( "F".equals( paragraph.getText() ) )
        {
            testsF( paragraph, stylesDocument );
        }
        else if ( "G".equals( paragraph.getText() ) )
        {
            testsG( paragraph, stylesDocument );
        }
        else if ( "H".equals( paragraph.getText() ) )
        {
            testsH( paragraph, stylesDocument );
        }
        else if ( "I".equals( paragraph.getText() ) )
        {
            testsI( paragraph, stylesDocument );
        }
    }

    private void testsA( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        // vertical aligment
        XWPFTableCell cell = (XWPFTableCell) paragraph.getBody();
        Enum jc = stylesDocument.getTableCellVerticalAlignment( cell );
        Assert.assertNull( jc );

        // text aligment
        ParagraphAlignment alignment = stylesDocument.getParagraphAlignment( paragraph );
        if ( alignment == null )
        {
            Assert.assertNull( alignment );
        }
        else
        {
            Assert.assertEquals( ParagraphAlignment.LEFT, alignment );
        }
    }

    private void testsB( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {

        // vertical aligment
        XWPFTableCell cell = (XWPFTableCell) paragraph.getBody();
        Enum jc = stylesDocument.getTableCellVerticalAlignment( cell );
        Assert.assertNull( jc );

        // text aligment
        ParagraphAlignment alignment = stylesDocument.getParagraphAlignment( paragraph );
        Assert.assertNotNull( alignment );
        Assert.assertEquals( ParagraphAlignment.CENTER, alignment );

    }

    private void testsC( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        // vertical aligment
        XWPFTableCell cell = (XWPFTableCell) paragraph.getBody();
        Enum jc = stylesDocument.getTableCellVerticalAlignment( cell );
        Assert.assertNull( jc );

        // text aligment
        ParagraphAlignment alignment = stylesDocument.getParagraphAlignment( paragraph );
        Assert.assertEquals( ParagraphAlignment.RIGHT, alignment );
    }

    private void testsD( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        // vertical aligment
        XWPFTableCell cell = (XWPFTableCell) paragraph.getBody();
        Enum jc = stylesDocument.getTableCellVerticalAlignment( cell );
        Assert.assertNotNull( jc );
        Assert.assertEquals( jc.intValue(), STVerticalJc.INT_CENTER );

        // text aligment
        ParagraphAlignment alignment = stylesDocument.getParagraphAlignment( paragraph );
        if ( alignment == null )
        {
            Assert.assertNull( alignment );
        }
        else
        {
            Assert.assertEquals( ParagraphAlignment.LEFT, alignment );
        }
    }

    private void testsE( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        // vertical aligment
        XWPFTableCell cell = (XWPFTableCell) paragraph.getBody();
        Enum jc = stylesDocument.getTableCellVerticalAlignment( cell );
        Assert.assertNotNull( jc );
        Assert.assertEquals( jc.intValue(), STVerticalJc.INT_CENTER );

        // text aligment
        ParagraphAlignment alignment = stylesDocument.getParagraphAlignment( paragraph );
        Assert.assertEquals( ParagraphAlignment.CENTER, alignment );
    }

    private void testsF( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        // vertical aligment
        XWPFTableCell cell = (XWPFTableCell) paragraph.getBody();
        Enum jc = stylesDocument.getTableCellVerticalAlignment( cell );
        Assert.assertNotNull( jc );
        Assert.assertEquals( jc.intValue(), STVerticalJc.INT_CENTER );

        // text aligment
        ParagraphAlignment alignment = stylesDocument.getParagraphAlignment( paragraph );
        Assert.assertEquals( ParagraphAlignment.RIGHT, alignment );
    }

    private void testsG( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        // vertical aligment
        XWPFTableCell cell = (XWPFTableCell) paragraph.getBody();
        Enum jc = stylesDocument.getTableCellVerticalAlignment( cell );
        Assert.assertNotNull( jc );
        Assert.assertEquals( jc.intValue(), STVerticalJc.INT_BOTTOM );

        // text aligment
        ParagraphAlignment alignment = stylesDocument.getParagraphAlignment( paragraph );
        if ( alignment == null )
        {
            Assert.assertNull( alignment );
        }
        else
        {
            Assert.assertEquals( ParagraphAlignment.LEFT, alignment );
        }
    }

    private void testsH( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        // vertical aligment
        XWPFTableCell cell = (XWPFTableCell) paragraph.getBody();
        Enum jc = stylesDocument.getTableCellVerticalAlignment( cell );
        Assert.assertNotNull( jc );
        Assert.assertEquals( jc.intValue(), STVerticalJc.INT_BOTTOM );

        // text aligment
        ParagraphAlignment alignment = stylesDocument.getParagraphAlignment( paragraph );
        Assert.assertEquals( ParagraphAlignment.CENTER, alignment );
    }

    private void testsI( XWPFParagraph paragraph, XWPFStylesDocument stylesDocument )
    {
        // vertical aligment
        XWPFTableCell cell = (XWPFTableCell) paragraph.getBody();
        Enum jc = stylesDocument.getTableCellVerticalAlignment( cell );
        Assert.assertNotNull( jc );
        Assert.assertEquals( jc.intValue(), STVerticalJc.INT_BOTTOM );

        // text aligment
        ParagraphAlignment alignment = stylesDocument.getParagraphAlignment( paragraph );
        Assert.assertEquals( ParagraphAlignment.RIGHT, alignment );
    }
}

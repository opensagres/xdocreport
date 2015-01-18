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
package org.odftoolkit.odfdom.converter.core;

import org.junit.Test;

public abstract class AbstractODFDOMConverterTest
{

    @Test
    public void convertCV()
        throws Exception
    {
        doGenerate( "CV.odt" );
    }

    @Test
    public void convertHeaderFooterTable()
        throws Exception
    {
        doGenerate( "HeaderFooterTable.odt" );
    }

    @Test
    public void convertODTBig()
        throws Exception
    {
        doGenerate( "ODTBig.odt" );
    }

    @Test
    public void testODTLettreRelance()
        throws Exception
    {
        doGenerate( "ODTLettreRelance.odt" );
    }

    @Test
    public void testODTStructures()
        throws Exception
    {
        doGenerate( "ODTStructures.odt" );
    }

    @Test
    public void testParagraphFomatting()
        throws Exception
    {
        doGenerate( "ParagraphFomatting.odt" );
    }

    @Test
    public void Taksee()
        throws Exception
    {
        doGenerate( "Taksee.odt" );
    }
    
    @Test
    public void testColor()
        throws Exception
    {
        doGenerate( "TestColor.odt" );
    }

    @Test
    public void testComplexTable()
        throws Exception
    {
        doGenerate( "TestComplexTable.odt" );
    }

    @Test
    public void testFonts()
        throws Exception
    {
        doGenerate( "TestFonts.odt" );
    }

    @Test
    public void testHeaderFooterPage()
        throws Exception
    {
        doGenerate( "TestHeaderFooterPage.odt" );
    }

    @Test
    public void testLandscapeFormat()
        throws Exception
    {
        doGenerate( "TestLandscapeFormat.odt" );
    }

    @Test
    public void testParagraphWithBackground()
        throws Exception
    {
        doGenerate( "TestParagraphWithBackground.odt" );
    }

    @Test
    public void testParagraphWithBorder()
        throws Exception
    {
        doGenerate( "TestParagraphWithBorder.odt" );
    }

    @Test
    public void testSpacingBefore()
        throws Exception
    {
        doGenerate( "TestSpacingBefore.odt" );
    }

    @Test
    public void testTable()
        throws Exception
    {
        doGenerate( "TestTable.odt" );
    }

    @Test
    public void testTableWithBorder()
        throws Exception
    {
        doGenerate( "TestTableWithBorder.odt" );
    }

    @Test
    public void testTableWithSpace()
        throws Exception
    {
        doGenerate( "TestTableWithSpace.odt" );
    }

    @Test
    public void testTextSectionsAndBreaks()
        throws Exception
    {
        doGenerate( "TestTextSectionsAndBreaks.odt" );
    }

    @Test
    public void testTextSectionsAndTabulators()
        throws Exception
    {
        doGenerate( "TestTextSectionsAndTabulators.odt" );
    }

    @Test
    public void testTitle()
        throws Exception
    {
        doGenerate( "TestTitle.odt" );
    }

    protected abstract void doGenerate( String fileInName )
        throws Exception;
}
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
    public void CV()
        throws Exception
    {
        doGenerate( "CV.odt" );
    }

    @Test
    public void FormattingTests()
        throws Exception
    {
        doGenerate( "FormattingTests.odt" );
    }

    @Test
    public void HeaderFooterTable()
        throws Exception
    {
        doGenerate( "HeaderFooterTable.odt" );
    }

    @Test
    public void Issue175()
        throws Exception
    {
        doGenerate( "Issue175.odt" );
    }

    @Test
    public void Issue191()
        throws Exception
    {
        doGenerate( "Issue191.odt" );
    }

    @Test
    public void Issue192()
        throws Exception
    {
        doGenerate( "Issue192.odt" );
    }

    @Test
    public void Issue193()
        throws Exception
    {
        doGenerate( "Issue193.odt" );
    }

    @Test
    public void Issue194()
        throws Exception
    {
        doGenerate( "Issue194.odt" );
    }

    @Test
    public void Issue203()
        throws Exception
    {
        doGenerate( "Issue203.odt" );
    }

    @Test
    public void Issue247()
        throws Exception
    {
        doGenerate( "Issue247.odt" );
    }

    @Test
    public void Issue251()
        throws Exception
    {
        doGenerate( "Issue251.odt" );
    }

    @Test
    public void Issue271()
        throws Exception
    {
        doGenerate( "Issue271.odt" );
    }

    @Test
    public void Issue366()
        throws Exception
    {
        doGenerate( "Issue366.odt" );
    }

    @Test
    public void Issue377()
        throws Exception
    {
        doGenerate( "Issue377.odt" );
    }

    @Test
    public void Issue682()
        throws Exception
    {
        doGenerate( "Issue682.odt" );
    }
    
    @Test
    public void ODTBig()
        throws Exception
    {
        doGenerate( "ODTBig.odt" );
    }

    @Test
    public void ODTLettreRelance()
        throws Exception
    {
        doGenerate( "ODTLettreRelance.odt" );
    }

    @Test
    public void ODTStructures()
        throws Exception
    {
        doGenerate( "ODTStructures.odt" );
    }

    @Test
    public void ParagraphFomatting()
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
    public void TestColor()
        throws Exception
    {
        doGenerate( "TestColor.odt" );
    }

    @Test
    public void TestComplexTable()
        throws Exception
    {
        doGenerate( "TestComplexTable.odt" );
    }

    @Test
    public void TestFonts()
        throws Exception
    {
        doGenerate( "TestFonts.odt" );
    }

    @Test
    public void TestHeaderFooterPage()
        throws Exception
    {
        doGenerate( "TestHeaderFooterPage.odt" );
    }

    @Test
    public void TestLandscapeFormat()
        throws Exception
    {
        doGenerate( "TestLandscapeFormat.odt" );
    }

    @Test
    public void TestPageNumber()
        throws Exception
    {
        doGenerate( "TestPageNumber.odt" );
    }

    @Test
    public void TestParagraphWithBackground()
        throws Exception
    {
        doGenerate( "TestParagraphWithBackground.odt" );
    }

    @Test
    public void TestParagraphWithBorder()
        throws Exception
    {
        doGenerate( "TestParagraphWithBorder.odt" );
    }

    @Test
    public void TestSpacingBefore()
        throws Exception
    {
        doGenerate( "TestSpacingBefore.odt" );
    }

    @Test
    public void TestTable()
        throws Exception
    {
        doGenerate( "TestTable.odt" );
    }

    @Test
    public void TestTableWithBorder()
        throws Exception
    {
        doGenerate( "TestTableWithBorder.odt" );
    }

    @Test
    public void TestTableWithSpace()
        throws Exception
    {
        doGenerate( "TestTableWithSpace.odt" );
    }

    @Test
    public void TestTextSectionsAndBreaks()
        throws Exception
    {
        doGenerate( "TestTextSectionsAndBreaks.odt" );
    }

    @Test
    public void TestTextSectionsAndTabulators()
        throws Exception
    {
        doGenerate( "TestTextSectionsAndTabulators.odt" );
    }

    @Test
    public void TestTitle()
        throws Exception
    {
        doGenerate( "TestTitle.odt" );
    }
    
    @Test
    public void TestImages()
        throws Exception
    {
        doGenerate( "TestImages.odt" );
    }

    protected abstract void doGenerate( String fileInName )
        throws Exception;
}
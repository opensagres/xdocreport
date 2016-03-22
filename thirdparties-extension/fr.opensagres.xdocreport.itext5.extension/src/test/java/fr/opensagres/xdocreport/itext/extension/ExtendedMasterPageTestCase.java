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
package fr.opensagres.xdocreport.itext.extension;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import junit.framework.TestCase;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;

public class ExtendedMasterPageTestCase
    extends TestCase
{

    public void testExtendedParagraph()
    {

        try
        {
            ExtendedDocument document = new ExtendedDocument( new FileOutputStream( "output.pdf" ) );

            // Create Master Page 1
            MasterPage masterPage1 = createMasterPage( "MP1" );
            // Add Master Page to document
            document.addMasterPage( masterPage1 );

            // Create Master Page 2
            MasterPage masterPage2 = createMasterPage( "MP2" );
            // Add Master Page to document
            document.addMasterPage( masterPage2 );

            // first page
            Paragraph bodyParagraph = new Paragraph();
            bodyParagraph.add( "My Body" );
            document.add( bodyParagraph );

            // second page
            document.newPage();
            bodyParagraph = new Paragraph();
            bodyParagraph.add( "My Body 2" );
            document.add( bodyParagraph );

            // three page (we change master page with MP2)
            document.setActiveMasterPage( masterPage2 );

            document.newPage();
            bodyParagraph = new Paragraph();
            bodyParagraph.add( "My Body 3" );
            document.add( bodyParagraph );

            // four page (we change master page with MP1)
            document.setActiveMasterPage( "MP1" );

            document.newPage();
            bodyParagraph = new Paragraph();
            bodyParagraph.add( "My Body 4" );
            document.add( bodyParagraph );

            document.close();

        }
        catch ( FileNotFoundException e1 )
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        catch ( DocumentException e1 )
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    private MasterPage createMasterPage( String name )
    {
        // Create Header
        MasterPageHeaderFooter header = new MasterPageHeaderFooter();
        PdfPCell headerCell = header.getTableCell();
        Paragraph headerParagraph = new Paragraph();
        headerParagraph.add( name + " - My Header" );
        headerCell.addElement( headerParagraph );
        header.flush();

        // Create Footer
        MasterPageHeaderFooter footer = new MasterPageHeaderFooter();
        Paragraph footerParagraph = new Paragraph();
        footerParagraph.add( name + " - My Footer" );
        PdfPCell footerCell = footer.getTableCell();
        footerCell.addElement( footerParagraph );
        footer.flush();

        MasterPage masterPage = new MasterPage( name );
        masterPage.setHeader( header );
        masterPage.setFooter( footer );

        return masterPage;
    }
}

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
package fr.opensagres.poi.xwpf.converter.pdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.junit.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr;

import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfString;
import com.lowagie.text.pdf.parser.PdfTextExtractor;

/**
 * Links in headers and footers are annotations of the page they are drawn on. They must not break the PDF generation
 * ("not all annotations could be added to the document, the document doesn't have enough pages") and must be on the
 * page and at the place where the header/footer is drawn.
 */
public class HeaderFooterLinksTest
{

    /** Same relationship id in the body, headers and footers, with different targets. */
    private static final String SHARED_ID = "rId100";

    @Test
    public void linksInHeadersAndFootersAreLinkUriAnnotationsOfEachPage()
        throws Exception
    {
        PdfReader pdf = convert( createDocument() );
        try
        {
            // section 1 has 2 pages, section 2 has 1 page
            assertEquals( 3, pdf.getNumberOfPages() );

            assertLinks( pdf, 1, "http://body.example/", "http://h1.example/", "http://h1-table.example/",
                         "http://f1-table.example/" );
            assertLinks( pdf, 2, "http://h1.example/", "http://h1-table.example/", "http://f1-table.example/" );
            assertLinks( pdf, 3, "http://h2.example/", "http://f2.example/" );

            for ( int page = 1; page <= 3; page++ )
            {
                float pageHeight = pdf.getPageSize( page ).getHeight();
                for ( PdfDictionary annot : linkAnnotations( pdf, page ) )
                {
                    String uri = uri( annot );
                    float y = ( (PdfArray) annot.getDirectObject( PdfName.RECT ) ).getAsNumber( 1 ).floatValue();
                    if ( uri.startsWith( "http://h" ) )
                    {
                        assertTrue( uri + " must be in the header of page " + page, y > pageHeight * 0.8f );
                    }
                    else if ( uri.startsWith( "http://f" ) )
                    {
                        assertTrue( uri + " must be in the footer of page " + page, y < pageHeight * 0.2f );
                    }
                }
            }
        }
        finally
        {
            pdf.close();
        }
    }

    @Test
    public void pageAndNumPagesFieldsAreDrawnOnTheirPage()
        throws Exception
    {
        PdfReader pdf = convert( createDocument() );
        try
        {
            PdfTextExtractor extractor = new PdfTextExtractor( pdf );
            assertTrue( extractor.getTextFromPage( 1 ).contains( "Page 1 of 3" ) );
            assertTrue( extractor.getTextFromPage( 2 ).contains( "Page 2 of 3" ) );
            assertTrue( extractor.getTextFromPage( 3 ).contains( "Page 3 of 3" ) );
        }
        finally
        {
            pdf.close();
        }
    }

    // ------------------------------------------------------------------ assertions

    private static void assertLinks( PdfReader pdf, int page, String... expectedUris )
    {
        Set<String> expected = new TreeSet<String>();
        for ( String uri : expectedUris )
        {
            expected.add( uri );
        }
        Set<String> actual = new TreeSet<String>();
        List<PdfDictionary> annotations = linkAnnotations( pdf, page );
        for ( PdfDictionary annot : annotations )
        {
            actual.add( uri( annot ) );
        }
        assertEquals( "links of page " + page, expected, actual );
        assertEquals( "number of annotations of page " + page, expected.size(), annotations.size() );
    }

    private static List<PdfDictionary> linkAnnotations( PdfReader pdf, int page )
    {
        List<PdfDictionary> links = new ArrayList<PdfDictionary>();
        PdfArray annots = pdf.getPageN( page ).getAsArray( PdfName.ANNOTS );
        if ( annots == null )
        {
            return links;
        }
        for ( int i = 0; i < annots.size(); i++ )
        {
            PdfDictionary annot = annots.getAsDict( i );
            assertEquals( PdfName.LINK, annot.getAsName( PdfName.SUBTYPE ) );
            links.add( annot );
        }
        return links;
    }

    private static String uri( PdfDictionary annot )
    {
        PdfDictionary action = annot.getAsDict( PdfName.A );
        assertEquals( PdfName.URI, action.getAsName( PdfName.S ) );
        return ( (PdfString) PdfReader.getPdfObject( action.get( PdfName.URI ) ) ).toUnicodeString();
    }

    // ------------------------------------------------------------------ fixture

    private static PdfReader convert( XWPFDocument document )
        throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfConverter.getInstance().convert( document, out, PdfOptions.create() );
        return new PdfReader( out.toByteArray() );
    }

    /**
     * Section 1 (2 pages) and section 2 (1 page), each one with its own header and footer. Headers and footers have a
     * paragraph with a link and a table with a link. All parts use the relationship id {@link #SHARED_ID}.
     */
    private static XWPFDocument createDocument()
        throws Exception
    {
        XWPFDocument document = new XWPFDocument();
        document.createStyles();

        XWPFParagraph first = document.createParagraph();
        first.createRun().setText( "Section 1, page 1 " );
        addLink( first.getCTP(), document.getPackagePart(), SHARED_ID, "http://body.example/", "body link" );
        XWPFParagraph pageBreak = document.createParagraph();
        pageBreak.setPageBreak( true );
        document.createParagraph().createRun().setText( "Section 1, page 2" );

        // the last paragraph of section 1 holds its sectPr
        XWPFParagraph lastOfSection1 = document.createParagraph();
        lastOfSection1.createRun().setText( "end of section 1" );
        CTSectPr section1 = lastOfSection1.getCTP().addNewPPr().addNewSectPr();
        configurePage( section1 );

        document.createParagraph().createRun().setText( "Section 2, page 3" );
        // POI always attaches headers/footers to the body sectPr: create them there, then move section 1's
        CTSectPr bodySectPr = document.getDocument().getBody().addNewSectPr();
        configurePage( bodySectPr );
        addHeaderAndFooter( new XWPFHeaderFooterPolicy( document, bodySectPr ), "h1", "f1", true );
        moveReferences( bodySectPr, section1 );
        addHeaderAndFooter( new XWPFHeaderFooterPolicy( document, bodySectPr ), "h2", "f2", false );

        // write and read it again so that POI loads the relationships of each part
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.write( out );
        document.close();
        return new XWPFDocument( new ByteArrayInputStream( out.toByteArray() ) );
    }

    private static void moveReferences( CTSectPr from, CTSectPr to )
    {
        while ( from.sizeOfHeaderReferenceArray() > 0 )
        {
            to.addNewHeaderReference().set( from.getHeaderReferenceArray( 0 ) );
            from.removeHeaderReference( 0 );
        }
        while ( from.sizeOfFooterReferenceArray() > 0 )
        {
            to.addNewFooterReference().set( from.getFooterReferenceArray( 0 ) );
            from.removeFooterReference( 0 );
        }
    }

    private static void moveReferences( CTSectPr from, CTSectPr to )
    {
        while ( from.sizeOfHeaderReferenceArray() > 0 )
        {
            to.addNewHeaderReference().set( from.getHeaderReferenceArray( 0 ) );
            from.removeHeaderReference( 0 );
        }
        while ( from.sizeOfFooterReferenceArray() > 0 )
        {
            to.addNewFooterReference().set( from.getFooterReferenceArray( 0 ) );
            from.removeFooterReference( 0 );
        }
    }

    private static void configurePage( CTSectPr sectPr )
    {
        sectPr.addNewPgSz().setW( BigInteger.valueOf( 11906 ) );
        sectPr.getPgSz().setH( BigInteger.valueOf( 16838 ) );
        sectPr.addNewPgMar().setTop( BigInteger.valueOf( 1440 ) );
        sectPr.getPgMar().setBottom( BigInteger.valueOf( 1440 ) );
        sectPr.getPgMar().setLeft( BigInteger.valueOf( 1440 ) );
        sectPr.getPgMar().setRight( BigInteger.valueOf( 1440 ) );
        sectPr.getPgMar().setHeader( BigInteger.valueOf( 720 ) );
        sectPr.getPgMar().setFooter( BigInteger.valueOf( 720 ) );
    }

    private static void addHeaderAndFooter( XWPFHeaderFooterPolicy policy, String headerName, String footerName,
                                            boolean withTables )
        throws Exception
    {
        XWPFHeader header = policy.createHeader( STHdrFtr.DEFAULT );
        XWPFParagraph headerParagraph = header.createParagraph();
        addLink( headerParagraph.getCTP(), header.getPackagePart(), SHARED_ID, "http://" + headerName + ".example/",
                 "header link" );
        if ( withTables )
        {
            XWPFTable table = header.createTable( 1, 1 );
            table.getCTTbl().addNewTblGrid().addNewGridCol().setW( BigInteger.valueOf( 5000 ) );
            addLink( table.getRow( 0 ).getCell( 0 ).getParagraphArray( 0 ).getCTP(), header.getPackagePart(),
                     "rId101", "http://" + headerName + "-table.example/", "header table link" );
        }

        XWPFFooter footer = policy.createFooter( STHdrFtr.DEFAULT );
        if ( withTables )
        {
            XWPFTable table = footer.createTable( 1, 1 );
            table.getCTTbl().addNewTblGrid().addNewGridCol().setW( BigInteger.valueOf( 5000 ) );
            XWPFParagraph cell = table.getRow( 0 ).getCell( 0 ).getParagraphArray( 0 );
            addLink( cell.getCTP(), footer.getPackagePart(), SHARED_ID, "http://" + footerName + "-table.example/",
                     "footer table link" );
        }
        else
        {
            addLink( footer.createParagraph().getCTP(), footer.getPackagePart(), SHARED_ID,
                     "http://" + footerName + ".example/", "footer link" );
        }
        XWPFParagraph pageParagraph = footer.createParagraph();
        pageParagraph.createRun().setText( "Page " );
        addField( pageParagraph.getCTP(), "PAGE", "1" );
        pageParagraph.createRun().setText( " of " );
        addField( pageParagraph.getCTP(), "NUMPAGES", "1" );
    }

    private static void addLink( CTP paragraph, PackagePart part, String id, String url, String text )
    {
        part.addExternalRelationship( url, XWPFRelation.HYPERLINK.getRelation(), id );
        CTHyperlink link = paragraph.addNewHyperlink();
        link.setId( id );
        link.addNewR().addNewT().setStringValue( text );
    }

    private static void addField( CTP paragraph, String instruction, String cachedResult )
    {
        paragraph.addNewR().addNewFldChar().setFldCharType( STFldCharType.BEGIN );
        paragraph.addNewR().addNewInstrText().setStringValue( " " + instruction + " " );
        paragraph.addNewR().addNewFldChar().setFldCharType( STFldCharType.SEPARATE );
        paragraph.addNewR().addNewT().setStringValue( cachedResult );
        paragraph.addNewR().addNewFldChar().setFldCharType( STFldCharType.END );
    }
}

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
package fr.opensagres.poi.xwpf.converter.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFHeaderFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFSDT;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.junit.Test;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPTab;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr;

/**
 * Relationship ids (rId) are only unique inside one OPC part: the body, each header and each footer may use the same
 * id for different hyperlink targets.
 */
public class HyperlinkRelationshipScopeTest
{

    private static final String SHARED_ID = "rId100";

    private static final String OTHER_REL_TYPE = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/oleObject";

    @Test
    public void hyperlinksAreResolvedInThePartOfTheirParagraph()
        throws Exception
    {
        XWPFDocument document = newDocument();

        addLinkParagraph( document.createParagraph(), document.getPackagePart(), SHARED_ID, "http://body.example/",
                          "body" );

        XWPFHeaderFooterPolicyHelper policy = new XWPFHeaderFooterPolicyHelper( document );
        XWPFHeader header = policy.header;
        addLinkParagraph( header.createParagraph(), header.getPackagePart(), SHARED_ID, "http://header.example/",
                          "header" );
        XWPFTable headerTable = withGrid( header.createTable( 1, 1 ) );
        addLinkParagraph( headerTable.getRow( 0 ).getCell( 0 ).getParagraphArray( 0 ), header.getPackagePart(),
                          "rId101", "http://header-table.example/", "headerTable" );

        XWPFFooter footer = policy.footer;
        addLinkParagraph( footer.createParagraph(), footer.getPackagePart(), SHARED_ID, "http://footer.example/",
                          "footer" );
        XWPFTable footerTable = withGrid( footer.createTable( 1, 1 ) );
        // same id as the body link, different target in the footer part
        addLinkParagraph( footerTable.getRow( 0 ).getCell( 0 ).getParagraphArray( 0 ), footer.getPackagePart(),
                          "rId102", "http://footer-table.example/", "footerTable" );

        Map<String, String> urls = collectUrls( reload( document ) );

        assertEquals( "http://body.example/", urls.get( "body" ) );
        assertEquals( "http://header.example/", urls.get( "header" ) );
        assertEquals( "http://header-table.example/", urls.get( "headerTable" ) );
        assertEquals( "http://footer.example/", urls.get( "footer" ) );
        assertEquals( "http://footer-table.example/", urls.get( "footerTable" ) );
    }

    @Test
    public void headerLinkWithoutMatchingBodyRelationshipIsResolved()
        throws Exception
    {
        XWPFDocument document = newDocument();
        document.createParagraph().createRun().setText( "no link in body" );

        XWPFHeader header = new XWPFHeaderFooterPolicyHelper( document ).header;
        addLinkParagraph( header.createParagraph(), header.getPackagePart(), "rIdHeaderOnly",
                          "http://header-only.example/", "header" );

        Map<String, String> urls = collectUrls( reload( document ) );

        assertEquals( "http://header-only.example/", urls.get( "header" ) );
    }

    @Test
    public void localAnchorIsKept()
        throws Exception
    {
        XWPFDocument document = newDocument();
        XWPFHeader header = new XWPFHeaderFooterPolicyHelper( document ).header;
        XWPFParagraph paragraph = header.createParagraph();
        CTHyperlink link = paragraph.getCTP().addNewHyperlink();
        link.setAnchor( "bookmark1" );
        link.addNewR().addNewT().setStringValue( "anchor" );

        Map<String, String> urls = collectUrls( reload( document ) );

        assertEquals( "#bookmark1", urls.get( "anchor" ) );
    }

    @Test
    public void relationshipOfAnotherTypeIsNotUsedAsHyperlink()
        throws Exception
    {
        XWPFDocument document = newDocument();
        // the body owns a real hyperlink with the same id
        addLinkParagraph( document.createParagraph(), document.getPackagePart(), SHARED_ID, "http://body.example/",
                          "body" );

        XWPFHeader header = new XWPFHeaderFooterPolicyHelper( document ).header;
        XWPFParagraph paragraph = header.createParagraph();
        header.getPackagePart().addExternalRelationship( "http://not-a-link.example/", OTHER_REL_TYPE, SHARED_ID );
        CTHyperlink link = paragraph.getCTP().addNewHyperlink();
        link.setId( SHARED_ID );
        link.setAnchor( "fallback" );
        link.addNewR().addNewT().setStringValue( "other" );

        Map<String, String> urls = collectUrls( reload( document ) );

        assertEquals( "http://body.example/", urls.get( "body" ) );
        assertEquals( "#fallback", urls.get( "other" ) );
    }

    @Test
    public void unknownRelationshipIdGivesNoUrl()
        throws Exception
    {
        XWPFDocument document = newDocument();
        XWPFParagraph paragraph = document.createParagraph();
        CTHyperlink link = paragraph.getCTP().addNewHyperlink();
        link.setId( "rIdMissing" );
        link.addNewR().addNewT().setStringValue( "missing" );

        Map<String, String> urls = collectUrls( reload( document ) );

        assertNull( urls.get( "missing" ) );
    }

    // ------------------------------------------------------------------ helpers

    private static XWPFTable withGrid( XWPFTable table )
    {
        table.getCTTbl().addNewTblGrid().addNewGridCol().setW( java.math.BigInteger.valueOf( 5000 ) );
        return table;
    }

    private static XWPFDocument newDocument()
    {
        XWPFDocument document = new XWPFDocument();
        document.createStyles();
        document.getDocument().getBody().addNewSectPr();
        return document;
    }

    private static void addLinkParagraph( XWPFParagraph paragraph, PackagePart part, String id, String url,
                                          String text )
    {
        part.addExternalRelationship( url, XWPFRelation.HYPERLINK.getRelation(), id );
        CTHyperlink link = paragraph.getCTP().addNewHyperlink();
        link.setId( id );
        CTR run = link.addNewR();
        run.addNewT().setStringValue( text );
    }

    private static XWPFDocument reload( XWPFDocument document )
        throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.write( out );
        document.close();
        return new XWPFDocument( new ByteArrayInputStream( out.toByteArray() ) );
    }

    private static Map<String, String> collectUrls( XWPFDocument document )
        throws Exception
    {
        RecordingVisitor visitor = new RecordingVisitor( document );
        visitor.start();
        return visitor.urls;
    }

    /** Creates the default header and footer of the document. */
    private static class XWPFHeaderFooterPolicyHelper
    {
        final XWPFHeader header;

        final XWPFFooter footer;

        XWPFHeaderFooterPolicyHelper( XWPFDocument document )
        {
            org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy policy = document.createHeaderFooterPolicy();
            this.header = policy.createHeader( STHdrFtr.DEFAULT );
            this.footer = policy.createFooter( STHdrFtr.DEFAULT );
        }
    }

    private static class MasterPage
        implements IXWPFMasterPage<Object>
    {
        private final CTSectPr sectPr;

        private int type;

        MasterPage( CTSectPr sectPr )
        {
            this.sectPr = sectPr;
        }

        public Object getHeader()
        {
            return null;
        }

        public void setHeader( Object header )
        {
        }

        public Object getFooter()
        {
            return null;
        }

        public void setFooter( Object footer )
        {
        }

        public CTSectPr getSectPr()
        {
            return sectPr;
        }

        public boolean setType( int type )
        {
            this.type = type;
            return true;
        }

        public int getType()
        {
            return type;
        }
    }

    /** Visits the body, headers and footers and records the url given to each run, by run text. */
    private static class RecordingVisitor
        extends XWPFDocumentVisitor<Object, Options, MasterPage>
    {
        final Map<String, String> urls = new HashMap<String, String>();

        RecordingVisitor( XWPFDocument document )
            throws Exception
        {
            super( document, new Options() );
        }

        @Override
        protected void visitRun( XWPFRun run, boolean pageNumber, String url, Object container )
        {
            urls.put( run.text(), url );
        }

        @Override
        public MasterPage createMasterPage( CTSectPr sectPr )
        {
            return new MasterPage( sectPr );
        }

        @Override
        public void setActiveMasterPage( MasterPage currentMasterPage )
        {
        }

        @Override
        protected void visitHeader( XWPFHeader header, CTHdrFtrRef headerRef, CTSectPr sectPr, MasterPage masterPage )
            throws Exception
        {
            visitPart( header );
        }

        @Override
        protected void visitFooter( XWPFFooter footer, CTHdrFtrRef footerRef, CTSectPr sectPr, MasterPage masterPage )
            throws Exception
        {
            visitPart( footer );
        }

        private void visitPart( XWPFHeaderFooter part )
            throws Exception
        {
            List<IBodyElement> elements = getBodyElements( part );
            visitBodyElements( elements, new Object() );
        }

        @Override
        protected Object startVisitDocument()
        {
            return new Object();
        }

        @Override
        protected void endVisitDocument()
        {
        }

        @Override
        protected Object startVisitSDT( XWPFSDT contents, Object container )
        {
            return new Object();
        }

        @Override
        protected void endVisitSDT( XWPFSDT contents, Object container, Object sdtContainer )
        {
        }

        @Override
        protected Object startVisitParagraph( XWPFParagraph paragraph, ListItemContext itemContext, Object parent )
        {
            return new Object();
        }

        @Override
        protected void endVisitParagraph( XWPFParagraph paragraph, Object parent, Object paragraphContainer )
        {
        }

        @Override
        protected void visitEmptyRun( Object container )
        {
        }

        @Override
        protected void visitText( CTText ctText, boolean pageNumber, Object container )
        {
        }

        @Override
        protected void visitTab( CTPTab o, Object container )
        {
        }

        @Override
        protected void visitTabs( CTTabs tabs, Object container )
        {
        }

        @Override
        protected void visitBookmark( CTBookmark bookmark, XWPFParagraph paragraph, Object container )
        {
        }

        @Override
        protected void addNewLine( CTBr br, Object container )
        {
        }

        @Override
        protected void pageBreak()
        {
        }

        @Override
        protected Object startVisitTable( XWPFTable table, float[] colWidths, Object container )
        {
            return new Object();
        }

        @Override
        protected void endVisitTable( XWPFTable table, Object parent, Object tableContainer )
        {
        }

        @Override
        protected Object startVisitTableCell( XWPFTableCell cell, Object tableContainer, boolean firstRow,
                                              boolean lastRow, boolean firstCol, boolean lastCol,
                                              List<XWPFTableCell> vMergeCells )
        {
            return new Object();
        }

        @Override
        protected void endVisitTableCell( XWPFTableCell cell, Object tableContainer, Object cellContainer )
        {
        }

        @Override
        protected void visitPicture( org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture picture,
                                     Float offsetX,
                                     org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromH.Enum relativeFromH,
                                     Float offsetY,
                                     org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV.Enum relativeFromV,
                                     org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapText.Enum wrapText,
                                     Object parent )
        {
        }

        @Override
        protected void visitVmlPicture( org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture picture,
                                        Object parent )
        {
        }
    }
}

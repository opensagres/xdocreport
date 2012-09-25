package org.apache.poi.xwpf.converter.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr;

/**
 * See http://officeopenxml.com/WPsection.php
 */
public class MasterPageManager
    extends LinkedList<CTSectPr>
{

    private final XWPFDocument document;

    private final XWPFDocumentVisitor visitor;

    private final CTSectPr bodySectPr;

    private CTSectPr currentSectPr;

    private final Map<CTSectPr, IXWPFMasterPage> masterPages;

    private boolean initialized;

    private boolean changeSection;

    private int nbPages;

    private IXWPFMasterPage currentMasterPage;

    public MasterPageManager( XWPFDocument document, XWPFDocumentVisitor visitor )
        throws Exception
    {
        this.document = document;
        this.visitor = visitor;
        this.bodySectPr = document.getDocument().getBody().getSectPr();
        this.masterPages = new HashMap<CTSectPr, IXWPFMasterPage>();
        this.initialized = false;
        this.changeSection = false;
        this.nbPages = 0;
    }

    public void initialize()
        throws Exception
    {
        this.initialized = true;
        compute( document );
        if ( isEmpty() )
        {
            currentSectPr = bodySectPr;
            addSection( currentSectPr, false );
            fireSectionChanged( currentSectPr );
        }
        else
        {
            currentSectPr = super.poll();
            fireSectionChanged( currentSectPr );
        }
    }

    private void compute( XWPFDocument document )
        throws Exception
    {
        for ( IBodyElement bodyElement : document.getBodyElements() )
        {
            if ( bodyElement.getElementType() == BodyElementType.PARAGRAPH )
            {
                XWPFParagraph paragraph = (XWPFParagraph) bodyElement;

                CTSectPr sectPr = getSectPr( paragraph );
                if ( sectPr != null )
                {
                    addSection( sectPr, true );
                }
            }
        }
        addSection( bodySectPr, false );
    }

    public CTSectPr getBodySectPr()
    {
        return bodySectPr;
    }

    public void update( XWPFParagraph paragraph )
    {
        if ( changeSection )
        {
            changeSection = false;
            if ( !isEmpty() )
            {
                currentSectPr = super.poll();
                fireSectionChanged( currentSectPr );

            }
            else
            {
                currentSectPr = bodySectPr;
                fireSectionChanged( currentSectPr );
            }
        }
        else
        {
            CTSectPr sectPr = getSectPr( paragraph );
            if ( sectPr != null )
            {
                currentSectPr = sectPr;
                changeSection = true;
            }
        }
    }

    private void fireSectionChanged( CTSectPr sectPr )
    {
        currentMasterPage = getMasterPage( sectPr );
        visitor.setActiveMasterPage( currentMasterPage );
    }

    private void addSection( CTSectPr sectPr, boolean pushIt )
        throws Exception
    {
        if ( pushIt )
        {
            super.add( sectPr );
        }

        // For each <w:sectPr of the word/document.xml, create a master page.

        IXWPFMasterPage masterPage = visitor.createMasterPage( sectPr );
        visitHeadersFooters( masterPage, sectPr );
        masterPages.put( sectPr, masterPage );

    }

    // ------------------------------ Header/Footer visitor -----------

    private void visitHeadersFooters( IXWPFMasterPage masterPage, CTSectPr sectPr )
        throws Exception
    {
        // see titlePg at http://officeopenxml.com/WPsection.php i
        // Specifies whether the section should have a different header and footer
        // for its first page.
        // If the element is set to true (e.g., <w:titlePg/>),
        // then the section will use a first page header;
        // if it is false (e.g., <w:titlePg w:val="false"/>)
        // (the default value), then the first page uses the odd page header. If the element is set to true but the
        // first page header type is omitted, then a blank header is created.
        boolean ignoreFirstHeaderFooter = !XWPFUtils.isCTOnOff( sectPr.getTitlePg() );

        Collection<CTHdrFtrRef> headersRef = sectPr.getHeaderReferenceList();
        Collection<CTHdrFtrRef> footersRef = sectPr.getFooterReferenceList();

        boolean firstHeaderFooter = false;
        for ( CTHdrFtrRef headerRef : headersRef )
        {
            STHdrFtr type = headerRef.xgetType();
            firstHeaderFooter = ( type != null && type.enumValue() == STHdrFtr.FIRST );
            if ( !firstHeaderFooter || ( firstHeaderFooter && !ignoreFirstHeaderFooter ) )
            {
                masterPage.setType( type.enumValue().intValue() );
                visitor.visitHeaderRef( headerRef, sectPr, masterPage );
            }
        }

        for ( CTHdrFtrRef footerRef : footersRef )
        {
            STHdrFtr type = footerRef.xgetType();
            firstHeaderFooter = ( type != null && type.enumValue() == STHdrFtr.FIRST );
            if ( !firstHeaderFooter || ( firstHeaderFooter && !ignoreFirstHeaderFooter ) )
            {
                masterPage.setType( type.enumValue().intValue() );
                visitor.visitFooterRef( footerRef, sectPr, masterPage );
            }
        }
        masterPage.setType( STHdrFtr.INT_FIRST );

    }

    private CTSectPr getSectPr( XWPFParagraph paragraph )
    {
        CTPPr ppr = paragraph.getCTP().getPPr();
        if ( ppr != null )
        {
            return ppr.getSectPr();
        }
        return null;
    }

    public IXWPFMasterPage getMasterPage( CTSectPr sectPr )
    {
        return masterPages.get( sectPr );
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    public void onNewPage()
    {

        if ( currentMasterPage != null )
        {
            int oldType = currentMasterPage.getType();
            int newType = STHdrFtr.INT_DEFAULT;
            if ( nbPages % 2 == 0 )
            {
                newType = STHdrFtr.INT_EVEN;
            }
            if ( oldType != newType )
            {
                boolean changed = currentMasterPage.setType( newType );
                if ( changed )
                {
                    // fireSectionChanged( currentMasterPage.getSectPr() );
                }
            }
        }
    }
    // nbPages++;
    //
}

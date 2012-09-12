package org.apache.poi.xwpf.converter.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtrRef;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

public class MasterPageManager
    extends Stack<CTSectPr>
{

    private final XWPFDocument document;

    private final XWPFDocumentVisitor visitor;

    private final CTSectPr bodySectPr;

    private CTSectPr currentSectPr;

    private final Map<CTSectPr, IXWPFMasterPage> masterPages;

    private boolean initialized;

    public MasterPageManager( XWPFDocument document, XWPFDocumentVisitor visitor )
        throws Exception
    {
        this.document = document;
        this.visitor = visitor;
        this.bodySectPr = document.getDocument().getBody().getSectPr();
        this.masterPages = new HashMap<CTSectPr, IXWPFMasterPage>();
        this.initialized = false;
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
        if ( !isEmpty() )
        {
            if ( currentSectPr == null )
            {
                currentSectPr = super.pop();
                fireSectionChanged( currentSectPr );
            }
            else
            {
                CTSectPr sectPr = getSectPr( paragraph );
                if ( sectPr != null )
                {
                    if ( !currentSectPr.equals( sectPr ) )
                    {
                        currentSectPr = sectPr;
                        fireSectionChanged( currentSectPr );
                    }
                }
            }
        }
    }

    private void fireSectionChanged( CTSectPr sectPr )
    {
        visitor.setActiveMasterPage( getMasterPage( sectPr ) );
    }

    private void addSection( CTSectPr sectPr, boolean pushIt )
        throws Exception
    {
        if ( pushIt )
        {
            super.push( sectPr );
        }

        // For each <w:sectPr of the word/document.xml, create a master page.

        IXWPFMasterPage masterPage = visitor.createMasterPage( sectPr );
        visitHeadersFooters( masterPage, sectPr );
        masterPages.put( sectPr, masterPage );

        visitor.sectionAdded( sectPr );
    }

    // ------------------------------ Header/Footer visitor -----------

    private void visitHeadersFooters( IXWPFMasterPage masterPage, CTSectPr sectPr )
        throws Exception
    {
        Collection<CTHdrFtrRef> headersRef = sectPr.getHeaderReferenceList();
        Collection<CTHdrFtrRef> footersRef = sectPr.getFooterReferenceList();

        for ( CTHdrFtrRef headerRef : headersRef )
        {
            visitor.visitHeaderRef( headerRef, sectPr, masterPage );
        }

        for ( CTHdrFtrRef footerRef : footersRef )
        {
            visitor.visitFooterRef( footerRef, sectPr, masterPage );
        }
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

}

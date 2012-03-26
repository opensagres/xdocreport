package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes;

import static fr.opensagres.xdocreport.document.docx.DocxConstants.ID_ATTR;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.W_NS;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isFldSimple;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isP;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import fr.opensagres.xdocreport.core.utils.XMLUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.DocXBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.docx.preprocessor.DocxBufferedDocument;
import fr.opensagres.xdocreport.document.docx.preprocessor.FldSimpleBufferedRegion;
import fr.opensagres.xdocreport.document.docx.preprocessor.PBufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;

public abstract class AbstractNotesBufferedDocument
    extends DocxBufferedDocument
{

    private AbstractNoteBufferedRegion currentNoteRegion;

    public AbstractNotesBufferedDocument( DocXBufferedDocumentContentHandler handler )
    {
        super( handler );
    }

    @Override
    protected BufferedElement createElement( BufferedElement parent, String uri, String localName, String name,
                                             Attributes attributes )
        throws SAXException
    {
        if ( isNote( uri, localName, name ) )
        {
            // <w:footnote w:id="1"> or <w:endnote w:id="1">
            int idIndex = attributes.getIndex( W_NS, ID_ATTR );
            if ( idIndex != -1 )
            {
                String attrName = attributes.getQName( idIndex );
                AttributesImpl attributesImpl = XMLUtils.toAttributesImpl( attributes );
                attributesImpl.removeAttribute( idIndex );
                String id = attributes.getValue( idIndex );
                // w:footnote element or w:endnote 
                currentNoteRegion = createNoteBufferedRegion( uri, localName, name, attributesImpl );
                currentNoteRegion.setId( attrName, id );
                return currentNoteRegion;
            }
        }
        return super.createElement( parent, uri, localName, name, attributes );
    }

    @Override
    public void onEndEndElement( String uri, String localName, String name )
    {

        if ( currentNoteRegion != null )
        {
            if ( isFldSimple( uri, localName, name ) && getCurrentFldSimpleRegion() != null )
            {
                FldSimpleBufferedRegion fdldSimpleRegion = getCurrentFldSimpleRegion();
                super.onEndEndElement( uri, localName, name );
                if ( fdldSimpleRegion.getName() != null && !fdldSimpleRegion.isReseted() )
                {
                    currentNoteRegion.setContainsField( true );
                }
                return;
            }

            if ( isP( uri, localName, name ) && getCurrentPRegion() != null )
            {
                PBufferedRegion pRegion = getCurrentPRegion();
                super.onEndEndElement( uri, localName, name );
                if ( pRegion.isContainsField() && !pRegion.isReseted() )
                {
                    currentNoteRegion.setContainsField( true );
                }
                return;
            }

            if ( isNote( uri, localName, name ) )
            {
                currentNoteRegion.process();
                currentNoteRegion = null;
            }
        }
        super.onEndEndElement( uri, localName, name );
    }

    protected abstract AbstractNoteBufferedRegion createNoteBufferedRegion( String uri, String localName, String name,
                                                                            Attributes attributes );

    protected abstract boolean isNote( String uri, String localName, String name );
}

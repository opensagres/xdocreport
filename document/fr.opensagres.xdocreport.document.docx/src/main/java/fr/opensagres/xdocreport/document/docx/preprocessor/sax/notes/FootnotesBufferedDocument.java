package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes;

import static fr.opensagres.xdocreport.document.docx.DocxConstants.ID_ATTR;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.W_NS;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isFldSimple;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isFootnote;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isP;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import fr.opensagres.xdocreport.core.utils.XMLUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.DocxBufferedDocument;
import fr.opensagres.xdocreport.document.docx.preprocessor.FldSimpleBufferedRegion;
import fr.opensagres.xdocreport.document.docx.preprocessor.PBufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;

public class FootnotesBufferedDocument
    extends DocxBufferedDocument
{

    private FootnoteBufferedRegion currentFootnoteRegion;

    public FootnotesBufferedDocument( DocxFootnotesDocumentContentHandler handler )
    {
        super( handler );        
    }

    @Override
    protected BufferedElement createElement( BufferedElement parent, String uri, String localName, String name,
                                             Attributes attributes )
        throws SAXException
    {
        if ( isFootnote( uri, localName, name ) )
        {
            // <w:footnote w:id="1">
            int idIndex = attributes.getIndex( W_NS, ID_ATTR );
            if ( idIndex != -1 )
            {
                String attrName = attributes.getQName( idIndex );
                AttributesImpl attributesImpl = XMLUtils.toAttributesImpl( attributes );
                attributesImpl.removeAttribute( idIndex );
                String id = attributes.getValue( idIndex );
                // w:footnote element
                currentFootnoteRegion =
                    new FootnoteBufferedRegion( (DocxFootnotesDocumentContentHandler) handler, getCurrentElement(),
                                                uri, localName, name, attributesImpl );
                currentFootnoteRegion.setId( attrName, id );
                return currentFootnoteRegion;
            }
        }
        return super.createElement( parent, uri, localName, name, attributes );
    }

    @Override
    public void onEndEndElement( String uri, String localName, String name )
    {

        if ( currentFootnoteRegion != null )
        {
            if ( isFldSimple( uri, localName, name ) && getCurrentFldSimpleRegion() != null )
            {
                FldSimpleBufferedRegion fdldSimpleRegion = getCurrentFldSimpleRegion();
                super.onEndEndElement( uri, localName, name );
                if ( fdldSimpleRegion.getName() != null && !fdldSimpleRegion.isReseted() )
                {
                    currentFootnoteRegion.setContainsField( true );
                }
                return;
            }

            if ( isP( uri, localName, name ) && getCurrentPRegion() != null )
            {
                PBufferedRegion pRegion =getCurrentPRegion();
                super.onEndEndElement( uri, localName, name );
                if (pRegion.isContainsField() && !pRegion.isReseted()) {
                    currentFootnoteRegion.setContainsField( true );
                }
                return;
            }
            
            if ( isFootnote( uri, localName, name ) )
            {
                currentFootnoteRegion.process();
                currentFootnoteRegion = null;
            }
        }
        super.onEndEndElement( uri, localName, name );
    }
}

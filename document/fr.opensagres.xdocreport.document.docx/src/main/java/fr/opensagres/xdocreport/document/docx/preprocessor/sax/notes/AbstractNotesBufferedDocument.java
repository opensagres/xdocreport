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
package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes;

import static fr.opensagres.xdocreport.document.docx.DocxConstants.ID_ATTR;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.TYPE_ATTR;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.W_NS;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isFldSimple;
import static fr.opensagres.xdocreport.document.docx.DocxUtils.isP;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import fr.opensagres.xdocreport.core.utils.XMLUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.DocXBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.DocxBufferedDocument;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.FldSimpleBufferedRegion;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.PBufferedRegion;
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
                currentNoteRegion = createNoteBufferedRegion( uri, localName, name, attributesImpl );
                currentNoteRegion.setId( attrName, id );
                currentNoteRegion.setType( attributes.getValue( W_NS, TYPE_ATTR ) );
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
                    // The current footnote/endnote contains fields which must be evaluated by template engine.
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
                    // The current footnote/endnote contains fields which must be evaluated by template engine.
                    currentNoteRegion.setContainsField( true );
                }
                return;
            }

            if ( isNote( uri, localName, name ) )
            {
                // Process the current footnote/endnote
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

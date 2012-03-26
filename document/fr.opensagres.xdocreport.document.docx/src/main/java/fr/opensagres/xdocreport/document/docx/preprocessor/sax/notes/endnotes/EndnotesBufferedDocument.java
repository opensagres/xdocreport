package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.endnotes;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.document.docx.DocxUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.DocXBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.AbstractNoteBufferedRegion;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.AbstractNotesBufferedDocument;

public class EndnotesBufferedDocument
    extends AbstractNotesBufferedDocument
{

    public EndnotesBufferedDocument( DocXBufferedDocumentContentHandler handler )
    {
        super( handler );
    }

    @Override
    protected AbstractNoteBufferedRegion createNoteBufferedRegion( String uri, String localName, String name,
                                                                   Attributes attributes )
    {
        return new EndnoteBufferedRegion( handler, getCurrentElement(), uri, localName, name, attributes );
    }
    
    @Override
    protected boolean isNote( String uri, String localName, String name )
    {
        return DocxUtils.isEndnote( uri, localName, name );
    }
}

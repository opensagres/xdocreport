package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.footnotes;

import java.util.Map;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.document.docx.preprocessor.DocXBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.AbstractNoteReferenceBufferedRegion;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.InitialNoteInfoMap;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.NoteUtils;
import fr.opensagres.xdocreport.document.docx.template.DocxContextHelper;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;

public class FootnoteReferenceBufferedRegion
    extends AbstractNoteReferenceBufferedRegion
{

    public FootnoteReferenceBufferedRegion( DocXBufferedDocumentContentHandler handler, BufferedElement parent,
                                            String uri, String localName, String name, Attributes attributes )
    {
        super( handler, parent, uri, localName, name, attributes );
    }

    @Override
    protected String getNoteRegistryKey()
    {
        return DocxContextHelper.FOOTNOTE_REGISTRY_KEY;
    }
    
    @Override
    protected InitialNoteInfoMap getInitialNoteInfoMap( Map<String, Object> sharedContext )
    {
        return NoteUtils.getInitialFootNoteInfoMap( sharedContext );
    }
}

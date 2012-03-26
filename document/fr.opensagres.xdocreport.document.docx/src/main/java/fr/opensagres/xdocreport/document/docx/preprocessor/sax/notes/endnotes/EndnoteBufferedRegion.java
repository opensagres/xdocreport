package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.endnotes;

import java.util.Map;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.document.docx.preprocessor.DocXBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.AbstractNoteBufferedRegion;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.InitialNoteInfoMap;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes.NoteUtils;
import fr.opensagres.xdocreport.document.docx.template.DocxContextHelper;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;

public class EndnoteBufferedRegion
    extends AbstractNoteBufferedRegion
{

    public EndnoteBufferedRegion( DocXBufferedDocumentContentHandler handler, BufferedElement parent, String uri,
                                  String localName, String name, Attributes attributes )
    {
        super( handler, parent, uri, localName, name, attributes );
    }

    @Override
    protected String getNoteRegistryKey()
    {
        return DocxContextHelper.ENDNOTE_REGISTRY_KEY;
    }

    @Override
    protected InitialNoteInfoMap getInitialNoteInfoMap( Map<String, Object> sharedContext )
    {
        return NoteUtils.getInitialEndNoteInfoMap( sharedContext );
    }

    @Override
    protected void putInitialNoteInfoMap( Map<String, Object> sharedContext, InitialNoteInfoMap infos )
    {
        NoteUtils.putInitialEndNoteInfoMap( sharedContext, infos );
    }
}

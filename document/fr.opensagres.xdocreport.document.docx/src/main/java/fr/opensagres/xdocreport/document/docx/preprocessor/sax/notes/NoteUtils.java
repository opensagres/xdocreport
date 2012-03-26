package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes;

import java.util.Map;

import fr.opensagres.xdocreport.document.docx.template.DocxContextHelper;

public class NoteUtils
{

    /**
     * Returns the {@link InitialNoteInfoMap} from the given sharedContext.
     * 
     * @param sharedContext
     * @return
     */
    public static InitialNoteInfoMap getInitialFootNoteInfoMap( Map<String, Object> sharedContext )
    {
        if ( sharedContext == null )
        {
            return null;
        }
        return (InitialNoteInfoMap) sharedContext.get( DocxContextHelper.FOOTNOTE_REGISTRY_KEY );
    }

    public static void putInitialFootNoteInfoMap( Map<String, Object> sharedContext, InitialNoteInfoMap notesMap )
    {
        sharedContext.put( DocxContextHelper.FOOTNOTE_REGISTRY_KEY, notesMap );
    }

    public static InitialNoteInfoMap getInitialEndNoteInfoMap( Map<String, Object> sharedContext )
    {
        if ( sharedContext == null )
        {
            return null;
        }
        return (InitialNoteInfoMap) sharedContext.get( DocxContextHelper.ENDNOTE_REGISTRY_KEY );
    }

    public static void putInitialEndNoteInfoMap( Map<String, Object> sharedContext, InitialNoteInfoMap notesMap )
    {
        sharedContext.put( DocxContextHelper.ENDNOTE_REGISTRY_KEY, notesMap );
    }
}

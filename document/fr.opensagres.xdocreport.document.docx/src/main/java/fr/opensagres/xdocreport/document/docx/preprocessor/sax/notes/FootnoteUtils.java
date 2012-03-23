package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes;

import java.util.Map;

public class FootnoteUtils
{

    /**
     * Returns the {@link InitialFootNoteInfoMap} from the given sharedContext.
     * 
     * @param sharedContext
     * @return
     */
    public static InitialFootNoteInfoMap getInitialFootNoteInfoMap( Map<String, Object> sharedContext )
    {
        if ( sharedContext == null )
        {
            return null;
        }
        return (InitialFootNoteInfoMap) sharedContext.get( FootnoteRegistry.KEY );
    }

    public static void putInitialFootNoteInfoMap( Map<String, Object> sharedContext, InitialFootNoteInfoMap footnotMap )
    {
        sharedContext.put( FootnoteRegistry.KEY, footnotMap );
    }
}

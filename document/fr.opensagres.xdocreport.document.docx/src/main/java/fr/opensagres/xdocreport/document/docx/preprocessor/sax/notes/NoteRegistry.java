package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.TemplateContextHelper;

public class NoteRegistry
{
    
    public static final String REGISTER_NOTE_METHOD = "registerNote";
    public static final String GET_NOTES_METHOD = "getNotes";

    private Map<String, List<NoteInfo>> notesMap;

    public String registerNote( String id, Object content)
        throws XDocReportException, IOException
    {
        if ( notesMap == null )
        {
            notesMap = new HashMap<String, List<NoteInfo>>();
        }
        List<NoteInfo> footnotes = notesMap.get( id );
        if ( footnotes == null )
        {
            footnotes = new ArrayList<NoteInfo>();
            notesMap.put( id, footnotes );
        }
        String newId = "" + ( footnotes.size() + 1 );
       
        footnotes.add( new NoteInfo( newId, content.toString() ) );
        return newId;
    }
    
    public List<NoteInfo> getNotes( String id )
    {
        if ( notesMap == null )
        {
            return Collections.emptyList();
        }
        List<NoteInfo> footnotes = notesMap.get( id );
        if ( footnotes == null )
        {
            return Collections.emptyList();
        }
        return footnotes;
    }

}

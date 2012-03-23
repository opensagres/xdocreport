package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FootnoteRegistry
{
    public static final String KEY = "___FootnoteRegistry";

    private Map<String, List<FootnoteInfo>> footnotesMap;

    public String registerFootnote( String id, String content )
    {
        if ( footnotesMap == null )
        {
            footnotesMap = new HashMap<String, List<FootnoteInfo>>();
        }
        List<FootnoteInfo> footnotes = footnotesMap.get( id );
        if ( footnotes == null )
        {
            footnotes = new ArrayList<FootnoteInfo>();
            footnotesMap.put( id, footnotes );
        }
        String newId = "" + ( footnotes.size() + 1 );
        footnotes.add( new FootnoteInfo( newId, content ) );
        return newId;
    }

    public List<FootnoteInfo> getFootnotes( String id )
    {
        if ( footnotesMap == null )
        {
            return Collections.emptyList();
        }
        List<FootnoteInfo> footnotes = footnotesMap.get( id );
        if ( footnotes == null )
        {
            return Collections.emptyList();
        }
        return footnotes;
    }
}

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

public class FootnoteRegistry
{
    public static final String KEY = "___FootnoteRegistry";

    private InitialFootNoteInfoMap initialFootNoteInfoMap;
    
    private Map<String, List<FootnoteInfo>> footnotesMap;

    public FootnoteRegistry( InitialFootNoteInfoMap initialFootNoteInfoMap )
    {
        this.initialFootNoteInfoMap=initialFootNoteInfoMap;
    }

    public String registerFootnote( String id, Object content)
        throws XDocReportException, IOException
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
       
        footnotes.add( new FootnoteInfo( newId, content.toString() ) );
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

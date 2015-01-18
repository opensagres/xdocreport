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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.opensagres.xdocreport.core.XDocReportException;

/**
 * Footnote/Endnote information registry.
 */
public class NoteRegistry
{

    public static final String REGISTER_NOTE_METHOD = "registerNote";

    public static final String GET_NOTES_METHOD = "getNotes";

    private Map<String, List<NoteInfo>> notesMap;

    /**
     * Register information (id +content) for footnote/endnote and add it to the list.
     * 
     * @param id
     * @param content
     * @return
     * @throws XDocReportException
     * @throws IOException
     */
    public String registerNote( String id, Object content )
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
        String newId = "" + createNewId();

        footnotes.add( new NoteInfo( newId, content != null ? content.toString() : "" ) );
        return newId;
    }

    /**
     * Returns the list of footnote/endnote informations for the given id.
     * 
     * @param id
     * @return
     */
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

    /**
     * Create a sequencial new id according to current number of notes (resulting of loops included).
     * @return id according to current element size
     */
    private int createNewId()
    {
        int result = 1;

        for ( List<NoteInfo> elements : notesMap.values() )
        {
            result += elements.size();
        }

        return result;
    }

}

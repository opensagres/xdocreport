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

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

/**
 * Information about a footnote or endnote (id + content of the note).
 */
public class NoteInfo
{

    public static final String CONTEXT_KEY = "___NoEscapeNoteInfo";

    public static final String ID_PROPERTY = "id";

    public static final String CONTENT_PROPERTY = "content";

    private final String id;

    private final String content;

    public NoteInfo( String id, String content )
    {
        this.id = id;
        this.content = content;
    }

    /**
     * Returns the id of the footnote/endnote (ex return "1" for <:footnote w:id="1").
     * 
     * @return
     */
    public String getId()
    {
        return id;
    }

    /**
     * Returns the inner text of the footnote/endnote.
     * 
     * @return
     */
    public String getContent()
    {
        return content;
    }

}

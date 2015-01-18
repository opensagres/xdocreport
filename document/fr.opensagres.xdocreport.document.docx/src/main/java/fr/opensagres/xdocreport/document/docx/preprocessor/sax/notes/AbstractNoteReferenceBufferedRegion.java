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

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.document.docx.preprocessor.sax.DocXBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedAttribute;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public abstract class AbstractNoteReferenceBufferedRegion
    extends BufferedElement
{

    private static final String CONTEXT_NOTE = "___note";

    private final DocXBufferedDocumentContentHandler handler;

    private BufferedAttribute idAttribute;

    public AbstractNoteReferenceBufferedRegion( DocXBufferedDocumentContentHandler handler, BufferedElement parent,
                                                String uri, String localName, String name, Attributes attributes )
    {
        super( parent, uri, localName, name, attributes );
        this.handler = handler;
    }

    public void setId( String name, String value )
    {
        if ( idAttribute == null )
        {
            idAttribute = super.setAttribute( name, value );
        }
        idAttribute.setValue( value );

        process();
    }

    private void process()
    {
        IDocumentFormatter formatter = handler.getFormatter();
        if ( handler.hasSharedContext() && formatter != null )
        {

            InitialNoteInfoMap infos = getInitialNoteInfoMap( handler.getSharedContext() );
            if ( infos != null )
            {
                String id = idAttribute.getValue();
                NoteInfo info = infos.get( id );
                if ( info != null )
                {
                    String name = CONTEXT_NOTE;

                    StringBuilder newId = new StringBuilder();
                    newId.append( formatter.getDefineDirective( name, info.getContent() ) );
                    newId.append( formatter.getFunctionDirective( getNoteRegistryKey(),
                                                                  NoteRegistry.REGISTER_NOTE_METHOD, "'" + id + "'",
                                                                  name ) );
                    // <w:footnote w:id="[#assign ___note][/#assign]${___FootnoteRegistry.registerNote('1',___note)}"
                    // ...
                    idAttribute.setValue( newId.toString() );
                }
            }
        }
    }

    protected abstract String getNoteRegistryKey();

    protected abstract InitialNoteInfoMap getInitialNoteInfoMap( Map<String, Object> sharedContext );
}

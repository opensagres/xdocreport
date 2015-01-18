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

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.DocXBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedAttribute;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public abstract class AbstractNoteBufferedRegion
    extends BufferedElement
{

    private final DocXBufferedDocumentContentHandler handler;

    private BufferedAttribute idAttribute;

    private String type;
    
    private boolean containsField;

    public AbstractNoteBufferedRegion( DocXBufferedDocumentContentHandler handler, BufferedElement parent, String uri,
                                       String localName, String name, Attributes attributes )
    {
        super( parent, uri, localName, name, attributes );
        this.handler = handler;
    }

    public void process()
    {

        IDocumentFormatter formatter = handler.getFormatter();
        if ( formatter == null )
        {
            return;
        }
        if ( handler.hasSharedContext() )
        {
            String id = idAttribute.getValue();
            
            if ( !StringUtils.isEmpty( type ))
            {
                // w:id of the footnote/endnote have a type (it's a separator/continuationSeparator) don't process it.
                // Ex: <w:footnote w:type="separator" w:id="-1">
                return;
            }

            // Get the new inner text of the footnote/endnote
            // 1) if there are fields to evaluate the inner text of the note is replaced by
            // [#noescape]${___NoEscapeNoteInfo.content}[/#noescape]
            // 2) if there are NO fields to evaluate, the note keep the initial inner text.
            String content = "";
            if ( isContainsField() )
            {
                // the note as fields to evaluate by the template engine.
                // 1) the inner text of the note must be evaluated by the word/document.xml entry and stores it in note
                // registry.
                content = super.getInnerText();
                // 2) the inner text is replaced by the content (evaluated in the word/document.xml)
                super.setInnerText( formatter.formatAsSimpleField( true, true, NoteInfo.CONTEXT_KEY,
                                                                   NoteInfo.CONTENT_PROPERTY ) );
            }

            InitialNoteInfoMap infos = getInitialNoteInfoMap( handler.getSharedContext() );
            if ( infos == null )
            {
                infos = new InitialNoteInfoMap();
                putInitialNoteInfoMap( handler.getSharedContext(), infos );
            }

            // Register the note information.
            NoteInfo info = new NoteInfo( id, content );
            infos.put( id, info );

            // <w:footnote w:id="${___NoEscapeNoteInfo.id}"
            String newId = formatter.formatAsSimpleField( true, NoteInfo.CONTEXT_KEY, NoteInfo.ID_PROPERTY );
            idAttribute.setValue( newId );

            // [#list ___FootnoteRegistry.getNotes('1') as ___NoEscapeNoteInfo]
            // <w:footnote w:id="${___NoEscapeNoteInfo.id}"
            String listName =
                formatter.getFunctionDirective( false, getNoteRegistryKey(), NoteRegistry.GET_NOTES_METHOD, "'" + id
                    + "'" );
            String before = formatter.getStartLoopDirective( NoteInfo.CONTEXT_KEY, listName );
            this.setContentBeforeStartTagElement( before );

            // [#list ___FootnoteRegistry.getNotes('1') as ___NoEscapeNoteInfo]
            // <w:footnote w:id="${___NoEscapeNoteInfo.id}"
            // [#noescape]${___NoEscapeNoteInfo.content}[/#noescape]
            // [#list]
            String after = formatter.getEndLoopDirective( "" );
            this.setContentAfterEndTagElement( after );

        }
    }

    protected abstract InitialNoteInfoMap getInitialNoteInfoMap( Map<String, Object> sharedContext );

    protected abstract void putInitialNoteInfoMap( Map<String, Object> sharedContext, InitialNoteInfoMap infos );

    public void setId( String name, String value )
    {
        if ( idAttribute == null )
        {
            idAttribute = super.setAttribute( name, value );
        }
        idAttribute.setValue( value );
    }

    public void setContainsField( boolean containsField )
    {
        this.containsField = containsField;
    }

    public boolean isContainsField()
    {
        return containsField;
    }

    public void setType( String type )
    {
        this.type = type;
    }
    
    protected abstract String getNoteRegistryKey();
}

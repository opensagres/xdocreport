package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes;

import java.util.Map;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.DocXBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedAttribute;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public abstract class AbstractNoteBufferedRegion
    extends BufferedElement
{

    private final DocXBufferedDocumentContentHandler handler;

    private BufferedAttribute idAttribute;

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

            Integer intId = StringUtils.asInteger( id );
            if ( intId == null || intId < 1 )
            {
                return;
            }

            String content = "";
            if ( isContainsField() )
            {
                content = super.getInnerText();
                super.setInnerText( formatter.formatAsSimpleField( true, true, NoteInfo.CONTEXT_KEY,
                                                                   NoteInfo.CONTENT_PROPERTY ) );
            }

            InitialNoteInfoMap infos = getInitialNoteInfoMap( handler.getSharedContext() );
            if ( infos == null )
            {
                infos = new InitialNoteInfoMap();
                putInitialNoteInfoMap( handler.getSharedContext(), infos );
            }

            NoteInfo info = new NoteInfo( id, content );
            infos.put( id, info );

            String newId = formatter.formatAsSimpleField( true, NoteInfo.CONTEXT_KEY, NoteInfo.ID_PROPERTY );
            idAttribute.setValue( newId );

            String listName =
                formatter.getFunctionDirective( false, getNoteRegistryKey(), NoteRegistry.GET_NOTES_METHOD, "'" + id
                    + "'" );
            String before = formatter.getStartLoopDirective( NoteInfo.CONTEXT_KEY, listName );

            if ( StringUtils.isNotEmpty( this.getStartTagElement().getBefore() ) )
            {
                before = this.getStartTagElement().getBefore() + before;
            }
            else
            {
                this.setContentBeforeStartTagElement( before );
            }
            String after = formatter.getEndLoopDirective( "" );
            this.setContentAfterEndTagElement( after );

        }
        // String content = "";
        // String id =
        // formatter.getFunctionDirective( FootnoteRegistry.KEY, "registerFootnote", "\"" + idAttribute.getValue()
        // + "\"", "\"" + content + "\"" );
        // idAttribute.setValue( id );
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

    protected abstract String getNoteRegistryKey();
}

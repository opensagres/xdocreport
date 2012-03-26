package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes;

import java.util.Date;
import java.util.Map;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.document.docx.preprocessor.DocXBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedAttribute;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public abstract class AbstractNoteReferenceBufferedRegion
    extends BufferedElement
{

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
                    String name= "___footnote";

                    StringBuilder newId = new StringBuilder();
                    newId.append( formatter.getDefineDirective( name, info.getContent()) );
                    newId.append( formatter.getFunctionDirective( getNoteRegistryKey(), NoteRegistry.REGISTER_NOTE_METHOD, "'" + id
                        + "'", name ) );
                    idAttribute.setValue( newId.toString() );
                }
            }
        }
    }

    protected abstract String getNoteRegistryKey();
    
    protected abstract InitialNoteInfoMap getInitialNoteInfoMap( Map<String, Object> sharedContext );
}

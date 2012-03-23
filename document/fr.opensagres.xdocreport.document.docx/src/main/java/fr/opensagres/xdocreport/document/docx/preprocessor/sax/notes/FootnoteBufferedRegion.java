package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedAttribute;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public class FootnoteBufferedRegion
    extends BufferedElement
{

    private final DocxFootnotesDocumentContentHandler handler;

    private BufferedAttribute idAttribute;

    private boolean containsField;

    public FootnoteBufferedRegion( DocxFootnotesDocumentContentHandler handler, BufferedElement parent, String uri,
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
            String content = null;
            if ( isContainsField() )
            {
                content = super.getInnerText();
                super.setInnerText( formatter.formatAsSimpleField( true, "___info", "content" ) );
            }

            InitialFootNoteInfoMap infos = FootnoteUtils.getInitialFootNoteInfoMap( handler.getSharedContext() );
            if ( infos == null )
            {
                infos = new InitialFootNoteInfoMap();
                FootnoteUtils.putInitialFootNoteInfoMap( handler.getSharedContext(), infos );
            }

            FootnoteInfo info = new FootnoteInfo( id, content );
            infos.put( id, info );

            String newId = formatter.formatAsSimpleField( true, "___info", "id" );
            idAttribute.setValue( newId );
            
            String listName= formatter.getFunctionDirective( FootnoteRegistry.KEY, "getFootnotes", "\"" + id + "\"");
            String before = formatter.getStartLoopDirective( "___info", listName );
            
            this.setContentBeforeStartTagElement( before );

            String after = formatter.getEndLoopDirective( "" );
            this.setContentAfterEndTagElement( after );

        }
        // String content = "";
        // String id =
        // formatter.getFunctionDirective( FootnoteRegistry.KEY, "registerFootnote", "\"" + idAttribute.getValue()
        // + "\"", "\"" + content + "\"" );
        // idAttribute.setValue( id );
    }

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
}

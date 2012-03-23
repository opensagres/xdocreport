package fr.opensagres.xdocreport.document.docx.preprocessor.sax.notes;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.document.docx.preprocessor.DocXBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedAttribute;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;

public class FootnoteReferenceBufferedRegion
    extends BufferedElement
{

    private final DocXBufferedDocumentContentHandler handler;

    private BufferedAttribute idAttribute;

    public FootnoteReferenceBufferedRegion( DocXBufferedDocumentContentHandler handler, BufferedElement parent,
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
        if ( handler.hasSharedContext() && handler.getFormatter() != null)
        {
            
            InitialFootNoteInfoMap infos = FootnoteUtils.getInitialFootNoteInfoMap( handler.getSharedContext() );
            if ( infos != null )
            {
                String id = idAttribute.getValue();
                FootnoteInfo info = infos.get( id );
                if ( info != null )
                {
                    String newId = handler.getFormatter().getFunctionDirective( FootnoteRegistry.KEY, "registerFootnote", "'" + id+ "'", "'" + info.getContent() + "'"); 
                    idAttribute.setValue( newId );
                }
            }
        }
    }

}

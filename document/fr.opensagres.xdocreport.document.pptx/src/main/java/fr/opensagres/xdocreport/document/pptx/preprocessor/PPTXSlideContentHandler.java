package fr.opensagres.xdocreport.document.pptx.preprocessor;

import static fr.opensagres.xdocreport.document.pptx.PPTXUtils.isAT;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public class PPTXSlideContentHandler
    extends TransformedBufferedDocumentContentHandler<PPTXSlideDocument>
{

    private boolean atParsing;

    protected PPTXSlideContentHandler( FieldsMetadata fieldsMetadata, IDocumentFormatter formater,
                                       Map<String, Object> sharedContext )
    {
        super( fieldsMetadata, formater, sharedContext );
        this.atParsing = false;
    }

    @Override
    protected PPTXSlideDocument createDocument()
    {
        return new PPTXSlideDocument( this );
    }

    @Override
    protected String getTableCellName()
    {
        return null;
    }

    @Override
    protected String getTableRowName()
    {
        return null;
    }

    @Override
    public boolean doStartElement( String uri, String localName, String name, Attributes attributes )
        throws SAXException
    {
        if ( isAT( uri, localName, name ) )
        {
            atParsing = true;
        }
        return super.doStartElement( uri, localName, name, attributes );
    }

    @Override
    public void doEndElement( String uri, String localName, String name )
        throws SAXException
    {
        if ( isAT( uri, localName, name ) )
        {
            atParsing = false;
        }
        super.doEndElement( uri, localName, name );
    }

    @Override
    protected void flushCharacters( String characters )
    {
        if ( atParsing )
        {
            ARBufferedRegion arBufferedRegion = bufferedDocument.getCurrentARRegion();
            if ( arBufferedRegion != null )
            {
                arBufferedRegion.setTContent( characters );
                return;
            }
        }
        super.flushCharacters( characters );
    }

}

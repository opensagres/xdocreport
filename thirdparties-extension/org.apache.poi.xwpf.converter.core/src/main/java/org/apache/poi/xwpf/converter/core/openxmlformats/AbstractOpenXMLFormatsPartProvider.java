package org.apache.poi.xwpf.converter.core.openxmlformats;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocument1;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.DocumentDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.FontsDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.HdrDocument;
import org.xml.sax.SAXException;

public abstract class AbstractOpenXMLFormatsPartProvider
    implements IOpenXMLFormatsPartProvider
{

    private CTDocument1 document;

    private final Map<String, RelashionShipsHandler> handlers;

    public AbstractOpenXMLFormatsPartProvider()
    {
        this.handlers = new HashMap<String, RelashionShipsHandler>();
    }

    public CTDocument1 getDocument()
        throws Exception
    {
        if ( document == null )
        {
            DocumentDocument doc = DocumentDocument.Factory.parse( getEntryInputStream( "word/document.xml" ) );
            document = doc.getDocument();
        }
        return document;
    }

    public CTStyles getStyle()
        throws Exception

    {
        return getHandler( null ).getStyle();
    }

    public List<FontsDocument> getFontsDocument()
        throws Exception

    {
        return getHandler( null ).getFontsDocument();
    }

    public CTSettings getSettings()
        throws Exception

    {
        return getHandler( null ).getSettings();
    }

    public List<ThemeDocument> getThemeDocuments()
        throws Exception

    {
        return getHandler( null ).getThemeDocuments();
    }

    public FtrDocument getFtrDocumentByPartId( String relId )
        throws Exception
    {
        return getHandler( null ).getFtrDocument( relId );
    }

    public HdrDocument getHdrDocumentByPartId( String relId )
        throws Exception
    {
        return getHandler( null ).getHdrDocument( relId );
    }

    private RelashionShipsHandler getHandler( String partName )
        throws SAXException, IOException
    {
        if ( partName == null )
        {
            partName = "document.xml";
        }
        else
        {
            partName = getHandler( null ).getRelationship( partName ).target;
        }
        RelashionShipsHandler handler = handlers.get( partName );
        if ( handler == null )
        {
            handler = RelashionShipsHandler.create( partName, this );
            handlers.put( partName, handler );
        }
        return handler;
    }

    @Override
    public InputStream getInputStreamByRelId( String partName, String relId )
        throws Exception
    {
        return getHandler( partName ).getInputStreamByRelId( relId );
    }

    protected abstract InputStream getEntryInputStream( String entryName );

}

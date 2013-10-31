package org.apache.poi.xwpf.converter.core.openxmlformats;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

    private RelashionShipsHandler handler;

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
        return getHandler().getStyle();
    }

    public List<FontsDocument> getFontsDocument()
        throws Exception

    {
        return getHandler().getFontsDocument();
    }

    public CTSettings getSettings()
        throws Exception

    {
        return getHandler().getSettings();
    }

    public List<ThemeDocument> getThemeDocuments()
        throws Exception

    {
        return getHandler().getThemeDocuments();
    }

    public FtrDocument getFtrDocumentByPartId( String relId )
        throws Exception
    {
        return getHandler().getFtrDocument( relId );
    }

    public HdrDocument getHdrDocumentByPartId( String relId )
        throws Exception
    {
        return getHandler().getHdrDocument( relId );
    }

    private RelashionShipsHandler getHandler()
        throws SAXException, IOException
    {
        if ( handler == null )
        {
            handler = RelashionShipsHandler.create( this );
        }
        return handler;
    }

    protected abstract InputStream getEntryInputStream( String entryName );

}

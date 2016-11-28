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
package fr.opensagres.poi.xwpf.converter.core.openxmlformats;

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

    public InputStream getInputStreamByRelId( String partName, String relId )
        throws Exception
    {
        return getHandler( partName ).getInputStreamByRelId( relId );
    }

    protected abstract InputStream getEntryInputStream( String entryName );

}

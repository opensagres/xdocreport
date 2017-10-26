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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.ThemeDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.FontsDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.FtrDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.HdrDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.SettingsDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class RelashionShipsHandler
    extends DefaultHandler
{

    static class Relationship
    {
        public final String id;

        public final String type;

        public final String target;

        public Relationship( String id, String type, String target )
        {
            this.id = id;
            this.type = type;
            this.target = target;
        }
    }

    private final Map<String, Relationship> relationships;

    private final AbstractOpenXMLFormatsPartProvider provider;

    private CTStyles styles;

    private final List<ThemeDocument> themeDocuments;

    private final List<FontsDocument> fontsDocuments;

    private CTSettings settings;

    private RelashionShipsHandler( AbstractOpenXMLFormatsPartProvider provider )
    {
        this.provider = provider;
        this.relationships = new HashMap<String, RelashionShipsHandler.Relationship>();
        this.themeDocuments = new ArrayList<ThemeDocument>();
        this.fontsDocuments = new ArrayList<FontsDocument>();
    }

    public static RelashionShipsHandler create( String partName, AbstractOpenXMLFormatsPartProvider provider )
        throws SAXException, IOException
    {
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        RelashionShipsHandler contentHandler = new RelashionShipsHandler( provider );
        xmlReader.setContentHandler( contentHandler );
        xmlReader.parse( new InputSource( provider.getEntryInputStream( "word/_rels/" + partName + ".rels" ) ) );
        return contentHandler;
    }

    @Override
    public final void startElement( String uri, String localName, String name, Attributes attributes )
        throws SAXException
    {
        if ( "Relationship".equals( localName ) )
        {
            String id = attributes.getValue( "Id" );
            String type = attributes.getValue( "Type" );
            String target = attributes.getValue( "Target" );
            relationships.put( id, new Relationship( id, type, target ) );
            if ( "http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings".equals( type ) )
            {
                // settings
                InputStream in = getInputStream( target );
                try
                {
                    settings = SettingsDocument.Factory.parse( in ).getSettings();
                }
                catch ( Exception e )
                {
                    throw new SAXException( e );
                }
            }
            else if ( "http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles".equals( type ) )
            {
                // style
                InputStream in = getInputStream( target );
                try
                {
                    styles = StylesDocument.Factory.parse( in ).getStyles();
                }
                catch ( Exception e )
                {
                    throw new SAXException( e );
                }
            }
            else if ( "http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme".equals( type ) )
            {
                // theme
                InputStream in = getInputStream( target );
                try
                {
                    ThemeDocument theme = ThemeDocument.Factory.parse( in );
                    themeDocuments.add( theme );
                }
                catch ( Exception e )
                {
                    throw new SAXException( e );
                }
            }
            else if ( "http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable".equals( type ) )
            {
                // fontTable
                InputStream in = getInputStream( target );
                try
                {
                    FontsDocument fonts = FontsDocument.Factory.parse( in );
                    fontsDocuments.add( fonts );
                }
                catch ( Exception e )
                {
                    throw new SAXException( e );
                }
            }
        }
    }

    private InputStream getInputStream( String target )
    {
        return provider.getEntryInputStream( "word/" + target );
    }

    public InputStream getInputStreamByRelId( String relId )
    {
        Relationship relation = getRelationship( relId );
        return getInputStream( relation.target );
    }

    public List<ThemeDocument> getThemeDocuments()
    {
        return themeDocuments;
    }

    public List<FontsDocument> getFontsDocument()
    {
        return fontsDocuments;
    }

    public CTSettings getSettings()
    {
        return settings;
    }

    public CTStyles getStyle()
    {
        return styles;
    }

    public FtrDocument getFtrDocument( String relId )
        throws XmlException, IOException
    {
        Relationship relationship = getRelationship( relId );
        InputStream in = provider.getEntryInputStream( "word/" + relationship.target );
        FtrDocument ftrDoc = FtrDocument.Factory.parse( in );
        return ftrDoc;
    }

    Relationship getRelationship( String relId )
    {
        return relationships.get( relId );
    }

    public HdrDocument getHdrDocument( String relId )
        throws XmlException, IOException
    {
        Relationship relationship = getRelationship( relId );
        InputStream in = provider.getEntryInputStream( "word/" + relationship.target );
        HdrDocument hdrDoc = HdrDocument.Factory.parse( in );
        return hdrDoc;
    }
}

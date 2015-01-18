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
package fr.opensagres.xdocreport.document.docx.preprocessor.sax.contenttypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.core.document.ImageFormat;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.numbering.NumberingRegistry;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.IBufferedRegion;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * Parse content of the [Content_Types].xml to add missing image format. Ex :
 * 
 * <pre>
 * <Default Extension="jpg" ContentType="image/jpeg" />
 * </pre>
 */
public class DocxContentTypesDocumentContentHandler
    extends BufferedDocumentContentHandler
{

    private static final String TYPES_ELT = "Types";

    private static final String DEFAULT_ELT = "Default";

    private static final String EXTENSION_ATTR = "Extension";

    private static final String OVERRIDE_ELT = "Override";

    private List<ImageFormat> missingFormats = new ArrayList<ImageFormat>();

    protected final String entryName;

    protected final IDocumentFormatter formatter;

    protected final FieldsMetadata fieldsMetadata;

    private boolean hasNumbering;

    public DocxContentTypesDocumentContentHandler( String entryName, FieldsMetadata fieldsMetadata,
                                                   IDocumentFormatter formatter, Map<String, Object> sharedContext )
    {
        this.entryName = entryName;
        this.formatter = formatter;
        this.fieldsMetadata = fieldsMetadata;
        this.hasNumbering = false;
    }

    @Override
    public void startDocument()
        throws SAXException
    {
        ImageFormat format = null;
        ImageFormat[] formats = ImageFormat.values();
        for ( int i = 0; i < formats.length; i++ )
        {
            format = formats[i];
            missingFormats.add( format );
        }
        super.startDocument();
    }

    @Override
    public boolean doStartElement( String uri, String localName, String name, Attributes attributes )
        throws SAXException
    {
        if ( DEFAULT_ELT.equals( name ) )
        {
            ImageFormat format = ImageFormat.getFormatByExtension( attributes.getValue( EXTENSION_ATTR ) );
            if ( format != null )
            {
                missingFormats.remove( format );
            }
        }
        else if ( OVERRIDE_ELT.equals( name ) )
        {
            if ( !hasNumbering )
            {
                hasNumbering = "/word/numbering.xml".equals( attributes.getValue( "PartName" ) );
            }
        }
        return super.doStartElement( uri, localName, name, attributes );
    }

    @Override
    public void doEndElement( String uri, String localName, String name )
        throws SAXException
    {
        if ( TYPES_ELT.equals( name ) )
        {
            for ( ImageFormat format : missingFormats )
            {
                IBufferedRegion currentRegion = getCurrentElement();
                currentRegion.append( "<Default Extension=\"" );
                currentRegion.append( format.name() );
                currentRegion.append( "\" ContentType=\"image/" );
                currentRegion.append( format.getType() );
                currentRegion.append( "\" />" );

            }

            if ( !hasNumbering && NumberingRegistry.hasDynamicNumbering( fieldsMetadata ) )
            {
                // <Override PartName="/word/numbering.xml"
                // ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.numbering+xml" />
                IBufferedRegion currentRegion = getCurrentElement();
                currentRegion.append( "<Override PartName=\"/word/numbering.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.wordprocessingml.numbering+xml\" />" );
            }
        }
        super.doEndElement( uri, localName, name );
    }
}

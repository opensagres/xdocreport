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
package fr.opensagres.xdocreport.document.odt.preprocessor;

import static fr.opensagres.xdocreport.document.odt.ODTConstants.MANIFEST_ELT;

import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedDocument;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedDocumentContentHandler;
import fr.opensagres.xdocreport.template.TemplateContextHelper;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public class ODTManifestXMLDocumentContentHandler
    extends BufferedDocumentContentHandler<BufferedDocument>
{

    private static final String ITEM_INFO = "___info";

    protected final IDocumentFormatter formatter;

    public ODTManifestXMLDocumentContentHandler( FieldsMetadata fieldsMetadata, IDocumentFormatter formatter )
    {
        this.formatter = formatter;
    }

    @Override
    public void doEndElement( String uri, String localName, String name )
        throws SAXException
    {
        if ( MANIFEST_ELT.equals( localName ) )
        {
            StringBuilder script = new StringBuilder();

            String startIf = formatter.getStartIfDirective( TemplateContextHelper.IMAGE_REGISTRY_KEY );
            script.append( startIf );

            // 1) Generate script for dynamic images
            generateScriptsForDynamicImages( script );

            script.append( formatter.getEndIfDirective( TemplateContextHelper.IMAGE_REGISTRY_KEY ) );

            getCurrentElement().append( script.toString() );

        }
        super.doEndElement( uri, localName, name );
    }

    private void generateScriptsForDynamicImages( StringBuilder script )
    {

        String listInfos =
            formatter.formatAsSimpleField( false, TemplateContextHelper.IMAGE_REGISTRY_KEY, "ImageProviderInfos" );
        String itemListInfos = formatter.formatAsSimpleField( false, ITEM_INFO );

        String startLoop = formatter.getStartLoopDirective( itemListInfos, listInfos );

        // 1) Start loop
        script.append( startLoop );

        // <Relationship Id="rId4"
        // Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
        // Target="media/image1.png"/>

        String mediaType = "image/" + formatter.formatAsSimpleField( true, ITEM_INFO, "ImageType" );
        String fullPath = formatter.formatAsSimpleField( true, ITEM_INFO, "ImageFullPath" );
        generateManifestFileEntry( script, mediaType, fullPath );

        // 3) end loop
        script.append( formatter.getEndLoopDirective( itemListInfos ) );

    }

    protected void generateManifestFileEntry( StringBuilder script, String mediaType, String fullPath )
    {

        // <manifest:file-entry manifest:media-type="image/jpeg"
        // manifest:full-path="Pictures/1000000000000754000002868739138F.jpg" />

        script.append( "<manifest:file-entry manifest:media-type=\"" );
        script.append( mediaType );
        script.append( "\" manifest:full-path=\"" );
        script.append( fullPath );
        script.append( "\" />" );
    }
}

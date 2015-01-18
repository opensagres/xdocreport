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
package fr.opensagres.xdocreport.document.docx.preprocessor.sax.rels;

import static fr.opensagres.xdocreport.document.docx.DocxConstants.RELATIONSHIPS_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.RELATIONSHIPS_HYPERLINK_NS;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.RELATIONSHIPS_IMAGE_NS;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.RELATIONSHIPS_NUMBERING_NS;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.RELATIONSHIP_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.RELATIONSHIP_ID_ATTR;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.RELATIONSHIP_TARGET_ATTR;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.RELATIONSHIP_TARGET_MODE_ATTR;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.RELATIONSHIP_TYPE_ATTR;

import java.util.Collection;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.document.docx.images.DocxImageRegistry;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.hyperlinks.HyperlinkInfo;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.hyperlinks.HyperlinkRegistry;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.hyperlinks.HyperlinkUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.numbering.NumberingRegistry;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.IBufferedRegion;
import fr.opensagres.xdocreport.template.TemplateContextHelper;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * This handler modify the XML entry word/_rels/document.xml.rels to add Relationship for dynamic image and hyperlink :
 * 
 * <pre>
 *   <?xml version="1.0" encoding="UTF-8" standalone="yes" ?> 
 * <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
 *   <Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings" Target="settings.xml" /> 
 *   <Relationship Id="rId7" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme" Target="theme/theme1.xml" /> 
 *   <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml" /> 
 *   <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXml" Target="../customXml/item1.xml" /> 
 *   <Relationship Id="rId6" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable" Target="fontTable.xml" /> 
 *   <Relationship Id="rId5" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink" Target="mailto:$developers.Mail" TargetMode="External" /> 
 *   <Relationship Id="rId4" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/webSettings" Target="webSettings.xml" /> 
 * </Relationships>
 * </pre>
 * 
 * to add template engine script like this for Freemarker :
 * 
 * <pre>
 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
 * <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
 * 	<Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings" Target="settings.xml"/>
 * 	<Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
 * 	<Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXml" Target="../customXml/item1.xml"/>
 * 	<Relationship Id="rId6" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme" Target="theme/theme1.xml"/>
 * 	<Relationship Id="rId5" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable" Target="fontTable.xml"/>
 * 	<Relationship Id="rId4" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/webSettings" Target="webSettings.xml"/>
 * 	[#if imageRegistry??]
 * 		[#list imageRegistry.imageProviderInfos as ___info]
 * 		<Relationship Id="${___info.imageId}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/${___info.imageFileName}" />
 * 		[/#list]
 * 	[/#if]
 * </Relationships>
 * </pre>
 * 
 * * to add template engine script like this for Velocity :
 * 
 * <pre>
 * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
 * <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">	
 * 	<Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings" Target="settings.xml"/>
 * 	<Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
 * 	<Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXml" Target="../customXml/item1.xml"/>
 * 	<Relationship Id="rId6" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme" Target="theme/theme1.xml"/>
 * 	<Relationship Id="rId5" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable" Target="fontTable.xml"/>
 * 	<Relationship Id="rId4" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/webSettings" Target="webSettings.xml"/>
 * 	#if( $imageRegistry)
 * 		#foreach( $___info in $imageRegistry.ImageProviderInfos)<Relationship Id="$___info.ImageId" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/$___info.ImageFileName" />
 * 		#end
 * 	#end
 * </Relationships>
 * </pre>
 */
public class DocxDocumentXMLRelsDocumentContentHandler
    extends BufferedDocumentContentHandler
{

    private static final String ITEM_INFO = "___info";

    protected final String entryName;

    protected final IDocumentFormatter formatter;

    protected final FieldsMetadata fieldsMetadata;

    private final Map<String, HyperlinkInfo> hyperlinksMap;

    private boolean hyperlinkParsing = false;

    private boolean hasNumbering;

    public DocxDocumentXMLRelsDocumentContentHandler( String entryName, FieldsMetadata fieldsMetadata,
                                                      IDocumentFormatter formatter, Map<String, Object> sharedContext )
    {
        this.entryName = entryName;
        this.formatter = formatter;
        this.fieldsMetadata = fieldsMetadata;
        this.hyperlinksMap = HyperlinkUtils.getInitialHyperlinkMap( entryName, sharedContext );
        this.hasNumbering = false;
    }

    @Override
    public boolean doStartElement( String uri, String localName, String name, Attributes attributes )
        throws SAXException
    {
        if ( RELATIONSHIP_ELT.equals( name ) )
        {
            String type = attributes.getValue( RELATIONSHIP_TYPE_ATTR );
            if ( RELATIONSHIPS_HYPERLINK_NS.equals( type ) )
            {
                if ( this.hyperlinksMap != null )
                {
                    // Ignore element
                    hyperlinkParsing = true;
                    return false;
                }
            }
            else if ( RELATIONSHIPS_NUMBERING_NS.equals( type ) )
            {
                // <Relationship Id="rId2"
                // Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/numbering"
                // Target="numbering.xml" />
                if ( !hasNumbering )
                {
                    hasNumbering = "numbering.xml".equals( attributes.getValue( RELATIONSHIP_TARGET_ATTR ) );
                }
            }
        }
        return super.doStartElement( uri, localName, name, attributes );
    }

    @Override
    public void doEndElement( String uri, String localName, String name )
        throws SAXException
    {
        if ( hyperlinkParsing )
        {
            hyperlinkParsing = false;
            return;
        }
        if ( RELATIONSHIPS_ELT.equals( name ) )
        {
            StringBuilder script = new StringBuilder();

            // 1) Generate script for dynamic images
            generateScriptsForDynamicImages( script );

            // 2) Generate static hyperlink
            generateScriptsForStaticHyperlinks( script );
            // 3) Generate script for dynamic hyperlinks
            generateScriptsForDynamicHyperlinks( script );
            // 4) Generate numbering.xml if needed
            generateNumberingRelationShip( script );

            IBufferedRegion currentRegion = getCurrentElement();
            currentRegion.append( script.toString() );
        }
        super.doEndElement( uri, localName, name );
    }

    private void generateNumberingRelationShip( StringBuilder script )
    {
        if ( !hasNumbering && NumberingRegistry.hasDynamicNumbering( fieldsMetadata ) )
        {
            String relationId = "xdocreportNumbering";
            String target = "numbering.xml";
            generateRelationship( script, relationId, RELATIONSHIPS_NUMBERING_NS, target, null );
        }
    }

    private void generateScriptsForDynamicImages( StringBuilder script )
    {

        String startIf = formatter.getStartIfDirective( TemplateContextHelper.IMAGE_REGISTRY_KEY );
        script.append( startIf );

        String listInfos =
            formatter.formatAsSimpleField( false, TemplateContextHelper.IMAGE_REGISTRY_KEY, "ImageProviderInfos" );
        String itemListInfos = formatter.formatAsSimpleField( false, ITEM_INFO );

        // 1) Start loop
        String startLoop = formatter.getStartLoopDirective( itemListInfos, listInfos );
        script.append( startLoop );

        // <Relationship Id="rId4"
        // Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
        // Target="media/image1.png"/>
        String relationId = formatter.formatAsSimpleField( true, ITEM_INFO, "ImageId" );
        String target = DocxImageRegistry.MEDIA_PATH + formatter.formatAsSimpleField( true, ITEM_INFO, "ImageFileName" );
        generateRelationship( script, relationId, RELATIONSHIPS_IMAGE_NS, target, null );

        // 3) end loop
        script.append( formatter.getEndLoopDirective( itemListInfos ) );

        script.append( formatter.getEndIfDirective( TemplateContextHelper.IMAGE_REGISTRY_KEY ) );
    }

    private void generateScriptsForStaticHyperlinks( StringBuilder script )
    {
        if ( this.hyperlinksMap != null )
        {
            Collection<HyperlinkInfo> hyperlinks = hyperlinksMap.values();
            for ( HyperlinkInfo hyperlink : hyperlinks )
            {
                generateRelationship( script, hyperlink.getId(), RELATIONSHIPS_HYPERLINK_NS, hyperlink.getTarget(),
                                      hyperlink.getTargetMode() );
            }
        }
    }

    /**
     * Generate scripts for fynamic hyperlink. Ex for Freemarker :
     * 
     * <pre>
     * [#if ___HyperlinkRegistry??]
     *    [#list ___HyperlinkRegistry.hyperlinks as ___info]
     *    <Relationship Id="${___info.id}" 
     *    				Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink" 
     *    				Target="${___info.target}" 
     *    				TargetMode="${___info.targetMode}" />
     *    [/#list]
     *    [/#if]
     * </pre>
     * 
     * @param script
     */
    private void generateScriptsForDynamicHyperlinks( StringBuilder script )
    {

        // Start if
        // if the current entry is "word/_rels/document.xml.rels", key used will
        // be "word/document.xml").
        String registryKey =
            HyperlinkUtils.getHyperlinkRegistryKey( HyperlinkUtils.getEntryNameWithoutRels( entryName ) );
        String startIf = formatter.getStartIfDirective( registryKey );
        script.append( startIf );

        String listInfos = formatter.formatAsSimpleField( false, registryKey, "Hyperlinks" );
        String itemListInfos = formatter.formatAsSimpleField( false, ITEM_INFO );

        // 1) Start loop
        String startLoop = formatter.getStartLoopDirective( itemListInfos, listInfos );
        script.append( startLoop );

        // <Relationship Id="rId4"
        // Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
        // Target="media/image1.png"/>
        String relationId = formatter.formatAsSimpleField( true, ITEM_INFO, "Id" );
        String target = formatter.formatAsSimpleField( true, ITEM_INFO, "Target" );
        String targetMode = formatter.formatAsSimpleField( true, ITEM_INFO, "TargetMode" );
        generateRelationship( script, relationId, RELATIONSHIPS_HYPERLINK_NS, target, targetMode );

        // 3) end loop
        script.append( formatter.getEndLoopDirective( itemListInfos ) );

        script.append( formatter.getEndIfDirective( HyperlinkRegistry.KEY ) );
    }

    /**
     * Generate Relationship XML element.
     * 
     * @param script
     * @param relationId
     * @param type
     * @param target
     * @param targetMode
     */
    protected void generateRelationship( StringBuilder script, String relationId, String type, String target,
                                         String targetMode )
    {
        script.append( "<" );
        script.append( RELATIONSHIP_ELT );
        // Id
        script.append( " " );
        script.append( RELATIONSHIP_ID_ATTR );
        script.append( "=\"" );
        script.append( relationId );
        // Type
        script.append( "\" " );
        script.append( RELATIONSHIP_TYPE_ATTR );
        script.append( "=\"" );
        script.append( type );
        // Target
        script.append( "\" " );
        script.append( RELATIONSHIP_TARGET_ATTR );
        script.append( "=\"" );
        script.append( target );
        if ( targetMode != null )
        {
            script.append( "\" " );
            script.append( RELATIONSHIP_TARGET_MODE_ATTR );
            script.append( "=\"" );
            script.append( targetMode );

        }
        script.append( "\" />" );
    }
}

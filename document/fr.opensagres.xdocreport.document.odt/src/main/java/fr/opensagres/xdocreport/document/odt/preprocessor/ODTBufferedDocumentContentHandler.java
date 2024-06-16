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

import static fr.opensagres.xdocreport.document.odt.ODTConstants.ANNOTATION_NAME_ATTR;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.DRAW_NAME_ATTR;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.DRAW_NS;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.HEIGHT_ATTR;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.HREF_ATTR;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.SVG_NS;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.WIDTH_ATTR;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.XLINK_NS;
import static fr.opensagres.xdocreport.document.odt.ODTUtils.isAnnotation;
import static fr.opensagres.xdocreport.document.odt.ODTUtils.isAnnotationEnd;
import static fr.opensagres.xdocreport.document.odt.ODTUtils.isDrawFrame;
import static fr.opensagres.xdocreport.document.odt.ODTUtils.isDrawImage;
import static fr.opensagres.xdocreport.document.odt.ODTUtils.isOfficeAutomaticStyles;
import static fr.opensagres.xdocreport.document.odt.ODTUtils.isTextA;
import static fr.opensagres.xdocreport.document.odt.ODTUtils.isTextInput;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.images.AbstractImageRegistry;
import fr.opensagres.xdocreport.document.images.IImageRegistry;
import fr.opensagres.xdocreport.document.images.ImageProviderInfo;
import fr.opensagres.xdocreport.document.odt.ODTConstants;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.textstyling.ITransformResult;
import fr.opensagres.xdocreport.template.TemplateContextHelper;
import fr.opensagres.xdocreport.template.formatter.FieldMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * SAX content handler to generate lazy Freemarker/Velocity loop directive in the table row which contains a list
 * fields.
 */
public class ODTBufferedDocumentContentHandler
    extends TransformedBufferedDocumentContentHandler<ODTBufferedDocument>
{
    private static final String TEXT_P = "text:p";

    private String dynamicImageName;

    private boolean textInputParsing = false;

    private ODTAnnotationParsingHelper annotationHelper;

    public ODTBufferedDocumentContentHandler( String entryName, FieldsMetadata fieldsMetadata,
                                              IDocumentFormatter formatter, Map<String, Object> sharedContext )
    {
        super( entryName, fieldsMetadata, formatter, sharedContext );
        annotationHelper = new ODTAnnotationParsingHelper();
    }

    @Override
    protected ODTBufferedDocument createDocument()
    {
        return new ODTBufferedDocument();
    }

    @Override
    public boolean doStartElement( String uri, String localName, String name, Attributes attributes )
        throws SAXException
    {
        FieldsMetadata fieldsMetadata = super.getFieldsMetadata();
        IDocumentFormatter formatter = super.getFormatter();
        if ( annotationHelper.isParsing() )
        {
            annotationHelper.setCurrentElement( uri, localName, name );
            // only text input
            return false;
        }
        else if ( isAnnotationEnd(uri, localName, name) )
        {
            String annotationName = attributes.getValue( ODTConstants.ANNOTATION_NS, ANNOTATION_NAME_ATTR );
            annotationHelper.resetRangeAnnotation(annotationName, false);
            return false;
        }
        else if ( isTextInput( uri, localName, name ) )
        {
            // Ignore element start text:text-input
            this.textInputParsing = true;
            return false;
        }
        else if ( isTextA( uri, localName, name ) )
        {
            // <text:a xlink:type="simple"
            // xlink:href="mailto:$developers.Mail">$developers.Mail</text:a>
            if ( fieldsMetadata != null && formatter != null )
            {
                String href = attributes.getValue( XLINK_NS, HREF_ATTR );
                if ( StringUtils.isNotEmpty( href ) )
                {
                    String newHref = processRowIfNeeded( StringUtils.decode( href ) );
                    if ( newHref != null )
                    {

                        if ( StringUtils.isNotEmpty( getStartNoParse() ) )
                        {
                            getCurrentElement().setContentBeforeStartTagElement( getEndNoParse() );
                            getCurrentElement().setContentAfterEndTagElement( getStartNoParse() );
                        }
                        AttributesImpl attributesImpl = toAttributesImpl( attributes );
                        int index = attributesImpl.getIndex( XLINK_NS, HREF_ATTR );
                        attributesImpl.setValue( index, newHref );
                        attributes = attributesImpl;
                    }
                }
            }
        }
        else if ( isDrawFrame( uri, localName, name ) )
        {
            /*
             * <draw:frame draw:style-name="fr1" draw:name="images1" text:anchor-type="paragraph" svg:x="69.96pt"
             * svg:y="18.31pt" svg:width="21pt" svg:height="22.51pt" draw:z-index="0"> <draw:image
             * xlink:href="Pictures/100000000000001C0000001EE8812A78.png" xlink:type="simple" xlink:show="embed"
             * xlink:actuate="onLoad" /> </draw:frame>
             */
            if ( fieldsMetadata != null )
            {
                String drawName = attributes.getValue( DRAW_NS, DRAW_NAME_ATTR );
                String imageFieldName = fieldsMetadata.getImageFieldName( drawName );
                if ( imageFieldName != null )
                {
                    dynamicImageName = processRowIfNeeded( imageFieldName, true );
                    if ( dynamicImageName != null && formatter != null )
                    {

                        // insert before start bookmark image script (Velocity,
                        // Freemarker)
                        // #set($___imageInfo=${imageRegistry.registerImage($logo,'logo',$___context)})
                        // #if($___imageInfo)
                        String set =
                            formatter.getSetDirective( IImageRegistry.IMAGE_INFO,
                                                       formatter.getFunctionDirective( false,
                                                                                       TemplateContextHelper.IMAGE_REGISTRY_KEY,
                                                                                       IImageRegistry.REGISTER_IMAGE_METHOD,
                                                                                       dynamicImageName,
                                                                                       "'" + drawName + "'",
                                                                                       TemplateContextHelper.CONTEXT_KEY ),
                                                       false );
                        String imageInfoIf =
                            formatter.getStartIfDirective( formatter.formatAsSimpleField( false,
                                                                                          AbstractImageRegistry.IMAGE_INFO,
                                                                                          ImageProviderInfo.NOT_REMOVE_IMAGE_TEMPLATE_METHOD ),
                                                           false );
                        StringBuilder before = new StringBuilder();
                        before.append( set );
                        before.append( imageInfoIf );

                        String after = formatter.getEndIfDirective( AbstractImageRegistry.IMAGE_INFO );
                        if ( StringUtils.isNotEmpty( getStartNoParse() ) )
                        {
                            getCurrentElement().setContentBeforeStartTagElement( getEndNoParse() + before.toString() );
                            getCurrentElement().setContentAfterEndTagElement( getStartNoParse() + after.toString() );
                        }
                        else
                        {
                            getCurrentElement().setContentBeforeStartTagElement( before.toString() );
                            getCurrentElement().setContentAfterEndTagElement( after.toString() );
                        }

                        // Modify svg:width="21pt" svg:height="22.51pt" with
                        // Freemarker/Velocity directive
                        //
                        String newWith = null;
                        String newHeight = null;
                        String defaultWidth = null;
                        String defaultHeight = null;
                        int widthIndex = attributes.getIndex( SVG_NS, WIDTH_ATTR );
                        if ( widthIndex != -1 )
                        {
                            defaultWidth = attributes.getValue( widthIndex );
                        }
                        int heightIndex = attributes.getIndex( SVG_NS, HEIGHT_ATTR );
                        if ( heightIndex != -1 )
                        {
                            defaultHeight = attributes.getValue( heightIndex );
                        }

                        // get the parameters for the get width and height methods
                        String[] parameters = null;
                        if (defaultWidth != null && defaultHeight != null) {
                            parameters = new String[]{IImageRegistry.IMAGE_INFO, "'" + defaultWidth + "'", "'" + defaultHeight + "'"};
                        } else if (defaultWidth != null) {
                            parameters = new String[]{IImageRegistry.IMAGE_INFO, "'" + defaultWidth + "'"};
                        } else if (defaultHeight != null) {
                            parameters = new String[]{IImageRegistry.IMAGE_INFO, "'" + defaultHeight + "'"};
                        }

                        if ( defaultWidth != null )
                        {
                            newWith =
                                formatter.getFunctionDirective( TemplateContextHelper.IMAGE_REGISTRY_KEY,
                                                                IImageRegistry.GET_WIDTH_METHOD,
                                                                parameters );
                        }
                        if ( defaultHeight != null )
                        {
                            newHeight =
                                formatter.getFunctionDirective( TemplateContextHelper.IMAGE_REGISTRY_KEY,
                                                                IImageRegistry.GET_HEIGHT_METHOD,
                                                                parameters );
                        }

                        if ( newWith != null || newHeight != null )
                        {
                            AttributesImpl attr = toAttributesImpl( attributes );
                            if ( newWith != null )
                            {
                                attr.setValue( widthIndex, newWith );
                            }
                            if ( newHeight != null )
                            {
                                attr.setValue( heightIndex, newHeight );
                            }
                            attributes = attr;
                        }
                    }
                }
            }
        }
        else if ( isDrawImage( uri, localName, name ) )
        {
            if ( dynamicImageName != null && formatter != null )
            {
                int index = attributes.getIndex( XLINK_NS, HREF_ATTR );
                if ( index != -1 )
                {
                    // ${imageRegistry.getPath(___imageInfo,'Pictures/100000000000001C0000001EE8812A78.png')}
                    String href = attributes.getValue( index );
                    String newHref =
                        formatter.getFunctionDirective( TemplateContextHelper.IMAGE_REGISTRY_KEY,
                                                        IImageRegistry.GET_PATH_METHOD,
                                                        AbstractImageRegistry.IMAGE_INFO, "'" + href + "'" );

                    AttributesImpl attributesImpl = toAttributesImpl( attributes );
                    attributesImpl.setValue( index, newHref );
                    attributes = attributesImpl;
                }
            }
        }
        // <office:annotation office:name="__Annotation__49_1708543979">
        else if ( isAnnotation( uri, localName, name ) )
        {
            if ( !annotationHelper.isRangeAnnotation() )
            {
                String annotationName = attributes.getValue( ODTConstants.ANNOTATION_NS, ANNOTATION_NAME_ATTR );
                annotationHelper.setParsingBegin(annotationName, getElementIndex());
            }
            return false;
        }
        return super.doStartElement( uri, localName, name, attributes );
    }

    @Override
    public void doEndElement( String uri, String localName, String name )
        throws SAXException
    {
        if ( isAnnotation( uri, localName, name ) )
        {
            // ignore element end office:annotation
            if ( annotationHelper.isParsing() )
            {
                annotationHelper.setParsingEnd();
                if ( !annotationHelper.isRangeAnnotation() )
                {
                    BufferedElement elementInfo = findParentElementInfo( annotationHelper.getParents() );
                    if ( elementInfo != null )
                    {
                        if ( annotationHelper.hasBefore() )
                        {
                            String before = formatDirective( annotationHelper.getBefore() );
                            elementInfo.setContentBeforeStartTagElement(before );
                        }
                        if ( annotationHelper.hasAfter() )
                        {
                            String after = formatDirective( annotationHelper.getAfter() );
                            elementInfo.setContentAfterEndTagElement( after );
                        }
                    }
                    if ( annotationHelper.hasReplacement() )
                    {
                        String replacement = formatDirective( annotationHelper.getReplacement() );
                        getCurrentElement().setInnerText(replacement);
                    }
                }
            }
            return;
        }
        if ( annotationHelper.isParsing() || isAnnotationEnd(uri, localName, name) )
        {
            // Ignore end elements from office:annotation to office:annotation-end
            return;
        }
        else if ( isTextInput( uri, localName, name ) )
        {
            // Ignore element end text:text-input
            this.textInputParsing = false;
        }
        else
        {
            if ( isDrawFrame( uri, localName, name ) )
            {
                dynamicImageName = null;
            }
            else if ( isOfficeAutomaticStyles( uri, localName, name ) && needToProcessAutomaticStyles() )
            {
                // IDocumentFormatter formatter = super.getFormatter();
                // if ( formatter != null )
                // {
                // IBufferedRegion region = getCurrentElement();
                // region.append( formatter.getFunctionDirective( true, true, ODTContextHelper.STYLES_GENERATOR_KEY,
                // IODTStylesGenerator.generateAllStyles,
                // ODTContextHelper.DEFAULT_STYLE_KEY ) );
                // }
                /*
                 * IBufferedRegion region = getCurrentElement(); // Add bold, italic, bold+italic styles for text
                 * styling. region.append( styleGen.generateTextStyles() ); // Add paragraph styles for text styling.
                 * region.append( styleGen.generateParagraphStyles() ); // Add styles for lists region.append(
                 * styleGen.generateListStyle() );
                 */
            }
            super.doEndElement( uri, localName, name );
        }
    }

    protected boolean needToProcessAutomaticStyles()
    {
        return true;
    }
    
    @Override
    protected String getTableTableName()
    {
        return "table:table";
    }

    @Override
    protected String getTableRowName()
    {
        return "table:table-row";
    }

    @Override
    protected String getTableCellName()
    {
        return "table:table-cell";
    }

    @Override
    protected void flushCharacters( String characters )
    {
        if ( textInputParsing || annotationHelper.isParsing() || annotationHelper.isRangeAnnotation() )
        {
            IDocumentFormatter formatter = getFormatter();
            if ( formatter != null
                && ( formatter.containsInterpolation( characters ) || formatter.hasDirective( characters ) ) )
            {
                // It's an interpolation, unescape the XML
                characters = StringUtils.xmlUnescape( characters );
            }
            characters = customFormat( characters, formatter );

            if( textInputParsing )
            {
                String fieldName = characters;
                if ( processScriptBefore( fieldName ) )
                {
                    return;
                }
                if ( processScriptAfter( fieldName ) )
                {
                    return;
                }
                if ( getFormatter() != null )
                {
                    FieldMetadata fieldAsTextStyling = getFieldAsTextStyling( fieldName );
                    if ( fieldAsTextStyling != null )
                    {
                        // register parent buffered element
                        long variableIndex = getVariableIndex();
                        BufferedElement textPElement = getCurrentElement().findParent( TEXT_P );
                        if ( textPElement == null )
                        {
                            textPElement = getCurrentElement().getParent();
                        }
                        String elementId = registerBufferedElement( variableIndex, textPElement );

                        // Transform field name if it is inside a table row.
                        // See https://code.google.com/p/xdocreport/issues/detail?id=313
                        String newFieldName = super.processRowIfNeeded( fieldName );
                        if ( StringUtils.isEmpty( newFieldName ) )
                        {
                            newFieldName = fieldName;
                        }

                        // [#assign
                        // 1327511861250_id=___TextStylingRegistry.transform(comments_odt,"NoEscape","ODT","1327511861250_id",___context)]
                        String setVariableDirective =
                            getFormatter().formatAsCallTextStyling( variableIndex, newFieldName, DocumentKind.ODT.name(),
                                                                    fieldAsTextStyling.getSyntaxKind(),
                                                                    fieldAsTextStyling.isSyntaxWithDirective(), elementId,
                                                                    super.getEntryName() );

                        String textBefore =
                            getFormatter().formatAsTextStylingField( variableIndex, ITransformResult.TEXT_BEFORE_PROPERTY );
                        String textBody =
                            getFormatter().formatAsTextStylingField( variableIndex, ITransformResult.TEXT_BODY_PROPERTY );
                        String textEnd =
                            getFormatter().formatAsTextStylingField( variableIndex, ITransformResult.TEXT_END_PROPERTY );

                        textPElement.setContentBeforeStartTagElement( formatDirective( setVariableDirective + textBefore ) );
                        textPElement.setContentAfterEndTagElement( formatDirective( textEnd ) );
                        super.flushCharacters( formatDirective( textBody ) );
                        return;
                    }
                    else
                    {
                        // Simple field.
                        characters = formatDirective( characters );
                    }
                }
                super.flushCharacters( characters );
            }
            else if (annotationHelper.isParsing() )
            {
                annotationHelper.append(characters);
            }
            // range annotation content here
            else
            {
                if( StringUtils.isNotEmpty( characters ) && annotationHelper.isNotReplacedYet() )
                {
                    annotationHelper.setReplacementDone();
                    BufferedElement container = getCurrentElement();
                    if ( annotationHelper.hasBefore() )
                    {
                        String before = formatDirective( annotationHelper.getBefore() );
                        container.setContentBeforeStartTagElement(before );
                    }
                    if ( annotationHelper.hasAfter() )
                    {
                        String after = formatDirective( annotationHelper.getAfter() );
                        container.setContentAfterEndTagElement( after );
                    }
                    if ( annotationHelper.hasReplacement() )
                    {
                        String replacement = formatDirective( annotationHelper.getReplacement() );
                        getCurrentElement().setInnerText(replacement);
                    }
                }
            }
        }
        else
        {
            super.flushCharacters( characters );
        }
    }

    private String customFormat( String content, IDocumentFormatter formatter )
    {
        FieldsMetadata metadata = getFieldsMetadata();
        if ( metadata == null )
        {
            return content;
        }
        String newContent = metadata.customFormat( content, formatter );
        return newContent != null ? newContent : content;
    }

}

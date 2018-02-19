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
package fr.opensagres.xdocreport.document.docx.preprocessor.sax;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.textstyling.ITransformResult;
import fr.opensagres.xdocreport.template.formatter.Directive;
import fr.opensagres.xdocreport.template.formatter.FieldMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public abstract class MergefieldBufferedRegion
    extends BufferedElement
{

    private static final String TEXTSTYLING = "_TEXTSTYLING";

    private static final String W_P = "w:p";

    private static final String START_MERGEFORMAT = "\\*";
    
    private static final String MERGEFORMAT = "MERGEFORMAT";

    private static final String MERGEFIELD_FIELD_TYPE = "MERGEFIELD";

    private static final String HYPERLINK_FIELD_TYPE = "HYPERLINK";

    private final TransformedBufferedDocumentContentHandler handler;

    private String fieldName;

    private BufferedElement tRegion;

    public MergefieldBufferedRegion( TransformedBufferedDocumentContentHandler handler, BufferedElement parent,
                                     String uri, String localName, String name, Attributes attributes )
    {
        super( parent, uri, localName, name, attributes );
        this.handler = handler;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public String setInstrText( String instrText, FieldMetadata fieldAsTextStyling )
    {
        // compute field name if it's MERGEFIELD
        this.fieldName = getFieldName( this, instrText, fieldAsTextStyling, handler.getFormatter(), handler );
        if ( fieldName == null )
        {
            // Not a MERGEFIELD, instrText must be decoded if it's an HYPERLINK
            // and field is an interpolation
            // ex : HYPERLINK "mailto:$%7bdeveloper.mail%7d"
            // must be modified to
            // ex : HYPERLINK "mailto:${developer.mail}"
            return decodeInstrTextIfNeeded( instrText );
        }
        return instrText;
    }

    private String decodeInstrTextIfNeeded( String instrText )
    {
        // It's not a mergefield.
        IDocumentFormatter formatter = handler.getFormatter();
        if ( formatter == null )
        {
            return instrText;
        }
        // Test if it's HYPERLINK
        // ex : HYPERLINK "mailto:$%7bdeveloper.mail%7d"
        int index = instrText.indexOf( HYPERLINK_FIELD_TYPE );
        if ( index != -1 )
        {
            // It's HYPERLINK, remove HYPERLINK prefix
            // ex : "mailto:$%7bdeveloper.mail%7d"
            String fieldName = instrText.substring( index + HYPERLINK_FIELD_TYPE.length(), instrText.length() ).trim();
            if ( StringUtils.isNotEmpty( fieldName ) )
            {
                // remove double quote
                // ex : mailto:$%7bdeveloper.mail%7d
                if ( fieldName.startsWith( "\"" ) && fieldName.endsWith( "\"" ) )
                {
                    fieldName = fieldName.substring( 1, fieldName.length() - 1 );
                }
                // decode it
                // ex : mailto:${developer.mail}
                fieldName = StringUtils.decode( fieldName );
                if ( formatter.containsInterpolation( fieldName ) )
                {
                    // It's an interpolation, returns the decoded field
                    return StringUtils.decode( instrText );
                }
            }
        }
        return instrText;
    }

    private static String getFieldName( MergefieldBufferedRegion mergefield, String instrText,
                                        FieldMetadata fieldAsTextStyling, IDocumentFormatter formatter,
                                        TransformedBufferedDocumentContentHandler handler )
    {
        if ( StringUtils.isEmpty( instrText ) )
        {
            return null;
        }
        int index = instrText.indexOf( MERGEFIELD_FIELD_TYPE );
        if ( index != -1 )
        {
            // Extract field name and add it to the current buffer
            String fieldName = instrText.substring( index + MERGEFIELD_FIELD_TYPE.length(), instrText.length() ).trim();
            if ( StringUtils.isNotEmpty( fieldName ) )
            {
                // Test if fieldName ends with \* MERGEFORMAT
            	// sometimes \* MERGEFORMAT is splitted in several w:instrText
            	// ex : 
            	// <w:r><w:instrText xml:space="preserve"> MERGEFIELD ${acuOther.cond} \* MER</w:instrText></w:r>
            	// <w:r><w:instrText xml:space="preserve">GEFORMAT </w:instrText></w:r>
            	//
            	// see https://github.com/dnmd/xdocreport/blob/master/Issue42.java
            	int mergeFormatIndex = fieldName.lastIndexOf(START_MERGEFORMAT);
                if ( mergeFormatIndex != -1 )
                {
                	String mergeformat = fieldName.substring(mergeFormatIndex + START_MERGEFORMAT.length(), fieldName.length()).trim();
                	if (MERGEFORMAT.startsWith(mergeformat)) {
                		fieldName = fieldName.substring( 0, mergeFormatIndex ).trim();
                	}
                }
                if ( StringUtils.isNotEmpty( fieldName ) )
                {
                    // if #foreach is used w:fldSimple looks like this :
                    // <w:fldSimple w:instr="MERGEFIELD "#foreach($developer in
                    // $developers)" \\* MERGEFORMAT\"> to have
                    // foreach($developer in $developers)
                    // remove first " if needed
                    if ( fieldName.startsWith( "\"" ) && fieldName.endsWith( "\"" ) )
                    {                    	
                    	if (fieldName.length() == 1) {
                    		// in some case, filedName can be just "
                        	// <w:instrText xml:space="preserve"> MERGEFIELD "</w:instrText>
                        	// see https://code.google.com/p/xdocreport/issues/detail?id=430
                    		fieldName = "";
                    	} else {
                    		fieldName = fieldName.substring( 1, fieldName.length() - 1 );
                    	}
                    }
                    // Fix bug
                    // http://code.google.com/p/xdocreport/issues/detail?id=29
                    // Replace \" with "
                    // ex : replace [#if \"a\" = \"one\"]1[#else]not 1[/#if]
                    // to have [#if "a" = "one"]1[#else]not 1[/#if]
                    fieldName = StringUtils.replaceAll( fieldName, "\\\"", "\"" );
                    // ex : remplace [#if &apos;a&apos; = \"one\"]1[#else]not
                    // 1[/#if]
                    // to have [#if 'a' = "one"]1[#else]not 1[/#if]
                    fieldName = StringUtils.xmlUnescape( fieldName );

                    FieldMetadata fieldMetadata = fieldAsTextStyling;
                    if ( fieldMetadata == null ) {
                        fieldMetadata = handler.getFieldAsTextStyling(fieldName);
                    }

                    if ( fieldMetadata != null )
                    {
                        // Find parent paragraph
                        BufferedElement parent = mergefield.findParent( W_P );
                        // Test if $___NoEscape0.TextBefore was already generated for the field.
                        String key = new StringBuilder( fieldName ).append( TEXTSTYLING ).toString();
                        String directive = parent.get( key );
                        if ( directive != null )
                        {
                            // The $___NoEscape0.TextBefore is already generated
                            // don't add it
                            return directive;
                        }
                     
                        // register parent buffered element
                        long variableIndex = handler.getVariableIndex();

                        String elementId = handler.registerBufferedElement( variableIndex, parent );

                        // Set
                        String setVariableDirective =
                            formatter.formatAsCallTextStyling( variableIndex, fieldName, DocumentKind.DOCX.name(),
                                                               fieldMetadata.getSyntaxKind(),
                                                               fieldMetadata.isSyntaxWithDirective(), elementId,
                                                               handler.getEntryName() );

                        String textBefore =
                            formatter.formatAsTextStylingField( variableIndex, ITransformResult.TEXT_BEFORE_PROPERTY );
                        String textBody =
                            formatter.formatAsTextStylingField( variableIndex, ITransformResult.TEXT_BODY_PROPERTY );
                        String textEnd =
                            formatter.formatAsTextStylingField( variableIndex, ITransformResult.TEXT_END_PROPERTY );

                        parent.setContentBeforeStartTagElement( Directive.formatDirective( setVariableDirective + " "
                            + textBefore, handler.getStartNoParse(), handler.getEndNoParse() ) );
                        parent.setContentAfterEndTagElement( Directive.formatDirective( textEnd,
                                                                                        handler.getStartNoParse(),
                                                                                        handler.getEndNoParse() ) );
                        directive =
                            Directive.formatDirective( textBody, handler.getStartNoParse(), handler.getEndNoParse() );
                        parent.put( key, directive );
                        return directive;

                    }
                    return Directive.formatDirective( fieldName, handler.getStartNoParse(), handler.getEndNoParse() );
                }
            }
        }
        return null;
    }

    public BufferedElement getTRegion(int index)
    {
    	return super.findChildAt( "w:t", index );       
    }

}

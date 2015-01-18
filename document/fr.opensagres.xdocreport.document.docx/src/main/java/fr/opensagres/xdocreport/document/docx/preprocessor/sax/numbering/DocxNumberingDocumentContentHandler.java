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
package fr.opensagres.xdocreport.document.docx.preprocessor.sax.numbering;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.docx.DocxUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;
import fr.opensagres.xdocreport.document.docx.template.DocxContextHelper;
import fr.opensagres.xdocreport.document.docx.textstyling.IDocxStylesGenerator;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.IBufferedRegion;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public class DocxNumberingDocumentContentHandler
    extends BufferedDocumentContentHandler
{

    private static final String DECIMAL = "decimal";

    private static final String BULLET = "bullet";

    private static final String W_ABSTRACT_NUM_ID_ATTR = "w:abstractNumId";

    private static final String W_VAL_ATTR = "w:val";

    private static final String W_NUM_ID_ATTR = "w:numId";

    private static final String ITEM_INFO = "___NumberInfo";

    private final IDocumentFormatter formatter;

    private final Map<String, Object> sharedContext;

    private String currentAbstractNumId;

    private Integer maxNumId;

    private boolean hasDynamicAbstractNum;

    public DocxNumberingDocumentContentHandler( IDocumentFormatter formatter, Map<String, Object> sharedContext )
    {
        this.formatter = formatter;
        this.sharedContext = sharedContext;
        this.hasDynamicAbstractNum = false;
    }

    @Override
    public boolean doStartElement( String uri, String localName, String name, Attributes attributes )
        throws SAXException
    {
        /**
         * <w:abstractNum w:abstractNumId="1"> <w:nsid w:val="68EC0125" /> <w:multiLevelType w:val="hybridMultilevel" />
         * <w:tmpl w:val="3D16DCB8" /> <w:lvl w:ilvl="0" w:tplc="040C0001"> <w:start w:val="1" /> <w:numFmt
         * w:val="bullet" />
         */
        if ( DocxUtils.isAbstractNum( uri, localName, name ) )
        {
            // w:abstractNumId
            this.currentAbstractNumId = attributes.getValue( W_ABSTRACT_NUM_ID_ATTR );
        }
        else if ( DocxUtils.isNumFmt( uri, localName, name ) )
        {
            String firstNumFmt = attributes.getValue( W_VAL_ATTR );
            if ( StringUtils.isNotEmpty( firstNumFmt ) )
            {
                if ( BULLET.equals( firstNumFmt ) )
                {
                    DefaultStyle defaultStyle = DocxContextHelper.getDefaultStyle( sharedContext );
                    if ( defaultStyle.getAbstractNumIdForUnordererList() == null )
                    {
                        defaultStyle.setAbstractNumIdForUnordererList( StringUtils.asInteger( currentAbstractNumId ) );
                    }
                }
                else if ( DECIMAL.equals( firstNumFmt ) )
                {
                    DefaultStyle defaultStyle = DocxContextHelper.getDefaultStyle( sharedContext );
                    if ( defaultStyle.getAbstractNumIdForOrdererList() == null )
                    {
                        defaultStyle.setAbstractNumIdForOrdererList( StringUtils.asInteger( currentAbstractNumId ) );
                    }
                }
            }
        }
        else if ( DocxUtils.isNum( uri, localName, name ) )
        {
            Integer numId = StringUtils.asInteger( attributes.getValue( W_NUM_ID_ATTR ) );
            if ( numId != null )
            {
                DefaultStyle defaultStyle = DocxContextHelper.getDefaultStyle( sharedContext );
                if ( defaultStyle.getMaxNumId() == null || numId > defaultStyle.getMaxNumId() )
                {
                    defaultStyle.setMaxNumId( numId );
                }
            }

            generateDynamicAbstractNumIfNeeded();
        }
        return super.doStartElement( uri, localName, name, attributes );
    }

    private void generateDynamicAbstractNumIfNeeded()
    {
        if ( hasDynamicAbstractNum )
        {
            return;
        }
        IBufferedRegion region = getCurrentElement();
        DefaultStyle defaultStyle = DocxContextHelper.getDefaultStyle( sharedContext );
        if ( defaultStyle.getAbstractNumIdForUnordererList() == null )
        {
            region.append( formatter.getFunctionDirective( DocxContextHelper.STYLES_GENERATOR_KEY,
                                                           IDocxStylesGenerator.generateAbstractNumBullet,
                                                           DocxContextHelper.DEFAULT_STYLE_KEY ) );
        }
        region.append( generateScriptsForDynamicOrderedNumbers() );
        hasDynamicAbstractNum = true;

    }

    @Override
    public void doEndElement( String uri, String localName, String name )
        throws SAXException
    {
        if ( DocxUtils.isAbstractNum( uri, localName, name ) )
        {
            this.currentAbstractNumId = null;
        }
        else if ( DocxUtils.isNumbering( uri, localName, name ) )
        {
            generateDynamicAbstractNumIfNeeded();
            IBufferedRegion region = getCurrentElement();
            region.append( generateScriptsForDynamicNumbers() );

        }
        super.doEndElement( uri, localName, name );
    }

    private String generateScriptsForDynamicNumbers()
    {

        StringBuilder script = new StringBuilder();
        // Start if
        String startIf = formatter.getStartIfDirective( DocxContextHelper.NUMBERING_REGISTRY_KEY );
        script.append( startIf );

        String listInfos =
            formatter.formatAsSimpleField( false, DocxContextHelper.NUMBERING_REGISTRY_KEY,
                                           NumberingRegistry.numbersMethod );
        String itemListInfos = formatter.formatAsSimpleField( false, ITEM_INFO );

        // 1) Start loop
        String startLoop = formatter.getStartLoopDirective( itemListInfos, listInfos );
        script.append( startLoop );

        String numId = formatter.formatAsSimpleField( true, ITEM_INFO, "numId" );
        String abstractNumId = formatter.formatAsSimpleField( true, ITEM_INFO, "abstractNumId" );

        // <w:num w:numId="1">
        // <w:abstractNumId w:val="1" />
        // </w:num>
        script.append( "<w:num w:numId=\"" );
        script.append( numId );
        script.append( "\">" );
        script.append( "<w:abstractNumId w:val=\"" );
        script.append( abstractNumId );
        script.append( "\"/>" );
        script.append( "</w:num>" );

        // 3) end loop
        script.append( formatter.getEndLoopDirective( itemListInfos ) );

        script.append( formatter.getEndIfDirective( DocxContextHelper.NUMBERING_REGISTRY_KEY ) );

        return script.toString();
    }
    
    private String generateScriptsForDynamicOrderedNumbers()
    {

        StringBuilder script = new StringBuilder();
        // Start if
        String startIf = formatter.getStartIfDirective( DocxContextHelper.NUMBERING_REGISTRY_KEY );
        script.append( startIf );

        String listInfos =
            formatter.formatAsSimpleField( false, DocxContextHelper.NUMBERING_REGISTRY_KEY,
                                           NumberingRegistry.numbersMethod );
        String itemListInfos = formatter.formatAsSimpleField( false, ITEM_INFO );

        // 1) Start loop
        String startLoop = formatter.getStartLoopDirective( itemListInfos, listInfos );
        script.append( startLoop );

        String abstractNumId = formatter.formatAsSimpleField( false, ITEM_INFO, "abstractNumId" );

        String abstractNumOrdered = formatter.formatAsSimpleField( false, ITEM_INFO, "ordered" );
        String startIfOrderedList = formatter.getStartIfDirective( abstractNumOrdered );
        script.append( startIfOrderedList );

        script.append( formatter.getFunctionDirective( DocxContextHelper.STYLES_GENERATOR_KEY,
                                                           IDocxStylesGenerator.generateAbstractNumDecimal,
                                                           DocxContextHelper.DEFAULT_STYLE_KEY, abstractNumId ) );
		
        script.append( formatter.getEndIfDirective( abstractNumOrdered ) );

        // 3) end loop
        script.append( formatter.getEndLoopDirective( itemListInfos ) );

        script.append( formatter.getEndIfDirective( DocxContextHelper.NUMBERING_REGISTRY_KEY ) );

        return script.toString();
    }
}

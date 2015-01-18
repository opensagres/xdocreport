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
package fr.opensagres.xdocreport.document.docx.preprocessor.sax.styles;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;
import fr.opensagres.xdocreport.document.docx.template.DocxContextHelper;
import fr.opensagres.xdocreport.document.docx.textstyling.IDocxStylesGenerator;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedDocumentContentHandler;
import fr.opensagres.xdocreport.document.preprocessor.sax.IBufferedRegion;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * SAX Content Handler which parses word/styles.xml to add XDocReport styles (hyperlink, numbered list, etc...)
 */
public class DocxStylesDocumentContentHandler
    extends BufferedDocumentContentHandler
{

    // Elements
    private static final String STYLE_ELT = "style";

    private static final String STYLES_ELT = "styles";

    private static final String NAME_ELT = "name";

    // Attributes
    private static final String W_STYLE_ID_ATTR = "w:styleId";

    private static final String W_VAL_ATTR = "w:val";

    // Styles names
    private static final String HYPERLINK_STYLE_NAME = "Hyperlink";

    private final IDocumentFormatter formatter;

    private final Map<String, Object> sharedContext;

    // Current w:styleId attribute.
    private String currentStyleId = null;

    public DocxStylesDocumentContentHandler( IDocumentFormatter formatter, Map<String, Object> sharedContext )
    {
        this.formatter = formatter;
        this.sharedContext = sharedContext;
    }

    @Override
    public boolean doStartElement( String uri, String localName, String name, Attributes attributes )
        throws SAXException
    {
        if ( STYLE_ELT.equals( localName ) )
        {
            this.currentStyleId = attributes.getValue( W_STYLE_ID_ATTR );
        }
        else if ( NAME_ELT.equals( localName ) )
        {
            /**
             * w:style w:type="character" w:styleId="Lienhypertexte"> <w:name w:val="Hyperlink" />
             */
            String val = attributes.getValue( W_VAL_ATTR );
            if ( StringUtils.isNotEmpty( val ) )
            {
                if ( HYPERLINK_STYLE_NAME.equals( val ) )
                {
                    DefaultStyle defaultStyle = DocxContextHelper.getDefaultStyle( sharedContext );
                    defaultStyle.setHyperLinkStyleId( currentStyleId );
                }
                else if ( val.startsWith( "heading " ) )
                {
                    String index = val.substring( "heading ".length(), val.length() );
                    Integer level = StringUtils.asInteger( index );
                    if ( level != null )
                    {
                        DefaultStyle defaultStyle = DocxContextHelper.getDefaultStyle(sharedContext);
                        defaultStyle.addHeaderStyle( level, currentStyleId );
                    }
                }
            }
        }
        return super.doStartElement( uri, localName, name, attributes );
    }

    @Override
    public void doEndElement( String uri, String localName, String name )
        throws SAXException
    {

        if ( STYLE_ELT.equals( localName ) )
        {
            this.currentStyleId = null;
        }
        else if ( STYLES_ELT.equals( localName ) )
        {
            // Generate Hyperlink styles
            IBufferedRegion region = getCurrentElement();
            region.append( formatter.getFunctionDirective( DocxContextHelper.STYLES_GENERATOR_KEY,
                                                           IDocxStylesGenerator.generateAllStyles,
                                                           DocxContextHelper.DEFAULT_STYLE_KEY ) );
        }
        super.doEndElement( uri, localName, name );
    }

    public IDocumentFormatter getFormatter()
    {
        return formatter;
    }
}

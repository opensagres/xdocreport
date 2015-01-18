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
package fr.opensagres.xdocreport.document.textstyling.html;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import fr.opensagres.xdocreport.core.logging.LogUtils;
import fr.opensagres.xdocreport.core.utils.StringEscapeUtils;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.textstyling.AbstractTextStylingTransformer;
import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;
import fr.opensagres.xdocreport.document.textstyling.ITextStylingTransformer;

/**
 * HTML text styling transformer to transform HTML to another document kind (odt, docx, etc) syntax. The ODT, DOCX is
 * represented with the given {@link IDocumentHandler}.
 */
public class HTMLTextStylingTransformer
    extends AbstractTextStylingTransformer
{

    public static final ITextStylingTransformer INSTANCE = new HTMLTextStylingTransformer();

    private static final String START_XML = "<?xml version=\"1.0\" ?>"
        + StringEscapeUtils.Entities.HTML40.generateDocType( "entities" ) + "<root>";

    private static final String END_XML = "</root>";

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LogUtils.getLogger( HTMLTextStylingTransformer.class.getName() );

    private static final String[] searchList = { "\r", "\n", "\t", "&nbsp;" };

    private static final String[] replacementList = { "", "", "", " " };

    @Override
    protected void doTransform( String content, IDocumentHandler documentHandler )
        throws Exception
    {

        // pre-process content : can be used to integrate a markup based html generator like markdown
        content = generateXhtmlFromContent( content );

        // remove special characters \n, \r
        String xml = StringUtils.replaceEach( content, searchList, replacementList );
        // add root element if xml doesn't contain xml root element.
        xml = new StringBuilder( START_XML ).append( xml ).append( END_XML ).toString();

        if ( LOGGER.isLoggable( Level.FINE ) )
        {
            LOGGER.fine( xml );
        }
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.setContentHandler( new HTMLTextStylingContentHandler( documentHandler ) );
        xmlReader.parse( new InputSource( new StringReader( xml ) ) );
    }

    protected String generateXhtmlFromContent( String content )
        throws Exception
    {
        return content;
    }

}

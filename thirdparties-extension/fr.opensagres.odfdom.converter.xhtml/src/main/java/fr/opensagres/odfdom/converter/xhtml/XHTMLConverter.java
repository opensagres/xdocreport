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
package fr.opensagres.odfdom.converter.xhtml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.OdfContentDom;
import org.odftoolkit.odfdom.dom.OdfStylesDom;
import org.odftoolkit.odfdom.incubator.doc.office.OdfOfficeMasterStyles;

import fr.opensagres.odfdom.converter.core.AbstractODFConverter;
import fr.opensagres.odfdom.converter.core.IURIResolver;
import fr.opensagres.odfdom.converter.core.ODFConverterException;
import fr.opensagres.odfdom.converter.internal.xhtml.ElementVisitorForXHTML;
import fr.opensagres.odfdom.converter.internal.xhtml.ODFXHTMLPage;
import fr.opensagres.odfdom.converter.internal.xhtml.StyleEngineForXHTML;

public class XHTMLConverter
    extends AbstractODFConverter<XHTMLOptions>
{

    private static final XHTMLConverter INSTANCE = new XHTMLConverter();

    public static XHTMLConverter getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected void doConvert( OdfDocument odfDocument, OutputStream out, Writer writer, XHTMLOptions options )
        throws ODFConverterException, IOException
    {
        // 1) Get configuration
        boolean generateCSSComments = false;
        IURIResolver resolver = IURIResolver.DEFAULT;
        int indent = 0;
        if ( options != null )
        {
            generateCSSComments = options.isGenerateCSSComments();
            resolver = options.getURIResolver();
            indent = options.getIndent();
        }

        StyleEngineForXHTML styleEngine = new StyleEngineForXHTML( odfDocument, generateCSSComments, indent, resolver );
        ODFXHTMLPage xhtml = new ODFXHTMLPage( styleEngine, indent );
        try
        {

            OdfStylesDom stylesDom = odfDocument.getStylesDom();
            OdfContentDom contentDom = odfDocument.getContentDom();

            // 1) Compute CSS styles declaration

            // 1.1) Parse styles.xml//office:document-styles/office:styles
            stylesDom.getOfficeStyles().accept( styleEngine );
            ;
            // 1.2) Parse
            // styles.xml//office:document-styles/office:automatic-styles
            stylesDom.getAutomaticStyles().accept( styleEngine );
            // 1.3) Parse
            // content.xml//office:document-content/office:automatic-styles
            contentDom.getAutomaticStyles().accept( styleEngine );

            ElementVisitorForXHTML visitorForXHTML =
                new ElementVisitorForXHTML( xhtml, options, odfDocument, out, writer );

            // 2) Generate XHTML Page

            // 2.1) Parse
            // styles.xml//office:document-styles/office:master-styles
            OdfOfficeMasterStyles masterStyles = odfDocument.getOfficeMasterStyles();
            masterStyles.accept( visitorForXHTML );

            // 2) Compute meta
            // TODO
            odfDocument.getContentRoot().accept( visitorForXHTML );

            if ( writer != null )
            {
                xhtml.save( writer );
            }
            else
            {
                xhtml.save( out );
            }

        }
        catch ( Exception e )
        {
            throw new ODFConverterException( e );
        }
    }

}

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
package fr.opensagres.xdocreport.converter.docx.docx4j.xhtml;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.docx4j.convert.out.html.AbstractHtmlExporter;
import org.docx4j.convert.out.html.AbstractHtmlExporter.HtmlSettings;
import org.docx4j.convert.out.html.HtmlExporterNG2;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.converter.MimeMappingConstants;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.converter.internal.AbstractConverterNoEntriesSupport;
import fr.opensagres.xdocreport.core.logging.LogUtils;

public class Docx2XHTMLViaDocx4jConverter
    extends AbstractConverterNoEntriesSupport
    implements MimeMappingConstants
{

    private static final Docx2XHTMLViaDocx4jConverter INSTANCE = new Docx2XHTMLViaDocx4jConverter();

    /**
     * Logger for this class
     */
    private static final Logger LOGGER = LogUtils.getLogger( Docx2XHTMLViaDocx4jConverter.class.getName() );

    public static Docx2XHTMLViaDocx4jConverter getInstance()
    {
        return INSTANCE;
    }

    public void convert( InputStream in, OutputStream out, Options options )
        throws XDocConverterException
    {
        try
        {
            // 1) load docx with docx4j
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load( in );
            // 2) convert it to HTML
            AbstractHtmlExporter exporter = new HtmlExporterNG2();
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult( out );
            exporter.html( wordMLPackage, result, toHtmlSettings( options ) );
        }
        catch ( Exception e )
        {
            LOGGER.severe( e.getMessage() );
            throw new XDocConverterException( e );
        }
    }

    public HtmlSettings toHtmlSettings( Options options )
    {
        if ( options == null )
        {
            return null;
        }
        Object value = options.getSubOptions( HtmlSettings.class );
        if ( value instanceof HtmlSettings )
        {
            return (HtmlSettings) value;
        }
        return new HtmlSettings();
    }

    public MimeMapping getMimeMapping()
    {
        return XHTML_MIME_MAPPING;
    }

}

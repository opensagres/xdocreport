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
package fr.opensagres.xdocreport.converter.docx.docx4j.pdf;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.docx4j.convert.out.pdf.viaXSLFO.PdfSettings;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.converter.MimeMappingConstants;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.converter.internal.AbstractConverterNoEntriesSupport;
import fr.opensagres.xdocreport.core.logging.LogUtils;

public class Docx2PDFViaDocx4jConverter
    extends AbstractConverterNoEntriesSupport
{

    private static final Docx2PDFViaDocx4jConverter INSTANCE = new Docx2PDFViaDocx4jConverter();

    /**ss
     * Logger for this class
     */
    private static final Logger LOGGER = LogUtils.getLogger( Docx2PDFViaDocx4jConverter.class.getName() );

    public static Docx2PDFViaDocx4jConverter getInstance()
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
            // 2) convert it to PDF
            org.docx4j.convert.out.pdf.PdfConversion c
            // = new org.docx4j.convert.out.pdf.viaHTML.Conversion(wordMLPackage);
            = new org.docx4j.convert.out.pdf.viaXSLFO.Conversion( wordMLPackage );
             //= new org.docx4j.convert.out.pdf.viaIText.Conversion(wordMLPackage);

           // ( (org.docx4j.convert.out.pdf.viaXSLFO.Conversion) c ).setSaveFO( new java.io.File( inputfilepath + ".fo" ) );            
            c.output( out, toPdfSettings(options) );

        }
        catch ( Exception e )
        {
            LOGGER.severe( e.getMessage() );
            throw new XDocConverterException( e );
        }
    }

    public PdfSettings toPdfSettings( Options options )
    {
        if ( options == null )
        {
            return null;
        }
        Object value = options.getSubOptions( PdfSettings.class );
        if ( value instanceof PdfSettings )
        {
            return (PdfSettings) value;
        }
       return new PdfSettings();
    }

    public MimeMapping getMimeMapping()
    {

        return MimeMappingConstants.PDF_MIME_MAPPING;
    }
    
}

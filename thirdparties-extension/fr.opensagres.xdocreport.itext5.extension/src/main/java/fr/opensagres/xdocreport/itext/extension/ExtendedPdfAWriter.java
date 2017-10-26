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
package fr.opensagres.xdocreport.itext.extension;

import java.io.OutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfDocument;

public class ExtendedPdfAWriter
    extends PdfAWriter {

    private final IPdfAWriterConfiguration configuration;

    protected ExtendedPdfAWriter( PdfDocument document, OutputStream os, PdfAConformanceLevel conformanceLevel, IPdfAWriterConfiguration configuration )
    {
        super( document, os, conformanceLevel );
        pdf = document;
        this.configuration = configuration;
        directContent = new ExtendedPdfContentByte( this );
        directContentUnder = new ExtendedPdfContentByte( this );
    }

    public static ExtendedPdfAWriter getInstance( Document document, OutputStream os, IPdfAWriterConfiguration configuration )
            throws DocumentException
    {
        PdfDocument pdf = new PdfDocument();
        document.addDocListener( pdf );

        PdfAConformanceLevel conformanceLevel = null;
        if ( configuration != null ) {
            conformanceLevel = configuration.getConformanceLevel();
        }

        ExtendedPdfAWriter writer = new ExtendedPdfAWriter( pdf, os, conformanceLevel, configuration );
        pdf.addWriter( writer );
        return writer;
    }

    @Override
    public void open() {
        super.open();

        if ( configuration != null ) {
            configuration.configureOutputIntents(this);
        }
    }

}

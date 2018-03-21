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

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfWriter;

public class ExtendedPdfWriter
    extends PdfWriter
{
    protected ExtendedPdfWriter( PdfDocument document, OutputStream os )
    {
        super( document, os );
        pdf = document;
        directContent = new ExtendedPdfContentByte( this );
        directContentUnder = new ExtendedPdfContentByte( this );
    }

    public static ExtendedPdfWriter getInstance( Document document, OutputStream os )
        throws DocumentException
    {
        PdfDocument pdf = new PdfDocument();
        document.addDocListener( pdf );
        ExtendedPdfWriter writer = new ExtendedPdfWriter( pdf, os );
        pdf.addWriter( writer );
        return writer;
    }

    @Override
    public void setPageCount( int pageNumber )
    {
        pdf.setPageCount( pageNumber );
    }
}

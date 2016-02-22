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

import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;

/**
 * API for the configuration to use to configure iText {@link PdfAWriter} and null otherwise
 */
public interface IPdfAWriterConfiguration {

    /**
     * Configure the given {@link PdfAWriter}
     *
     * @param writer iText {@link PdfAWriter} to configure.
     */
    void configure( PdfAWriter writer );

    /**
     * Returns the iText {@link PdfAConformanceLevel} of this configuration
     */
    PdfAConformanceLevel getConformanceLevel();

    /**
     * Configure the iText {@link PdfAWriter} color profile. <br>
     * <b>Must be executed with the instance of the iText {@link PdfAWriter} opened.</b>
     *
     * @param writer iText {@link PdfAWriter} to configure.
     */
    void configureOutputIntents( PdfAWriter writer );

}

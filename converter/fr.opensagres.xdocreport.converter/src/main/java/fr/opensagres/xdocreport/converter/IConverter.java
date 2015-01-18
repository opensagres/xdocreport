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
package fr.opensagres.xdocreport.converter;

import java.io.InputStream;
import java.io.OutputStream;

import fr.opensagres.xdocreport.core.io.IEntryInputStreamProvider;

/**
 * Converter API to convert source stream (docx, odt...) to another format (PDF, FO...)
 */
public interface IConverter
{

    /**
     * Convert the given entry input stream provider document to another format.
     * 
     * @param inProvider
     * @param out
     * @param options
     * @throws XDocConverterException
     */
    void convert( IEntryInputStreamProvider inProvider, OutputStream out, Options options )
        throws XDocConverterException;

    /**
     * Convert the given entry input stream document zipped (odt, docx...) to another format.
     * 
     * @param inProvider
     * @param out
     * @param options
     * @throws XDocConverterException
     */
    void convert( InputStream in, OutputStream out, Options options )
        throws XDocConverterException;

    /**
     * Returns mime mapping switch the kind of the converter (pdf, fo...).
     * 
     * @return
     */
    MimeMapping getMimeMapping();

    /**
     * Returns true if converter can support input stream entry provider and false otherwise.
     * 
     * @return
     */
    boolean canSupportEntries();

    /**
     * Returns true if the converter is the default converter to use when there is several converters with same from/to
     * and false otherwise.
     * 
     * @return
     */
    boolean isDefault();
}

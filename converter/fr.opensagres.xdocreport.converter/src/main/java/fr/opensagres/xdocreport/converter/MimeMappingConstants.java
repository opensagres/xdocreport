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

/**
 * Mime mapping constants used for converter and process template generation.
 */
public interface MimeMappingConstants
{

    String XHTML_MIME_TYPE = "text/html";

    String XML_MIME_TYPE = "application/xml";

    // FO Mime mapping
    String FO_EXTENSION = "fo";

    MimeMapping FO_MIME_MAPPING = new MimeMapping( FO_EXTENSION, XML_MIME_TYPE );

    // PDF Mime mapping
    String PDF_EXTENSION = "pdf";

    String PDF_MIME_TYPE = "application/pdf";

    MimeMapping PDF_MIME_MAPPING = new MimeMapping( PDF_EXTENSION, PDF_MIME_TYPE );

    // XHTML Mime mapping
    String XHTML_EXTENSION = "html";

    MimeMapping XHTML_MIME_MAPPING = new MimeMapping( XHTML_EXTENSION, XHTML_MIME_TYPE );

    // Zip Mime mapping

    String ZIP_MIME_TYPE = "application/zip ";
    String ZIP_EXTENSION = "zip";

    MimeMapping ZIP_MIME_MAPPING = new MimeMapping( ZIP_EXTENSION, ZIP_MIME_TYPE );

}

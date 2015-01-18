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
package fr.opensagres.xdocreport.core.document;

/**
 * Document king supported by XDocReport.
 */
public enum DocumentKind
{

    ODT( "application/vnd.oasis.opendocument.text" ), ODS( "application/vnd.oasis.opendocument.spreadsheet" ), ODP(
        "application/vnd.oasis.opendocument.presentation" ), DOCX(
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ), PPTX(
        "application/vnd.openxmlformats-officedocument.presentationml.presentation" );

    private final String mimeType;

    private DocumentKind( String mimeType )
    {
        this.mimeType = mimeType;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public static DocumentKind fromMimeType( String mimetype )
    {
        DocumentKind[] kinds = values();
        DocumentKind documentKind = null;
        for ( int i = 0; i < kinds.length; i++ )
        {
            documentKind = kinds[i];
            // match must be done with starts with and not equals
            // because according the browser mimeType id different.
            // Ex:with chrome : application/vnd.openxmlformats-officedocument.wordprocessingml.document.main
            // with FF: application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml
            if ( mimetype.startsWith( documentKind.getMimeType() ) )
            {
                return documentKind;
            }
        }
        // not found...
        return null;
    }
}

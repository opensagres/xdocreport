/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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
package org.apache.poi.xwpf.converter.itext;

import org.apache.poi.xwpf.converter.Options;

import com.lowagie.text.FontFactory;

public class PDFViaITextOptions
    extends Options
{

    private String fontEncoding = determineSystemEncoding();

    private String determineSystemEncoding()
    {
        // don't rely on file.encoding property because
        // it may be changed if application is launched inside an ide
        String systemEncoding = System.getProperty( "sun.jnu.encoding" );
        if ( systemEncoding != null && systemEncoding.length() > 0 )
        {
            return systemEncoding;
        }
        systemEncoding = System.getProperty( "ibm.system.encoding" );
        if ( systemEncoding != null && systemEncoding.length() > 0 )
        {
            return systemEncoding;
        }
        return FontFactory.defaultEncoding;
    }

    private PDFViaITextOptions()
    {
    }

    public static PDFViaITextOptions create()
    {
        return new PDFViaITextOptions();
    }

    public String getFontEncoding()
    {
        return fontEncoding;
    }

    /**
     * Set font encoding to use when retrieving fonts. The default value is underlying operating system encoding
     * 
     * @param fontEncoding font encoding to use
     * @return this instance
     */
    public PDFViaITextOptions fontEncoding( String fontEncoding )
    {
        this.fontEncoding = fontEncoding;
        return this;
    }
}

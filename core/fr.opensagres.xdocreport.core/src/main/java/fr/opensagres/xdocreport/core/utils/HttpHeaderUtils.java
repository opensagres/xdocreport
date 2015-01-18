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
package fr.opensagres.xdocreport.core.utils;

/**
 * HTTP Header Utilities.
 */
public class HttpHeaderUtils
{

    // HTTP Header constants
    public static final String SAT_6_MAY_1995_12_00_00_GMT = "Sat, 6 May 1995 12:00:00 GMT";

    public static final String EXPIRES = "Expires";

    public static final String POST_CHECK_0_PRE_CHECK_0 = "post-check=0, pre-check=0";

    public static final String NO_CACHE = "no-cache";

    public static final String PRAGMA = "Pragma";

    public static final String NO_STORE_NO_CACHE_MUST_REVALIDATE = "no-store, no-cache, must-revalidate";

    public static final String CACHE_CONTROL_HTTP_HEADER = "Cache-Control";

    // Content-Disposition HTTP response Header
    public static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";

    public static final String ATTACHMENT_FILENAME_START = "attachment; filename=\"";

    public static final String ATTACHMENT_FILENAME_END = "\"";

    /**
     * Return the header attachment for the given filename.
     * 
     * @param fileName
     * @return
     */
    public static String getAttachmentFileName( String fileName )
    {
        StringBuilder attachment = new StringBuilder( ATTACHMENT_FILENAME_START );
        attachment.append( fileName );
        attachment.append( ATTACHMENT_FILENAME_END );
        return attachment.toString();
    }
}

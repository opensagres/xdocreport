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
package fr.opensagres.xdocreport.document.odp;

import fr.opensagres.xdocreport.converter.MimeMapping;

/**
 * Open Office ODS constants. For mime mapping please see {@see
 * http://framework.openoffice.org/documentation/mimetypes/mimetypes.html}.
 */
public interface ODPConstants
{

    String CONTENT_XML_ENTRY = "content.xml";

    String STYLES_XML_ENTRY = "styles.xml";

    // Mime type
    String MIMETYPE = "mimetype";

    String ODP_MIMETYPE = "application/vnd.oasis.opendocument.presentation";

    String ODP_EXTENSION = "odp";

    // Mime mapping
    MimeMapping MIME_MAPPING = new MimeMapping( ODP_EXTENSION, ODP_MIMETYPE );

    // Meta data for discovery
    String ID_DISCOVERY = "odp";

    String DESCRIPTION_DISCOVERY = "Manage OpenOffice ODP document.";
}

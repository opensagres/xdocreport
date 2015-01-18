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
package fr.opensagres.xdocreport.document.pptx;

import fr.opensagres.xdocreport.converter.MimeMapping;

/**
 * Open Office ODS constants. For mime mapping please see {@see
 * http://framework.openoffice.org/documentation/mimetypes/mimetypes.html}.
 */
public class PPTXConstants
{

    public static final String PRESENTATION_PRESENTATION_XML_ENTRY = "ppt/presentation.xml";

    public static final String PRESENTATION_SLIDES_XML_ENTRY = "ppt/slides/slide*.xml";

    // Mime type
    public static final String MIMETYPE = "mimetype";

    // [Content_Types].xml
    public static final String CONTENT_TYPES_XML = "[Content_Types].xml";

    public static final String PRESENTATIONML_PRESENTATION =
        "application/vnd.openxmlformats-officedocument.presentationml.presentation.main+xml";

    public static final String PPTX_EXTENSION = "pptx";

    // Namespaces
    public static final String P_NS = "http://schemas.openxmlformats.org/presentationml/2006/main";

    public static final String A_NS = "http://schemas.openxmlformats.org/drawingml/2006/main";

    // Element
    public static final String P_ELT = "p";

    public static final String PPR_ELT = "pPr";

    public static final String R_ELT = "r";

    public static final String T_ELT = "t";

    public static final String TXBODY_ELT = "txBody";

    // Attributes
    public static final String LVL_ATTR = "lvl";

    // Mime mapping
    public static final MimeMapping MIME_MAPPING = new MimeMapping( PPTX_EXTENSION, PRESENTATIONML_PRESENTATION );

    // Meta data for discovery
    public static final String ID_DISCOVERY = "pptx";

    public static final String DESCRIPTION_DISCOVERY = "Manage Microsoft Office pptx document.";
}

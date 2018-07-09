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
package fr.opensagres.xdocreport.document.odt;

import fr.opensagres.xdocreport.converter.MimeMapping;

/**
 * Open Office ODT constants.
 */
public class ODTConstants
{

    public static final String CONTENT_XML_ENTRY = "content.xml";

    public static final String STYLES_XML_ENTRY = "styles.xml";

    public static final String METAINF_MANIFEST_XML_ENTRY = "META-INF/manifest.xml";

    public static final String TEXT_NS = "urn:oasis:names:tc:opendocument:xmlns:text:1.0";

    public static final String TEXT_INPUT_ELT = "text-input";

    public static final String TEXT_A_ELT = "a";

    public static final String DRAW_NS = "urn:oasis:names:tc:opendocument:xmlns:drawing:1.0";

    public static final String DRAW_FRAME_ELT = "frame";

    public static final String DRAW_NAME_ATTR = "name";

    public static final String DRAW_IMAGE_ELT = "image";

    public static final String TABLE_NS = "urn:oasis:names:tc:opendocument:xmlns:table:1.0";

    public static final String TABLE_ELT = "table";

    public static final String TABLE_ROW_ELT = "table-row";

    public static final String XLINK_NS = "http://www.w3.org/1999/xlink";

    public static final String HREF_ATTR = "href";

    public static final String OFFICE_NS = "urn:oasis:names:tc:opendocument:xmlns:office:1.0";

    public static final String OFFICE_AUTOMATIC_STYLES_ELT = "automatic-styles";

    public static final String ANNOTATION_NS = "urn:oasis:names:tc:opendocument:xmlns:office:1.0";

    public static final String ANNOTATION_ELT = "annotation";

    public static final String ANNOTATION_END_ELT = "annotation-end";

    public static final String ANNOTATION_NAME_ATTR = "name";

    public static final String ANNOTATION_CREATOR_ELT = "creator";

    public static final String ANNOTATION_DATE_ELT = "date";

    public static final String ANNOTATION_DC_NS = "http://purl.org/dc/elements/1.1/";

    public static final String PARAGRAPH_ELT = "p";

    public static final String SVG_NS = "urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0";

    public static final String WIDTH_ATTR = "width";

    public static final String HEIGHT_ATTR = "height";

    // Mime type
    public static final String MIMETYPE = "mimetype";

    public static final String ODT_MIMETYPE = "application/vnd.oasis.opendocument.text";

    public static final String ODT_EXTENSION = "odt";

    // Manifest
    public static final String MANIFEST_ELT = "manifest";

    // Mime mapping
    public static final MimeMapping MIME_MAPPING = new MimeMapping( ODT_EXTENSION, ODT_MIMETYPE );

    // Meta data for discovery
    public static final String ID_DISCOVERY = "odt";

    public static final String DESCRIPTION_DISCOVERY = "Manage OpenOffice ODT document.";
}

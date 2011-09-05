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
package fr.opensagres.xdocreport.document.odt;

import fr.opensagres.xdocreport.converter.MimeMapping;

/**
 * Open Office ODT constants.
 * 
 */
public interface ODTConstants {

	String CONTENT_XML_ENTRY = "content.xml";
	String STYLES_XML_ENTRY = "styles.xml";
	String METAINF_MANIFEST_XML_ENTRY = "META-INF/manifest.xml";

	String TEXT_NS = "urn:oasis:names:tc:opendocument:xmlns:text:1.0";
	String TEXT_INPUT_ELT = "text-input";
	String TEXT_A_ELT = "a";

	String DRAW_NS = "urn:oasis:names:tc:opendocument:xmlns:drawing:1.0";
	String DRAW_FRAME_ELT = "frame";
	String DRAW_NAME_ATTR = "name";
	String DRAW_IMAGE_ELT = "image";

	String TABLE_NS = "urn:oasis:names:tc:opendocument:xmlns:table:1.0";
	String TABLE_ELT = "table";
	String TABLE_ROW_ELT = "table-row";

	String XLINK_NS = "http://www.w3.org/1999/xlink";
	String HREF_ATTR = "href";

	// Mime type
	String MIMETYPE = "mimetype";
	String ODT_MIMETYPE = "application/vnd.oasis.opendocument.text";
	String ODT_EXTENSION = "odt";

	
	// Manifest
	String MANIFEST_ELT = "manifest";
	// Mime mapping
	MimeMapping MIME_MAPPING = new MimeMapping(ODT_EXTENSION, ODT_MIMETYPE);

	// Meta data for discovery
	String ID_DISCOVERY = "odt";
	String DESCRIPTION_DISCOVERY = "Manage OpenOffice ODT document.";
}

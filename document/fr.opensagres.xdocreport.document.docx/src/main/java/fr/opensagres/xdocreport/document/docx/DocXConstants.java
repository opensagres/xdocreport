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
package fr.opensagres.xdocreport.document.docx;

import fr.opensagres.xdocreport.converter.MimeMapping;

/**
 * MS Word DOCX constants.
 * 
 */
public interface DocXConstants {

	String WORD_DOCUMENT_XML_ENTRY = "word/document.xml";
	String WORD_STYLES_XML_ENTRY = "word/styles.xml";
	String WORD_HEADER_XML_ENTRY = "word/header*.xml";
	String WORD_FOOTER_XML_ENTRY = "word/footer*.xml";
	String CONTENT_TYPES_XML_ENTRY = "[Content_Types].xml";
	String WORD_RELS_DOCUMENTXMLRELS_XML_ENTRY = "word/_rels/document.xml.rels";
	
	String W_NS = "http://schemas.openxmlformats.org/wordprocessingml/2006/main";
	String A_NS = "http://schemas.openxmlformats.org/drawingml/2006/main";
	String R_NS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships";
	String RELATIONSHIPS_IMAGE_NS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image";
	String RELATIONSHIPS_HYPERLINK_NS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink";

	// Table element
	String TBL_ELT = "tbl";
	// Row element
	String TR_ELT = "tr";
	String T_ELT = "t";

	// w:r element
	String R_ELT = "r";
	// w:p element
	String P_ELT = "p";

	// Merge field fldSimple
	String FLDSIMPLE_ELT = "fldSimple";
	String INSTR_ATTR = "instr";

	// Merge field instrText
	String INSTR_TEXT_ELT = "instrText";
	// w:fldChar
	String FLDCHAR_ELT = "fldChar";
	String FLDCHARTYPE_ATTR = "fldCharType";

	// Bookmark
	String BOOKMARK_START_ELT = "bookmarkStart";
	String BOOKMARK_END_ELT = "bookmarkEnd";

	// blip
	String BLIP_ELT = "blip";
	String EMBED_ATTR = "embed";

	// hyperlink
	String HYPERLINK_ELT = "hyperlink";
	String ID_ATTR = "id";
	
	// [Content_Types].xml
	String CONTENT_TYPES_XML = "[Content_Types].xml";
	String WORDPROCESSINGML_DOCUMENT = "application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml";
	String DOCX_EXTENSION = "docx";

	// word/_rels/document.xml.rels
	String RELATIONSHIPS_ELT = "Relationships";
	String RELATIONSHIP_ELT = "Relationship";
	String RELATIONSHIP_ID_ATTR = "Id";
	String RELATIONSHIP_TYPE_ATTR = "Type";
	String RELATIONSHIP_TARGET_ATTR = "Target";
	String RELATIONSHIP_TARGET_MODE_ATTR = "TargetMode";
	String TARGET_MODE_EXTERNAL= "External";
	
	// Mime mapping
	MimeMapping MIME_MAPPING = new MimeMapping(DOCX_EXTENSION,
			WORDPROCESSINGML_DOCUMENT);

	// Meta data for discovery
	String ID_DISCOVERY = "docx";
	String DESCRIPTION_DISCOVERY = "Manage Microsoft Office docx document.";
}

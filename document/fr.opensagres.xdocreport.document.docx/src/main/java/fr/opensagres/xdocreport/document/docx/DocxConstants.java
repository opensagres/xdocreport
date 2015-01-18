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
package fr.opensagres.xdocreport.document.docx;

import fr.opensagres.xdocreport.converter.MimeMapping;

/**
 * MS Word DOCX constants.
 */
public class DocxConstants
{

    public static final String WORD_REGEXP = "word*";

    public static final String WORD_DOCUMENT_XML_ENTRY = "word/document.xml";

    public static final String WORD_STYLES_XML_ENTRY = "word/styles.xml";

    public static final String WORD_HEADER_XML_ENTRY = "word/header*.xml";

    public static final String WORD_FOOTER_XML_ENTRY = "word/footer*.xml";

    public static final String CONTENT_TYPES_XML_ENTRY = "[Content_Types].xml";

    public static final String WORD_RELS_XMLRELS_XML_ENTRY = "word/_rels/*.xml.rels";

    public static final String WORD_FOOTNOTES_XML_ENTRY = "word/footnotes.xml";

    public static final String WORD_ENDNOTES_XML_ENTRY = "word/endnotes.xml";

    public static final String WORD_NUMBERING_XML_ENTRY = "word/numbering.xml";

    public static final String W_NS = "http://schemas.openxmlformats.org/wordprocessingml/2006/main";

    public static final String WP_NS = "http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing";

    public static final String A_NS = "http://schemas.openxmlformats.org/drawingml/2006/main";

    public static final String R_NS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships";

    public static final String RELATIONSHIPS_IMAGE_NS =
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/image";

    public static final String RELATIONSHIPS_HYPERLINK_NS =
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink";

    public static final String RELATIONSHIPS_NUMBERING_NS =
        "http://schemas.openxmlformats.org/officeDocument/2006/relationships/numbering";

    // Table element
    public static final String TBL_ELT = "tbl";

    // Row element
    public static final String TR_ELT = "tr";

    public static final String T_ELT = "t";

    // w:r element
    public static final String R_ELT = "r";

    // w:p element
    public static final String P_ELT = "p";

    // Merge field fldSimple
    public static final String FLDSIMPLE_ELT = "fldSimple";

    public static final String INSTR_ATTR = "instr";

    // Merge field instrText
    public static final String INSTR_TEXT_ELT = "instrText";

    // w:fldChar
    public static final String FLDCHAR_ELT = "fldChar";

    public static final String FLDCHARTYPE_ATTR = "fldCharType";

    // Bookmark
    public static final String BOOKMARK_START_ELT = "bookmarkStart";

    public static final String BOOKMARK_END_ELT = "bookmarkEnd";

    // blip
    public static final String BLIP_ELT = "blip";

    public static final String EMBED_ATTR = "embed";

    public static final String EXTENT_ELT = "extent";

    public static final String EXT_ELT = "ext";

    public static final String CX_ATTR = "cx";

    public static final String CY_ATTR = "cy";

    public static final String DRAWING_ELT = "drawing";

    // hyperlink
    public static final String HYPERLINK_ELT = "hyperlink";

    public static final String RFONTS_ELT = "rFonts";

    public static final String ABSTRACT_NUM_ELT = "abstractNum";

    public static final String ABSTRACT_NUM_ID_ELT = "abstractNumId";

    public static final String NUMBERING_ELT = "numbering";

    public static final String NUM_FMT_ELT = "numFmt";

    public static final String NUM_ELT = "num";

    public static final String ID_ATTR = "id";
    
    public static final String TYPE_ATTR = "type";

    public static final String FOOTNOTE_ELT = "footnote";

    public static final String FOOTNOTE_REFERENCE_ELT = "footnoteReference";

    public static final String ENDNOTE_ELT = "endnote";
    
    public static final String ENDNOTE_REFERENCE_ELT = "endnoteReference";
    
    // [Content_Types].xml
    public static final String CONTENT_TYPES_XML = "[Content_Types].xml";

    public static final String WORDPROCESSINGML_DOCUMENT =
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml";

    public static final String DOCX_EXTENSION = "docx";

    public static final String NAME_ATTR = "name";

    // word/_rels/document.xml.rels
    public static final String RELATIONSHIPS_ELT = "Relationships";

    public static final String RELATIONSHIP_ELT = "Relationship";

    public static final String RELATIONSHIP_ID_ATTR = "Id";

    public static final String RELATIONSHIP_TYPE_ATTR = "Type";

    public static final String RELATIONSHIP_TARGET_ATTR = "Target";

    public static final String RELATIONSHIP_TARGET_MODE_ATTR = "TargetMode";

    public static final String TARGET_MODE_EXTERNAL = "External";

    // Mime mapping
    public static final MimeMapping MIME_MAPPING = new MimeMapping( DOCX_EXTENSION, WORDPROCESSINGML_DOCUMENT );

    // Meta data for discovery
    public static final String ID_DISCOVERY = "docx";

    public static final String DESCRIPTION_DISCOVERY = "Manage Microsoft Office docx document.";

}

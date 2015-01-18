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

import static fr.opensagres.xdocreport.document.docx.DocxConstants.ABSTRACT_NUM_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.ABSTRACT_NUM_ID_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.A_NS;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.BLIP_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.BOOKMARK_END_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.BOOKMARK_START_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.CONTENT_TYPES_XML;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.DRAWING_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.ENDNOTE_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.ENDNOTE_REFERENCE_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.EXTENT_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.EXT_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.FLDCHAR_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.FLDSIMPLE_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.FOOTNOTE_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.FOOTNOTE_REFERENCE_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.HYPERLINK_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.INSTR_TEXT_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.NUMBERING_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.NUM_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.NUM_FMT_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.P_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.RFONTS_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.R_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.TBL_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.TR_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.T_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.WP_NS;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.W_NS;
import fr.opensagres.xdocreport.core.io.XDocArchive;

/**
 * Utilities for docx.
 */
public class DocxUtils
{

    /**
     * Returns true if the given document archive is a docx and false otherwise.
     * 
     * @param documentArchive
     * @return
     */

    public static boolean isDocx( XDocArchive documentArchive )
    {
        if ( !documentArchive.hasEntry( CONTENT_TYPES_XML ) )
        {
            return false;
        }
        // TODO check from [Content_Types].xml
        // <Override PartName="/word/document.xml"
        // ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"
        // />
        return documentArchive.getEntryNames( DocxConstants.WORD_REGEXP ).size() > 0;
    }

    public static boolean isTable( String uri, String localName, String name )
    {
        return W_NS.equals( uri ) && TBL_ELT.equals( localName );
    }

    public static boolean isTableRow( String uri, String localName, String name )
    {
        return W_NS.equals( uri ) && TR_ELT.equals( localName );
    }

    /**
     * Returns true if current element is w:fldSimple and false otherwise.
     * 
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isFldSimple( String uri, String localName, String name )
    {
        return ( W_NS.equals( uri ) && FLDSIMPLE_ELT.equals( localName ) );
    }

    /**
     * Returns true if current element is w:fldSimple with MERGEFIELD type and false otherwise. If element is
     * w:fldSimple :
     * 
     * <pre>
     * <w:fldSimple w:instr=" MERGEFIELD  ${name} ">
     * 		<w:r w:rsidR="00396432">
     * 			<w:rPr>
     * 				<w:noProof/>
     * 				</w:rPr>
     * 				<w:t>�${name}�</w:t>
     * 		</w:r>
     * 	</w:fldSimple>
     * </pre>
     * 
     * it is transformed with
     * 
     * <pre>
     * <w:r w:rsidR="00396432">
     * 			<w:rPr>
     * 				<w:noProof/>
     * 				</w:rPr>
     * 				<w:t>${name}</w:t>
     * 		</w:r>
     * </pre>
     * 
     * @param uri
     * @param localName
     * @param name
     * @return
     */

    public static boolean isP( String uri, String localName, String name )
    {
        return ( W_NS.equals( uri ) && P_ELT.equals( localName ) );
    }

    public static boolean isR( String uri, String localName, String name )
    {
        return ( W_NS.equals( uri ) && R_ELT.equals( localName ) );
    }

    public static boolean isT( String uri, String localName, String name )
    {
        return ( W_NS.equals( uri ) && T_ELT.equals( localName ) );
    }

    public static boolean isFldChar( String uri, String localName, String name )
    {
        return ( W_NS.equals( uri ) && FLDCHAR_ELT.equals( localName ) );
    }

    public static boolean isInstrText( String uri, String localName, String name )
    {
        return ( W_NS.equals( uri ) && INSTR_TEXT_ELT.equals( localName ) );
    }

    /**
     * Returns true if current element is w:bookmarkStart and false otherwise.
     * 
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isBookmarkStart( String uri, String localName, String name )
    {
        return ( W_NS.equals( uri ) && BOOKMARK_START_ELT.equals( localName ) );
    }

    /**
     * Returns true if current element is w:bookmarkEnd and false otherwise.
     * 
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isBookmarkEnd( String uri, String localName, String name )
    {
        return ( W_NS.equals( uri ) && BOOKMARK_END_ELT.equals( localName ) );
    }

    /**
     * Returns true if current element is a:blip and false otherwise.
     * 
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isBlip( String uri, String localName, String name )
    {
        return ( A_NS.equals( uri ) && BLIP_ELT.equals( localName ) );
    }

    /**
     * Returns true if current element is wp:extent and false otherwise.
     * 
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isExtent( String uri, String localName, String name )
    {
        return ( WP_NS.equals( uri ) && EXTENT_ELT.equals( localName ) );
    }

    /**
     * Returns true if current element is a:ext and false otherwise.
     * 
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isExt( String uri, String localName, String name )
    {
        return ( A_NS.equals( uri ) && EXT_ELT.equals( localName ) );
    }

    /**
     * Returns true if current element is w:hyperlink and false otherwise.
     * 
     * @param uri
     * @param localName
     * @param name
     * @return
     */
    public static boolean isHyperlink( String uri, String localName, String name )
    {
        return ( W_NS.equals( uri ) && HYPERLINK_ELT.equals( localName ) );
    }

    public static boolean isRFonts( String uri, String localName, String name )
    {
        return W_NS.equals( uri ) && RFONTS_ELT.equals( localName );
    }

    public static boolean isAbstractNum( String uri, String localName, String name )
    {
        return W_NS.equals( uri ) && ABSTRACT_NUM_ELT.equals( localName );
    }

    public static boolean isNumFmt( String uri, String localName, String name )
    {
        return W_NS.equals( uri ) && NUM_FMT_ELT.equals( localName );
    }

    public static boolean isNum( String uri, String localName, String name )
    {
        return W_NS.equals( uri ) && NUM_ELT.equals( localName );
    }

    public static boolean isAbstractNumId( String uri, String localName, String name )
    {
        return W_NS.equals( uri ) && ABSTRACT_NUM_ID_ELT.equals( localName );
    }

    public static boolean isNumbering( String uri, String localName, String name )
    {
        return W_NS.equals( uri ) && NUMBERING_ELT.equals( localName );
    }

    public static boolean isDrawing( String uri, String localName, String name )
    {
        return W_NS.equals( uri ) && DRAWING_ELT.equals( localName );
    }

    public static boolean isFootnote( String uri, String localName, String name )
    {
        return W_NS.equals( uri ) && FOOTNOTE_ELT.equals( localName );
    }

    public static boolean isEndnote( String uri, String localName, String name )
    {
        return W_NS.equals( uri ) && ENDNOTE_ELT.equals( localName );
    }
    
    public static boolean isFootnoteReference( String uri, String localName, String name )
    {
        return ( W_NS.equals( uri ) && FOOTNOTE_REFERENCE_ELT.equals( localName ) );
    }

    public static boolean isEndnoteReference( String uri, String localName, String name )
    {
        return ( W_NS.equals( uri ) && ENDNOTE_REFERENCE_ELT.equals( localName ) );
    }

}

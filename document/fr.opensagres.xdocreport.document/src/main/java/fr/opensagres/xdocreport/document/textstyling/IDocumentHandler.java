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
package fr.opensagres.xdocreport.document.textstyling;

import java.io.IOException;

import fr.opensagres.xdocreport.document.textstyling.properties.HeaderProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ListItemProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ListProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ParagraphProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.SpanProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableCellProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableRowProperties;

/**
 * Handler to build a document.
 */
public interface IDocumentHandler
    extends ITransformResult
{

    public enum TextLocation
    {
        Before, Body, End;
    }

    /**
     * Start the document.
     */
    void startDocument()
        throws IOException;

    /**
     * End the document.
     */
    void endDocument()
        throws IOException;

    /**
     * Start paragraph.
     */
    void startParagraph( ParagraphProperties properties )
        throws IOException;

    /**
     * End paragraph.
     */
    void endParagraph()
        throws IOException;

    /**
     * Start bold style.
     */
    void startBold()
        throws IOException;

    /**
     * End bold style.
     */
    void endBold()
        throws IOException;

    /**
     * Start italics style.
     */
    void startItalics()
        throws IOException;

    /**
     * End italics style.
     */
    void endItalics()
        throws IOException;

    /**
     * Start underline style.
     */
    void startUnderline()
        throws IOException;

    /**
     * End underline style.
     */
    void endUnderline()
        throws IOException;

    /**
     * Start Strike style.
     */
    void startStrike()
        throws IOException;

    /**
     * End Strike style.
     */
    void endStrike()
        throws IOException;

    /**
     * Start Subscript style.
     */
    void startSubscript()
        throws IOException;

    /**
     * End Subscript style.
     */
    void endSubscript()
        throws IOException;

    /**
     * Start Superscript style.
     */
    void startSuperscript()
        throws IOException;

    /**
     * End Superscript style.
     */
    void endSuperscript()
        throws IOException;

    /**
     * Start ordered list.
     * 
     * @param properties
     */
    void startOrderedList( ListProperties properties )
        throws IOException;

    /**
     * End ordered list.
     */
    void endOrderedList()
        throws IOException;

    /**
     * Start unordered list.
     * 
     * @param properties
     */
    void startUnorderedList( ListProperties properties )
        throws IOException;

    /**
     * End unordered list.
     */
    void endUnorderedList()
        throws IOException;

    /**
     * Start list item.
     */
    void startListItem( ListItemProperties properties )
        throws IOException;

    /**
     * End list item.
     */
    void endListItem()
        throws IOException;

    /**
     * Start span item.
     */
    void startSpan( SpanProperties properties )
        throws IOException;

    /**
     * End span item.
     */
    void endSpan()
        throws IOException;

    /**
     * Text content.
     * 
     * @param s
     */
    void handleString( String s )
        throws IOException;

    /**
     * Start heading.
     * 
     * @param level
     * @throws IOException
     */
    void startHeading( int level, HeaderProperties properties )
        throws IOException;

    /**
     * End heading.
     * 
     * @param level
     * @throws IOException
     */
    void endHeading( int level )
        throws IOException;

    /**
     * Start table.
     * 
     * @throws IOException
     */
    void startTable( TableProperties properties )
        throws IOException;

    /**
     * End table.
     * 
     * @throws IOException
     */
    void endTable()
        throws IOException;

    /**
     * Start table row.
     * 
     * @param properties  table row properties.
       @throws IOException
     */
    void startTableRow (TableRowProperties properties )
        throws IOException;

    /**
     * End table row.
     * 
     * @throws IOException
     */
    void endTableRow()
        throws IOException;

    /**
     * Start table cell.
     * 
     * @param properties table cell propertie.
     * @throws IOException
     */
    void startTableCell( TableCellProperties properties )
        throws IOException;

    /**
     * End table cell.
     * 
     * @throws IOException
     */
    void endTableCell()
        throws IOException;

    /**
     * Handle image.
     * 
     * @param ref
     * @param label
     * @throws IOException
     */
    void handleImage( String ref, String label )
        throws IOException;

    /**
     * Handle reference.
     * 
     * @param ref
     * @param label
     * @throws IOException
     */
    void handleReference( String ref, String label )
        throws IOException;

    void handleLineBreak()
        throws IOException;

    //
    // void endCaption();
    // void endDocument();
    // void endHeading1();
    // void endHeading2();
    // void endHeading3();
    // void endHeading4();
    // void endHeading5();
    // void endHeading6();
    // void endIndent();
    // void endItalics();
    // void endLiteral();
    // void endNormalLinkWithCaption();
    // void endOrderedList();
    // void endOrderedListItem();
    // void endParagraph();
    // void endPre();
    // void endSmartLinkWithCaption();
    // void endTable();
    // void endTableData();
    // void endTableHeader();
    // void endTableRecord();
    // void endUnorderedList();
    // void endUnorderedListItem();
    // void handleNormalLinkWithoutCaption(String string);
    // void handleNowiki(String nowiki);
    // void handleSmartLinkWithoutCaption(String string);
    // void handleString(String s);
    //
    // void startCaption(AttributeList captionOptions);
    // void startDocument();
    // void startHeading1();
    // void startHeading2();
    // void startHeading3();
    // void startHeading4();
    // void startHeading5();
    // void startHeading6();
    // void startIndent();
    // void startItalics();
    // void startLiteral();
    // void startNormalLinkWithCaption(String s);
    // void startOrderedList();
    // void startOrderedListItem();
    // void startParagraph();
    // void startPre();
    // void startSmartLinkWithCaption(String s);
    // void startTable(AttributeList tableOptions);
    // void startTableData(AttributeList options);
    // void startTableHeader(AttributeList list);
    // void startTableRecord(AttributeList rowOptions);
    // void startUnorderedList();
    // void startUnorderedListItem();

}

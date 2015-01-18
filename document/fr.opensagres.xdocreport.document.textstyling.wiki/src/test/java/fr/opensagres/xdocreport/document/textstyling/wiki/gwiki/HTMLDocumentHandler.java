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
package fr.opensagres.xdocreport.document.textstyling.wiki.gwiki;

import java.io.IOException;

import fr.opensagres.xdocreport.document.textstyling.AbstractDocumentHandler;
import fr.opensagres.xdocreport.document.textstyling.properties.HeaderProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ListItemProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ListProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ParagraphProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.SpanProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableCellProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableRowProperties;

/**
 * Basic Document handler implementation to build html fragment content.
 */
public class HTMLDocumentHandler
    extends AbstractDocumentHandler
{

    public HTMLDocumentHandler()
    {
        super( null, null, null );
    }

    public void startDocument()
    {
        // super.write("<html>");
        // super.write("<body>");
    }

    public void endDocument()
    {
        // super.write("</body>");
        // super.write("</html>");
    }

    public void startBold()
        throws IOException
    {
        super.write( "<strong>" );
    }

    public void endBold()
        throws IOException
    {
        super.write( "</strong>" );
    }

    public void startItalics()
        throws IOException
    {
        super.write( "<i>" );
    }

    public void endItalics()
        throws IOException
    {
        super.write( "</i>" );
    }

    public void startUnderline()
        throws IOException
    {
        super.write( "<u>" );
    }

    public void endUnderline()
        throws IOException
    {
        super.write( "</u>" );
    }

    //@Override
    public void startStrike()
        throws IOException
    {
        super.write( "<strike>" );
    }

    public void endStrike()
        throws IOException
    {
        super.write( "</strike>" );

    }

    //@Override
    public void startSubscript()
        throws IOException
    {
        super.write( "<sub>" );
    }

    public void endSubscript()
        throws IOException
    {
        super.write( "</sub>" );
    }

    //@Override
    public void startSuperscript()
        throws IOException
    {
        super.write( "<sup>" );
    }

    public void endSuperscript()
        throws IOException
    {
        super.write( "</sup>" );
    }

    public void startListItem( ListItemProperties properties )
        throws IOException
    {
        super.write( "<li>" );
    }

    public void endListItem()
        throws IOException
    {
        super.write( "</li>" );
    }

    @Override
    protected void doStartOrderedList( ListProperties properties )
        throws IOException
    {
        super.write( "<ol>" );
    }

    @Override
    protected void doEndOrderedList()
        throws IOException
    {
        super.write( "</ol>" );
    }

    @Override
    protected void doStartUnorderedList( ListProperties properties )
        throws IOException
    {
        super.write( "<ul>" );
    }

    @Override
    protected void doEndUnorderedList()
        throws IOException
    {
        super.write( "</ul>" );
    }

    public void startParagraph( ParagraphProperties properties )
        throws IOException
    {
        super.write( "<p>" );
    }

    public void endParagraph()
        throws IOException
    {
        super.write( "</p>" );
    }

    public void startSpan( SpanProperties properties )
        throws IOException
    {
        super.write( "<span>" );
    }

    public void endSpan()
        throws IOException
    {
        super.write( "</span>" );
    }

    public void startHeading( int level, HeaderProperties properties )
        throws IOException
    {
        super.write( "<h" );
        super.write( level );
        super.write( ">" );

    }

    public void endHeading( int level )
        throws IOException
    {
        super.write( "</h" );
        super.write( level );
        super.write( ">" );
    }

    public void handleReference( String ref, String label )
        throws IOException
    {
        super.write( "<a href=\"" );
        super.write( ref );
        super.write( "\" >" );
        super.write( label );
        super.write( "</a>" );

    }

    public void handleImage( String ref, String label )
        throws IOException
    {
        super.write( "<img src=\"" );
        super.write( ref );
        super.write( "/>" );

    }

    public void handleLineBreak()
        throws IOException
    {
        super.write( "<br />" );
    }

    public void doStartTable( TableProperties properties )
        throws IOException
    {
        super.write( "<table>" );
    }

    public void doEndTable(TableProperties properties)
        throws IOException
    {
        super.write( "</table>" );
    }

    protected void doStartTableRow( TableRowProperties properties )
        throws IOException
    {
        super.write( "<tr>" );
    }

    public void doEndTableRow()
        throws IOException
    {
        super.write( "</tr>" );
    }

    protected void doStartTableCell( TableCellProperties properties )
        throws IOException
    {
        super.write( "<td>" );
    }

    public void doEndTableCell()
        throws IOException
    {
        super.write( "</td>" );
    }
}

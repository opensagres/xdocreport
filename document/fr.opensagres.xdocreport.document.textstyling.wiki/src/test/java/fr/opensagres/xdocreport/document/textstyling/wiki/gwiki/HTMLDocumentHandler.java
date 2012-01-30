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
package fr.opensagres.xdocreport.document.textstyling.wiki.gwiki;

import java.io.IOException;

import fr.opensagres.xdocreport.document.textstyling.AbstractDocumentHandler;

/**
 * Basic Document handler implementation to build html fragment content.
 */
public class HTMLDocumentHandler
    extends AbstractDocumentHandler
{

    public HTMLDocumentHandler()
    {
        super( null, null );
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

    public void startListItem()
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
    protected void doStartOrderedList()
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
    protected void doStartUnorderedList()
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

    public void startParagraph()
        throws IOException
    {
        super.write( "<p>" );
    }

    public void endParagraph()
        throws IOException
    {
        super.write( "</p>" );
    }

    public void startHeading( int level )
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

}

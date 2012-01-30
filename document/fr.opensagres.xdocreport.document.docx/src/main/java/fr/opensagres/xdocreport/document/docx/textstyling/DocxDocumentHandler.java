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
package fr.opensagres.xdocreport.document.docx.textstyling;

import java.io.IOException;
import java.util.Stack;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.textstyling.AbstractDocumentHandler;
import fr.opensagres.xdocreport.template.IContext;

/**
 * Document handler implementation to build docx fragment content.
 */
public class DocxDocumentHandler
    extends AbstractDocumentHandler
{

    private boolean bolding;

    private boolean italicsing;

    private Stack<Boolean> paragraphsStack;

    public DocxDocumentHandler( BufferedElement parent, IContext context )
    {
        super( parent, context );
    }

    public void startDocument()
    {
        this.bolding = false;
        this.italicsing = false;
        this.paragraphsStack = new Stack<Boolean>();
    }

    public void endDocument()
        throws IOException
    {
        if ( !paragraphsStack.isEmpty() )
        {
            paragraphsStack.size();
            for ( int i = 0; i < paragraphsStack.size(); i++ )
            {
                internalEndParagraph();
            }
        }
    }

    public void startBold()
    {
        this.bolding = true;
    }

    public void endBold()
    {
        this.bolding = false;
    }

    public void startItalics()
    {
        this.italicsing = true;
    }

    public void endItalics()
    {
        this.italicsing = false;
    }

    @Override
    public void handleString( String content )
        throws IOException
    {
        // startParagraphIfNeeded();
        super.write( "<w:r>" );
        if ( bolding || italicsing )
        {
            super.write( "<w:rPr>" );
            if ( bolding )
            {
                super.write( "<w:b />" );
            }
            if ( italicsing )
            {
                super.write( "<w:i />" );
            }
            super.write( "</w:rPr>" );
        }
        super.write( "<w:t xml:space=\"preserve\" >" );
        super.write( content );
        super.write( "</w:t>" );
        super.write( "</w:r>" );
    }

    private void startParagraphIfNeeded()
        throws IOException
    {
        if ( paragraphsStack.isEmpty() )
        {
            internalStartParagraph( false );
        }
    }

    private void internalStartParagraph( boolean containerIsList )
        throws IOException
    {
        super.write( "<w:p>" );
        paragraphsStack.push( containerIsList );
    }

    private void internalEndParagraph()
        throws IOException
    {
        super.write( "</w:p>" );
        paragraphsStack.pop();
    }

    public void startListItem()
        throws IOException
    {
        // if (!paragraphsStack.isEmpty() && !paragraphsStack.peek()) {
        // internalEndParagraph();
        // }
        internalStartParagraph( true );
        boolean ordered = super.getCurrentListOrder();
        super.write( "<w:pPr>" );
        super.write( "<w:pStyle w:val=\"Paragraphedeliste\" />" );
        super.write( "<w:numPr>" );

        // <w:ilvl w:val="0" />
        int ilvlVal = super.getCurrentListIndex();
        super.write( "<w:ilvl w:val=\"" );
        super.write( String.valueOf( ilvlVal ) );
        super.write( "\" />" );

        // "<w:numId w:val="1" />"
        int numIdVal = ordered ? 2 : 1;
        super.write( "<w:numId w:val=\"" );
        // super.write(String.valueOf(numIdVal));
        super.write( String.valueOf( numIdVal ) );
        super.write( "\" />" );

        super.write( "</w:numPr>" );
        super.write( "</w:pPr>" );

    }

    public void endListItem()
        throws IOException
    {
        internalEndParagraph();
    }

    public void startParagraph()
        throws IOException
    {
        internalStartParagraph( false );
    }

    public void endParagraph()
        throws IOException
    {
        internalEndParagraph();
    }

    public void startHeading( int level )
    {
        // TODO Auto-generated method stub

    }

    public void endHeading( int level )
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void doEndUnorderedList()
        throws IOException
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void doEndOrderedList()
        throws IOException
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void doStartUnorderedList()
        throws IOException
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void doStartOrderedList()
        throws IOException
    {
        // TODO Auto-generated method stub

    }

}

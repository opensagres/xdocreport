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
package fr.opensagres.xdocreport.itext.extension;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;

public class ExtendedChunk
    extends Chunk
{

    private final ExtendedDocument ownerDocument;

    private boolean pageNumberChunk;

    public ExtendedChunk( ExtendedDocument ownerDocument, String textContent )
    {
        this( ownerDocument, textContent, new Font() );
    }

    public ExtendedChunk( ExtendedDocument ownerDocument, String textContent, Font font )
    {
        super( textContent, font );
        this.ownerDocument = ownerDocument;
    }

    public ExtendedChunk( ExtendedDocument ownerDocument, boolean pageNumberChunk, Font font )
    {
        super( "", font );
        this.pageNumberChunk = pageNumberChunk;
        this.ownerDocument = ownerDocument;
    }

    public boolean isPageNumberChunk()
    {
        return pageNumberChunk;
    }

    public void setPageNumberChunk( boolean pageNumberChunk )
    {
        this.pageNumberChunk = pageNumberChunk;
    }

    @Override
    public String getContent()
    {
        return pageNumberChunk ? String.valueOf( ownerDocument.getPageNumber() ) : super.getContent();
    }
}

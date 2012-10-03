/**
 * Copyright (C) 2011 The XDocReport Team <xdocreport@googlegroups.com>
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
package org.apache.poi.xwpf.converters.pdf.internal.elements;

import com.lowagie.text.Element;

import fr.opensagres.xdocreport.itext.extension.ExtendedPdfPTable;
import fr.opensagres.xdocreport.itext.extension.IITextContainer;

public class StylableTable
    extends ExtendedPdfPTable
    implements IITextContainer
{

    private final StylableDocument ownerDocument;

    private final IITextContainer parent;

    public StylableTable( StylableDocument ownerDocument, IITextContainer parent, int numColumns )
    {
        super( numColumns );
        // cancel ExtendedPdfPTable settings
        // we raise text in StylableParagraph so extra spacing here is unnecessary
        super.setSpacingBefore( 0.0f );
        this.ownerDocument = ownerDocument;
        this.parent = parent;
    }

    public IITextContainer getParent()
    {
        return parent;
    }

    public StylableDocument getOwnerDocument()
    {
        return ownerDocument;
    }

    public Element getElement()
    {
        return this;
    }

    public int getColIdx()
    {
        return currentRowIdx;
    }
}

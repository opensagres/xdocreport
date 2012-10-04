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
package org.odftoolkit.odfdom.converter.pdf.internal.stylable;

import java.util.List;

import org.odftoolkit.odfdom.converter.pdf.internal.styles.Style;

import com.lowagie.text.Chunk;

public class StylableHeading
    extends StylableParagraph
{
    private static final long serialVersionUID = 664309269352903329L;

    private List<Integer> headingNumbering;

    public StylableHeading( StylableDocument ownerDocument, IStylableContainer parent, List<Integer> headingNumbering )
    {
        super( ownerDocument, parent );
        this.headingNumbering = headingNumbering;
    }

    @Override
    public void applyStyles( Style style )
    {
        super.applyStyles( style );

        // compute numbering
        StringBuilder sbuf = new StringBuilder();
        for ( Integer nr : headingNumbering )
        {
            sbuf.append( nr.toString() );
            sbuf.append( "." );
        }
        sbuf.append( " " );
        addElement( new Chunk( sbuf.toString() ) );
    }
}

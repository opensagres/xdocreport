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

import java.awt.Color;

import javax.swing.text.Style;

import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;

import fr.opensagres.xdocreport.itext.extension.ExtendedParagraph;
import fr.opensagres.xdocreport.itext.extension.IITextContainer;

public class StylableParagraph
    extends ExtendedParagraph
    implements IITextContainer
{

    private static final long serialVersionUID = 664309269352903329L;

    private static final float DEFAULT_LINE_HEIGHT = 1.0f;

    private final StylableDocument ownerDocument;

    private IITextContainer parent;

    private Style lastStyleApplied = null;

    public StylableParagraph( StylableDocument ownerDocument, IITextContainer parent )
    {
        super();
        this.ownerDocument = ownerDocument;
        this.parent = parent;
        super.setMultipliedLeading( DEFAULT_LINE_HEIGHT );
    }

    public StylableParagraph( StylableDocument ownerDocument, Paragraph title, IITextContainer parent )
    {
        super( title );
        this.ownerDocument = ownerDocument;
        this.parent = parent;
    }

    public void addElement( Element element )
    {
        super.add( element );
    }

//    public void applyStyles( XWPFParagraph p, Style style )
//    {
//
//        if ( style != null )
//        {
//            // first process values from "style"
//            // will be overridden by in-line values if available...
//            StyleParagraphProperties paragraphProperties = style.getParagraphProperties();
//
//            if ( paragraphProperties != null )
//            {
//                FontInfos fontInfos = paragraphProperties.getFontInfos();
//                if ( fontInfos != null )
//                {
//                    Font font =
//                        XWPFFontRegistry.getRegistry().getFont( fontInfos.getFontFamily(), fontInfos.getFontEncoding(),
//                                                                fontInfos.getFontSize(), fontInfos.getFontStyle(),
//                                                                fontInfos.getFontColor() );
//                    setFont( font );
//                }
//                // Alignment
//                int alignment = paragraphProperties.getAlignment();
//                if ( alignment != Element.ALIGN_UNDEFINED )
//                {
//                    setAlignment( alignment );
//                }
//
//                Float lineHeight = paragraphProperties.getLineHeight();
//                if ( lineHeight != null )
//                {
//                    // super.getPdfPCell().setMinimumHeight(lineHeight);
//                    // FIXME : Is it correct???
//                    setLeading( lineHeight * super.getTotalLeading() );
//                }
//            }
//
//        }
//    }

    // FIXME check with Angelo the purpose of this method....
    public Style getLastStyleApplied()
    {
        return lastStyleApplied;
    }

    // FIXME check with Angelo the purpose of this method....
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
        return this;// super.getContainer();
    }

    public void setBackgroundColor( Color backgroundColor )
    {
        getPdfPCell().setBackgroundColor( backgroundColor );
    }

}

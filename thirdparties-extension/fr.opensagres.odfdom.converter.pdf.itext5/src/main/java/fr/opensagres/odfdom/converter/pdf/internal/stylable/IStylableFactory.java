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
package fr.opensagres.odfdom.converter.pdf.internal.stylable;

import java.util.List;

import com.itextpdf.text.Image;

import fr.opensagres.xdocreport.itext.extension.font.FontGroup;

public interface IStylableFactory
{
    StylableAnchor createAnchor( IStylableContainer parent );

    StylableChunk createChunk( IStylableContainer parent, String textContent, FontGroup fontGroup );

    StylableDocumentSection createDocumentSection( IStylableContainer parent, boolean inHeaderFooter );

    StylableHeaderFooter createHeaderFooter( boolean header );

    StylableHeading createHeading( IStylableContainer parent, List<Integer> headingNumbering );

    StylableImage createImage( IStylableContainer parent, Image image, Float x, Float y, Float width, Float height );

    StylableList createList( IStylableContainer parent, int listLevel );

    StylableListItem createListItem( IStylableContainer parent );

    StylableParagraph createParagraph( IStylableContainer parent );

    StylablePhrase createPhrase( IStylableContainer parent );

    StylableTab createTab( IStylableContainer parent, boolean inTableOfContent );

    StylableTable createTable( IStylableContainer parent, int numColumns );

    StylableTableCell createTableCell( IStylableContainer parent );
}

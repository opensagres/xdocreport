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

public interface IStylableFactory
{
    StylableParagraph createParagraph( IStylableContainer parent );

    StylableHeading createHeading( IStylableContainer parent, List<Integer> headingNumbering );

    StylablePhrase createPhrase( IStylableContainer parent );

    StylableAnchor createAnchor( IStylableContainer parent );

    StylableList createList( IStylableContainer parent, int listLevel );

    StylableListItem createListItem( IStylableContainer parent );

    StylableDocumentSection createDocumentSection( IStylableContainer parent, boolean inHeaderFooter );

    StylableTable createTable( IStylableContainer parent, int numColumns );

    StylableTableCell createTableCell( IStylableContainer parent );

    StylableChapter createChapter( IStylableContainer parent, StylableParagraph title );

    StylableChunk createChunk( IStylableContainer parent, String textContent );

    StylableSection createSection( IStylableContainer parent, StylableParagraph title, int numberDepth );

    StylableChapter getCurrentChapter();
}

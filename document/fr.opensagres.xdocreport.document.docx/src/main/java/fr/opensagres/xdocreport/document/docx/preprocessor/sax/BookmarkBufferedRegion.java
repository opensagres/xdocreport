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
package fr.opensagres.xdocreport.document.docx.preprocessor.sax;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;

/**
 * <pre>
 * <w:bookmarkStart w:id="0" w:name="logo" />
 * 			<w:r>
 * 				<w:rPr>
 * 					<w:noProof />
 * 				</w:rPr>
 * 				<w:drawing>
 * 					<wp:inline distT="0" distB="0" distL="0" distR="0">
 * 						<wp:extent cx="266700" cy="285750" />
 * 						<wp:effectExtent l="19050" t="0" r="0" b="0" />
 * 						<wp:docPr id="1" name="Image 0" descr="template.png" />
 * 						<wp:cNvGraphicFramePr>
 * 							<a:graphicFrameLocks
 * 								xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
 * 								noChangeAspect="1" />
 * 						</wp:cNvGraphicFramePr>
 * 						<a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
 * 							<a:graphicData
 * 								uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
 * 								<pic:pic
 * 									xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
 * 									<pic:nvPicPr>
 * 										<pic:cNvPr id="0" name="template.png" />
 * 										<pic:cNvPicPr />
 * 									</pic:nvPicPr>
 * 									<pic:blipFill>
 * 										<a:blip r:embed="rId5" />
 * 										<a:stretch>
 * 											<a:fillRect />
 * 										</a:stretch>
 * 									</pic:blipFill>
 * 									<pic:spPr>
 * 										<a:xfrm>
 * 											<a:off x="0" y="0" />
 * 											<a:ext cx="266700" cy="285750" />
 * 										</a:xfrm>
 * 										<a:prstGeom prst="rect">
 * 											<a:avLst />
 * 										</a:prstGeom>
 * 									</pic:spPr>
 * 								</pic:pic>
 * 							</a:graphicData>
 * 						</a:graphic>
 * 					</wp:inline>
 * 				</w:drawing>
 * 			</w:r>
 * 			<w:bookmarkEnd w:id="0" />
 * </pre>
 */
public class BookmarkBufferedRegion
    extends BufferedElement
{

    private final String bookmarkName;

    private final String imageFieldName;

    public BookmarkBufferedRegion( String bookmarkName, String imageFieldName, BufferedElement parent, String uri,
                                   String localName, String name, Attributes attributes )
    {
        super( parent, uri, localName, name, attributes );
        this.bookmarkName = bookmarkName;
        this.imageFieldName = imageFieldName;
    }

    public String getBookmarkName()
    {
        return bookmarkName;
    }

    public String getImageFieldName()
    {
        return imageFieldName;
    }

}

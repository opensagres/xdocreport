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

import junit.framework.Assert;

import org.junit.Test;

import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;
import fr.opensagres.xdocreport.document.textstyling.ITextStylingTransformer;

public class GWikiTextStylingTransformerTestCase
{

    @Test
    public void testGWiki2HTML()
        throws Exception
    {
        ITextStylingTransformer formatter = GWikiTextStylingTransformer.INSTANCE;
        IDocumentHandler handler = new HTMLDocumentHandler();
        formatter.transform( "Here are severals styles" + "\n * *Bold* style." + "\n * _Italic_ style."
            + "\n * _*BoldAndItalic*_ style."

            + "\n\n Here are 3 styles"

            + "\n # *Bold* style." + "\n # _Italic_ style." + "\n # _*BoldAndItalic*_ style.", handler );
        Assert.assertEquals( "<p>Here are severals styles</p><ul><li><strong>Bold</strong> style</li><li><i>Italic</i> style</li><li><i><strong>BoldAndItalic</i></strong> style</li></ul>Here are 3 styles<ol><li><strong>Bold</strong> style</li><li><i>Italic</i> style</li><li><i><strong>BoldAndItalic</i></strong> style</li></ol>",
                             handler.getTextBody() );
    }
}

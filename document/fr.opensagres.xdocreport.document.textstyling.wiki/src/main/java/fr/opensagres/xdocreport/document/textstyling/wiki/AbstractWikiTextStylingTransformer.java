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
package fr.opensagres.xdocreport.document.textstyling.wiki;

import java.io.StringReader;

import org.wikimodel.wem.IWikiParser;

import fr.opensagres.xdocreport.document.textstyling.AbstractTextStylingTransformer;
import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;

/**
 * Abstract class styling transformer for Wiki Syntax.
 */
public abstract class AbstractWikiTextStylingTransformer
    extends AbstractTextStylingTransformer
{

    @Override
    protected void doTransform( String content, IDocumentHandler handler )
        throws Exception
    {
        // 1) Create an instance of {@link IWikiParser}.
        IWikiParser parser = createWikiParser();
        // 2) Parser the wiki content and call the well method of the document handler.
        parser.parse( new StringReader( content ), new WemListenerAdapter( handler ) );
    }

    /**
     * Create an instance of {@link IWikiParser}.
     * 
     * @return
     */
    protected abstract IWikiParser createWikiParser();

}

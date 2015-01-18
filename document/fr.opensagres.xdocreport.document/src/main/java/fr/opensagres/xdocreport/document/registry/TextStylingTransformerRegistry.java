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
package fr.opensagres.xdocreport.document.registry;

import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.document.SyntaxKind;
import fr.opensagres.xdocreport.core.registry.AbstractRegistry;
import fr.opensagres.xdocreport.document.discovery.ITextStylingTransformerDiscovery;
import fr.opensagres.xdocreport.document.textstyling.ITextStylingTransformer;

/**
 * Text styling registry stores instance of {@link ITextStylingTransformer} for text styling kind {@link SyntaxKind}
 * (Html, Mediawiki, etc...).
 * <p>
 * Instance of {@link ITextStylingTransformer} must be declared in files
 * META-INF\services\fr.opensagres.xdocreport.template.discovery. ITextStylingTransformerDiscovery
 * </p>
 */
public class TextStylingTransformerRegistry
    extends AbstractRegistry<ITextStylingTransformerDiscovery>
{

    private static final TextStylingTransformerRegistry INSTANCE = new TextStylingTransformerRegistry();

    private final Map<String, ITextStylingTransformer> transformers = new HashMap<String, ITextStylingTransformer>();

    public TextStylingTransformerRegistry()
    {
        super( ITextStylingTransformerDiscovery.class );
    }

    public static TextStylingTransformerRegistry getRegistry()
    {
        return INSTANCE;
    }

    @Override
    protected boolean registerInstance( ITextStylingTransformerDiscovery discovery )
    {
        transformers.put( discovery.getId(), discovery.getTransformer() );
        return true;

    }

    @Override
    protected void doDispose()
    {
        transformers.clear();
    }

    /**
     * Returns the text styling transformer for the given syntax kind.
     * 
     * @param syntaxKind
     * @return
     */
    public ITextStylingTransformer getTextStylingTransformer( SyntaxKind syntaxKind )
    {
        return getTextStylingTransformer( syntaxKind.name() );
    }

    /**
     * Returns the text styling transformer for the given syntax kind.
     * 
     * @param syntaxKind
     * @return
     */
    public ITextStylingTransformer getTextStylingTransformer( String syntaxKind )
    {
        super.initializeIfNeeded();
        return transformers.get( syntaxKind );
    }

}

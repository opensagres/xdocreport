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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.registry.AbstractRegistry;
import fr.opensagres.xdocreport.document.DocumentContextHelper;
import fr.opensagres.xdocreport.document.discovery.ITextStylingDocumentHandlerFactoryDiscovery;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.textstyling.BasicTransformResult;
import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;
import fr.opensagres.xdocreport.document.textstyling.ITextStylingTransformer;
import fr.opensagres.xdocreport.document.textstyling.ITransformResult;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.TemplateContextHelper;

/**
 * Text styling registry to register {@link IDocumentHandler} and transform some content from syntax (HTML, MediaWiki,
 * etc) to another syntax (docx, odt, etc) by using text styling transformer registered in the
 * {@link TextStylingTransformerRegistry}.
 */
public class TextStylingRegistry
    extends AbstractRegistry<ITextStylingDocumentHandlerFactoryDiscovery>
{

    private static final TextStylingRegistry INSTANCE = new TextStylingRegistry();

    private final Map<String, ITextStylingDocumentHandlerFactoryDiscovery> documentHandlers =
        new HashMap<String, ITextStylingDocumentHandlerFactoryDiscovery>();

    public TextStylingRegistry()
    {
        super( ITextStylingDocumentHandlerFactoryDiscovery.class );
    }

    public static TextStylingRegistry getRegistry()
    {
        return INSTANCE;
    }

    @Override
    protected boolean registerInstance( ITextStylingDocumentHandlerFactoryDiscovery discovery )
    {
        documentHandlers.put( discovery.getId(), discovery );
        return true;
    }

    @Override
    protected void doDispose()
    {
        documentHandlers.clear();
    }

    /**
     * Transform the given content written with the given syntaxKind (HTML, MediaWiki, etc) to another syntax defined by
     * the document kind (docx, odt).
     * 
     * @param content the content to transform.
     * @param syntaxKind the syntax of the content.
     * @param syntaxWithDirective true if there is directive in the content and false otherwise.
     * @param documentKind the syntax to obtain after the transformation.
     * @return
     */
    public ITransformResult transform( final String initialContent, String syntaxKind, boolean syntaxWithDirective,
                                       String documentKind, String elementId, IContext context, String entryName )
    {
        // 1) Retrieve transformer from the text styling transformer registry.
        ITextStylingTransformer transformer =
            TextStylingTransformerRegistry.getRegistry().getTextStylingTransformer( syntaxKind );
        if ( transformer != null )
        {
            try
            {
                String content = initialContent;
                if ( syntaxWithDirective )
                {
                    // the content contains some directive (${name} which must be replaced)
                    // Apply template engine to this content.
                    ITemplateEngine templateEngine = TemplateContextHelper.getTemplateEngine( context );
                    if ( templateEngine != null )
                    {
                        StringWriter newContent = new StringWriter();
                        templateEngine.process( entryName, context, new StringReader( initialContent ), newContent );
                        content = newContent.toString();
                    }
                }

                // Transformer found, create an instance of document handler
                // (docx, odt, etc).
                IDocumentHandler visitor = createDocumentHandler( documentKind, elementId, context, entryName );
                // 3) Process the transformation.
                return transformer.transform( content, visitor );
            }
            catch ( Throwable e )
            {
                // Error while transformation, returns the original content???
                e.printStackTrace();
                return new BasicTransformResult( initialContent );
            }
        }
        return new BasicTransformResult( initialContent );
    }

    /**
     * Create an instance of document handler for the given document kind.
     * 
     * @param documentKind
     * @return
     */
    public IDocumentHandler createDocumentHandler( String documentKind, String elementId, IContext context,
                                                   String entryName )
    {
        super.initializeIfNeeded();
        ITextStylingDocumentHandlerFactoryDiscovery factory = documentHandlers.get( documentKind );
        if ( factory == null )
        {
            return null;
        }

        // Get the parent buffered element
        BufferedElement parent = DocumentContextHelper.getElementById( context, elementId );
        return factory.createDocumentHandler( parent, context, entryName );
    }

}

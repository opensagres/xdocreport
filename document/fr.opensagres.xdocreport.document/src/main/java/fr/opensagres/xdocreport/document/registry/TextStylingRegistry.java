package fr.opensagres.xdocreport.document.registry;

import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.registry.AbstractRegistry;
import fr.opensagres.xdocreport.document.discovery.ITextStylingDocumentHandlerFactoryDiscovery;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.textstyling.BasicTransformResult;
import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;
import fr.opensagres.xdocreport.document.textstyling.ITextStylingTransformer;
import fr.opensagres.xdocreport.document.textstyling.ITransformResult;
import fr.opensagres.xdocreport.document.textstyling.TextStylingTransformerRegistry;
import fr.opensagres.xdocreport.template.IContext;

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
     * @param documentKind the syntax to obtain after the transformation.
     * @return
     */
    public ITransformResult transform( String content, String syntaxKind, String documentKind, String elementId,
                                       IContext context )
    {
        // 1) Retrieve transformer from the text styling transformer registry.
        ITextStylingTransformer transformer =
            TextStylingTransformerRegistry.getRegistry().getTextStylingTransformer( syntaxKind );
        if ( transformer != null )
        {
            try
            {
                // Transformer found, create an instance of document handler
                // (docx, odt, etc).
                IDocumentHandler visitor = createDocumentHandler( documentKind, elementId, context );
                // 3) Process the transformation.
                return transformer.transform( content, visitor );
            }
            catch ( Throwable e )
            {
                // Error while transformation, returns the original content???
                e.printStackTrace();
                return new BasicTransformResult( content );
            }
        }
        return new BasicTransformResult( content );
    }

    /**
     * Create an instance of document handler for the given document kind.
     * 
     * @param documentKind
     * @return
     */
    public IDocumentHandler createDocumentHandler( String documentKind, String elementId, IContext context )
    {
        super.initializeIfNeeded();
        ITextStylingDocumentHandlerFactoryDiscovery factory = documentHandlers.get( documentKind );
        if ( factory == null )
        {
            return null;
        }

        // Get the parent buffered element
        BufferedElement parent = null;
        Map<String, BufferedElement> elements = (Map<String, BufferedElement>) context.get( BufferedElement.KEY );
        if ( elements != null )
        {
            parent = elements.get( elementId );
        }
        return factory.createDocumentHandler( parent, context );
    }

}

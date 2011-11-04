package fr.opensagres.xdocreport.document.registry;

import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.registry.AbstractRegistry;
import fr.opensagres.xdocreport.document.discovery.ITextStylingDocumentHandlerFactoryDiscovery;
import fr.opensagres.xdocreport.template.textstyling.IDocumentHandler;
import fr.opensagres.xdocreport.template.textstyling.ITextStylingTransformer;
import fr.opensagres.xdocreport.template.textstyling.TextStylingTransformerRegistry;

/**
 * Text styling registry to register {@link IDocumentHandler} and transform some
 * content from syntax (HTML, MediaWiki, etc) to another syntax (docx, odt, etc)
 * by using text styling transformer registered in the
 * {@link TextStylingTransformerRegistry}.
 * 
 */
public class TextStylingRegistry extends
		AbstractRegistry<ITextStylingDocumentHandlerFactoryDiscovery> {

	public static final String KEY = TextStylingTransformerRegistry.KEY;

	private static final TextStylingRegistry INSTANCE = new TextStylingRegistry();
	private final Map<String, ITextStylingDocumentHandlerFactoryDiscovery> documentHandlers = new HashMap<String, ITextStylingDocumentHandlerFactoryDiscovery>();

	public TextStylingRegistry() {
		super(ITextStylingDocumentHandlerFactoryDiscovery.class);
	}

	public static TextStylingRegistry getRegistry() {
		return INSTANCE;
	}

	@Override
	protected boolean registerInstance(
			ITextStylingDocumentHandlerFactoryDiscovery discovery) {
		documentHandlers.put(discovery.getId(), discovery);
		return true;
	}

	@Override
	protected void doDispose() {
		documentHandlers.clear();
	}

	/**
	 * Transform the given content written with the given syntaxKind (HTML,
	 * MediaWiki, etc) to another syntax defined by the document kind (docx,
	 * odt).
	 * 
	 * @param content
	 *            the content to transform.
	 * @param syntaxKind
	 *            the syntax of the content.
	 * @param documentKind
	 *            the syntax to obtain after the transformation.
	 * @return
	 */
	public String transform(String content, String syntaxKind,
			String documentKind) {
		// 1) Retrieve transformer from the text styling transformer registry.
		ITextStylingTransformer transformer = TextStylingTransformerRegistry
				.getRegistry().getTextStylingTransformer(syntaxKind);
		if (transformer != null) {
			try {
				// 2) Transformer found, create an instance of document handler
				// (docx, odt, etc).
				IDocumentHandler visitor = createDocumentHandler(documentKind);
				// 3) Process the transformation.
				return transformer.transform(content, visitor);
			} catch (Throwable e) {
				// Error while transformation, returns the original content???
				e.printStackTrace();
				return content;
			}
		}
		return content;
	}

	/**
	 * Create an instance of document handler for the given document kind.
	 * 
	 * @param documentKind
	 * @return
	 */
	public IDocumentHandler createDocumentHandler(String documentKind) {
		super.initializeIfNeeded();
		ITextStylingDocumentHandlerFactoryDiscovery factory = documentHandlers
				.get(documentKind);
		if (factory == null) {
			return null;
		}
		return factory.createDocumentHandler();
	}

}

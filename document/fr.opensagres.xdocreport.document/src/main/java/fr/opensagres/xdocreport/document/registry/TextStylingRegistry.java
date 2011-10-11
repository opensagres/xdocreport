package fr.opensagres.xdocreport.document.registry;

import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.registry.AbstractRegistry;
import fr.opensagres.xdocreport.document.discovery.ITextStylingDocumentVisitorFactoryDiscovery;
import fr.opensagres.xdocreport.template.textstyling.IDocumentVisitor;
import fr.opensagres.xdocreport.template.textstyling.ITextStylingFormatter;
import fr.opensagres.xdocreport.template.textstyling.TextStylingFormatterRegistry;

public class TextStylingRegistry extends
		AbstractRegistry<ITextStylingDocumentVisitorFactoryDiscovery> {

	public static final String KEY = TextStylingFormatterRegistry.KEY;

	private static final TextStylingRegistry INSTANCE = new TextStylingRegistry();
	private final Map<String, ITextStylingDocumentVisitorFactoryDiscovery> documentVisitors = new HashMap<String, ITextStylingDocumentVisitorFactoryDiscovery>();

	public TextStylingRegistry() {
		super(ITextStylingDocumentVisitorFactoryDiscovery.class);
	}

	public static TextStylingRegistry getRegistry() {
		return INSTANCE;
	}

	@Override
	protected boolean registerInstance(
			ITextStylingDocumentVisitorFactoryDiscovery discovery) {
		// document visitor
		documentVisitors.put(discovery.getId(), discovery);
		return true;
	}

	@Override
	protected void doDispose() {
		documentVisitors.clear();
	}

	public String format(String content, String documentKind,
			String textStylingKind) throws XDocReportException {
		ITextStylingFormatter formatter = TextStylingFormatterRegistry
				.getRegistry().getTextStylingFormatter(textStylingKind);
		if (formatter != null) {
			// Create document visitor
			IDocumentVisitor visitor = createDocumentVisitor(documentKind);
			return formatter.format(content, visitor);
		}
		return content;
	}

	public IDocumentVisitor createDocumentVisitor(String documentKind) {
		super.initializeIfNeeded();
		ITextStylingDocumentVisitorFactoryDiscovery factory = documentVisitors
				.get(documentKind);
		if (factory == null) {
			return null;
		}
		return factory.createDocumentVisitor();
	}

}

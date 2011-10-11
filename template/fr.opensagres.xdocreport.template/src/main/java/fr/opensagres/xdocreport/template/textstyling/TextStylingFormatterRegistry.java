package fr.opensagres.xdocreport.template.textstyling;

import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.registry.AbstractRegistry;
import fr.opensagres.xdocreport.template.TextStylingKind;
import fr.opensagres.xdocreport.template.discovery.ITextStylingFormatterDiscovery;

public class TextStylingFormatterRegistry extends
		AbstractRegistry<ITextStylingFormatterDiscovery> {

	private static final TextStylingFormatterRegistry INSTANCE = new TextStylingFormatterRegistry();
	public static final String KEY = "___TextStylingRegistry";
	private final Map<String, ITextStylingFormatter> formatters = new HashMap<String, ITextStylingFormatter>();

	public TextStylingFormatterRegistry() {
		super(ITextStylingFormatterDiscovery.class);
	}

	public static TextStylingFormatterRegistry getRegistry() {
		return INSTANCE;
	}

	@Override
	protected boolean registerInstance(ITextStylingFormatterDiscovery discovery) {
		// formatter
		formatters.put(discovery.getId(), discovery.getFormatter());
		return true;

	}

	@Override
	protected void doDispose() {
		formatters.clear();
	}

	public ITextStylingFormatter getTextStylingFormatter(
			TextStylingKind textStylingKind) {
		return getTextStylingFormatter(textStylingKind.name());
	}

	public ITextStylingFormatter getTextStylingFormatter(String textStylingKind) {
		super.initializeIfNeeded();
		return formatters.get(textStylingKind);
	}

}

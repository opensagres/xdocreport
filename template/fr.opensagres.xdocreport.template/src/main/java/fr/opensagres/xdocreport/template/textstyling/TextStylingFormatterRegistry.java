package fr.opensagres.xdocreport.template.textstyling;

import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.core.registry.AbstractRegistry;
import fr.opensagres.xdocreport.template.TextStylingKind;
import fr.opensagres.xdocreport.template.discovery.ITextStylingFormatterDiscovery;

public class TextStylingFormatterRegistry extends
		AbstractRegistry<ITextStylingFormatterDiscovery> {

	public static final String KEY = "___TextStylingFormatterRegistry";

	private static final TextStylingFormatterRegistry INSTANCE = new TextStylingFormatterRegistry();
	private final Map<String, ITextStylingFormatter> formatters = new HashMap<String, ITextStylingFormatter>();

	public TextStylingFormatterRegistry() {
		super(ITextStylingFormatterDiscovery.class);
	}

	public static TextStylingFormatterRegistry getRegistry() {
		return INSTANCE;
	}

	@Override
	protected boolean registerInstance(ITextStylingFormatterDiscovery discovery) {
		formatters.put(discovery.getId(), discovery.getFormatter());
		return true;
	}

	@Override
	protected void doDispose() {
		formatters.clear();
	}

	public ITextStylingFormatter getTextStylingFormatter(
			DocumentKind documentKind, TextStylingKind textStylingKind) {
		return getTextStylingFormatter(documentKind.name(),
				textStylingKind.name());
	}

	public ITextStylingFormatter getTextStylingFormatter(String documentKind,
			String textStylingKind) {
		String key = getKey(documentKind, textStylingKind);
		return getTextStylingFormatter(key);
	}

	public ITextStylingFormatter getTextStylingFormatter(String key) {
		super.initializeIfNeeded();
		return formatters.get(key);
	}

	public static String getKey(String documentKind, String textStylingKind) {
		StringBuilder key = new StringBuilder(documentKind);
		key.append("_");
		key.append(textStylingKind);
		return key.toString();
	}

	public String format(String content, String formatterKey) {
		ITextStylingFormatter formatter = getTextStylingFormatter(formatterKey);
		if (formatter != null) {
			return formatter.format(content);
		}
		return content;
	}
}

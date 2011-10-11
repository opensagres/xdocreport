package fr.opensagres.xdocreport.template.discovery;

import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.template.TextStylingKind;
import fr.opensagres.xdocreport.template.textstyling.TextStylingFormatterRegistry;

public abstract class AbstractTextStylingFormatterDiscovery implements
		ITextStylingFormatterDiscovery {

	private final String id;

	public AbstractTextStylingFormatterDiscovery(DocumentKind documentKind,
			TextStylingKind textStylingKind) {
		this(documentKind.name(), textStylingKind.name());
	}
	
	public AbstractTextStylingFormatterDiscovery(String documentKind,
			String textStylingKind) {
		this.id = TextStylingFormatterRegistry.getKey(documentKind,
				textStylingKind);
	}

	public String getId() {
		return id;
	}
}

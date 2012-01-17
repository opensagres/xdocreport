package fr.opensagres.xdocreport.document.tools;

import java.io.InputStream;
import java.util.HashMap;

import fr.opensagres.xdocreport.document.Generator;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class Request extends HashMap<String, String> {

	private static final long serialVersionUID = 4987378991548527030L;
	private final InputStream in;
	private final FieldsMetadata fieldsMetadata;
	private final Iterable<IDataProvider> dataProviders;

	public Request(InputStream in, String templateEngineKind,
			FieldsMetadata fieldsMetadata, Iterable<IDataProvider> dataProviders) {
		this.in = in;
		super.put(Generator.TEMPLATE_ENGINE_KIND_HTTP_PARAM, templateEngineKind);
		this.put(Generator.REPORT_ID_HTTP_PARAM, "xxxx");
		this.fieldsMetadata = fieldsMetadata;
		this.dataProviders = dataProviders;
	}

	public InputStream getIn() {
		return in;
	}

	public Iterable<IDataProvider> getDataProviders() {
		return dataProviders;
	}

	public FieldsMetadata getFieldsMetadata() {
		return fieldsMetadata;
	}
}

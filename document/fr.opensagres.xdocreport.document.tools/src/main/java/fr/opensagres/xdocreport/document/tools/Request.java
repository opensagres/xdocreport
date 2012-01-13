package fr.opensagres.xdocreport.document.tools;

import java.io.InputStream;
import java.util.HashMap;

import fr.opensagres.xdocreport.document.Generator;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class Request extends HashMap<String, String> {

	private static final long serialVersionUID = 4987378991548527030L;
	private final InputStream in;
	private final FieldsMetadata fieldsMetadata;
	private final IDataProvider contextAware;

	public Request(InputStream in, String templateEngineKind,
			FieldsMetadata fieldsMetadata, IDataProvider contextAware) {
		this.in = in;
		super.put(Generator.TEMPLATE_ENGINE_KIND_HTTP_PARAM, templateEngineKind);
		this.put(Generator.REPORT_ID_HTTP_PARAM, "xxxx");
		this.fieldsMetadata = fieldsMetadata;
		this.contextAware = contextAware;
	}

	public InputStream getIn() {
		return in;
	}

	public IDataProvider getDataProvider() {
		return contextAware;
	}
	
	public FieldsMetadata getFieldsMetadata() {
		return fieldsMetadata;
	}
}

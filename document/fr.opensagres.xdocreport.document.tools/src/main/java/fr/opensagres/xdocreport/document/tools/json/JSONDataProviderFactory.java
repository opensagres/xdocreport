package fr.opensagres.xdocreport.document.tools.json;

import java.util.Map;

import fr.opensagres.xdocreport.document.tools.IDataProvider;
import fr.opensagres.xdocreport.document.tools.IDataProviderFactory;

public class JSONDataProviderFactory implements IDataProviderFactory {

	public String getId() {
		return "json";
	}

	public String getDescription() {
		return "JSON Data Provider Factory";
	}

	public IDataProvider create(Map<String, String> parameters)
			throws Exception {
		String jsonData = parameters.get("jsonData");
		return new JSONDataProvider(jsonData);
	}

}

package fr.opensagres.xdocreport.document.tools;

import java.io.InputStream;
import java.io.OutputStream;

import fr.opensagres.xdocreport.core.discovery.IBaseDiscovery;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public interface IDataProviderFactory extends IBaseDiscovery {

	IDataProvider create(InputStream data, InputStream properties)
			throws Exception;

	void generateDefaultData(FieldsMetadata fieldsMetadata, OutputStream out)
			throws Exception;

}

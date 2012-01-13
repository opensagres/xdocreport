package fr.opensagres.xdocreport.document.tools;

import java.util.Map;

import fr.opensagres.xdocreport.core.discovery.IBaseDiscovery;

public interface IDataProviderFactory extends IBaseDiscovery {

	IDataProvider create(Map<String, String> parameters) throws Exception;

}

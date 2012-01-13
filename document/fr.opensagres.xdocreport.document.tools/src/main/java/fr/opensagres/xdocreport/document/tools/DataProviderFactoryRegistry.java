package fr.opensagres.xdocreport.document.tools;

import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.registry.AbstractRegistry;

public class DataProviderFactoryRegistry extends
		AbstractRegistry<IDataProviderFactory> {

	private static final DataProviderFactoryRegistry INSTANCE = new DataProviderFactoryRegistry();
	private Map<String, IDataProviderFactory> factories = new HashMap<String, IDataProviderFactory>();

	public static DataProviderFactoryRegistry getRegistry() {
		return INSTANCE;
	}

	public DataProviderFactoryRegistry() {
		super(IDataProviderFactory.class);
	}

	@Override
	protected boolean registerInstance(IDataProviderFactory factory) {
		factories.put(factory.getId(), factory);
		return true;
	}

	@Override
	protected void doDispose() {
		factories.clear();
	}

	public IDataProvider create(String id, Map<String, String> parameters)
			throws Exception {
		return getFactory(id).create(parameters);
	}

	public IDataProviderFactory getFactory(String id) {
		initializeIfNeeded();
		return factories.get(id);
	}

}

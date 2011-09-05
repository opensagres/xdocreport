/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com>, Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.opensagres.xdocreport.converter.internal.xslt;

import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

/**
 * XSLT {@link Templates} registry which is used to compute XSLT and cache it.
 * 
 */
public class XSLTemplatesRegistry {

	private static final XSLTemplatesRegistry INSTANCE = new XSLTemplatesRegistry();

	private Map<String, Templates> cachedTemplates = new HashMap<String, Templates>();

	private ITransformerFactory transformerFactory = DefaultTransformerFactory
			.getInstance();

	private XSLTemplatesRegistry() {

	}

	public static XSLTemplatesRegistry getRegistry() {
		return INSTANCE;
	}

	public Templates getTemplates(String uri) {
		return cachedTemplates.get(uri);
	}

	public Templates loadTemplates(String uri, Reader reader,
			URIResolver resolver) throws TransformerConfigurationException {
		return loadTemplates(uri, new StreamSource(reader), resolver);
	}

	public Templates loadTemplates(String uri, InputStream stream,
			URIResolver resolver) throws TransformerConfigurationException {
		return loadTemplates(uri, new StreamSource(stream), resolver);
	}

	public synchronized Templates loadTemplates(String uri, Source source,
			URIResolver resolver) throws TransformerConfigurationException {
		Templates templates = getTemplates(uri);
		if (templates != null) {
			return templates;
		}
		TransformerFactory factory = getTransformerFactory()
				.createTransformerFactory();
		if (resolver != null) {
			factory.setURIResolver(resolver);
		}
		templates = factory.newTemplates(source);
		cachedTemplates.put(uri, templates);
		return templates;
	}

	public void setTransformerFactory(ITransformerFactory transformerFactory) {
		this.transformerFactory = transformerFactory;
	}

	public ITransformerFactory getTransformerFactory() {
		return transformerFactory;
	}

	public void registerTemplates(String uri, Templates templates) {
		cachedTemplates.put(uri, templates);
	}
}

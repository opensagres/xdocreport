package fr.opensagres.xdocreport.template.formatter.xsd;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.impl.xs.XSModelImpl;
import org.apache.xerces.xs.XSModel;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IFieldsMetadataClassSerializer;

public class XSDFieldsMetadataClassSerializer implements
		IFieldsMetadataClassSerializer {

	public String getId() {
		return "JAXB";
	}

	public String getDescription() {
		return "JAXB based serializer";
	}

	public void load(FieldsMetadata fieldsMetadata, String key, Class<?> clazz)
			throws XDocReportException {
		load(fieldsMetadata, key, clazz, false);

	}

	public void load(FieldsMetadata fieldsMetadata, String key, Class<?> clazz,
			boolean listType) throws XDocReportException {
		try {
			XMLSchemaLoader loader = new XMLSchemaLoader();
			XSModel model = loader.loadURI(new File("schema1.xsd").toURI().toString());

			JAXBContext jc = JAXBContext.newInstance(clazz);
			final DOMResult dmoResult = new DOMResult();
			jc.generateSchema(new SchemaOutputResolver() {

				@Override
				public Result createOutput(String namespaceUri,
						String suggestedFileName) throws IOException {
					//return new StreamResult(new File(suggestedFileName));
					return dmoResult;
				}
			});


		} catch (JAXBException e) {
			throw new XDocReportException(e);
		} catch (IOException e) {
			throw new XDocReportException(e);
		}

	}

}

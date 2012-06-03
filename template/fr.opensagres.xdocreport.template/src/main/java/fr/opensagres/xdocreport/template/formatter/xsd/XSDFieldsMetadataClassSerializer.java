package fr.opensagres.xdocreport.template.formatter.xsd;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.impl.xs.XSModelImpl;
import org.apache.xerces.xs.XSModel;
import org.w3c.dom.Document;

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


			JAXBContext jc = JAXBContext.newInstance(clazz);

			DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();

			// création d'un constructeur de documents
			DocumentBuilder constructeur = fabrique.newDocumentBuilder();

			final Document document=constructeur.newDocument( );


			jc.generateSchema(new SchemaOutputResolver() {

				@Override
				public Result createOutput(String namespaceUri,
						String suggestedFileName) throws IOException {
					//return new StreamResult(new File(suggestedFileName));
					DOMResult res= new DOMResult(document);
					res.setSystemId("demo");
					return res;
				}
			});



		} catch (JAXBException e) {
			throw new XDocReportException(e);
		} catch (IOException e) {
			throw new XDocReportException(e);
		} catch (ParserConfigurationException e) {
			throw new XDocReportException(e);
		}

	}

}

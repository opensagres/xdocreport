package fr.opensagres.xdocreport.template.textstyling.html;

import java.io.StringReader;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import fr.opensagres.xdocreport.template.textstyling.AbstractTextStylingFormatter;
import fr.opensagres.xdocreport.template.textstyling.IDocumentVisitor;

public abstract class HTMLTextStylingFormatter extends
		AbstractTextStylingFormatter {

	public String format(String s) {
		try {
			IDocumentVisitor visitor = createVisitor();
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setContentHandler(new HTMLTextStylingContentHandler(
					visitor));
			xmlReader.parse(new InputSource(new StringReader(s)));
			return visitor.toString();
		} catch (Throwable e) {
			e.printStackTrace();
			return s;
		}
	}

}

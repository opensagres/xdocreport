package fr.opensagres.xdocreport.template.textstyling.html;

import java.io.StringReader;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import fr.opensagres.xdocreport.template.textstyling.AbstractTextStylingFormatter;
import fr.opensagres.xdocreport.template.textstyling.IDocumentVisitor;
import fr.opensagres.xdocreport.template.textstyling.ITextStylingFormatter;

public class HTMLTextStylingFormatter extends AbstractTextStylingFormatter {

	public static final ITextStylingFormatter INSTANCE = new HTMLTextStylingFormatter();

	@Override
	protected void doFormat(String s, IDocumentVisitor visitor)
			throws Exception {
		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		xmlReader.setContentHandler(new HTMLTextStylingContentHandler(visitor));
		xmlReader.parse(new InputSource(new StringReader(s)));
	}

}

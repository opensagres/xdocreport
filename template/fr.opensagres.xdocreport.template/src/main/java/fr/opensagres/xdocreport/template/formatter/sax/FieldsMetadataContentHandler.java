package fr.opensagres.xdocreport.template.formatter.sax;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class FieldsMetadataContentHandler extends DefaultHandler {

	//
	// Data
	//

	/** Print writer. */
	protected PrintWriter fOut;

	/** Canonical output. */
	protected boolean fCanonical;

	/** Document locator. */
	protected Locator fLocator;

	/** Processing XML 1.1 document. */
	protected boolean fXML11;

	/** In CDATA section. */
	protected boolean fInCDATA;

	//
	// Constructors
	//

	/** Default constructor. */
	public FieldsMetadataContentHandler() {
	} // <init>()

	//
	// Public methods
	//

	/** Sets whether output is canonical. */
	public void setCanonical(boolean canonical) {
		fCanonical = canonical;
	} // setCanonical(boolean)

	/** Sets the output stream for printing. */
	public void setOutput(OutputStream stream, String encoding)
			throws UnsupportedEncodingException {

		if (encoding == null) {
			encoding = "UTF8";
		}

		java.io.Writer writer = new OutputStreamWriter(stream, encoding);
		fOut = new PrintWriter(writer);

	} // setOutput(OutputStream,String)

	/** Sets the output writer. */
	public void setOutput(java.io.Writer writer) {

		fOut = writer instanceof PrintWriter ? (PrintWriter) writer
				: new PrintWriter(writer);

	} // setOutput(java.io.Writer)

	//
	// ContentHandler methods
	//

	/** Start document. */
	public void startDocument() throws SAXException {
		fOut.println("<?xml version=\"1.1\" encoding=\"UTF-8\"?>");

	} // startDocument()

	private FieldsMetadata fieldsMetadata;

	public FieldsMetadata getFieldsMetadata() {
		return fieldsMetadata;
	}

	/** Start element. */
	public void startElement(String uri, String local, String raw,
			Attributes attrs) throws SAXException {
		if ("fields".equals(local)) {
			fieldsMetadata = new FieldsMetadata();
		} else if ("field".equals(local)) {
			Boolean listType = null;

			String listTypeStr = attrs.getValue("listType");
			if (listTypeStr != null) {
				listType = Boolean.valueOf(listTypeStr);
			}

			fieldsMetadata.addField(attrs.getValue("name"), listType,
					attrs.getValue("imageName"), attrs.getValue("syntaxKind"));
		}

		
		fOut.print('<');
		fOut.print(raw);
		if (attrs != null) {
			// attrs = sortAttributes(attrs);
			int len = attrs.getLength();
			for (int i = 0; i < len; i++) {
				fOut.print(' ');
				fOut.print(attrs.getQName(i));
				fOut.print("=\"");
				normalizeAndPrint(attrs.getValue(i), true);
				fOut.print('"');
			}
		}
		fOut.print('>');
		fOut.flush();

	} // startElement(String,String,String,Attributes)

	/** Normalizes and prints the given string. */
	protected void normalizeAndPrint(String s, boolean isAttValue) {

		int len = (s != null) ? s.length() : 0;
		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			normalizeAndPrint(c, isAttValue);
		}

	} // normalizeAndPrint(String,boolean)

	/** Characters. */
	public void characters(char ch[], int start, int length)
			throws SAXException {

		if (!fInCDATA) {
			normalizeAndPrint(ch, start, length, false);
		} else {
			for (int i = 0; i < length; ++i) {
				fOut.print(ch[start + i]);
			}
		}
		fOut.flush();

	} // characters(char[],int,int);

	/** End element. */
	public void endElement(String uri, String local, String raw)
			throws SAXException {

		
		fOut.print("</");
		fOut.print(raw);
		fOut.print('>');
		fOut.flush();

	} // endElement(String)

	/** Normalizes and prints the given array of characters. */
	protected void normalizeAndPrint(char[] ch, int offset, int length,
			boolean isAttValue) {
		for (int i = 0; i < length; i++) {
			normalizeAndPrint(ch[offset + i], isAttValue);
		}
	} // normalizeAndPrint(char[],int,int,boolean)

	/** Normalizes and print the given character. */
	protected void normalizeAndPrint(char c, boolean isAttValue) {

		switch (c) {
		case '<': {
			fOut.print("&lt;");
			break;
		}
		case '>': {
			fOut.print("&gt;");
			break;
		}
		case '&': {
			fOut.print("&amp;");
			break;
		}
		case '"': {
			// A '"' that appears in character data
			// does not need to be escaped.
			if (isAttValue) {
				fOut.print("&quot;");
			} else {
				fOut.print("\"");
			}
			break;
		}
		case '\r': {
			// If CR is part of the document's content, it
			// must not be printed as a literal otherwise
			// it would be normalized to LF when the document
			// is reparsed.
			fOut.print("&#xD;");
			break;
		}
		case '\n': {
			if (fCanonical) {
				fOut.print("&#xA;");
				break;
			}
			// else, default print char
		}
		default: {
			// In XML 1.1, control chars in the ranges [#x1-#x1F, #x7F-#x9F]
			// must be
			// escaped.
			//
			// Escape space characters that would be normalized to #x20 in
			// attribute
			// values
			// when the document is reparsed.
			//
			// Escape NEL (0x85) and LSEP (0x2028) that appear in content
			// if the document is XML 1.1, since they would be normalized to LF
			// when the document is reparsed.
			if (fXML11
					&& ((c >= 0x01 && c <= 0x1F && c != 0x09 && c != 0x0A)
							|| (c >= 0x7F && c <= 0x9F) || c == 0x2028)
					|| isAttValue && (c == 0x09 || c == 0x0A)) {
				fOut.print("&#x");
				fOut.print(Integer.toHexString(c).toUpperCase());
				fOut.print(";");
			} else {
				fOut.print(c);
			}
		}
		}
	} // normalizeAndPrint(char,boolean)

} // class Writer

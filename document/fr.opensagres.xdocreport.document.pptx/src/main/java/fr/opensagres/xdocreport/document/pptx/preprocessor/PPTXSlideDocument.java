package fr.opensagres.xdocreport.document.pptx.preprocessor;

import static fr.opensagres.xdocreport.document.pptx.PPTXUtils.isAP;
import static fr.opensagres.xdocreport.document.pptx.PPTXUtils.isAR;
import static fr.opensagres.xdocreport.document.pptx.PPTXUtils.isPTxBody;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocument;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public class PPTXSlideDocument extends TransformedBufferedDocument {

	private APBufferedRegion currentAPRegion;
	private ARBufferedRegion currentARRegion;
	private PTxBodyBufferedRegion currentTtxBodyRegion;

	private final PPTXSlideContentHandler handler;

	public PPTXSlideDocument(PPTXSlideContentHandler handler) {
		this.handler = handler;
	}

	@Override
	protected boolean isTable(String arg0, String arg1, String arg2) {
		return false;
	}

	@Override
	protected boolean isTableRow(String arg0, String arg1, String arg2) {
		return false;
	}

	@Override
	protected BufferedElement createElement(BufferedElement parent, String uri,
			String localName, String name, Attributes attributes)
			throws SAXException {
		if (isPTxBody(uri, localName, name)) {
			currentTtxBodyRegion = new PTxBodyBufferedRegion(parent, uri,
					localName, name, attributes);
			return currentTtxBodyRegion;
		}
		if (isAP(uri, localName, name)) {
			currentAPRegion = new APBufferedRegion(this, parent, uri,
					localName, name, attributes);
			if (currentTtxBodyRegion != null) {
				currentTtxBodyRegion.addRegion(currentAPRegion);
			}
			return currentAPRegion;
		}
		if (isAR(uri, localName, name)) {
			currentARRegion = new ARBufferedRegion(parent, uri, localName,
					name, attributes);
			if (currentAPRegion != null) {
				currentAPRegion.addRegion(currentARRegion);
			}
			return currentARRegion;
		}
		return super.createElement(parent, uri, localName, name, attributes);
	}

	@Override
	public void onEndEndElement(String uri, String localName, String name) {
		if (isPTxBody(uri, localName, name) && currentTtxBodyRegion != null) {
			super.onEndEndElement(uri, localName, name);
			currentTtxBodyRegion.process();
			currentTtxBodyRegion = null;
			return;
		}
		if (isAP(uri, localName, name) && currentAPRegion != null) {
			super.onEndEndElement(uri, localName, name);
			currentAPRegion = null;
			return;
		}
		if (isAR(uri, localName, name) && currentAPRegion != null) {
			super.onEndEndElement(uri, localName, name);
			currentARRegion = null;
			return;
		}
		super.onEndEndElement(uri, localName, name);
	}

	public APBufferedRegion getCurrentAPRegion() {
		return currentAPRegion;
	}

	public ARBufferedRegion getCurrentARRegion() {
		return currentARRegion;
	}

	public FieldsMetadata getFieldsMetadata() {
		return handler.getFieldsMetadata();
	}

	public IDocumentFormatter getFormatter() {
		return handler.getFormatter();
	}
}

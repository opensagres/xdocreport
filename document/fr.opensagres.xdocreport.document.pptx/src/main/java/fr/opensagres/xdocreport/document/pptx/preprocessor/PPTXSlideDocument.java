package fr.opensagres.xdocreport.document.pptx.preprocessor;

import static fr.opensagres.xdocreport.document.pptx.PPTXUtils.isAP;
import static fr.opensagres.xdocreport.document.pptx.PPTXUtils.isAR;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocument;

public class PPTXSlideDocument extends TransformedBufferedDocument {

	private APBufferedRegion currentAPRegion;
	private ARBufferedRegion currentARRegion;

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
		if (isAP(uri, localName, name)) {
			currentAPRegion = new APBufferedRegion(parent, uri, localName,
					name, attributes);
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
		if (isAP(uri, localName, name) && currentAPRegion != null) {
			super.onEndEndElement(uri, localName, name);
			currentAPRegion.process();
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
}

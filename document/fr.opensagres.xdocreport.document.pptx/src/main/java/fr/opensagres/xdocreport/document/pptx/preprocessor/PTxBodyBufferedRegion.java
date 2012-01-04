package fr.opensagres.xdocreport.document.pptx.preprocessor;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.ISavable;

/**
 * 
 * <p:txBody>
 * 
 */
public class PTxBodyBufferedRegion extends BufferedElement {

	private List<APBufferedRegion> apBufferedRegions = new ArrayList<APBufferedRegion>();

	public PTxBodyBufferedRegion(BufferedElement parent, String uri,
			String localName, String name, Attributes attributes) {
		super(parent, uri, localName, name, attributes);
	}

	@Override
	public void addRegion(ISavable region) {
		if (region instanceof APBufferedRegion) {
			apBufferedRegions.add((APBufferedRegion) region);
		} else {
			super.addRegion(region);
		}
	}

	public void process() {
		for (APBufferedRegion p : apBufferedRegions) {
			p.process();
		}
	}

}

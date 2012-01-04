package fr.opensagres.xdocreport.document.pptx.preprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.ISavable;

public class APBufferedRegion extends BufferedElement {

	private List<ARBufferedRegion> arBufferedRegions = new ArrayList<ARBufferedRegion>();

	public APBufferedRegion(BufferedElement parent, String uri,
			String localName, String name, Attributes attributes) {
		super(parent, uri, localName, name, attributes);
	}

	@Override
	public void addRegion(ISavable region) {
		if (region instanceof ARBufferedRegion) {
			arBufferedRegions.add((ARBufferedRegion) region);
		} else {
			super.addRegion(region);
		}
	}

	public void process() {
		Collection<BufferedElement> toRemove = new ArrayList<BufferedElement>();

		int size = arBufferedRegions.size();
		String s = null;
		StringBuilder fullContent = new StringBuilder();
		boolean fieldFound = false;
		ARBufferedRegion currentAR = null;
		ARBufferedRegion lastAR = null;
		for (int i = 0; i < size; i++) {
			currentAR = arBufferedRegions.get(i);
			s = currentAR.getTContent();
			if (fieldFound) {
				fieldFound = !(s == null || s.length() == 0 || Character
						.isWhitespace(s.charAt(0)));
			} else {
				fieldFound = s != null && s.indexOf("$") != -1;
			}
			if (fieldFound) {
				fullContent.append(s);
				toRemove.add(currentAR);
			} else {
				if (fullContent.length() > 0) {
					lastAR.setTContent(fullContent.toString());
					fullContent.setLength(0);
					toRemove.remove(lastAR);
				}
			}
			lastAR = currentAR;
		}
		if (fullContent.length() > 0) {
			lastAR.setTContent(fullContent.toString());
			fullContent.setLength(0);
			toRemove.remove(lastAR);
		}
		super.removeAll(toRemove);

	}

}

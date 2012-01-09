package fr.opensagres.xdocreport.document.pptx.preprocessor;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.core.utils.StringUtils;
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
		APBufferedRegion p = null;
		String itemNameList = null;
		int size = apBufferedRegions.size();
		Integer level = null;
		for (int i = 0; i < size; i++) {
			p = apBufferedRegions.get(i);
			p.process();

			itemNameList = p.getItemNameList();
			if (StringUtils.isNotEmpty(itemNameList)) {
				level = p.getLevel();
				APBufferedRegion nextP = null;
				if (i < size) {
					// Get next p
					nextP = getNextPInferiorToLevel(i + 1, level);
				}
				if (nextP != null) {
					nextP.addIgnoreLoopDirective(itemNameList);
					nextP.addEndLoopDirective(itemNameList);
				} else {
					p.addEndLoopDirective(itemNameList);
				}
			}

		}
	}

	private APBufferedRegion getNextPInferiorToLevel(int start, Integer level) {
		APBufferedRegion p = null;
		APBufferedRegion lastP = null;
		for (int i = start; i < apBufferedRegions.size(); i++) {
			p = apBufferedRegions.get(i);
			if (p.getLevel() == null) {
				return lastP;
			}
			if (level != null) {
				if (p.getLevel() >= level) {
					return lastP;
				}
			}
			lastP = p;
		}
		return lastP;
	}

}

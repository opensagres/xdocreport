package fr.opensagres.xdocreport.document.pptx.preprocessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.ISavable;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public class APBufferedRegion extends BufferedElement {

	private final PPTXSlideDocument document;
	private final List<ARBufferedRegion> arBufferedRegions;

	public APBufferedRegion(PPTXSlideDocument document, BufferedElement parent,
			String uri, String localName, String name, Attributes attributes) {
		super(parent, uri, localName, name, attributes);
		this.document = document;
		this.arBufferedRegions = new ArrayList<ARBufferedRegion>();
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
				update(toRemove, fullContent, lastAR);
			}
			lastAR = currentAR;
		}
		update(toRemove, fullContent, lastAR);
		super.removeAll(toRemove);

	}

	private void update(Collection<BufferedElement> toRemove,
			StringBuilder fullContent, ARBufferedRegion lastAR) {
		if (fullContent.length() > 0) {
			String content = fullContent.toString();
			String itemNameList = getItemNameList(content);
			lastAR.setTContent(itemNameList != null ? itemNameList : content);
			fullContent.setLength(0);
			toRemove.remove(lastAR);
		}

	}

	private String getItemNameList(String content) {
		IDocumentFormatter formatter = document.getFormatter();
		FieldsMetadata fieldsMetadata = document.getFieldsMetadata();
		if (formatter != null && fieldsMetadata != null) {

			Collection<String> fieldsAsList = fieldsMetadata.getFieldsAsList();
			for (final String fieldName : fieldsAsList) {
				if (content.contains(fieldName)) {
					String itemNameList = formatter.extractItemNameList(
							content, fieldName, true);
					if (StringUtils.isNotEmpty(itemNameList)) {
						setStartLoopDirective(formatter
								.getStartLoopDirective(itemNameList));
						setEndLoopDirective(formatter
								.getEndLoopDirective(itemNameList));

						return formatter.formatAsFieldItemList(content,
								fieldName, true);
					}
				}
			}
		}
		return null;
	}

	private void setEndLoopDirective(String endLoopDirective) {
		this.endTagElement.setAfter(endLoopDirective);
	}

	private void setStartLoopDirective(String startLoopDirective) {
		this.startTagElement.setBefore(startLoopDirective);
	}

}

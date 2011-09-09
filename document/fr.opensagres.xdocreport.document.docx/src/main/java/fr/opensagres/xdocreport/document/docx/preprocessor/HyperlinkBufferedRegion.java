/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.xdocreport.document.docx.preprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import fr.opensagres.xdocreport.document.preprocessor.sax.AttributeBufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.IBufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.ProcessRowResult;
import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.formatter.LoopDirective;

/**
 * <pre>
 *  <w:hyperlink r:id="rId5" w:history="1">
 * 		
 * 		<w:r w:rsidRPr="000F2653">
 * 		 	<w:rPr>
 *   			<w:rStyle w:val="Lienhypertexte" /> 
 *   		</w:rPr>
 *   		<w:t>$</w:t> 
 *  	</w:r>
 *  	<w:proofErr w:type="spellStart" /> 
 *  	<w:r w:rsidRPr="000F2653">
 *  		<w:rPr>
 *   			<w:rStyle w:val="Lienhypertexte" /> 
 *   		</w:rPr>
 *   		<w:t>developers.Mail</w:t> 
 *  	</w:r>
 *  	<w:proofErr w:type="spellEnd" /> 
 *  </w:hyperlink>
 * </pre>
 * 
 */
public class HyperlinkBufferedRegion extends BufferedRegion {

	private final TransformedBufferedDocumentContentHandler handler;
	private List<RBufferedRegion> rBufferedRegions = new ArrayList<RBufferedRegion>();
	private AttributeBufferedRegion idAttribute;

	public HyperlinkBufferedRegion(
			TransformedBufferedDocumentContentHandler handler,
			IBufferedRegion parent) {
		super(parent);
		this.handler = handler;
	}

	@Override
	public void addRegion(IBufferedRegion region) {
		if (region instanceof RBufferedRegion) {
			rBufferedRegions.add((RBufferedRegion) region);
		}
		super.addRegion(region);
	}

	public void process() {
		IDocumentFormatter formatter = handler.getFormatter();
		if (formatter == null) {
			return;
		}
		// Concat all w:t text node
		String content = getAllTContent();
		ProcessRowResult result = handler.getProcessRowResult(content, false);
		String newContent = result.getContent();
		if (newContent != null) {
			if (result.getFieldName() != null) {
				// Hyperlink is in a Table and hyperlink is a list (see
				// FieldsMetadata), transform it.

				// 1) Modify w:t
				modifyTContents(newContent);
				// 2) Populate HyperlinkInfo if needed
				if (handler.hasSharedContext()) {
					String item = result.getItemNameList();
					populateHyperlinkInfo(formatter, item,
							result.getStartLoopDirective(),
							result.getEndLoopDirective());
				}
			} else {
				if (formatter.containsInterpolation(newContent)) {
					// the new content contains fields which are interpolation
					// (ex:${developer.name}

					// 1) Modify w:t
					modifyTContents(newContent);
					if (handler.hasSharedContext()) {

						// 2) Populate HyperlinkInfo if needed
						Stack<LoopDirective> directives = handler
								.getDirectives();
						if (!directives.isEmpty()) {
							LoopDirective directive = directives.peek();

							String item = formatter
									.extractModelTokenPrefix(newContent);
							if (item.equals(directive.getItem())) {
								String startLoopDirective = directive
										.getStartLoopDirective();
								String endLoopDirective = directive
										.getEndLoopDirective();
								populateHyperlinkInfo(formatter, item,
										startLoopDirective, endLoopDirective);
							}
						}
					}
				}
			}
		}

	}

	private String getAllTContent() {
		StringBuilder t = new StringBuilder();
		for (RBufferedRegion r : rBufferedRegions) {
			String c = r.getTContent();
			if (c != null) {
				t.append(c);
			}
		}
		return t.toString();
	}

	private void populateHyperlinkInfo(IDocumentFormatter formatter,
			String item, String startLoopDirective, String endLoopDirective) {

		String loopCount = formatter.getLoopCountDirective(item);
		String hyperlinkId = idAttribute.getValue();
		String scriptHyperlinkId = hyperlinkId + "_" + loopCount;
		idAttribute.setValue(scriptHyperlinkId);

		// Add hyperlink info in the shared context
		HyperlinkInfo hyperlink = new HyperlinkInfo(hyperlinkId,
				scriptHyperlinkId, startLoopDirective, endLoopDirective);
		Map<String, HyperlinkInfo> hyperlinks = (Map<String, HyperlinkInfo>) handler
				.getSharedContext().get(HyperlinkInfo.KEY);
		if (hyperlinks == null) {
			hyperlinks = new HashMap<String, HyperlinkInfo>();
			handler.getSharedContext().put(HyperlinkInfo.KEY, hyperlinks);
		}
		hyperlinks.put(hyperlinkId, hyperlink);
	}

	private void modifyTContents(String newContent) {
		for (int i = 0; i < rBufferedRegions.size(); i++) {
			if (i == rBufferedRegions.size() - 1) {
				rBufferedRegions.get(i).setTContent(newContent);
			} else {
				rBufferedRegions.get(i).setTContent("");
			}
		}
	}

	public void setId(String name, String value) {
		if (idAttribute == null) {
			idAttribute = new AttributeBufferedRegion(this, name, value);
		}
		idAttribute.setValue(value);
	}
}

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

import fr.opensagres.xdocreport.document.preprocessor.sax.AttributeBufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.IBufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.ProcessRowResult;
import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

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
	private final boolean hasContext;

	public HyperlinkBufferedRegion(
			TransformedBufferedDocumentContentHandler handler,
			IBufferedRegion parent) {
		super(parent);
		this.handler = handler;
		hasContext = handler.getSharedContext() != null;
	}

	@Override
	public void addRegion(IBufferedRegion region) {
		if (region instanceof RBufferedRegion) {
			rBufferedRegions.add((RBufferedRegion) region);
		}
		super.addRegion(region);
	}

	public void process() {
		FieldsMetadata fieldsMetadata = handler.getFieldsMetadata();
		IDocumentFormatter formatter = handler.getFormatter();
		if (fieldsMetadata == null || formatter == null) {
			return;
		}
		StringBuilder t = new StringBuilder();
		for (RBufferedRegion r : rBufferedRegions) {
			String c = r.getTContent();
			if (c != null) {
				t.append(c);
			}
		}
		String content = t.toString();

		// If hyperlink is in a Table and hyperlink is a list (see
		// FieldsMetadata), transform it.
		ProcessRowResult result = handler.getProcessRowResult(content, false);
		String newContent = result.getContent();
		if (newContent != null && result.getFieldName() != null) {

			// Modify w:t
			for (int i = 0; i < rBufferedRegions.size(); i++) {
				if (i == rBufferedRegions.size() - 1) {
					rBufferedRegions.get(i).setTContent(newContent);
				} else {
					rBufferedRegions.get(i).setTContent("");
				}
			}

			// Modify id attribute
			if (hasContext) {
				String loopCount = formatter.getLoopCountDirective(result
						.getItemNameList());
				String hyperlinkId = idAttribute.getValue();
				String scriptHyperlinkId = hyperlinkId + "_" + loopCount;
				idAttribute.setValue(scriptHyperlinkId);

				// Add hyperlink info in the shared context
				HyperlinkInfo hyperlink = new HyperlinkInfo(hyperlinkId,
						scriptHyperlinkId, result.getStartLoopDirective(),
						result.getEndLoopDirective());
				Map<String, HyperlinkInfo> hyperlinks = (Map<String, HyperlinkInfo>) handler
						.getSharedContext().get(HyperlinkInfo.KEY);
				if (hyperlinks == null) {
					hyperlinks = new HashMap<String, HyperlinkInfo>();
					handler.getSharedContext().put(HyperlinkInfo.KEY, hyperlinks);
				}
				hyperlinks.put(hyperlinkId, hyperlink);
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

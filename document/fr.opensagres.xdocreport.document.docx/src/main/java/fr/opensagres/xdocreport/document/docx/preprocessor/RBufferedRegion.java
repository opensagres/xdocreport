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

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;
import fr.opensagres.xdocreport.document.preprocessor.sax.StringBufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.TransformedBufferedDocumentContentHandler;
import fr.opensagres.xdocreport.template.formatter.FieldMetadata;

public class RBufferedRegion extends MergefieldBufferedRegion {

	private String fldCharType;
	private StringBufferedRegion tContentRegion = null;
	private String originalInstrText;
	private FieldMetadata fieldAsTextStyling;

	public RBufferedRegion(TransformedBufferedDocumentContentHandler handler,
			BufferedElement parent, String uri, String localName, String name,
			Attributes attributes) {
		super(handler, parent, uri, localName, name, attributes);
		this.originalInstrText = null;
	}

	public void setFldCharType(String fldCharType) {
		this.fldCharType = fldCharType;
	}

	public String getFldCharType() {
		return fldCharType;
	}

	public void setTContent(String tContent) {
		getTRegion().setTextContent(tContent);
	}

	@Override
	public String setInstrText(String instrText,
			FieldMetadata fieldAsTextStyling) {
		this.originalInstrText = instrText;
		this.fieldAsTextStyling = fieldAsTextStyling;
		instrText = super.setInstrText(instrText, fieldAsTextStyling);
		super.append(instrText);
		return instrText;
	}

	public String getOriginalInstrText() {
		return originalInstrText;
	}

	public boolean hasInstrText() {
		return originalInstrText != null;
	}

	public String getTContent() {
		return getTRegion().getTextContent();
	}

	public FieldMetadata getFieldAsTextStyling() {
		return fieldAsTextStyling;
	}
}

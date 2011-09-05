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

import fr.opensagres.xdocreport.document.preprocessor.sax.IBufferedRegion;

/**
 * w:fldSimple with MERGEFIELD type and false otherwise.
 * 
 * If element is w:fldSimple :
 * 
 * <pre>
 * <w:fldSimple w:instr=" MERGEFIELD  ${name} ">
 * 		<w:r w:rsidR="00396432">
 * 			<w:rPr>
 * 				<w:noProof/>
 * 				</w:rPr>
 * 				<w:t>«${name}»</w:t>
 * 		</w:r>
 * 	</w:fldSimple>
 * </pre>
 * 
 * it is transformed with
 * 
 * <pre>
 * <w:r w:rsidR="00396432">
 * 			<w:rPr>
 * 				<w:noProof/>
 * 				</w:rPr>
 * 				<w:t>${name}</w:t>
 * 		</w:r>
 * </pre>
 * 
 */
public class FldSimpleBufferedRegion extends MergefieldBufferedRegion {

	public FldSimpleBufferedRegion(IBufferedRegion parent) {
		super(parent);
	}

	public void setTContent(String tContent) {
		String fieldName = getFieldName();
		if (fieldName != null) {
			super.append(fieldName);
		} else {
			super.append(tContent);
		}
	}
}

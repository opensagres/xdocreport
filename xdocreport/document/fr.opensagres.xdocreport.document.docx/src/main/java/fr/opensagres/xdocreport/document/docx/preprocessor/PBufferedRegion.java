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
import java.util.List;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.IBufferedRegion;

/**
 * <pre>
 * <w:p w:rsidR="007B3A53" w:rsidRDefault="00F95F02">
 *  <w:pPr>
 *  <w:rPr>
 *   <w:color w:val="FF0000" /> 
 *   </w:rPr>
 *   </w:pPr>
 *  <w:r w:rsidRPr="00CE3A74">
 *  <w:rPr>
 *   <w:color w:val="FF0000" /> 
 *   </w:rPr>
 *   <w:fldChar w:fldCharType="begin" /> 
 *   </w:r>
 *  <w:r w:rsidR="000050F2" w:rsidRPr="00CE3A74">
 *  <w:rPr>
 *   <w:color w:val="FF0000" /> 
 *   </w:rPr>
 *   <w:instrText xml:space="preserve">MERGEFIELD Titre</w:instrText> 
 *   </w:r>
 *  <w:r w:rsidRPr="00CE3A74">
 *  <w:rPr>
 *   <w:color w:val="FF0000" /> 
 *   </w:rPr>
 *   <w:fldChar w:fldCharType="separate" /> 
 *   </w:r>
 *  <w:r w:rsidR="006716CB">
 *  <w:rPr>
 *   <w:noProof /> 
 *   <w:color w:val="FF0000" /> 
 *   </w:rPr>
 *   <w:t>«Titre»</w:t> 
 *   </w:r>
 *  <w:r w:rsidRPr="00CE3A74">
 *  <w:rPr>
 *   <w:color w:val="FF0000" /> 
 *   </w:rPr>
 *   <w:fldChar w:fldCharType="end" /> 
 *   </w:r>
 *   </w:p>
 * 
 * </pre>
 * 
 */
public class PBufferedRegion extends BufferedRegion {

	private List<RBufferedRegion> rBufferedRegions = new ArrayList<RBufferedRegion>();

	public PBufferedRegion(IBufferedRegion parent) {
		super(parent);
	}

	@Override
	public void addRegion(IBufferedRegion region) {
		if (region instanceof RBufferedRegion) {
			rBufferedRegions.add((RBufferedRegion) region);
		}
		super.addRegion(region);
	}

	public void process() {
		List<RBufferedRegion> toRemove = new ArrayList<RBufferedRegion>();
		boolean remove = false;
		boolean fieldNameSetted = false;
		String fieldName = null;
		for (int i = 0; i < rBufferedRegions.size(); i++) {
			RBufferedRegion rBufferedRegion = rBufferedRegions.get(i);
			if ("begin".equals(rBufferedRegion.getFldCharType())) {
				i++;
				RBufferedRegion nextR = rBufferedRegions.get(i);
				fieldName = nextR.getFieldName();
				if (fieldName != null) {
					toRemove.add(rBufferedRegion);
					toRemove.add(nextR);
					remove = true;
				}
			} else if ("separate".equals(rBufferedRegion.getFldCharType())
					&& remove) {
				toRemove.add(rBufferedRegion);
			} else if ("end".equals(rBufferedRegion.getFldCharType()) && remove) {
				toRemove.add(rBufferedRegion);
				remove = false;
				fieldName = null;
				fieldNameSetted = false;
			} else if (fieldName != null) {
				if (!fieldNameSetted) {
					rBufferedRegion.setTContent(fieldName);
					fieldNameSetted = true;
				} else {
					// remove
					toRemove.add(rBufferedRegion);
				}
			}
		}
		rBufferedRegions.removeAll(toRemove);
		regions.removeAll(toRemove);
	}

}

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

import fr.opensagres.xdocreport.core.EncodingConstants;
import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedRegion;
import fr.opensagres.xdocreport.document.preprocessor.sax.IBufferedRegion;

public abstract class MergefieldBufferedRegion extends BufferedRegion implements EncodingConstants {

	private static final String MERGEFORMAT = "\\* MERGEFORMAT";

	private static final String MERGEFIELD_FIELD_TYPE = "MERGEFIELD";

	private String fieldName;

	public MergefieldBufferedRegion(IBufferedRegion parent) {
		super(parent);
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setInstrText(String instrText) {
		this.fieldName = getFieldName(instrText);
	}

	public static String getFieldName(String instrText) {
		if (StringUtils.isEmpty(instrText)) {
			return null;
		}
		int index = instrText.indexOf(MERGEFIELD_FIELD_TYPE);
		if (index != -1) {
			// Extract field name and add it to the current buffer
			String fieldName = instrText.substring(
					index + MERGEFIELD_FIELD_TYPE.length(), instrText.length())
					.trim();
			if (StringUtils.isNotEmpty(fieldName)) {
				// Test if fieldName ends with \* MERGEFORMAT
				if (fieldName.endsWith(MERGEFORMAT)) {
					fieldName = fieldName.substring(0,
							fieldName.length() - MERGEFORMAT.length()).trim();
				}
				if (StringUtils.isNotEmpty(fieldName)) {
					// if #foreach is used w:fldSimple looks like this :
					// <w:fldSimple w:instr="MERGEFIELD "#foreach($developer in
					// $developers)" \\* MERGEFORMAT\"> to have
					// foreach($developer in $developers)
					// remove first " if needed
					if (fieldName.startsWith("\"") && fieldName.endsWith("\"")) {
						fieldName = fieldName.substring(1,
								fieldName.length() - 1);
					}
					// Fix bug http://code.google.com/p/xdocreport/issues/detail?id=29
					// Replace \" with "
					// ex : remplace [#if \"a\" = \"one\"]1[#else]not 1[/#if]
					// to have [#if "a" = "one"]1[#else]not 1[/#if]
					fieldName = StringUtils.replaceAll(fieldName, "\\\"", "\"");
					// ex : remplace [#if &apos;a&apos; = \"one\"]1[#else]not 1[/#if]
					// to have [#if 'a' = "one"]1[#else]not 1[/#if]
					fieldName = StringUtils.replaceAll(fieldName, APOS, "'");
					return fieldName;
				}
			}
		}
		return null;
	}

}

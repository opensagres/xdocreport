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
package fr.opensagres.xdocreport.template.freemarker;

import java.util.Collection;
import java.util.Stack;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.template.config.ITemplateEngineConfiguration;
import fr.opensagres.xdocreport.template.config.ReplaceText;
import fr.opensagres.xdocreport.template.formatter.AbstractDocumentFormatter;
import fr.opensagres.xdocreport.template.formatter.LoopDirective;

/**
 * Freemarker document formatter used to format fields list with Freemarker
 * syntax + escape XML and another characters.
 * 
 */
public class FreemarkerDocumentFormatter extends AbstractDocumentFormatter {

	private static final String START_LIST_DIRECTIVE = "[#list ";
	private static final String AS_DIRECTIVE = " as ";
	private static final String END_LIST_DIRECTIVE = "[/#list]";

	private static final String DOLLAR_TOTKEN = "${";
	protected static final String ITEM_TOKEN = "item_";

	private static final String START_ESCAPE = "[#escape any as any";
	private static final String XML_ESCAPE = "?xml";
	private static final String START_REPLACE_ESCAPE = "?replace(\"";
	private static final String BODY_REPLACE_ESCAPE = "\",\"";
	private static final String END_REPLACE_ESCAPE = "\")";
	private static final String CLOSE_ESCAPE = "]\n";
	private static final String END_ESCAPE = "[/#escape]";

	private static final String START_IF = "[#if ";
	private static final String END_IF = "[/#if]";

	private static final String START_IMAGE_DIRECTIVE = DOLLAR_TOTKEN
			+ IMAGE_REGISTRY_KEY + ".registerImage(";
	private static final String END_IMAGE_DIRECTIVE = ")}";

	public String formatAsFieldItemList(String content, String fieldName,
			boolean forceAsField) {
		if (forceAsField) {
			return ITEM_TOKEN + content;
		}
		if (!isModelField(content, fieldName)) {
			return content;
		}
		int startIndex = content.indexOf(DOLLAR_TOTKEN);
		startIndex = startIndex + DOLLAR_TOTKEN.length();
		int endIndex = content.indexOf('}');
		if (endIndex == -1) {
			return null;
		}
		String startContent = content.substring(0, startIndex);
		String endContent = content.substring(endIndex, content.length());
		String contentToFormat = content.substring(startIndex, endIndex);
		String formattedContent = StringUtils.replaceAll(contentToFormat,
				fieldName, getItemToken() + fieldName);
		return startContent + formattedContent + endContent;
	}

	public String getStartLoopDirective(String itemNameList, String listName) {
		StringBuilder result = new StringBuilder(START_LIST_DIRECTIVE);
		result.append(listName);
		result.append(AS_DIRECTIVE);
		result.append(itemNameList);
		result.append(']');
		return result.toString();
	}

	public String getEndLoopDirective(String itemNameList) {
		return END_LIST_DIRECTIVE;
	}

	protected boolean isModelField(String content, String fieldName) {
		if (StringUtils.isEmpty(content)) {
			return false;
		}
		int dollarIndex = content.indexOf(DOLLAR_TOTKEN);
		if (dollarIndex == -1) {
			// Not included to FM directive
			return false;
		}
		int fieldNameIndex = content.indexOf(fieldName);
		if (fieldNameIndex < dollarIndex) {
			// Not included to FM directive
			return false;
		}
		return true;
	}

	@Override
	protected String getItemToken() {
		return ITEM_TOKEN;
	}

	public void setConfiguration(ITemplateEngineConfiguration configuration) {
		Collection<ReplaceText> replacment = configuration.getReplacment();
		StringBuilder startEscape = new StringBuilder();
		if (!configuration.escapeXML() && replacment.size() < 1) {
			return;
		}
		startEscape.append(START_ESCAPE);
		if (configuration.escapeXML()) {
			startEscape.append(XML_ESCAPE);
		}

		for (ReplaceText replaceText : replacment) {
			startEscape.append(START_REPLACE_ESCAPE);
			startEscape.append(replaceText.getOldText());
			startEscape.append(BODY_REPLACE_ESCAPE);
			startEscape.append(replaceText.getNewText());
			startEscape.append(END_REPLACE_ESCAPE);
		}

		if (startEscape.length() > 0) {
			startEscape.append(CLOSE_ESCAPE);
			setStartDocumentDirective(startEscape.toString());
			setEndDocumentDirective(END_ESCAPE);
		}

	}

	public String getImageDirective(String fieldName) {
		StringBuilder directive = new StringBuilder(START_IMAGE_DIRECTIVE);
		directive.append(fieldName);
		directive.append(END_IMAGE_DIRECTIVE);
		return directive.toString();
	}

	public String formatAsSimpleField(boolean encloseInDirective,
			String... fields) {
		StringBuilder field = new StringBuilder();
		if (encloseInDirective) {
			field.append(DOLLAR_TOTKEN);
		}
		for (int i = 0; i < fields.length; i++) {
			if (i == 0) {
				field.append(fields[i]);
			} else {
				field.append('.');
				String f = fields[i];
				field.append(f.substring(0, 1).toLowerCase());
				field.append(f.substring(1, f.length()));
			}
		}
		if (encloseInDirective) {
			field.append('}');
		}
		return field.toString();
	}

	public String getStartIfDirective(String fieldName) {
		StringBuilder directive = new StringBuilder(START_IF);
		directive.append(fieldName);
		directive.append("??");
		directive.append(']');
		return directive.toString();
	}

	public String getEndIfDirective(String fieldName) {
		return END_IF;
	}

	public String getLoopCountDirective(String fieldName) {
		StringBuilder directive = new StringBuilder(DOLLAR_TOTKEN);
		directive.append(fieldName);
		directive.append("_index");
		directive.append('}');
		return directive.toString();
	}

	public boolean containsInterpolation(String content) {
		if (StringUtils.isEmpty(content)) {
			return false;
		}
		int dollarIndex = content.indexOf(DOLLAR_TOTKEN);
		if (dollarIndex == -1) {
			// Not included to FM directive
			return false;
		}
		return true;
	}

	public int extractListDirectiveInfo(String content,
			Stack<LoopDirective> directives) {
		// content='xxxx[#list developers as d]yyy'
		int startOfEndListDirectiveIndex = content.indexOf(END_LIST_DIRECTIVE);
		int startOfStartListDirectiveIndex = content
				.indexOf(START_LIST_DIRECTIVE);
		if (startOfStartListDirectiveIndex == -1
				&& startOfEndListDirectiveIndex == -1) {
			return 0;
		}

		if (startOfStartListDirectiveIndex == -1
				|| (startOfEndListDirectiveIndex != -1 && startOfStartListDirectiveIndex > startOfEndListDirectiveIndex)) {
			// content contains (at first [/#list])
			if (!directives.isEmpty()) {
				// remove the LoopDirective from the stack
				directives.pop();
			}
			// get content after the [/#list]
			String afterEndList = content.substring(END_LIST_DIRECTIVE.length()
					+ startOfEndListDirectiveIndex, content.length());
			int nbLoop = -1;
			// parse the content after the [/#list]
			nbLoop += extractListDirectiveInfo(afterEndList, directives);
			return nbLoop;
		}

		// contentWichStartsWithList='[#list developers as d]yyy'
		String contentWhichStartsWithList = content.substring(
				startOfStartListDirectiveIndex, content.length());
		int endOfStartListDirectiveIndex = contentWhichStartsWithList
				.indexOf(']');
		if (endOfStartListDirectiveIndex == -1) {
			// [#list not closed with ']'
			return 0;
		}
		// startLoopDirective='[#list developers as d]'
		String startLoopDirective = contentWhichStartsWithList.substring(0,
				endOfStartListDirectiveIndex + 1);
		// insideLoop='developers as d]'
		String insideLoop = startLoopDirective.substring(
				START_LIST_DIRECTIVE.length(), startLoopDirective.length());
		int indexBeforeAs = insideLoop.indexOf(" ");
		if (indexBeforeAs == -1) {
			return 0;
		}

		// afterSequence=' as d]'
		String afterSequence = insideLoop.substring(indexBeforeAs,
				insideLoop.length());
		int indexAfterAs = afterSequence.indexOf(AS_DIRECTIVE);
		if (indexAfterAs == -1) {
			return 0;
		}

		// sequence='developpers'
		String sequence = insideLoop.substring(0, indexBeforeAs).trim();
		if (StringUtils.isEmpty(sequence)) {
			return 0;
		}
		// afterAs='d]'
		String afterAs = afterSequence.substring(AS_DIRECTIVE.length(),
				afterSequence.length());
		int endListIndex = afterAs.indexOf(']');
		if (endListIndex == -1) {
			return 0;
		}
		// item='d'
		String item = afterAs.substring(0, endListIndex).trim();
		if (StringUtils.isEmpty(item)) {
			return 0;
		}

		int nbLoop = 1;
		directives.push(new LoopDirective(startLoopDirective,
				getEndLoopDirective(null), sequence, item));

		// afterList = 'yyy'
		String afterList = content.substring(startOfStartListDirectiveIndex
				+ startLoopDirective.length(), content.length());
		nbLoop += extractListDirectiveInfo(afterList, directives);
		return nbLoop;
	}

	public String extractModelTokenPrefix(String fieldName) {
		if (fieldName == null) {
			return null;
		}
		int dollarIndex = fieldName.indexOf(DOLLAR_TOTKEN);
		if (dollarIndex == -1) {
			return null;
		}
		int endIndex = fieldName.indexOf('}');
		if (endIndex == -1) {
			return null;
		}
		String fieldNameWithoutDollar = fieldName.substring(dollarIndex
				+ DOLLAR_TOTKEN.length(), endIndex);
		int lastDotIndex = fieldNameWithoutDollar.lastIndexOf('.');
		if (lastDotIndex == -1) {
			return fieldNameWithoutDollar;
		}
		return fieldNameWithoutDollar.substring(0, lastDotIndex);
	}
}

/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com>, Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.opensagres.xdocreport.template.freemarker;

import fr.opensagres.xdocreport.core.fields.AbstractFieldFormater;
import fr.opensagres.xdocreport.core.fields.IFieldFormater;
import fr.opensagres.xdocreport.core.utils.StringUtils;

public class FreemarkerFieldFormater extends AbstractFieldFormater {


	public static final IFieldFormater INSTANCE = new FreemarkerFieldFormater();

	private static final String START_LIST_DIRECTIVE = "[#list ";
	private static final String AS_DIRECTIVE = " as ";
	private static final String END_LIST_DIRECTIVE = "[/#list]";

	private static final String DOLLAR_TOTKEN = "${";
	protected static final String ITEM_TOKEN = "item_";

	public String formatAsFieldItemList(String content, String fieldName) {
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

	public String getStartLoopDirective(String itemNameList) {
		StringBuilder result = new StringBuilder(START_LIST_DIRECTIVE);
		result.append(itemNameList.substring(getItemToken().length()));
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
}

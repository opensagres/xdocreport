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

import junit.framework.TestCase;

public class FreemarkerFieldFormater_FormatAsFieldItemListTestcase extends
		TestCase {

	public void testFormatAsFieldItemDollar() throws Exception {
		String result = FreemarkerFieldFormater.INSTANCE.formatAsFieldItemList(
				"$", "lines.reference");
		assertEquals("$", result);
	}
	
	public void testFormatAsFieldItemListNoIncludedDirective() throws Exception {
		String result = FreemarkerFieldFormater.INSTANCE.formatAsFieldItemList(
				"lines.reference", "lines.reference");
		assertEquals("lines.reference", result);
	}
	
	public void testFormatAsFieldItemListOneDirectiveAndNoIncludedDirective() throws Exception {
		String result = FreemarkerFieldFormater.INSTANCE.formatAsFieldItemList(
				"${lines.reference} lines.reference", "lines.reference");
		assertEquals("${item_lines.reference} lines.reference", result);
	}
	
	public void testFormatAsFieldItemListOneDirectiveAndNoIncludedDirective2() throws Exception {
		String result = FreemarkerFieldFormater.INSTANCE.formatAsFieldItemList(
				"lines.reference ${lines.reference}", "lines.reference");
		assertEquals("lines.reference ${item_lines.reference}", result);
	}
	
	public void testFormatAsFieldItemListOneDirective() throws Exception {
		String result = FreemarkerFieldFormater.INSTANCE.formatAsFieldItemList(
				"${lines.reference}", "lines.reference");
		assertEquals("${item_lines.reference}", result);
	}

	public void testFormatAsFieldItemListWithAssign() throws Exception {
		String result = FreemarkerFieldFormater.INSTANCE.formatAsFieldItemList(
				"[#assign a = lines.reference]", "lines.reference");
		assertEquals("[#assign a = item_lines.reference]", result);
	}
	public void testFormatAsFieldItemListSeveralDirectives() throws Exception {
		String result = FreemarkerFieldFormater.INSTANCE.formatAsFieldItemList(
				"${lines.reference} AAA ${lines.reference}", "lines.reference");
		assertEquals("${item_lines.reference} AAA ${item_lines.reference}",
				result);
	}
}

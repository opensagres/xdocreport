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

import java.util.Stack;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.template.formatter.LoopDirective;

public class FreemarkerTemplateEngineDocumentFormatterExtractLoopDirectiveTestCase
		extends TestCase {

	public void testFormatAsFieldItemList() throws Exception {
		FreemarkerDocumentFormatter formatter = new FreemarkerDocumentFormatter();
		String content = "xxxx[#list developers as d]yyy";

		Stack<LoopDirective> directives = new Stack<LoopDirective>();
		int nbDirective = formatter.extractListDirectiveInfo(content,
				directives);

		assertEquals(1, nbDirective);
		assertEquals(1, directives.size());
		LoopDirective directive = directives.get(0);
		assertEquals("developers", directive.getSequence());
		assertEquals("d", directive.getItem());
	}

	public void test2() throws Exception {
		FreemarkerDocumentFormatter formatter = new FreemarkerDocumentFormatter();
		String content = "xxxx[#list developers as d]yyy[#list roles as r]";

		Stack<LoopDirective> directives = new Stack<LoopDirective>();
		int nbDirective = formatter.extractListDirectiveInfo(content,
				directives);

		assertEquals(2, nbDirective);
		assertEquals(2, directives.size());
		LoopDirective directive = directives.get(0);
		assertEquals("developers", directive.getSequence());
		assertEquals("d", directive.getItem());

		directive = directives.get(1);
		assertEquals("roles", directive.getSequence());
		assertEquals("r", directive.getItem());
	}

	public void test3() throws Exception {
		FreemarkerDocumentFormatter formatter = new FreemarkerDocumentFormatter();
		String content = "xxxx[#list developers as d]yyy[/#list]";

		Stack<LoopDirective> directives = new Stack<LoopDirective>();
		int nbDirective = formatter.extractListDirectiveInfo(content,
				directives);

		assertEquals(0, nbDirective);
		assertEquals(0, directives.size());
	}
	
	public void test4() throws Exception {
		FreemarkerDocumentFormatter formatter = new FreemarkerDocumentFormatter();
		String content = "xxxx[#list developers as d]";

		Stack<LoopDirective> directives = new Stack<LoopDirective>();
		int nbDirective = formatter.extractListDirectiveInfo(content,
				directives);

		assertEquals(1, nbDirective);
		assertEquals(1, directives.size());
		LoopDirective directive = directives.get(0);
		assertEquals("developers", directive.getSequence());
		assertEquals("d", directive.getItem());
		
		content = "[/#list]";
		nbDirective = formatter.extractListDirectiveInfo(content,
				directives);

		assertEquals(-1, nbDirective);
		assertEquals(0, directives.size());
	}

}

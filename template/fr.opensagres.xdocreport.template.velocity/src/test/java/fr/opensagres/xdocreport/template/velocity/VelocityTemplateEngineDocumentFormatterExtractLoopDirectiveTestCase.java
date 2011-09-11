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
package fr.opensagres.xdocreport.template.velocity;

import java.util.Stack;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.template.formatter.LoopDirective;

public class VelocityTemplateEngineDocumentFormatterExtractLoopDirectiveTestCase
		extends TestCase {

	public void testFormatAsFieldItemList() throws Exception {
		VelocityDocumentFormatter formatter = new VelocityDocumentFormatter();
		String content = "xxxx#foreach($d in $developers)yyy";

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
		VelocityDocumentFormatter formatter = new VelocityDocumentFormatter();
		String content = "xxxx#foreach($d in $developers)]yyy#foreach($r in $roles)";

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
		VelocityDocumentFormatter formatter = new VelocityDocumentFormatter();
		String content = "xxxx#foreach($d in $developers)yyy#end";

		Stack<LoopDirective> directives = new Stack<LoopDirective>();
		int nbDirective = formatter.extractListDirectiveInfo(content,
				directives);

		assertEquals(0, nbDirective);
		assertEquals(0, directives.size());
	}

	public void test4() throws Exception {
		VelocityDocumentFormatter formatter = new VelocityDocumentFormatter();
		String content = "xxxx#foreach($d in $developers)";

		Stack<LoopDirective> directives = new Stack<LoopDirective>();
		int nbDirective = formatter.extractListDirectiveInfo(content,
				directives);

		assertEquals(1, nbDirective);
		assertEquals(1, directives.size());
		LoopDirective directive = directives.get(0);
		assertEquals("developers", directive.getSequence());
		assertEquals("d", directive.getItem());

		content = "#end";
		nbDirective = formatter.extractListDirectiveInfo(content, directives);

		assertEquals(-1, nbDirective);
		assertEquals(0, directives.size());
	}

}

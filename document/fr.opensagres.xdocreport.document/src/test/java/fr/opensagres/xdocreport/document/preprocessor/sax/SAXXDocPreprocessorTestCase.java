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
package fr.opensagres.xdocreport.document.preprocessor.sax;

import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.TestCase;

public class SAXXDocPreprocessorTestCase extends TestCase {

	public void testname() throws Exception {
		MockSAXXDocPreprocessor preprocessor = new MockSAXXDocPreprocessor();
		StringReader reader = new StringReader(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><p>&lt;</p>");
		StringWriter writer = new StringWriter();
		preprocessor.preprocess("test", reader, writer, null, null, null, null);
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><p>&lt;</p>",
				writer.toString());
	}

	public void testname2() throws Exception {
		MockSAXXDocPreprocessor preprocessor = new MockSAXXDocPreprocessor();
		StringReader reader = new StringReader(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><p>It&apos;s a document <span>to test</span></p>");
		StringWriter writer = new StringWriter();
		preprocessor.preprocess("test", reader, writer, null, null, null, null);
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><p>It&apos;s a document <span>to test</span></p>",
				writer.toString());
	}

	public void testElementWithTextContent() throws Exception {
		MockSAXXDocPreprocessor preprocessor = new MockSAXXDocPreprocessor();
		StringReader reader = new StringReader(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><a>aaa</a>");
		StringWriter writer = new StringWriter();
		preprocessor.preprocess("test", reader, writer, null, null, null, null);
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><a>aaa</a>",
				writer.toString());
	}

	public void testElementClosed() throws Exception {
		MockSAXXDocPreprocessor preprocessor = new MockSAXXDocPreprocessor();
		StringReader reader = new StringReader(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><a/>");
		StringWriter writer = new StringWriter();
		preprocessor.preprocess("test", reader, writer, null, null, null, null);
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><a/>",
				writer.toString());
	}

	public void testSeveralElements() throws Exception {
		MockSAXXDocPreprocessor preprocessor = new MockSAXXDocPreprocessor();
		StringReader reader = new StringReader(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><root><a><b/></a><c>vvvvv<d/>jjjj</c></root>");
		StringWriter writer = new StringWriter();
		preprocessor.preprocess("test", reader, writer, null, null, null, null);
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><root><a><b/></a><c>vvvvv<d/>jjjj</c></root>",
				writer.toString());
	}
}

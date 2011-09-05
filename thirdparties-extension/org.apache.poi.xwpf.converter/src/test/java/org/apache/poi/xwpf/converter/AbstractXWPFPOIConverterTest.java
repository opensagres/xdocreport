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
package org.apache.poi.xwpf.converter;

import java.io.IOException;

import org.junit.Test;

public abstract class AbstractXWPFPOIConverterTest {

	@Test
	public void convertCV() throws IOException {
		doGenerate("CV.docx");
	}

	@Test
	public void generateCV2() throws IOException {
		doGenerate("CV2.docx");
	}

	@Test
	public void convertDocxBig() throws IOException {
		doGenerate("DocxBig.docx");
	}

	@Test
	public void testDocxLettreRelance() throws IOException {
		doGenerate("DocxLettreRelance.docx");
	}

	@Test
	public void testDocxStructures() throws IOException {
		doGenerate("DocxStructures.docx");
	}

	@Test
	public void testColor() throws IOException {
		doGenerate("TestColor.docx");
	}
	
	@Test
	public void testComplexTable() throws IOException {
		doGenerate("TestComplexTable.docx");
	}
	
	@Test
	public void testFonts() throws IOException {
		doGenerate("TestFonts.docx");
	}

	@Test
	public void testHeaderFooterPage() throws IOException {
		doGenerate("TestHeaderFooterPage.docx");
	}

	@Test
	public void testLandscapeFormat() throws IOException {
		doGenerate("TestLandscapeFormat.docx");
	}
	
	@Test
	public void testTable() throws IOException {
		doGenerate("TestTable.docx");
	}

	@Test
	public void testTitle() throws IOException {
		doGenerate("TestTitle.docx");
	}

	

	protected abstract void doGenerate(String fileName) throws IOException;

}

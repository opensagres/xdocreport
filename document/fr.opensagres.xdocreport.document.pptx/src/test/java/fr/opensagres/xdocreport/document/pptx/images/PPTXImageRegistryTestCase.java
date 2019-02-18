/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
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
package fr.opensagres.xdocreport.document.pptx.images;

import org.junit.Test;

import junit.framework.TestCase;

public class PPTXImageRegistryTestCase extends TestCase{
	
	@Test
	public void test(){
		
		PPTXImageRegistry imageRegistry = new PPTXImageRegistry(null, null, null, null);
		Float result = imageRegistry.getSize("123456");
		assertNotNull("Result should not be null", result);
		assertTrue("Result should be equal to 12.961", Math.abs( result.floatValue() - 12.961) < 10E-3);
		
		
		result = imageRegistry.getSize("123cm");
		assertNull("Currently only valid fload can be parsed", result);
		
		
		result = imageRegistry.getSize(null);
		assertNull("No error should happen", result);
	}
}

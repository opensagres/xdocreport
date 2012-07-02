/**
 * Copyright (C) 2011 The XDocReport Team <xdocreport@googlegroups.com>
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
package org.springframework.xdocreport.core.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.xdocreport.core.TemplatesService;
import org.springframework.xdocreport.core.utils.TemplatesUtils;

/**
 * Test for templates generator
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * 
 * @see <a href="http://code.google.com/p/xdocreport/">xdocreport</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TemplatesServiceImplTest {

	private static Log LOG = LogFactory.getLog(TemplatesServiceImplTest.class);

	@Resource
	private TemplatesService velocityTest;

	private static final String HELLO_WORLD = "ODTHelloWorldWithVelocity.odt";
	private static final String HELLO_WORLD_NAME = "name";
	private static final String HELLO_WORLD_NAME_VALUE = "WORLD!!";

	/**
	 * Test HELLO_WORLD
	 */
	@Test
	public void testHelloWorld() {
		Map<String, String> values = new HashMap<String, String>();
		values.put(HELLO_WORLD_NAME, HELLO_WORLD_NAME_VALUE);
		try {
			InputStream is = velocityTest.generateTemplate(values, HELLO_WORLD);
			Assert.assertNotNull(is);
			Assert.assertTrue(TemplatesUtils.getODTContentAsString(is)
					.contains(HELLO_WORLD_NAME_VALUE));
		} catch (Exception e) {
			LOG.error(e);
			Assert.fail();
		}
	}

}

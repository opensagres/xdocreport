/* 
 * TemplatesServiceImplTest.java Copyright (C) 2012
 * 
 * This file is part of xdocreport project
 * 
 * This software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 * 
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
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

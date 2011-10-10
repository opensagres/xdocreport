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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class FreemarkerFieldsMetadataClassSerializerTestCase {

	private class Project {

		private final String name;

		public Project(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	private class Role {

		private final String name;

		public Role(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	private static class Developer {

		private final String name;
		private final String lastName;
		private final String mail;
		private final List<Role> roles;

		public Developer(String name, String lastName, String mail) {
			this.name = name;
			this.lastName = lastName;
			this.mail = mail;
			this.roles = new ArrayList<Role>();
		}

		public String getName() {
			return name;
		}

		public String getLastName() {
			return lastName;
		}

		public String getMail() {
			return mail;
		}

		public Developer addRole(Role role) {
			roles.add(role);
			return this;
		}

		public List<Role> getRoles() {
			return roles;
		}

	}

	@Test
	public void testSimplePOJO() {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		FreemarkerFieldsMetadataClassSerializer serializer = new FreemarkerFieldsMetadataClassSerializer();
		serializer.load(fieldsMetadata, "project", Project.class);

		// START remove that once it's implemented
		fieldsMetadata.addField("project.name", false, "", null);
		// END remove that once it's implemented

		Assert.assertEquals(1, fieldsMetadata.getFields().size());
		Assert.assertEquals("project.name", fieldsMetadata.getFields().get(0)
				.getFieldName());
	}

	@Test
	public void testListPOJO() {
		FieldsMetadata fieldsMetadata = new FieldsMetadata();
		FreemarkerFieldsMetadataClassSerializer serializer = new FreemarkerFieldsMetadataClassSerializer();
		serializer.load(fieldsMetadata, "developers", Developer.class, true);

		// START remove that once it's implemented
		fieldsMetadata.addField("developers.name", true, "", null);
		fieldsMetadata.addField("developers.lastName", true, "", null);
		// END remove that once it's implemented

		Assert.assertEquals(2, fieldsMetadata.getFields().size());
		Assert.assertEquals("developers.name", fieldsMetadata.getFields()
				.get(0).getFieldName());
		Assert.assertEquals("developers.lastName", fieldsMetadata.getFields()
				.get(1).getFieldName());
	}

	@Test
	public void testSimpleImage() {
		// Problem IImageProvider is in the document package
		// IImageProvider
	}
}

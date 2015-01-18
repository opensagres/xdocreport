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
package fr.opensagres.xdocreport.remoting.resources.domain;

import java.util.List;

/**
 * Resource Helper.
 */
public class ResourceHelper {

	/**
	 * Find fields metadata file from the template resource (ex:
	 * MyTemplate/META-INF/MyTemplate.fields.xml).
	 * 
	 * @param template
	 * @return
	 */
	public static Resource findFieldsMetadataFromTemplate(Resource template) {
		Resource metaInf = findMetaInfFromTemplate(template);
		if (metaInf == null) {
			return null;
		}
		return findFieldsMetadataFromMetaInf(metaInf);
	}

	/**
	 * Find META-INF category from the template resource (ex:
	 * MyTemplate/META-INF).
	 * 
	 * @param template
	 * @return
	 */
	public static Resource findMetaInfFromTemplate(Resource template) {
		List<Resource> resources = template.getChildren();
		for (Resource resource : resources) {
			if (ResourceFactory.META_INF.equals(resource.getName())) {
				return resource;
			}
		}
		return null;
	}

	/**
	 * Find fields metadata file from the META-INF resource (ex:
	 * META-INF/MyTemplate.fields.xml).
	 * 
	 * @param template
	 * @return
	 */
	public static Resource findFieldsMetadataFromMetaInf(Resource metaInf) {
		List<Resource> resources = metaInf.getChildren();
		for (Resource resource : resources) {
			if (isFieldsMetadata(resource)) {
				return resource;
			}
		}
		return null;
	}

	public static boolean isFieldsMetadata(Resource resource) {
		return isFieldsMetadata(resource.getName());
	}

	public static boolean isFieldsMetadata(String name) {
		return name != null && name.endsWith(ResourceFactory.FIELDS_XML);
	}

	/**
	 * Returns the resource path of the given resource.
	 * 
	 * @param resource
	 * @return
	 */
	public static String getResourcePath(Resource resource) {
		StringBuilder path = new StringBuilder();
		computePath(resource, path);
		return path.toString();
	}

	/**
	 * Compute the resource path of the given resource.
	 * 
	 * @param resource
	 * @param path
	 */
	private static void computePath(Resource resource, StringBuilder path) {
		Resource parent = resource.getParent();
		insertPath(resource, path);
		while (parent != null) {
			insertPath(parent, path);
			parent = parent.getParent();
		}
	}

	/**
	 * Insert path.
	 * 
	 * @param resource
	 * @param path
	 */
	private static void insertPath(Resource resource, StringBuilder path) {
		if (path.length() > 0) {
			path.insert(0, '/');
		}
		path.insert(0, resource.getName());
	}
}

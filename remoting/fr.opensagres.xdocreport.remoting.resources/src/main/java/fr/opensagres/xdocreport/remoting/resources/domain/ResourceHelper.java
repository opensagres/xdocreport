package fr.opensagres.xdocreport.remoting.resources.domain;

import java.util.List;

/**
 * Resource Helper.
 * 
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
		String name = null;
		for (Resource resource : resources) {
			name = resource.getName();
			if (name != null && name.endsWith(ResourceFactory.FIELDS_XML)) {
				return resource;
			}
		}
		return null;
	}
}

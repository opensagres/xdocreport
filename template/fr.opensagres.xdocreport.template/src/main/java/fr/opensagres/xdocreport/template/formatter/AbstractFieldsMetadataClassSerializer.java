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
package fr.opensagres.xdocreport.template.formatter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.opensagres.xdocreport.core.XDocReportException;

/**
 * Abstract class for Fields metadata serializer.
 */
public abstract class AbstractFieldsMetadataClassSerializer implements
		IFieldsMetadataClassSerializer {

	private final String id;

	private final String description;

	// package name to exclude while processing
	private final List<String> excludedPackages;

	private final Set<Class<?>> hasBeenProcessed = new HashSet<Class<?>>();

	public AbstractFieldsMetadataClassSerializer(String id, String description) {
		this.id = id;
		this.description = description;
		this.excludedPackages = new ArrayList<String>();
		this.excludedPackages.add("java.");
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.opensagres.xdocreport.template.formatter.IFieldsMetadataClassSerializer
	 * #load(fr.opensagres.xdocreport.template.formatter.FieldsMetadata,
	 * java.lang.String, java.lang.Class)
	 */
	public void load(FieldsMetadata fieldsMetadata, String key, Class<?> clazz)
			throws XDocReportException {
		load(fieldsMetadata, key, clazz, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * fr.opensagres.xdocreport.template.formatter.IFieldsMetadataClassSerializer
	 * #load(fr.opensagres.xdocreport.template.formatter.FieldsMetadata,
	 * java.lang.String, java.lang.Class, boolean)
	 */
	public void load(FieldsMetadata fieldsMetadata, String key, Class<?> clazz,
			boolean listType) throws XDocReportException {
		try {
			process(fieldsMetadata, key, clazz, listType);
		} catch (Exception e) {
			throw new XDocReportException(e);
		}
	}

	private void process(FieldsMetadata fieldsMetadata, String key,
			Class<?> clazz, boolean listType) throws IntrospectionException {
		if (!hasBeenProcessed.contains(clazz))
		{

			hasBeenProcessed.add(clazz);
			String fieldName = null;
			BeanInfo infos = Introspector.getBeanInfo(clazz);

			PropertyDescriptor[] desc = infos.getPropertyDescriptors();
			for (int i = 0; i < desc.length; i++) {
				Method method = desc[i].getReadMethod();
				if (isGetterMethod(method)) {
					Class returnTypeClass = method.getReturnType();
					if (Iterable.class.isAssignableFrom(returnTypeClass)) {
						// process generic collection
						Type collectionType = method.getGenericReturnType();
						if (collectionType != null
								&& (collectionType instanceof ParameterizedType)) {
							ParameterizedType parameterizedType = (ParameterizedType) method
									.getGenericReturnType();
							Type[] types = parameterizedType
									.getActualTypeArguments();
							if (types.length == 1) {
								Class itemClazz = (Class) types[0];
								fieldName = getFieldName(key, desc[i].getName());

								if(!fieldsMetadata.getFieldsAsList().contains(fieldName)){
										process(fieldsMetadata, fieldName, itemClazz,
													listType);

								}
							}
						}
					} else {
						fieldName = getFieldName(key, desc[i].getName());
						if(!fieldsMetadata.getFieldsAsList().contains(fieldName)){


						if (isClassToExclude(returnTypeClass)) {
							fieldsMetadata.addField(fieldName, listType, null,
									null, null);
						} else {
							process(fieldsMetadata, fieldName, returnTypeClass,
									listType);
						}
						}
					}
				}
			}

		}
	}

	/**
	 * Return true if package of the given class start with list of package to
	 * exclude and false otherwise.
	 *
	 * @param clazz
	 * @return
	 */
	private boolean isClassToExclude(Class clazz) {
		if (clazz != null && clazz.getPackage() != null) {
			String packageName = clazz.getPackage().getName();
			for (String excludePackageName : excludedPackages) {
				if (packageName.startsWith(excludePackageName))
					return true;
			}
		}
		return false;
	}

	private boolean isGetterMethod(Method method) {
		if (method == null) {
			return false;
		}
		String name = method.getName();
		return !name.equals("getClass")
				&& (name.startsWith("get") || name.startsWith("is"));
	}

	protected abstract String getFieldName(String key, String getterName);
}

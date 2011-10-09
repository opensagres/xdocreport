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

/**
 * 
 * Abstract class for Fields metadata serializer.
 * 
 */
public abstract class AbstractFieldsMetadataClassSerializer implements
		IFieldsMetadataClassSerializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.opensagres.xdocreport.template.formatter.IFieldsMetadataClassSerializer
	 * #load(fr.opensagres.xdocreport.template.formatter.FieldsMetadata,
	 * java.lang.String, java.lang.Class)
	 */
	public void load(FieldsMetadata fieldsMetadata, String string,
			Class<?> clazz) {
		load(fieldsMetadata, string, clazz, false);
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
			boolean listType) {
		// TODO check parameters,
		doLoad(fieldsMetadata, key, clazz, listType);
	}

	/**
	 * Load 
	 * @param fieldsMetadata
	 * @param key
	 * @param clazz
	 * @param listType
	 */
	protected abstract void doLoad(FieldsMetadata fieldsMetadata, String key,
			Class<?> clazz, boolean listType);
}

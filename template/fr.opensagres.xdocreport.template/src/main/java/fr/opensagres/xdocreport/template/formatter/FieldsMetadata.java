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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import fr.opensagres.xdocreport.core.utils.StringUtils;

/**
 * Fields Metadata is used in the preprocessing step to modify some XML entries
 * like generate script (Freemarker, Velocity...) for loop for Table row,
 * generate script for Image...
 * 
 * @author Angelo ZERR
 * 
 */
public class FieldsMetadata {

	public static final FieldsMetadata EMPTY = new FieldsMetadata();

	private final List<String> fieldsAsList;
	private final List<FieldMetadataImage> fieldsAsImage;

	public FieldsMetadata() {
		fieldsAsList = new ArrayList<String>();
		fieldsAsImage = new ArrayList<FieldMetadataImage>();
	}

	/**
	 * Add a field name which is considered as an image.
	 * 
	 * @param fieldName
	 */
	public void addFieldAsImage(String fieldName) {
		addFieldAsImage(fieldName, fieldName);
	}

	/**
	 * Add a field name which is considered as an image.
	 * 
	 * @param imageName
	 * @param fieldName
	 */
	public void addFieldAsImage(String imageName, String fieldName) {
		fieldsAsImage.add(new FieldMetadataImage(imageName, fieldName));
	}

	/**
	 * Add a field name which belongs to a list.
	 * 
	 * @param fieldName
	 */
	public void addFieldAsList(String fieldName) {
		fieldsAsList.add(fieldName);
	}

	/**
	 * Returns list of fields name which belongs to a list.
	 * 
	 * @return
	 */
	public Collection<String> getFieldsAsList() {
		return Collections.unmodifiableCollection(fieldsAsList);
	}

	/**
	 * Returns list of fields name which are considered as an image.
	 * 
	 * @return
	 */
	public Collection<FieldMetadataImage> getFieldsAsImage() {
		return Collections.unmodifiableCollection(fieldsAsImage);
	}

	/**
	 * Returns true if there are fields as image and false otherwise.
	 * 
	 * @return
	 */
	public boolean hasFieldsAsImage() {
		return fieldsAsImage.size() > 0;
	}

	public boolean isFieldAsImage(String fieldName) {
		if (StringUtils.isEmpty(fieldName)) {
			return false;
		}
		for (FieldMetadataImage metadata : fieldsAsImage) {
			if (metadata.getImageName().equals(fieldName)) {
				return true;
			}
		}
		return false;
	}
	
	public String getImageFieldName(String fieldName) {
		if (StringUtils.isEmpty(fieldName)) {
			return null;
		}
		for (FieldMetadataImage metadata : fieldsAsImage) {
			if (metadata.getImageName().equals(fieldName)) {
				return metadata.getFieldName();
			}
		}
		return null;
	}
}

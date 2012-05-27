/*
 * TemplatesUtils.java Copyright (C) 2012 
 * 
 * This file is part of xdocreport project
 * 
 * This software is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * As a special exception, if you link this library with other files to produce
 * an executable, this library does not by itself cause the resulting executable
 * to be covered by the GNU General Public License. This exception does not
 * however invalidate any other reasons why the executable file might be covered
 * by the GNU General Public License.
 * 
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 */
package org.springframework.xdocreport.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.StringEscapeUtils;

import fr.opensagres.xdocreport.core.EncodingConstants;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.odt.ODTConstants;

/**
 * Utility class for Template management
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * 
 */
public class TemplatesUtils {

	public static String EXTENSION_ODT = "ODT";
	public static String EXTENSION_ODS = "ODS";
	public static String EXTENSION_ODP = "ODP";
	public static String EXTENSION_DOCX = "DOCX";
	public static String EXTENSION_PPTX = "PPTX";
	public static final String PUNTO = StringEscapeUtils.escapeJava(".");

	/**
	 * Get document kind from a path
	 * 
	 * @param path
	 * 
	 * @return DocumentKind (only knows ODT, DOCX, ODP, PPTX and ODS)
	 * 
	 * @throws KindNotFoundException
	 */
	public static DocumentKind getDocumentKind(String path)
			throws KindNotFoundException {
		String extension = null;
		try {
			extension = getDocumentExtension(path).toUpperCase();
		} catch (Exception e) {
			return null;
		}
		if (extension.equals(EXTENSION_ODT)) {
			return DocumentKind.ODT;
		} else if (extension.equals(EXTENSION_ODS)) {
			return DocumentKind.ODS;
		} else if (extension.equals(EXTENSION_ODP)) {
			return DocumentKind.ODP;
		} else if (extension.equals(EXTENSION_DOCX)) {
			return DocumentKind.DOCX;
		} else if (extension.equals(EXTENSION_PPTX)) {
			return DocumentKind.PPTX;
		} else {
			throw new KindNotFoundException();
		}
	}

	/**
	 * Get document extension
	 * 
	 * @param path
	 * @return extension del documento
	 */
	public static String getDocumentExtension(String path)
			throws KindNotFoundException {
		String extension = null;
		try {
			extension = path.substring(path.lastIndexOf(PUNTO) + 1);
		} catch (Exception e) {
			return null;
		}
		return extension;
	}

	/**
	 * Get document name
	 * 
	 * @param path
	 * @return document name
	 */
	public static String getDocumentName(String path)
			throws KindNotFoundException {
		String name = null;
		try {
			name = path.substring(0, path.lastIndexOf(PUNTO));
		} catch (Exception e) {
			return null;
		}
		return name;
	}

	/**
	 * Creates a temp file
	 * 
	 * @param fileName
	 * 
	 * @return temp file
	 * 
	 * @throws IOException
	 */
	public static File createFileTemp(String fileName) throws IOException {
		String extension = null;
		String name = null;
		try {
			extension = getDocumentExtension(fileName);
			name = getDocumentName(fileName);
		} catch (Exception e) {
			return null;
		}
		return File.createTempFile(name, extension);
	}

	/**
	 * Get random name for a file
	 * 
	 * @param fileName
	 *            original
	 * 
	 * @return generated file
	 */
	public static String getRandomName(String fileName) {
		String extension = null;
		String name = null;
		try {
			extension = getDocumentExtension(fileName);
			name = getDocumentName(fileName);
		} catch (Exception e) {
			return null;
		}
		return name + new Random().nextInt() + PUNTO + extension;
	}

	/**
	 * Get content as string from a file
	 * 
	 * @param is
	 *            odt file as input stream
	 * 
	 * @return content as string or null
	 */
	public static String getODTContentAsString(InputStream is) {
		try {
			ZipInputStream zipInputStream = new ZipInputStream(is);
			ZipEntry entry;
			entry = zipInputStream.getNextEntry();
			String tmpContent = "content.txt";
			File tmpContentFile = TemplatesUtils.createFileTemp(tmpContent);
			while (entry != null) {

				if (entry.getName().equals(ODTConstants.CONTENT_XML_ENTRY)) {
					// Reads content
					byte[] buf = new byte[1024];
					int len;
					OutputStream out = new FileOutputStream(tmpContentFile);
					while ((len = zipInputStream.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.flush();
					break;
				}
				entry = zipInputStream.getNextEntry();
			}
			Reader reader = new InputStreamReader(new FileInputStream(
					tmpContentFile), EncodingConstants.UTF_8);
			StringWriter writer = new StringWriter();
			IOUtils.copy(reader, writer);

			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

}

/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com>, Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.opensagres.xdocreport.examples.openoffice.odt.converter.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import fr.opensagres.xdocreport.converter.ConverterRegistry;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.examples.openoffice.odt.ODTHelloWordWithFreemarker;

/**
 * Example to convert ODT 2 FO.
 * 
 */
public class ODTHelloWord2PDF {

	public static void main(String[] args) {

		try {
			// 1) Creation options ODT 2 PDF to select well converter form the
			// registry
			Options options = Options.getFrom(DocumentKind.ODT).to(
					ConverterTypeTo.PDF);

			// 2) Get the converter from the registry
			IConverter converter = ConverterRegistry.getRegistry()
					.getConverter(options);

			// 3) Convert ODT 2 PDF
			File out = new File("out");
			if (!out.exists()) {
				out.mkdir();
			}
			File file = new File(out, "ODTHelloWord2PDF.pdf");
			converter.convert(ODTHelloWordWithFreemarker.class
					.getResourceAsStream("ODTHelloWordWithFreemarker.odt"),
					new FileOutputStream(file), options);
		} catch (XDocConverterException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}

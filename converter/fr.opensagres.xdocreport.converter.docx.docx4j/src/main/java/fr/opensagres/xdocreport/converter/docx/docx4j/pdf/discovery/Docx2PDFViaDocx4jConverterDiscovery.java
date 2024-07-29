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
package fr.opensagres.xdocreport.converter.docx.docx4j.pdf.discovery;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.discovery.IConverterDiscovery;
import fr.opensagres.xdocreport.converter.docx.docx4j.pdf.Docx2PDFViaDocx4jConverter;
import fr.opensagres.xdocreport.core.document.DocumentKind;

public class Docx2PDFViaDocx4jConverterDiscovery
    implements IConverterDiscovery
{

    public String getId()
    {
        return "Docx2PDFViaDocx4jConverter";
    }

    public String getDescription()
    {
        return "Convert DOCX 2 PDF via docx4j";
    }

    public String getFrom()
    {
        return DocumentKind.DOCX.name();
    }

    public String getTo()
    {
        return ConverterTypeTo.PDF.name();
    }

    public String getVia()
    {
        return ConverterTypeVia.DOCX4J.name();
    }

    public IConverter getConverter()
    {
        return Docx2PDFViaDocx4jConverter.getInstance();
    }

    public String getWith() {
        return null;
    }

}

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
package fr.opensagres.xdocreport.document.preprocessor;

import java.io.IOException;
import java.util.Map;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * XML document preprocessor is used to modify the content of the original document from the archive of the XML document
 * (odt, docx...). For instance loop management in table managed with {@link FieldsMetadata} implements an
 * {@link IXDocPreprocessor} to add directive template engine (#foreach for velocity, [#list for freemarker) in the
 * table row.
 */
public interface IXDocPreprocessor
{

    /**
     * Modify the XML file identify with te entry name from the archive.
     * 
     * @param entryName Zip entry name fo the XML file to modify.
     * @param outputArchive the zip of the XML document.
     * @param fieldsMetadata metdata fields.
     * @param formater used to generate directive template engine
     * @param sharedContext shared context between the whole processor to execute. This Map can be used to share
     *            information between processors (according the order of the processor registration).
     * @throws XDocReportException
     * @throws IOException
     */
    void preprocess( String entryName, XDocArchive outputArchive, FieldsMetadata fieldsMetadata,
                     IDocumentFormatter formatter, Map<String, Object> sharedContext )
        throws XDocReportException, IOException;

    /**
     * Create the XML file identify with te entry name from the archive.
     * 
     * @param entryName Zip entry name fo the XML file to modify.
     * @param outputArchive the zip of the XML document.
     * @param fieldsMetadata metdata fields.
     * @param formater used to generate directive template engine
     * @param sharedContext shared context between the whole processor to execute. This Map can be used to share
     *            information between processors (according the order of the processor registration).
     * @throws XDocReportException
     * @throws IOException
     * @return true if processing can create entry and false otherwise.
     */
    boolean create( String entryName, XDocArchive outputArchive, FieldsMetadata fieldsMetadata,
                    IDocumentFormatter formatter, Map<String, Object> sharedContext )
        throws XDocReportException, IOException;

}

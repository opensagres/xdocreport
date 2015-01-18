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
package fr.opensagres.xdocreport.document.discovery;

import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.core.discovery.IBaseDiscovery;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.document.IXDocReport;

/**
 * Discovery used to register report factory in the {@link XDocReportLoader}. When report is loaded with
 * {@link XDocReportLoader#loadReport(java.io.InputStream)} discovery is used to create the well instance report
 * (ODTReport, DocxReport...) switch the type of ziped XML document coming from teh stream.
 * <p>
 * An implementation report factory discovery is declared in the xdocreport-discovery.properties with 'class' property.
 * Example for ODT report factory :
 * 
 * <pre>
 * -------------------------
 *  class=fr.opensagres.xdocreport.document.odt.discovery.ODTReportFactoryDiscovery
 *  -------------
 * </pre>
 * 
 * </p>
 */
public interface IXDocReportFactoryDiscovery
    extends IBaseDiscovery
{

    /**
     * Returns true if loaded document archive support this discovery and false otherwise.
     * 
     * @param archive
     * @return
     */
    boolean isAdaptFor( XDocArchive archive );

    /**
     * Returns true if file extension support this discovery and false otherwise.
     * 
     * @param archive
     * @return
     */
    boolean isAdaptFor( String fileExtension );

    /**
     * Create a new instance of {@link IXDocReport}.
     * 
     * @return
     */
    IXDocReport createReport();

    /**
     * Returns mime mapping switch the kind of the report (odt, docx...).
     * 
     * @return
     */
    MimeMapping getMimeMapping();

    /**
     * Returns class report created with this factory.
     * 
     * @return
     */
    Class<?> getReportClass();

}

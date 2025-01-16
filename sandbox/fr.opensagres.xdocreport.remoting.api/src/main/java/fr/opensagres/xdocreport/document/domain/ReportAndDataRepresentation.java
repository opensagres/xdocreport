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
package fr.opensagres.xdocreport.document.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlMimeType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReportAndDataRepresentation
{

    private String reportID;

    private String templateEngine;

    private byte[] document;

    private WSOptions options;

    private List<String> fieldsMetaData = new ArrayList<String>();

    private List<DataContext> dataContext = new ArrayList<DataContext>();

    @XmlMimeType( "application/octet-stream" )
    public byte[] getDocument()
    {
        return document;
    }

    public void setDocument( byte[] document )
    {
        this.document = document;
    }

    public List<String> getFieldsMetaData()
    {
        return fieldsMetaData;
    }

    public void setFieldsMetaData( List<String> fieldsMetaData )
    {
        this.fieldsMetaData = fieldsMetaData;
    }

    public String getReportID()
    {
        return reportID;
    }

    public void setReportID( String reportID )
    {
        this.reportID = reportID;
    }

    public String getTemplateEngine()
    {
        return templateEngine;
    }

    public void setTemplateEngine( String templateEngine )
    {
        this.templateEngine = templateEngine;
    }

    public WSOptions getOptions()
    {
        return options;
    }

    public void setOptions( WSOptions options )
    {
        this.options = options;
    }

    public List<DataContext> getDataContext()
    {
        return dataContext;
    }

    public void setDataContext( List<DataContext> dataContext )
    {
        this.dataContext = dataContext;
    }



}

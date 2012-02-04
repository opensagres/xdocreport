package fr.opensagres.xdocreport.document.service;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;

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

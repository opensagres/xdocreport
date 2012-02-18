package fr.opensagres.xdocreport.document.service;

import java.util.List;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.domain.ReportAndDataRepresentation;
import fr.opensagres.xdocreport.document.domain.ReportId;
import fr.opensagres.xdocreport.document.domain.ReportRepresentation;

public interface XDocReportService
{

    byte[] download( String reportID, String processState )
        throws XDocReportException;

    List<ReportId> listReports();

    void upload( ReportRepresentation report )
        throws XDocReportException;

    byte[] processReport( ReportAndDataRepresentation reportAndDataRepresentation )
        throws XDocReportException;

    void unRegister( String reportId );

}
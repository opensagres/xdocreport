package fr.opensagres.xdocreport.document.service;

import java.util.List;

import fr.opensagres.xdocreport.document.domain.ReportAndDataRepresentation;
import fr.opensagres.xdocreport.document.domain.ReportId;
import fr.opensagres.xdocreport.document.domain.ReportRepresentation;

public interface XDocReportService
{

    byte[] download( String reportID, String processState );

    List<ReportId> listReports();

    void upload( ReportRepresentation report );

    byte[] processReport( ReportAndDataRepresentation reportAndDataRepresentation );

    void unRegister( String reportId );

}
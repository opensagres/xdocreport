package fr.opensagres.xdocreport.service.ws;

import javax.jws.WebService;

import fr.opensagres.xdocreport.document.service.XDocReportService;

@WebService(serviceName="WSXDocReportService",targetNamespace="http://xdocreport.opensagres.fr")
public class WSXDocReportService extends XDocReportService {

//	private XDocReportService delegate = XDocReportService.INSTANCE;
//
//	public byte[] processReport(String documentID, DataContext[] d, Options o) throws WSException {
//		return delegate.processReport(documentID, d, o);
//	}
//
//	public byte[] processReport(byte[] document, DataContext[] dataContext,
//			String templateEngineID, Options o) throws WSException {
//		return delegate.processReport(document, dataContext, templateEngineID,
//				o);
//	}
//
//	public void upload(String reportID, byte[] content, String templateEngineID) throws WSException  {
//		delegate.upload(reportID, content, templateEngineID);
//	}
//
//	public byte[] download(String reportID) {
//		return delegate.download(reportID);
//	}
//
//	public void unRegister(String reportID) {
//		delegate.unRegister(reportID);
//	}

}

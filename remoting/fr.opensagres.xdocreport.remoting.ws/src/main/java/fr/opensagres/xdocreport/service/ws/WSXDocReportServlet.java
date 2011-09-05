package fr.opensagres.xdocreport.service.ws;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.xml.ws.Endpoint;

public class WSXDocReportServlet extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		String address = "/xdocreport";
		Endpoint.publish(address, new WSXDocReportService());
	}
}

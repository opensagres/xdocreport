package fr.opensagres.xdocreport.document.pptx;

import static fr.opensagres.xdocreport.document.pptx.PPTXConstants.A_NS;
import static fr.opensagres.xdocreport.document.pptx.PPTXConstants.P_ELT;
import static fr.opensagres.xdocreport.document.pptx.PPTXConstants.R_ELT;
import static fr.opensagres.xdocreport.document.pptx.PPTXConstants.T_ELT;

public class PPTXUtils {

	public static boolean isAP(String uri, String localName, String name) {
		return (A_NS.equals(uri) && P_ELT.equals(localName));
	}
	
	public static boolean isAR(String uri, String localName, String name) {
		return (A_NS.equals(uri) && R_ELT.equals(localName));
	}
	
	public static boolean isAT(String uri, String localName, String name) {
		return (A_NS.equals(uri) && T_ELT.equals(localName));
	}
}

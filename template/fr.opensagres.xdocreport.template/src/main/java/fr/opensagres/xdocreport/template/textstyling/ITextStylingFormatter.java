package fr.opensagres.xdocreport.template.textstyling;

import fr.opensagres.xdocreport.core.XDocReportException;

public interface ITextStylingFormatter {

	String format(String s, IDocumentVisitor visitor)
			throws XDocReportException;

}

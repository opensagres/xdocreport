package fr.opensagres.xdocreport.template.textstyling;

import fr.opensagres.xdocreport.core.XDocReportException;

public abstract class AbstractTextStylingFormatter implements
		ITextStylingFormatter {

	public String format(String s, IDocumentVisitor visitor)
			throws XDocReportException {
		try {
			doFormat(s, visitor);
			return visitor.toString();
		} catch (Throwable e) {
			if (e instanceof XDocReportException) {
				throw (XDocReportException) e;
			}
			throw new XDocReportException(e);
		}
	}

	protected abstract void doFormat(String s, IDocumentVisitor visitor)
			throws Exception;
}

package fr.opensagres.xdocreport.template.textstyling;

import fr.opensagres.xdocreport.core.XDocReportException;

public abstract class AbstractTextStylingFormatter implements
		ITextStylingFormatter {

	public String format(String s, IDocumentVisitor visitor)
			throws XDocReportException {
		try {
			doFormat(s, visitor);
			String result = visitor.toString();
			System.err.println(result);
			return result;
		} catch (Throwable e) {
			e.printStackTrace();
			if (e instanceof XDocReportException) {
				throw (XDocReportException) e;
			}
			throw new XDocReportException(e);
		}
	}

	protected abstract void doFormat(String s, IDocumentVisitor visitor)
			throws Exception;
}

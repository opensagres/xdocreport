package fr.opensagres.xdocreport.template.textstyling.wiki.gwiki;

import junit.framework.Assert;

import org.junit.Test;

import fr.opensagres.xdocreport.template.textstyling.IDocumentVisitor;
import fr.opensagres.xdocreport.template.textstyling.ITextStylingFormatter;
import fr.opensagres.xdocreport.template.textstyling.wiki.HTMLDocumentVisitor;

public class GWikiTextStylingFormatterTestCase {

	@Test
	public void testname() throws Exception {
		ITextStylingFormatter formatter = GWikiTextStylingFormatter.INSTANCE;
		IDocumentVisitor visitor = new HTMLDocumentVisitor();
		formatter.format("Here are severals styles : " +
				"\n * *Bold* style.", visitor);

		Assert.assertEquals("<strong>XDocReport</strong> means",
				visitor.toString());
	}
}

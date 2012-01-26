package fr.opensagres.xdocreport.document.textstyling.wiki.gwiki;

import junit.framework.Assert;

import org.junit.Test;

import fr.opensagres.xdocreport.document.textstyling.IDocumentHandler;
import fr.opensagres.xdocreport.document.textstyling.ITextStylingTransformer;
import fr.opensagres.xdocreport.document.textstyling.wiki.gwiki.GWikiTextStylingTransformer;

public class GWikiTextStylingTransformerTestCase {

	@Test
	public void testGWiki2HTML() throws Exception {
		ITextStylingTransformer formatter = GWikiTextStylingTransformer.INSTANCE;
		IDocumentHandler handler = new HTMLDocumentHandler();
		formatter.transform("Here are severals styles"
+"\n * *Bold* style."
+"\n * _Italic_ style."
+"\n * _*BoldAndItalic*_ style."

+"\n\n Here are 3 styles" 

+"\n # *Bold* style."
+"\n # _Italic_ style."
+"\n # _*BoldAndItalic*_ style.", handler);
		Assert.assertEquals(
				"<p>Here are severals styles</p><ul><li><strong>Bold</strong> style</li><li><i>Italic</i> style</li><li><i><strong>BoldAndItalic</i></strong> style</li></ul>Here are 3 styles<ol><li><strong>Bold</strong> style</li><li><i>Italic</i> style</li><li><i><strong>BoldAndItalic</i></strong> style</li></ol>",
				handler.getTextBody());
	}
}

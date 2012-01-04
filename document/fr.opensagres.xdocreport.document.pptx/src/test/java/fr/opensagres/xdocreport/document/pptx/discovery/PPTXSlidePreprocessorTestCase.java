package fr.opensagres.xdocreport.document.pptx.discovery;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.document.pptx.preprocessor.PPTXSlidePreprocessor;

public class PPTXSlidePreprocessorTestCase  extends TestCase {

	public void testname() throws Exception {
		PPTXSlidePreprocessor preprocessor = new PPTXSlidePreprocessor();
		StringReader reader = new StringReader(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\"" 
						+ " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
						+ " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
						+ "<p:txBody>"
						+ "<a:bodyPr>"
							+ "<a:spAutoFit/>"
						+ "</a:bodyPr>"
						+ "<a:lstStyle/>"
						+ "<a:p>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
								+ "<a:t>x</a:t>"
							+ "</a:r>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
								+ "<a:t>project.Name</a:t>"
							+ "</a:r>"
							+ "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>"
						+ "</a:p>"
					+ "</p:txBody>"
					+ "</p:sld>");
		StringWriter writer = new StringWriter();
		preprocessor.preprocess("test", reader, writer, null, null, null,
				new HashMap<String, Object>());
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\"" 
						+ " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
						+ " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
						+ "<p:txBody>"
						+ "<a:bodyPr>"
							+ "<a:spAutoFit/>"
						+ "</a:bodyPr>"
						+ "<a:lstStyle/>"
						+ "<a:p>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
								+ "<a:t>x</a:t>"
							+ "</a:r>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
								+ "<a:t>project.Name</a:t>"
							+ "</a:r>"
							+ "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>"
						+ "</a:p>"
					+ "</p:txBody>"
					+ "</p:sld>", writer.toString());
	}
	
	public void testname2() throws Exception {
		PPTXSlidePreprocessor preprocessor = new PPTXSlidePreprocessor();
		StringReader reader = new StringReader(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\"" 
						+ " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
						+ " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
						+ "<p:txBody>"
						+ "<a:bodyPr>"
							+ "<a:spAutoFit/>"
						+ "</a:bodyPr>"
						+ "<a:lstStyle/>"
						+ "<a:p>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
								+ "<a:t>$</a:t>"
							+ "</a:r>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
								+ "<a:t>project.Name</a:t>"
							+ "</a:r>"
							+ "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>"
						+ "</a:p>"
					+ "</p:txBody>"
					+ "</p:sld>");
		StringWriter writer = new StringWriter();
		preprocessor.preprocess("test", reader, writer, null, null, null,
				new HashMap<String, Object>());
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\"" 
						+ " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
						+ " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
						+ "<p:txBody>"
						+ "<a:bodyPr>"
							+ "<a:spAutoFit/>"
						+ "</a:bodyPr>"
						+ "<a:lstStyle/>"
						+ "<a:p>"
							//+ "<a:r>"
							//	+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
							//	+ "<a:t>$</a:t>"
							//+ "</a:r>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
								+ "<a:t>$project.Name</a:t>"
							+ "</a:r>"
							+ "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>"
						+ "</a:p>"
					+ "</p:txBody>"
					+ "</p:sld>", writer.toString());
	}
	
	public void testname3() throws Exception {
		PPTXSlidePreprocessor preprocessor = new PPTXSlidePreprocessor();
		StringReader reader = new StringReader(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\"" 
						+ " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
						+ " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
						+ "<p:txBody>"
						+ "<a:bodyPr>"
							+ "<a:spAutoFit/>"
						+ "</a:bodyPr>"
						+ "<a:lstStyle/>"
						+ "<a:p>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
								+ "<a:t>Project: </a:t>"
							+ "</a:r>"						
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
								+ "<a:t>$</a:t>"
							+ "</a:r>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
								+ "<a:t>project.Name</a:t>"
							+ "</a:r>"
							+ "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>"
						+ "</a:p>"
					+ "</p:txBody>"
					+ "</p:sld>");
		StringWriter writer = new StringWriter();
		preprocessor.preprocess("test", reader, writer, null, null, null,
				new HashMap<String, Object>());
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\"" 
						+ " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
						+ " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
						+ "<p:txBody>"
						+ "<a:bodyPr>"
							+ "<a:spAutoFit/>"
						+ "</a:bodyPr>"
						+ "<a:lstStyle/>"
						+ "<a:p>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
								+ "<a:t>Project: </a:t>"
							+ "</a:r>"						
							//+ "<a:r>"
							//	+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
							//	+ "<a:t>$</a:t>"
							//+ "</a:r>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
								+ "<a:t>$project.Name</a:t>"
							+ "</a:r>"
							+ "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>"
						+ "</a:p>"
					+ "</p:txBody>"
					+ "</p:sld>", writer.toString());
	}
	
	public void testname4() throws Exception {
		PPTXSlidePreprocessor preprocessor = new PPTXSlidePreprocessor();
		StringReader reader = new StringReader(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\"" 
						+ " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
						+ " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
						+ "<p:txBody>"
						+ "<a:bodyPr>"
							+ "<a:spAutoFit/>"
						+ "</a:bodyPr>"
						+ "<a:lstStyle/>"
						+ "<a:p>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
								+ "<a:t>Project: </a:t>"
							+ "</a:r>"						
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
								+ "<a:t>$</a:t>"
							+ "</a:r>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
								+ "<a:t>project.Name</a:t>"
							+ "</a:r>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
								+ "<a:t> </a:t>"
							+ "</a:r>"								
							+ "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>"
						+ "</a:p>"
					+ "</p:txBody>"
					+ "</p:sld>");
		StringWriter writer = new StringWriter();
		preprocessor.preprocess("test", reader, writer, null, null, null,
				new HashMap<String, Object>());
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\"" 
						+ " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
						+ " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
						+ "<p:txBody>"
						+ "<a:bodyPr>"
							+ "<a:spAutoFit/>"
						+ "</a:bodyPr>"
						+ "<a:lstStyle/>"
						+ "<a:p>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
								+ "<a:t>Project: </a:t>"
							+ "</a:r>"						
							//+ "<a:r>"
							//	+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
							//	+ "<a:t>$</a:t>"
							//+ "</a:r>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
								+ "<a:t>$project.Name</a:t>"
							+ "</a:r>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
								+ "<a:t> </a:t>"
							+ "</a:r>"								
							+ "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>"
						+ "</a:p>"
					+ "</p:txBody>"
					+ "</p:sld>", writer.toString());
	}
	
	public void testname5() throws Exception {
		PPTXSlidePreprocessor preprocessor = new PPTXSlidePreprocessor();
		StringReader reader = new StringReader(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\"" 
						+ " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
						+ " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
						+ "<p:txBody>"
						+ "<a:bodyPr>"
							+ "<a:spAutoFit/>"
						+ "</a:bodyPr>"
						+ "<a:lstStyle/>"
						+ "<a:p>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
								+ "<a:t>$name</a:t>"
							+ "</a:r>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
								+ "<a:t>$name2</a:t>"
							+ "</a:r>"
							+ "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>"
						+ "</a:p>"
					+ "</p:txBody>"
					+ "</p:sld>");
		StringWriter writer = new StringWriter();
		preprocessor.preprocess("test", reader, writer, null, null, null,
				new HashMap<String, Object>());
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
						+ "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\"" 
						+ " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
						+ " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
						+ "<p:txBody>"
						+ "<a:bodyPr>"
							+ "<a:spAutoFit/>"
						+ "</a:bodyPr>"
						+ "<a:lstStyle/>"
						+ "<a:p>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
								+ "<a:t>$name</a:t>"
							+ "</a:r>"
							+ "<a:r>"
								+ "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
								+ "<a:t>$name2</a:t>"
							+ "</a:r>"
							+ "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>"
						+ "</a:p>"
					+ "</p:txBody>"
					+ "</p:sld>", writer.toString());
	}
	
}

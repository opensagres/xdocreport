package fr.opensagres.xdocreport.template.textstyling.wiki;

import fr.opensagres.xdocreport.template.textstyling.AbstractDocumentVisitor;

public class HTMLDocumentVisitor extends AbstractDocumentVisitor  {

	public void startDocument() {
		// TODO Auto-generated method stub
		
	}

	public void endDocument() {
		// TODO Auto-generated method stub
		
	}

	public void startBold() {		
		writer.write("<strong>");
	}

	public void endBold() {
		writer.write("</strong>");		
	}

	public void startItalics() {
		// TODO Auto-generated method stub
		
	}

	public void endItalics() {
		// TODO Auto-generated method stub
		
	}

}

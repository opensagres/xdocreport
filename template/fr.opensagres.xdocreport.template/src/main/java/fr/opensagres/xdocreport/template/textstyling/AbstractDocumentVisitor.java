package fr.opensagres.xdocreport.template.textstyling;

import java.io.StringWriter;

public abstract class AbstractDocumentVisitor implements IDocumentVisitor {

	protected final StringWriter writer;
	
	public AbstractDocumentVisitor() {
		this.writer = new StringWriter();
	}
	
	public void handleString(String s) {
		writer.write(s);		
	}
	
	@Override
	public String toString() {
		return writer.toString();
	}
}

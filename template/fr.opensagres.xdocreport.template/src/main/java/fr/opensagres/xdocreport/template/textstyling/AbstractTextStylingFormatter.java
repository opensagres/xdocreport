package fr.opensagres.xdocreport.template.textstyling;

public abstract class AbstractTextStylingFormatter implements
		ITextStylingFormatter {

	protected abstract IDocumentVisitor createVisitor();

}

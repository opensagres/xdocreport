package fr.opensagres.xdocreport.document.textstyling;

public class BasicTransformResult implements ITransformResult {

	private final String content;
	private static final String EMPTY_STRING = "";

	public BasicTransformResult(String content) {
		this.content = content;
	}

	public String getTextBefore() {
		return EMPTY_STRING;
	}

	public String getTextBody() {
		return content;
	}

	public String getTextEnd() {
		return EMPTY_STRING;
	}

}

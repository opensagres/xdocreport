package fr.opensagres.xdocreport.template.velocity.internal;

public class NoEscape {

	public static final NoEscape INSTANCE= new NoEscape();
	public static final String NOESCAPE_KEY = "___NoEscape";
	
	public String s(String s) {
		return s;
	}
}

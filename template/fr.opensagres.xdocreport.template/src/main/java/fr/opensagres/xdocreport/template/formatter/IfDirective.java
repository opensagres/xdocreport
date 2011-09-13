package fr.opensagres.xdocreport.template.formatter;

public class IfDirective extends Directive {

	public IfDirective(String startDirective, String endDirective) {
		super(startDirective, endDirective);
	}

	@Override
	public DirectiveType getType() {
		return DirectiveType.IF;
	}
}

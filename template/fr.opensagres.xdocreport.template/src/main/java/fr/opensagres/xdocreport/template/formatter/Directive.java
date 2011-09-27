package fr.opensagres.xdocreport.template.formatter;

public abstract class Directive {

	public enum DirectiveType {
		LOOP, IF
	}

	private final Directive parent;
	private final String startDirective;
	private final String endDirective;

	public Directive(Directive parent, String startDirective, String endDirective) {
		this.parent=parent;
		this.startDirective = startDirective;
		this.endDirective = endDirective;
	}

	public String getStartDirective() {
		return startDirective;
	}

	public String getEndDirective() {
		return endDirective;
	}

	public abstract DirectiveType getType();

	public Directive getParent() {
		return parent;
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("[START_");
		s.append(getType());
		s.append(": ");
		s.append(startDirective);
		s.append(", END_");
		s.append(getType());
		s.append(": ");
		s.append(endDirective);
		return s.toString();
	}
}

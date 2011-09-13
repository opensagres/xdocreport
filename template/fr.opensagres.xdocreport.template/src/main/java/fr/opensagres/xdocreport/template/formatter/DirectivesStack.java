package fr.opensagres.xdocreport.template.formatter;

import java.util.Stack;

import fr.opensagres.xdocreport.template.formatter.Directive.DirectiveType;

public class DirectivesStack extends Stack<Directive> {

	private static final long serialVersionUID = -11427919871179717L;

	public Directive peekDirective(DirectiveType type) {
		if (isEmpty()) {
			return null;
		}
		Directive directive = super.peek();
		if (directive.getType().equals(type)) {
			return directive;
		}
		Directive d = null;
		Object[] elementData = super.toArray();
		for (int i = elementData.length - 1; i >= 0; i--) {
			d = (Directive) elementData[i];
			if (d.getType().equals(type)) {
				return d;
			}
		}
		return null;
	}
}

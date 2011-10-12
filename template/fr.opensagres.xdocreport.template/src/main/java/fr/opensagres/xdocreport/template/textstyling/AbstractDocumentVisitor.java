package fr.opensagres.xdocreport.template.textstyling;

import java.io.StringWriter;
import java.util.Stack;

public abstract class AbstractDocumentVisitor implements IDocumentVisitor {

	protected final StringWriter writer;
	private final Stack<Boolean> listStack;

	public AbstractDocumentVisitor() {
		this.writer = new StringWriter();
		this.listStack = new Stack<Boolean>();
	}

	public void handleString(String s) {
		writer.write(s);
	}

	@Override
	public String toString() {
		return writer.toString();
	}

	public final void startOrderedList() {
		listStack.push(true);
	}

	public final void endOrderedList() {
		listStack.pop();
		doEndOrderedList();
	}

	public final void startUnorderedList() {
		listStack.push(false);
	}

	public final void endUnorderedList() {
		listStack.pop();
		doEndUnorderedList();
	}

	protected boolean getCurrentListOrder() {
		if (listStack.isEmpty()) {
			return false;
		}
		return listStack.peek();
	}

	protected int getCurrentListIndex() {
		if (listStack.isEmpty()) {
			return 0;
		}
		return listStack.size() - 1;
	}

	protected void doEndUnorderedList() {

	}

	protected void doEndOrderedList() {

	}
}

package fr.opensagres.xdocreport.template.formatter;

public class LoopDirective {

	private final String startLoopDirective;
	private final String endLoopDirective;
	private final String sequence;
	private final String item;

	public LoopDirective(String startLoopDirective, String endLoopDirective,
			String sequence, String item) {
		this.sequence = sequence;
		this.item = item;
		this.startLoopDirective = startLoopDirective;
		this.endLoopDirective = endLoopDirective;
	}

	public String getSequence() {
		return sequence;
	}

	public String getItem() {
		return item;
	}

	public String getStartLoopDirective() {
		return startLoopDirective;
	}

	public String getEndLoopDirective() {
		return endLoopDirective;
	}
}

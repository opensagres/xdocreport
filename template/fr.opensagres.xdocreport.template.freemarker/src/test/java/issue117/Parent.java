package issue117;

import java.util.ArrayList;
import java.util.List;

public class Parent {

	private String name;

	private List<Child> children = new ArrayList<Child>();

	public List<Child> getChildren() {
		return children;
	}

	public void setChildren(List<Child> children) {
		this.children = children;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


}

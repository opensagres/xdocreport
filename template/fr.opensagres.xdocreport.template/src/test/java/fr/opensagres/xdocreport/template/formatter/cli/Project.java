package fr.opensagres.xdocreport.template.formatter.cli;

import java.util.List;

public class Project {

	private Person manager;

	private List<Person> resources;
	
	
	public List<Person> getResources() {
		return resources;
	}

	public void setResources(List<Person> resources) {
		this.resources = resources;
	}

	public Person getManager() {
		return manager;
	}

	public void setManager(Person manager) {
		this.manager = manager;
	}
	
	
	
}

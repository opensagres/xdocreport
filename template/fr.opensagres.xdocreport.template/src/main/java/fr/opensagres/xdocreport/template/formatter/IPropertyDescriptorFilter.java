package fr.opensagres.xdocreport.template.formatter;

import java.beans.PropertyDescriptor;

public interface IPropertyDescriptorFilter {

	 boolean test(PropertyDescriptor descriptor);
	 
}

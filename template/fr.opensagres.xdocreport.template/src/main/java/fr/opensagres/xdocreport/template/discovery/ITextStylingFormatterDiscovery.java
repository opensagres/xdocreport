package fr.opensagres.xdocreport.template.discovery;

import fr.opensagres.xdocreport.core.discovery.IBaseDiscovery;
import fr.opensagres.xdocreport.template.textstyling.ITextStylingFormatter;

public interface ITextStylingFormatterDiscovery extends IBaseDiscovery {

	ITextStylingFormatter getFormatter();

}

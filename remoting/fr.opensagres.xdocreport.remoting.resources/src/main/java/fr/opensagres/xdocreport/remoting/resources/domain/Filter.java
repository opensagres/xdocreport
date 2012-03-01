package fr.opensagres.xdocreport.remoting.resources.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Filter
{

    private List<PropertyRepresentation> properties;

    public void setProperties( List<PropertyRepresentation> properties )
    {
        this.properties = properties;
    }

    public List<PropertyRepresentation> getProperties()
    {
        return properties;
    }
}

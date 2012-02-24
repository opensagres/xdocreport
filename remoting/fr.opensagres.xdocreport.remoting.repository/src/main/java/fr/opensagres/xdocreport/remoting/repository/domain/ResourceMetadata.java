package fr.opensagres.xdocreport.remoting.repository.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResourceMetadata
{

    private String id;

    private ResourceMetadata parent;

    private List<ResourceMetadata> children;

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public void setParent( ResourceMetadata parent )
    {
        this.parent = parent;
    }

    public ResourceMetadata getParent()
    {
        return parent;
    }

    public void setChildren( List<ResourceMetadata> children )
    {
        this.children = children;
    }

    public List<ResourceMetadata> getChildren()
    {
        return children;
    }
}

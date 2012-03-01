package fr.opensagres.xdocreport.remoting.repository.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Resource
{
    public static int FOLDER_TYPE = 0;

    public static int FILE_TYPE = 1;

    private String name;

    //private Resource parent;

    private List<Resource> children;

    private int type;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

//    public Resource getParent()
//    {
//        return parent;
//    }

//    public void setParent( Resource parent )
//    {
//        this.parent = parent;
//    }

    public int getType()
    {
        return type;
    }

    public void setType( int type )
    {
        this.type = type;
    }

    public List<Resource> getChildren()
    {
        return children;
    }

    public void setChildren( List<Resource> children )
    {
        this.children = children;
    }
}

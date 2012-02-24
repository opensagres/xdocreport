package fr.opensagres.xdocreport.remoting.repository.domain;

import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResourceContent
    extends ResourceMetadata
{

    private byte[] content;

    @XmlMimeType( "application/octet-stream" )
    public byte[] getContent()
    {
        return content;
    }

    public void setContent( byte[] content )
    {
        this.content = content;
    }
}

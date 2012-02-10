package fr.opensagres.xdocreport.document.docx.preprocessor;

import fr.opensagres.xdocreport.core.utils.StringUtils;

public class DefaultStyle
{

    private String hyperLinkStyleId;

    public String getHyperLinkStyleId()
    {
        return hyperLinkStyleId;
    }

    public void setHyperLinkStyleId( String hyperLinkStyleId )
    {
        this.hyperLinkStyleId = hyperLinkStyleId;
    }

    public boolean hasHyperLinkStyleId()
    {
        return StringUtils.isNotEmpty( getHyperLinkStyleId() );
    }

}

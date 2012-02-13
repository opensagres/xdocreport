package fr.opensagres.xdocreport.document.docx.preprocessor;

import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.utils.StringUtils;

public class DefaultStyle
{

    private String hyperLinkStyleId;

    private Map<Integer, String> headersStyleId;

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

    public void addHeaderStyle( int level, String styleId )
    {
        if ( headersStyleId == null )
        {
            headersStyleId = new HashMap<Integer, String>();
        }
        headersStyleId.put( level, styleId );
    }

    public boolean hasHeaderStyle( int level )
    {
        return headersStyleId != null && headersStyleId.containsKey( level );
    }

    public String getHeaderStyle( int level )
    {
        return headersStyleId.get( level );
    }

}

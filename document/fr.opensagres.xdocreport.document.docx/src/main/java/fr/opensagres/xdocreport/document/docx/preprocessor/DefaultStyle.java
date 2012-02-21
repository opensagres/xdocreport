package fr.opensagres.xdocreport.document.docx.preprocessor;

import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.utils.StringUtils;

public class DefaultStyle
{

    private String hyperLinkStyleId;

    private Map<Integer, String> headersStyleId;

    private Integer abstractNumIdForOrdererList = null;

    private Integer abstractNumIdForUnordererList = null;

    private Integer maxNumId = null;

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

    public Integer getAbstractNumIdForOrdererList()
    {
        return abstractNumIdForOrdererList;
    }

    public void setAbstractNumIdForOrdererList( Integer abstractNumIdForOrdererList )
    {
        this.abstractNumIdForOrdererList = abstractNumIdForOrdererList;
    }

    public Integer getAbstractNumIdForUnordererList()
    {
        return abstractNumIdForUnordererList;
    }

    public void setAbstractNumIdForUnordererList( Integer abstractNumIdForUnordererList )
    {
        this.abstractNumIdForUnordererList = abstractNumIdForUnordererList;
    }

    public void setMaxNumId( Integer maxNumId )
    {
        this.maxNumId = maxNumId;
    }

    public Integer getMaxNumId()
    {
        return maxNumId;
    }

}

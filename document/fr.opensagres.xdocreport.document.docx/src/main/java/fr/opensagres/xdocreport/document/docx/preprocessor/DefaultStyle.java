package fr.opensagres.xdocreport.document.docx.preprocessor;

import java.util.HashMap;
import java.util.Map;

import fr.opensagres.xdocreport.core.utils.StringUtils;

public class DefaultStyle
{

    public static final int DEFAULT_NUMID = -1;

    private String hyperLinkStyleId;

    private Map<Integer, String> headersStyleId;

    private int numIdForOrdererList = DEFAULT_NUMID;

    private int numIdForUnordererList = DEFAULT_NUMID;

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

    public int getNumIdForOrdererList()
    {
        return numIdForOrdererList;
    }

    public void setNumIdForOrdererList( int numIdForOrdererList )
    {
        this.numIdForOrdererList = numIdForOrdererList;
    }

    public int getNumIdForUnordererList()
    {
        return numIdForUnordererList;
    }

    public void setNumIdForUnordererList( int numIdForUnordererList )
    {
        this.numIdForUnordererList = numIdForUnordererList;
    }

}

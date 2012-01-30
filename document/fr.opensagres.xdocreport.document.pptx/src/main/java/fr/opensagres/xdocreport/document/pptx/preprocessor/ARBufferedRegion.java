package fr.opensagres.xdocreport.document.pptx.preprocessor;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.document.preprocessor.sax.BufferedElement;

public class ARBufferedRegion
    extends BufferedElement
{

    private String tContent;

    private BufferedElement tRegion;

    public ARBufferedRegion( BufferedElement parent, String uri, String localName, String name, Attributes attributes )
    {
        super( parent, uri, localName, name, attributes );
    }

    public void setTContent( String content )
    {
        getTRegion().setTextContent( content );
    }

    public String getTContent()
    {
        return getTRegion().getTextContent();
    }

    public BufferedElement getTRegion()
    {
        if ( tRegion == null )
        {
            tRegion = super.findFirstChild( "a:t" );
        }
        return tRegion;
    }

}

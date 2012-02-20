package fr.opensagres.xdocreport.converter.docx.poi.xhtml;

import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.xdocreport.converter.IURIResolver;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.OptionsHelper;

public class XHTMLOptionsTestCase
{

    @Test
    public void testNullOptions()
        throws Exception
    {
        Options options = null;
        XHTMLOptions xhtmlOptions = XWPF2XHTMLConverter.getInstance().toXHTMLOptions( options );
        Assert.assertNull( xhtmlOptions );
    }

    @Test
    public void testNotNullOptions()
        throws Exception
    {
        Options options = Options.getFrom( "DOCX" );
        XHTMLOptions xhtmlOptions = XWPF2XHTMLConverter.getInstance().toXHTMLOptions( options );
        Assert.assertNotNull( xhtmlOptions );
    }

    @Test
    public void testOptionsWithURIResolver()
        throws Exception
    {
        Options options = Options.getFrom( "DOCX" );
        OptionsHelper.setURIResolver( options, new IURIResolver()
        {

            public String resolve( String uri )
            {
                return null;
            }
        } );
        XHTMLOptions xhtmlOptions = XWPF2XHTMLConverter.getInstance().toXHTMLOptions( options );
        Assert.assertNotNull( xhtmlOptions );
        Assert.assertNotNull( xhtmlOptions.getURIResolver() );
    }

    @Test
    public void testWithSubOptions()
        throws Exception
    {
        XHTMLOptions xhtmlOptions1 =
            XHTMLOptions.create().URIResolver( new org.apache.poi.xwpf.converter.IURIResolver()
            {

                public String resolve( String uri )
                {
                    return uri;
                }
            } );
        Options options = Options.getFrom( "DOCX" ).subOptions( xhtmlOptions1 );
        XHTMLOptions xhtmlOptions2 = XWPF2XHTMLConverter.getInstance().toXHTMLOptions( options );
        Assert.assertNotNull( xhtmlOptions2 );
        Assert.assertEquals( xhtmlOptions1, xhtmlOptions2 );
    }
}

package fr.opensagres.xdocreport.converter.odt.odfdom.xhtml;

import org.junit.Assert;
import org.junit.Test;
import org.odftoolkit.odfdom.converter.xhtml.XHTMLOptions;

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
        XHTMLOptions xhtmlOptions = ODF2XHTMLConverter.getInstance().toXHTMLOptions( options );
        Assert.assertNull( xhtmlOptions );
    }

    @Test
    public void testNotNullOptions()
        throws Exception
    {
        Options options = Options.getFrom( "ODT" );
        XHTMLOptions xhtmlOptions = ODF2XHTMLConverter.getInstance().toXHTMLOptions( options );
        Assert.assertNotNull( xhtmlOptions );
    }

    @Test
    public void testOptionsWithURIResolver()
        throws Exception
    {
        Options options = Options.getFrom( "ODT" );
        OptionsHelper.setURIResolver( options, new IURIResolver()
        {

            public String resolve( String uri )
            {
                return null;
            }
        } );
        XHTMLOptions xhtmlOptions = ODF2XHTMLConverter.getInstance().toXHTMLOptions( options );
        Assert.assertNotNull( xhtmlOptions );
        Assert.assertNotNull( xhtmlOptions.getURIResolver() );
    }

    @Test
    public void testWithSubOptions()
        throws Exception
    {
        XHTMLOptions xhtmlOptions1 =
            XHTMLOptions.create().URIResolver( new org.odftoolkit.odfdom.converter.IURIResolver()
            {

                public String resolve( String uri )
                {
                    return uri;
                }
            } );
        Options options = Options.getFrom( "ODT" ).subOptions( xhtmlOptions1 );
        XHTMLOptions xhtmlOptions2 = ODF2XHTMLConverter.getInstance().toXHTMLOptions( options );
        Assert.assertNotNull( xhtmlOptions2 );
        Assert.assertEquals( xhtmlOptions1, xhtmlOptions2 );
    }
}

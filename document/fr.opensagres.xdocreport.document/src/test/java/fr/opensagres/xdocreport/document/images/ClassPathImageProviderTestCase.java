package fr.opensagres.xdocreport.document.images;

import org.junit.Assert;
import org.junit.Test;

import fr.opensagres.xdocreport.core.document.ImageFormat;

public class ClassPathImageProviderTestCase
{

    @Test
    public void loadPNGWithoutUsingImageSize()
        throws Exception
    {
        IImageProvider imageProvider = new ClassPathImageProvider( ClassPathImageProviderTestCase.class, "logo.png" );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNull( imageProvider.getWidth() );
        Assert.assertNull( imageProvider.getHeight() );
    }

    @Test
    public void loadPNGAndUseImageSize()
        throws Exception
    {
        boolean useImageSize = true;
        IImageProvider imageProvider =
            new ClassPathImageProvider( ClassPathImageProviderTestCase.class, "logo.png", useImageSize );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertEquals( 220f, imageProvider.getWidth().floatValue(), 0 );
        Assert.assertEquals( 200f, imageProvider.getHeight().floatValue(), 0 );
    }

    @Test
    public void loadPNGAndModifyUseImageSize()
        throws Exception
    {
        // Load image
        boolean useImageSize = true;
        IImageProvider imageProvider =
            new ClassPathImageProvider( ClassPathImageProviderTestCase.class, "logo.png", useImageSize );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNotNull( imageProvider.getWidth() );
        Assert.assertEquals( 220f, imageProvider.getWidth().floatValue(), 0 );
        Assert.assertNotNull( imageProvider.getHeight() );
        Assert.assertEquals( 200f, imageProvider.getHeight().floatValue(), 0 );

        // Don't use width/height from the image
        imageProvider.setUseImageSize( false );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNull( imageProvider.getWidth() );
        Assert.assertNull( imageProvider.getHeight() );

        // Use width/height from the image
        imageProvider.setUseImageSize( true );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNotNull( imageProvider.getWidth() );
        Assert.assertEquals( 220f, imageProvider.getWidth().floatValue(), 0 );
        Assert.assertNotNull( imageProvider.getHeight() );
        Assert.assertEquals( 200f, imageProvider.getHeight().floatValue(), 0 );
    }

    @Test
    public void loadPNGWithoutUsingImageSizeAndForceWidth()
        throws Exception
    {
        IImageProvider imageProvider = new ClassPathImageProvider( ClassPathImageProviderTestCase.class, "logo.png" );
        imageProvider.setWidth( 100f );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNotNull( imageProvider.getWidth() );
        Assert.assertEquals( 100f, imageProvider.getWidth().floatValue(), 0 );
        Assert.assertNull( imageProvider.getHeight() );
    }

    @Test
    public void loadPNGByUsingImageSizeAndForceWidth()
        throws Exception
    {
        boolean useImageSize = true;
        IImageProvider imageProvider =
            new ClassPathImageProvider( ClassPathImageProviderTestCase.class, "logo.png", useImageSize );
        imageProvider.setWidth( 100f );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNotNull( imageProvider.getWidth() );
        Assert.assertEquals( 100f, imageProvider.getWidth().floatValue(), 0 );
        Assert.assertNotNull( imageProvider.getHeight() );
        Assert.assertEquals( 200f, imageProvider.getHeight().floatValue(), 0 );
    }

    @Test
    public void loadPNGByUsingImageSizeAndForceWidthAndUseRatio()
        throws Exception
    {
        boolean useImageSize = true;
        IImageProvider imageProvider =
            new ClassPathImageProvider( ClassPathImageProviderTestCase.class, "logo.png", useImageSize );
        imageProvider.setResize( true );
        imageProvider.setWidth( 100f );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNotNull( imageProvider.getWidth() );
        Assert.assertEquals( 100f, imageProvider.getWidth().floatValue(), 0 );
        // Here height is computed
        Assert.assertNotNull( imageProvider.getHeight() );
        Assert.assertEquals( 91f, imageProvider.getHeight().floatValue(), 0 );
    }

    @Test
    public void loadPNGByAndChangeResize()
        throws Exception
    {
        boolean useImageSize = true;
        IImageProvider imageProvider =
            new ClassPathImageProvider( ClassPathImageProviderTestCase.class, "logo.png", useImageSize );
        imageProvider.setResize( true );
        imageProvider.setWidth( 100f );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNotNull( imageProvider.getWidth() );
        Assert.assertEquals( 100f, imageProvider.getWidth().floatValue(), 0 );
        // Here height is computed
        Assert.assertNotNull( imageProvider.getHeight() );
        Assert.assertEquals( 91f, imageProvider.getHeight().floatValue(), 0 );

        // Use the original height
        imageProvider.setResize( false );
        Assert.assertNotNull( imageProvider.getHeight() );
        Assert.assertEquals( 200f, imageProvider.getHeight().floatValue(), 0 );

        // Use the computed height
        imageProvider.setResize( true );
        Assert.assertNotNull( imageProvider.getHeight() );
        Assert.assertEquals( 91f, imageProvider.getHeight().floatValue(), 0 );

        // Use null height
        imageProvider.setUseImageSize( false );
        Assert.assertNull( imageProvider.getHeight() );

        // Use the computed height
        imageProvider.setUseImageSize( true );
        Assert.assertNotNull( imageProvider.getHeight() );
        Assert.assertEquals( 91f, imageProvider.getHeight().floatValue(), 0 );

        // Force height and compute width.
        imageProvider.setSize( null, 1000f );
        Assert.assertNotNull( imageProvider.getWidth() );
        Assert.assertEquals( 1100f, imageProvider.getWidth().floatValue(), 0 );
        Assert.assertNotNull( imageProvider.getHeight() );
        Assert.assertEquals( 1000f, imageProvider.getHeight().floatValue(), 0 );
    }
}

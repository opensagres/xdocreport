/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.xdocreport.document.images;

import fr.opensagres.xdocreport.core.document.ImageFormat;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class FileImageProviderTestCase
{

    @Test
    public void loadPNGWithoutUsingImageSize()
        throws Exception
    {
        IImageProvider imageProvider =
            new FileImageProvider( new File( "src/test/resources/fr/opensagres/xdocreport/document/images/logo.png" ) );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNull( imageProvider.getWidth(null) );
        Assert.assertNull( imageProvider.getHeight(null) );
    }

    @Test
    public void loadSVGWithoutUsingImageSize()
        throws Exception
    {
        IImageProvider imageProvider =
            new FileImageProvider( new File( "src/test/resources/fr/opensagres/xdocreport/document/images/svg.svg" ) );
        Assert.assertEquals( ImageFormat.svg, imageProvider.getImageFormat() );
        Assert.assertNull( imageProvider.getWidth(null) );
        Assert.assertNull( imageProvider.getHeight(null) );
    }

    @Test
    public void loadPNGAndUseImageSize()
        throws Exception
    {
        boolean useImageSize = true;
        IImageProvider imageProvider =
            new FileImageProvider( new File( "src/test/resources/fr/opensagres/xdocreport/document/images/logo.png" ),
                                   useImageSize );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertEquals( 220f, imageProvider.getWidth(null).floatValue(), 0 );
        Assert.assertEquals( 200f, imageProvider.getHeight(null).floatValue(), 0 );
    }

    @Test
    public void loadPNGAndModifyUseImageSize()
        throws Exception
    {
        // Load image
        boolean useImageSize = true;
        IImageProvider imageProvider =
            new FileImageProvider( new File( "src/test/resources/fr/opensagres/xdocreport/document/images/logo.png" ),
                                   useImageSize );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNotNull( imageProvider.getWidth(null) );
        Assert.assertEquals( 220f, imageProvider.getWidth(null).floatValue(), 0 );
        Assert.assertNotNull( imageProvider.getHeight(null) );
        Assert.assertEquals( 200f, imageProvider.getHeight(null).floatValue(), 0 );

        // Don't use width/height from the image
        imageProvider.setUseImageSize( false );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNull( imageProvider.getWidth(null) );
        Assert.assertNull( imageProvider.getHeight(null) );

        // Use width/height from the image
        imageProvider.setUseImageSize( true );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNotNull( imageProvider.getWidth(null) );
        Assert.assertEquals( 220f, imageProvider.getWidth(null).floatValue(), 0 );
        Assert.assertNotNull( imageProvider.getHeight(null) );
        Assert.assertEquals( 200f, imageProvider.getHeight(null).floatValue(), 0 );
    }

    @Test
    public void loadPNGWithoutUsingImageSizeAndForceWidth()
        throws Exception
    {
        IImageProvider imageProvider =
            new FileImageProvider( new File( "src/test/resources/fr/opensagres/xdocreport/document/images/logo.png" ) );
        imageProvider.setWidth( 100f );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNotNull( imageProvider.getWidth(null) );
        Assert.assertEquals( 100f, imageProvider.getWidth(null).floatValue(), 0 );
        Assert.assertNull( imageProvider.getHeight(null) );
    }

    @Test
    public void loadPNGByUsingImageSizeAndForceWidth()
        throws Exception
    {
        boolean useImageSize = true;
        IImageProvider imageProvider =
            new FileImageProvider( new File( "src/test/resources/fr/opensagres/xdocreport/document/images/logo.png" ),
                                   useImageSize );
        imageProvider.setWidth( 100f );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNotNull( imageProvider.getWidth(null) );
        Assert.assertEquals( 100f, imageProvider.getWidth(null).floatValue(), 0 );
        Assert.assertNotNull( imageProvider.getHeight(null) );
        Assert.assertEquals( 200f, imageProvider.getHeight(null).floatValue(), 0 );
    }

    @Test
    public void loadPNGByUsingImageSizeAndForceWidthAndUseRatio()
        throws Exception
    {
        boolean useImageSize = true;
        IImageProvider imageProvider =
            new FileImageProvider( new File( "src/test/resources/fr/opensagres/xdocreport/document/images/logo.png" ),
                                   useImageSize );
        imageProvider.setResize( true );
        imageProvider.setWidth( 100f );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNotNull( imageProvider.getWidth(null) );
        Assert.assertEquals( 100f, imageProvider.getWidth(null).floatValue(), 0 );
        // Here height is computed
        Assert.assertNotNull( imageProvider.getHeight(null) );
        Assert.assertEquals( 91f, imageProvider.getHeight(null).floatValue(), 0 );
    }

    @Test
    public void loadPNGByAndChangeResize()
        throws Exception
    {
        boolean useImageSize = true;
        IImageProvider imageProvider =
            new FileImageProvider( new File( "src/test/resources/fr/opensagres/xdocreport/document/images/logo.png" ),
                                   useImageSize );
        imageProvider.setResize( true );
        imageProvider.setWidth( 100f );
        Assert.assertEquals( ImageFormat.png, imageProvider.getImageFormat() );
        Assert.assertNotNull( imageProvider.getWidth(null) );
        Assert.assertEquals( 100f, imageProvider.getWidth(null).floatValue(), 0 );
        // Here height is computed
        Assert.assertNotNull( imageProvider.getHeight(null) );
        Assert.assertEquals( 91f, imageProvider.getHeight(null).floatValue(), 0 );

        // Use the original height
        imageProvider.setResize( false );
        Assert.assertNotNull( imageProvider.getHeight(null) );
        Assert.assertEquals( 200f, imageProvider.getHeight(null).floatValue(), 0 );

        // Use the computed height
        imageProvider.setResize( true );
        Assert.assertNotNull( imageProvider.getHeight(null) );
        Assert.assertEquals( 91f, imageProvider.getHeight(null).floatValue(), 0 );

        // Use null height
        imageProvider.setUseImageSize( false );
        Assert.assertNull( imageProvider.getHeight(null) );

        // Use the computed height
        imageProvider.setUseImageSize( true );
        Assert.assertNotNull( imageProvider.getHeight(null) );
        Assert.assertEquals( 91f, imageProvider.getHeight(null).floatValue(), 0 );

        // Force height and compute width.
        imageProvider.setSize( null, 1000f );
        Assert.assertNotNull( imageProvider.getWidth(null) );
        Assert.assertEquals( 1100f, imageProvider.getWidth(null).floatValue(), 0 );
        Assert.assertNotNull( imageProvider.getHeight(null) );
        Assert.assertEquals( 1000f, imageProvider.getHeight(null).floatValue(), 0 );
    }

    @Test
    public void checkIfInputStreamIsClosedAfterLoadingImageInfo() throws Exception {
        File file = new File("src/test/resources/fr/opensagres/xdocreport/document/images/logo.png");
        final InputStream fis = new FileInputStream(file);
        FileImageProvider imageProvider = new FileImageProvider(file, true) {
            @Override
            protected InputStream getInputStream() throws IOException {
                return fis;
            }
        };

        imageProvider.getHeight(300f);

        try {
            // Check if stream is closed. If so, it should throw an Exception
            fis.read();
            Assert.fail();
        } catch (IOException e) {
        }
    }
}

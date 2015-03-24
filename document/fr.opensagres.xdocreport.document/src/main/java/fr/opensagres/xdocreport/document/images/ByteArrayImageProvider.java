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
import fr.opensagres.xdocreport.core.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Image provider implementation with byte array for image content. This provider is useful when image content can
 * change by calling {@link ByteArrayImageProvider#setImageByteArray(byte[])} or
 * {@link ByteArrayImageProvider#setImageStream(InputStream))}.
 */
public class ByteArrayImageProvider
    extends AbstractImageProvider
{

    private byte[] imageByteArray;

    public ByteArrayImageProvider( InputStream imageStream )
        throws IOException
    {
        this( imageStream, false );
    }

    public ByteArrayImageProvider( InputStream imageStream, boolean useImageSize )
        throws IOException
    {
        this( imageStream != null ? IOUtils.toByteArray( imageStream ) : null, useImageSize );
    }

    public ByteArrayImageProvider( byte[] imageByteArray )
    {
        this( imageByteArray, false );
    }

    public ByteArrayImageProvider( byte[] imageByteArray, boolean useImageSize )
    {
        super( useImageSize );
        setImageByteArray( imageByteArray );
    }

    public void setImageStream( InputStream imageStream )
        throws IOException
    {
        setImageByteArray( IOUtils.toByteArray( imageStream ) );
    }

    public void setImageByteArray( byte[] imageByteArray )
    {
        this.imageByteArray = imageByteArray;
        super.resetImageInfo();
    }

    public InputStream getImageStream()
    {
        return new ByteArrayInputStream( getImageByteArray() );
    }

    public byte[] getImageByteArray()
    {
        return imageByteArray;
    }

    public void write( OutputStream output )
        throws IOException
    {
        IOUtils.write( imageByteArray, output );
    }

    public ImageFormat getImageFormat()
    {
        try
        {
            IImageInfo imageInfo = getImageInfo();
            if ( imageInfo == null )
            {
                return null;
            }
            return imageInfo.getMimeType();
        }
        catch ( IOException e )
        {
            return null;
        }
    }

    @Override
    protected IImageInfo loadImageInfo()
        throws IOException
    {
        if ( imageByteArray == null )
        {
            return null;
        }
        SimpleImageInfo imageInfo = new SimpleImageInfo();
        imageInfo.setInput(new ByteArrayInputStream(imageByteArray));
        if (!imageInfo.check())
        {
            throw new IOException("Unable to read image info.");
        }
        return imageInfo;
    }

    protected boolean doIsValid()
    {
        return getImageByteArray() != null;
    }

}

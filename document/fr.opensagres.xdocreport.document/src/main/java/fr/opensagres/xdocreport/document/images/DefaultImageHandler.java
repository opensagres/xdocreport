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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import fr.opensagres.xdocreport.template.formatter.FieldMetadata;

public class DefaultImageHandler
    implements IImageHandler
{

    private static final IImageHandler INSTANCE = new DefaultImageHandler();

    public static IImageHandler getInstance()
    {
        return INSTANCE;
    }

    private boolean defaultUseImageSize;

    public DefaultImageHandler()
    {
        this.defaultUseImageSize = false;
    }

    public IImageProvider getImageProvider( Object image, String fieldName, FieldMetadata metadata )
        throws IOException
    {
        if ( image instanceof InputStream )
        {
            return getImageProvider( (InputStream) image, fieldName, metadata );
        }
        if ( image instanceof byte[] )
        {
            return getImageProvider( (byte[]) image, fieldName, metadata );
        }
        if ( image instanceof File )
        {
            return getImageProvider( (File) image, fieldName, metadata );
        }
        return null;
    }

    protected IImageProvider getImageProvider( InputStream imageStream, String fieldName, FieldMetadata metadata )
        throws IOException
    {
        return new ByteArrayImageProvider( imageStream, isUseImageSize( fieldName, metadata ) );
    }

    protected IImageProvider getImageProvider( byte[] imageByteArray, String fieldName, FieldMetadata metadata )
        throws IOException
    {
        return new ByteArrayImageProvider( imageByteArray, isUseImageSize( fieldName, metadata ) );
    }

    protected IImageProvider getImageProvider( File imageFile, String fieldName, FieldMetadata metadata )
        throws IOException
    {
        return new FileImageProvider( imageFile, isUseImageSize( fieldName, metadata ) );
    }

    public boolean isUseImageSize( String fieldName, FieldMetadata metadata )
    {
        if ( metadata != null )
        {
            return metadata.isUseImageSize();
        }
        return defaultUseImageSize;
    }

    public boolean isDefaultUseImageSize()
    {
        return defaultUseImageSize;
    }

    public void setDefaultUseImageSize( boolean defaultUseImageSize )
    {
        this.defaultUseImageSize = defaultUseImageSize;
    }

}

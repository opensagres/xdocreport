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

import java.io.IOException;
import java.io.InputStream;

import fr.opensagres.xdocreport.core.document.ImageFormat;

/**
 * Image provider implementation with image input stream coming from ClassPath.
 */
public class ClassPathImageProvider
    extends AbstractInputStreamImageProvider
{

    private final ClassLoader classLoader;

    private final Class<?> clazz;

    private final String resourceName;

    private final ImageFormat imageFormat;

    public ClassPathImageProvider( ClassLoader classLoader, String resourceName )
    {
        this( classLoader, resourceName, false );
    }

    public ClassPathImageProvider( ClassLoader classLoader, String resourceName, boolean useImageSize )
    {
        super( useImageSize );
        this.classLoader = classLoader;
        this.resourceName = resourceName;
        this.clazz = null;
        this.imageFormat = ImageFormat.getFormatByResourceName( resourceName );
    }

    public ClassPathImageProvider( Class<?> clazz, String resourceName )
    {
        this( clazz, resourceName, false );
    }

    public ClassPathImageProvider( Class<?> clazz, String resourceName, boolean useImageSize )
    {
        super( useImageSize );
        this.clazz = clazz;
        this.resourceName = resourceName;
        this.classLoader = null;
        this.imageFormat = ImageFormat.getFormatByResourceName( resourceName );
    }

    @Override
    protected InputStream getInputStream()
        throws IOException
    {
        if ( clazz != null )
        {
            return clazz.getResourceAsStream( resourceName );
        }
        return classLoader.getResourceAsStream( resourceName );
    }

    public ImageFormat getImageFormat()
    {
        return imageFormat;
    }
}

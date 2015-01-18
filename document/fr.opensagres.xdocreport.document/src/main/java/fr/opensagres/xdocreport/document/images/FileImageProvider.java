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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import fr.opensagres.xdocreport.core.document.ImageFormat;

/**
 * Image provider implementation with image input stream coming from {@link File}.
 */
public class FileImageProvider
    extends AbstractInputStreamImageProvider
{

    private final File imageFile;

    private final ImageFormat imageFormat;

    public FileImageProvider( File imageFile )
    {
        this( imageFile, false );
    }

    public FileImageProvider( File imageFile, boolean useImageSize )
    {
        super( useImageSize );
        this.imageFile = imageFile;
        this.imageFormat = ImageFormat.getFormatByResourceName( imageFile.getName() );
    }

    @Override
    protected InputStream getInputStream()
        throws IOException
    {
        return new FileInputStream( imageFile );
    }

    public ImageFormat getImageFormat()
    {
        return imageFormat;
    }
}

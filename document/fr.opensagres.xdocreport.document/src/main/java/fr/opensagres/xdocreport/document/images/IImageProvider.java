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
import java.io.OutputStream;

import fr.opensagres.xdocreport.core.document.ImageFormat;
import fr.opensagres.xdocreport.template.formatter.NullImageBehaviour;

/**
 * Image provider is used as "context" to manage dynamic images in the document source (odt, docx...).
 */
public interface IImageProvider
{

    /**
     * Write the binary data of the image in the given output stream.
     * 
     * @param outputStream
     * @throws IOException
     */
    void write( OutputStream outputStream )
        throws IOException;

    /**
     * Returns the image format.
     * 
     * @return
     */
    ImageFormat getImageFormat();

    /**
     * Returns the width image with pixel unit.
     * 
     * @param defaultWidth as pixel
     * @param defaultHeight as pixel
     * @return
     * @throws IOException
     */
    Float getWidth(Float defaultWidth, Float defaultHeight)
        throws IOException;

    /**
     * Returns the width image with pixel unit.
     *
     * @param defaultWidth as pixel
     * @return
     * @throws IOException
     */
    Float getWidth(Float defaultWidth)
            throws IOException;

    /**
     * Set the width image with pixel unit.
     * 
     * @param width
     */
    void setWidth( Float width );

    /**
     * Returns the height image with pixel unit.
     *
     * @param defaultWidth as pixel
     * @param defaultHeight as pixel
     * @return
     * @throws IOException
     */
    Float getHeight(Float defaultWidth, Float defaultHeight)
        throws IOException;

    /**
     * Returns the height image with pixel unit.
     *
     * @param defaultHeight as pixel
     * @return
     * @throws IOException
     */
    Float getHeight(Float defaultHeight)
            throws IOException;

    /**
     * Set the height image with pixel unit.
     * 
     * @param height
     */
    void setHeight( Float height );

    /**
     * Set the width and height image with pixel unit.
     * 
     * @param width
     * @param height
     */
    void setSize( Float width, Float height );

    /**
     * Returns true if image size comes from the image and false otherwise.
     * 
     * @return
     */
    boolean isUseImageSize();

    /**
     * Set true if image size comes from the image and false otherwise.
     * 
     * @param useImageSize
     */
    void setUseImageSize( boolean useImageSize );

    /**
     * Set true if call of {@link IImageProvider#setWidth(Float)} must compute image height with ratio or if call of
     * {@link IImageProvider#setHeight(Float)} must compute image with with ratio and false otherwise.
     * 
     * @param resize
     */
    void setResize( boolean resize );

    /**
     * Returns true if call of {@link IImageProvider#setWidth(Float)} must compute image height with ratio or if call of
     * {@link IImageProvider#setHeight(Float)} must compute image with with ratio and false otherwise.
     * 
     * @return
     */
    boolean isResize();

    /**
     * Returns the behaviour to use when the stream of the image is null.
     * 
     * @return
     */
    NullImageBehaviour getBehaviour();

    /**
     * Set the behaviour to use when the stream of the image is null.
     * 
     * @param behaviour
     */
    void setBehaviour( NullImageBehaviour behaviour );

    /**
     * Returns true if the image provider is valid (ex : input stream not null) and false otherwise.
     * 
     * @return
     */
    boolean isValid();
}

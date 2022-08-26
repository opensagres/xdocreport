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
import java.util.List;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.template.IContext;

/**
 * Image registry used to store the {@link IImageProvider} used in the "context", copy the binary data of the images in
 * the generated report, modify some entry of the generated report with image information.
 */
public interface IImageRegistry
{

    public static final String IMAGE_INFO = "___imageInfo";
    public static final String REGISTER_IMAGE_METHOD = "registerImage";
    public static final String GET_WIDTH_METHOD = "getWidth";
    public static final String GET_HEIGHT_METHOD = "getHeight";
    public static final String GET_PATH_METHOD = "getPath";

    /**
     * Register the instance of imageProvider in the registry. This done when context model is tracked while processing
     * of the template engine.
     * 
     * @param imageProvider
     * @return
     * @throws XDocReportException
     */
    ImageProviderInfo registerImage( Object imageProvider, String fieldName, IContext context )
        throws XDocReportException, IOException;

    /**
     * Called before processing of the template engine.
     * 
     * @throws XDocReportException
     */
    void preProcess()
        throws XDocReportException;

    /**
     * Called after processing of the template engine. In this step, list of image provider is populated. This method
     * can be implemented to copy the binary data of the images in the generated report.
     * 
     * @throws XDocReportException
     */
    void postProcess()
        throws XDocReportException;

    /**
     * Returns list of image provider info.
     * 
     * @return
     */
    List<ImageProviderInfo> getImageProviderInfos();

    /**
     * Returns the width of the image provider if not null and the given defaultWidth otherwise.
     *
     * @param imageProvider
     * @param defaultWidth
     * @return
     * @throws IOException
     */
    String getWidth( ImageProviderInfo info, String defaultWidth )
        throws IOException;

    /**
     * Returns the width of the image provider if not null and the given defaultWidth otherwise.
     * 
     * @param imageProvider
     * @param defaultWidth
     * @param defaultHeight
     * @return
     * @throws IOException
     */
    String getWidth( ImageProviderInfo info, String defaultWidth, String defaultHeight )
        throws IOException;

    /**
     * Returns the height of the image provider if not null and the given defaultHeight otherwise.
     *
     * @param imageProvider
     * @param defaultHeight
     * @return
     * @throws IOException
     */
    String getHeight( ImageProviderInfo info, String defaultHeight )
        throws IOException;

    /**
     * Returns the height of the image provider if not null and the given defaultHeight otherwise.
     * 
     * @param imageProvider
     * @param defaultWidth
     * @param defaultHeight
     * @return
     * @throws IOException
     */
    String getHeight( ImageProviderInfo info, String defaultWidth, String defaultHeight )
        throws IOException;
}

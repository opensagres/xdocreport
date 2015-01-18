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

/**
 * Information about image provider.
 */
public class ImageProviderInfo
{

    public static ImageProviderInfo RemoveImageTemplate = new ImageProviderInfo( null, null, null, null, false, false );

    public static ImageProviderInfo KeepImageTemplate = new ImageProviderInfo( null, null, null, null, true, true );

    public static final String KEEP_IMAGE_TEMPLATE_METHOD = "keepImageTemplate";

    public static final String NOT_REMOVE_IMAGE_TEMPLATE_METHOD = "notRemoveImageTemplate";

    private final IImageProvider imageProvider;

    private final String imageId;

    private final String imageBasePath;

    private final String imageFileName;

    private final boolean notRemoveImageTemplate;

    private final boolean keepImageTemplate;

    public ImageProviderInfo( IImageProvider imageProvider, String imageId, String imageBasePath, String imageFileName )
    {
        this( imageProvider, imageId, imageBasePath, imageFileName, true, false );
    }

    private ImageProviderInfo( IImageProvider imageProvider, String imageId, String imageBasePath,
                               String imageFileName, boolean notRemoveImageTemplate, boolean keepImageTemplate )
    {
        this.imageId = imageId;
        this.imageProvider = imageProvider;
        this.imageBasePath = imageBasePath;
        this.imageFileName = imageFileName;
        this.notRemoveImageTemplate = notRemoveImageTemplate;
        this.keepImageTemplate = keepImageTemplate;

    }

    /**
     * Returns the image provider instance.
     * 
     * @return
     */
    public IImageProvider getImageProvider()
    {
        return imageProvider;
    }

    /**
     * Returns the image id.
     * 
     * @return
     */
    public String getImageId()
    {
        return imageId;
    }

    /**
     * Returns the image base path.
     * 
     * @return
     */
    public String getImageBasePath()
    {
        return imageBasePath;
    }

    /**
     * Returns the image file name.
     * 
     * @return
     */
    public String getImageFileName()
    {
        return imageFileName;
    }

    /**
     * Returns the image type.
     * 
     * @return
     */
    public String getImageType()
    {
        return getImageProvider().getImageFormat().getType();
    }

    /**
     * Returns the full path of the image.
     * 
     * @return
     */
    public String getImageFullPath()
    {
        return getImageBasePath() + getImageFileName();
    }

    public boolean isValid()
    {
        return true;
    }

    public boolean isNotRemoveImageTemplate()
    {
        return notRemoveImageTemplate;
    }

    public boolean isKeepImageTemplate()
    {
        return keepImageTemplate;
    }

}

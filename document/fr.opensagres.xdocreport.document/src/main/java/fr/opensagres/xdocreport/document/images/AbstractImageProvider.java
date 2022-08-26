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

import fr.opensagres.xdocreport.template.formatter.NullImageBehaviour;

import java.io.IOException;

/**
 * Base class for image provider.
 */
public abstract class AbstractImageProvider
    implements IImageProvider
{

    private IImageInfo imageInfo;

    private Float width;

    private Float height;

    private Float widthFromImageInfo;

    private Float heightFromImageInfo;

    private Float[] fittedImageDimensions;

    private boolean useImageSize;

    private boolean resize;

    private NullImageBehaviour behaviour;
    
    private Boolean valid;

    public AbstractImageProvider( boolean useImageSize )
    {
        this.useImageSize = useImageSize;
        this.behaviour = null;
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.images.IImageProvider#isUseImageSize()
     */
    public boolean isUseImageSize()
    {
        return useImageSize;
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.images.IImageProvider#setUseImageSize (boolean)
     */
    public void setUseImageSize( boolean useImageSize )
    {
        this.useImageSize = useImageSize;
        resetImageInfo();
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.images.IImageProvider#getWidth(java.lang.Float)
     */
    public Float getWidth(Float defaultWidth)
        throws IOException
    {
        return getWidth(defaultWidth, null);
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.images.IImageProvider#getWidth(java.lang.Float, java.lang.Float)
     */
    public Float getWidth(Float defaultWidth, Float defaultHeight)
        throws IOException
    {
        // check if user wants to resize the image in order to fit template
        // replacement image aspect ratio is always kept intact
        if ( height == null && width == null && defaultWidth != null
                && defaultHeight != null && !isUseImageSize() && isResize() )
        {
            return getDimensions(defaultWidth, defaultHeight)[0];
        }
        if ( width != null )
        {
            return width;
        }
        if ( !isUseImageSize() )
        {
            return null;
        }
        if ( widthFromImageInfo != null )
        {
            return widthFromImageInfo;
        }
        // Width is null, test if height is setted and if resize flag is setted
        // to true, to compute width
        if ( height != null && isResize() )
        {
            float ratio =
                new Float( getImageInfo().getWidth() ).floatValue()
                    / new Float( getImageInfo().getHeight() ).floatValue();
            widthFromImageInfo = new Float( Math.round( height.floatValue() * ratio ) );
            return widthFromImageInfo;
        }
        return widthFromImageInfo = new Float( getImageInfo().getWidth() );
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.images.IImageProvider#getHeight(java.lang.Float)
     */
    public Float getHeight(Float defaultHeight)
        throws IOException
    {
        return getHeight(null, defaultHeight);
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.images.IImageProvider#getHeight(java.lang.Float, java.lang.Float)
     */
    public Float getHeight(Float defaultWidth, Float defaultHeight)
        throws IOException
    {
        // check if user wants to resize the image in order to fit template
        // replacement image aspect ratio is always kept intact
        if ( height == null && width == null && defaultWidth != null
                && defaultHeight != null && !isUseImageSize() && isResize() )
        {
            return getDimensions(defaultWidth, defaultHeight)[1];
        }
        if ( height != null )
        {
            return height;
        }
        if ( !isUseImageSize() )
        {
            return null;
        }
        if ( heightFromImageInfo != null )
        {
            return heightFromImageInfo;
        }
        // Height is null, test if width is setted and if resize flag is setted
        // to true, to compute height
        if ( width != null && isResize() )
        {
            float ratio =
                new Float( getImageInfo().getHeight() ).floatValue()
                    / new Float( getImageInfo().getWidth() ).floatValue();
            heightFromImageInfo = new Float( Math.round( width.floatValue() * ratio ) );
            return heightFromImageInfo;
        }
        return heightFromImageInfo = new Float( getImageInfo().getHeight() );
    }

    /**
     * Compute the new dimensions of the replacement image in order to fit the template.
     *
     * @param defaultWidth template width
     * @param defaultHeight template height
     * @return new dimensions of the image
     * @throws IOException
     */
    private Float[] getDimensions(Float defaultWidth, Float defaultHeight)
        throws IOException
    {
        if ( fittedImageDimensions != null )
        {
            return fittedImageDimensions;
        }

        float realImageHeight = getImageInfo().getHeight();
        float realImageWidth = getImageInfo().getWidth();
        float scale = Math.min(defaultWidth / realImageWidth, defaultHeight / realImageHeight);

        float finalImageHeight = realImageHeight * scale;
        float finalImageWidth = realImageWidth * scale;

        fittedImageDimensions = new Float[]{finalImageWidth, finalImageHeight};
        return fittedImageDimensions;
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.images.IImageProvider#setWidth(java .lang.Float)
     */
    public void setWidth( Float width )
    {
        this.width = width;
        resetImageInfo();
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.images.IImageProvider#setHeight(java .lang.Float)
     */
    public void setHeight( Float height )
    {
        this.height = height;
        resetImageInfo();
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.images.IImageProvider#setSize(java. lang.Float, java.lang.Float)
     */
    public void setSize( Float width, Float height )
    {
        setWidth( width );
        setHeight( height );
    }

    /**
     * Returns image info of the current image content.
     * 
     * @return
     * @throws IOException
     */
    public IImageInfo getImageInfo()
        throws IOException
    {
        if ( imageInfo == null )
        {
            imageInfo = loadImageInfo();
        }
        return imageInfo;
    }

    /**
     * Reset image info.
     */
    public void resetImageInfo()
    {
        this.widthFromImageInfo = null;
        this.heightFromImageInfo = null;
        this.imageInfo = null;
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.images.IImageProvider#setResize(boolean )
     */
    public void setResize( boolean resize )
    {
        this.resize = resize;
        resetImageInfo();
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.images.IImageProvider#isResize()
     */
    public boolean isResize()
    {
        return resize;
    }

    /**
     * Load image info.
     * 
     * @return
     * @throws IOException
     */
    protected abstract IImageInfo loadImageInfo()
        throws IOException;

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.images.IImageProvider#getBehaviour()
     */
    public NullImageBehaviour getBehaviour()
    {
        return behaviour;
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.opensagres.xdocreport.document.images.IImageProvider#setBehaviour(fr.opensagres.xdocreport.document.images.
     * NullImageBehaviour)
     */
    public void setBehaviour( NullImageBehaviour behaviour )
    {
        this.behaviour = behaviour;
    }

    public boolean isValid()
    {
        if (valid == null) {
            valid = doIsValid();
        }
        return valid;
    }

    protected abstract boolean doIsValid();
    
}

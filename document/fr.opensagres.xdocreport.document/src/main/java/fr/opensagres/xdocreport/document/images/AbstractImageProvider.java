/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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

import fr.opensagres.xdocreport.template.formatter.NullImageStrategy;

/**
 * Base class for image provider.
 */
public abstract class AbstractImageProvider
    implements IImageProvider
{

    private SimpleImageInfo imageInfo;

    private Float width;

    private Float height;

    private Float widthFromImageInfo;

    private Float heightFromImageInfo;

    private boolean useImageSize;

    private boolean resize;

    private NullImageStrategy strategy;
    
    private Boolean valid;

    public AbstractImageProvider( boolean useImageSize )
    {
        this.useImageSize = useImageSize;
        this.strategy = null;
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
     * @see fr.opensagres.xdocreport.document.images.IImageProvider#getWidth()
     */
    public Float getWidth()
        throws IOException
    {
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
     * @see fr.opensagres.xdocreport.document.images.IImageProvider#getHeight()
     */
    public Float getHeight()
        throws IOException
    {
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
    public SimpleImageInfo getImageInfo()
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
    protected abstract SimpleImageInfo loadImageInfo()
        throws IOException;

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.images.IImageProvider#getStrategy()
     */
    public NullImageStrategy getStrategy()
    {
        return strategy;
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.opensagres.xdocreport.document.images.IImageProvider#setStrategy(fr.opensagres.xdocreport.document.images.
     * NullImageStrategy)
     */
    public void setStrategy( NullImageStrategy strategy )
    {
        this.strategy = strategy;
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

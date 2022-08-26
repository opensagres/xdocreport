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
package fr.opensagres.xdocreport.document.json;

import java.io.IOException;
import java.io.OutputStream;

import fr.opensagres.xdocreport.core.document.ImageFormat;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.template.formatter.NullImageBehaviour;

public class JSONImage
    extends JSONObject
    implements IImageProvider
{

    public void write( OutputStream outputStream )
        throws IOException
    {
        // TODO Auto-generated method stub

    }

    public ImageFormat getImageFormat()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Float getWidth(Float defaultWidth, Float defaultHeight)
        throws IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Float getWidth(Float defaultWidth)
        throws IOException
    {
        return getWidth(defaultWidth, null);
    }

    public void setWidth( Float width )
    {
        // TODO Auto-generated method stub

    }

    public Float getHeight(Float defaultWidth, Float defaultHeight)
        throws IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Float getHeight(Float defaultHeight)
        throws IOException
    {
        return getHeight(null, defaultHeight);
    }

    public void setHeight( Float height )
    {
        // TODO Auto-generated method stub

    }

    public void setSize( Float width, Float height )
    {
        // TODO Auto-generated method stub

    }

    public boolean isUseImageSize()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void setUseImageSize( boolean useImageSize )
    {
        // TODO Auto-generated method stub

    }

    public void setResize( boolean resize )
    {
        // TODO Auto-generated method stub

    }

    public boolean isResize()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public NullImageBehaviour getBehaviour()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setBehaviour( NullImageBehaviour behaviour )
    {
        // TODO Auto-generated method stub

    }

    public boolean isValid()
    {
        // TODO Auto-generated method stub
        return false;
    }

}

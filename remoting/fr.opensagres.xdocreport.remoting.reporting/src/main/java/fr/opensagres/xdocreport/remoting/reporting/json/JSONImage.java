package fr.opensagres.xdocreport.remoting.reporting.json;

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

    public Float getWidth()
        throws IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setWidth( Float width )
    {
        // TODO Auto-generated method stub

    }

    public Float getHeight()
        throws IOException
    {
        // TODO Auto-generated method stub
        return null;
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

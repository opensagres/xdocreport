package fr.opensagres.xdocreport.document.images;

import java.io.IOException;
import java.io.InputStream;

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

    public IImageProvider getImageProvider( Object image, String fieldName )
        throws IOException
    {
        if ( image instanceof InputStream )
        {
            return getImageProvider( (InputStream) image, fieldName );
        }
        if ( image instanceof byte[] )
        {
            return getImageProvider( (byte[]) image, fieldName );
        }
        return null;
    }

    protected IImageProvider getImageProvider( InputStream imageStream, String fieldName )
        throws IOException
    {
        return new ByteArrayImageProvider( imageStream, isUseImageSize( fieldName ) );
    }

    protected IImageProvider getImageProvider( byte[] imageByteArray, String fieldName )
        throws IOException
    {
        return new ByteArrayImageProvider( imageByteArray, isUseImageSize( fieldName ) );
    }

    public boolean isUseImageSize( String fieldName )
    {
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

package fr.opensagres.xdocreport.document.images;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import fr.opensagres.xdocreport.template.formatter.FieldMetadata;

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

    public IImageProvider getImageProvider( Object image, String fieldName, FieldMetadata metadata )
        throws IOException
    {
        if ( image instanceof InputStream )
        {
            return getImageProvider( (InputStream) image, fieldName, metadata );
        }
        if ( image instanceof byte[] )
        {
            return getImageProvider( (byte[]) image, fieldName, metadata );
        }
        if ( image instanceof File )
        {
            return getImageProvider( (File) image, fieldName, metadata );
        }
        return null;
    }

    protected IImageProvider getImageProvider( InputStream imageStream, String fieldName, FieldMetadata metadata )
        throws IOException
    {
        return new ByteArrayImageProvider( imageStream, isUseImageSize( fieldName, metadata ) );
    }

    protected IImageProvider getImageProvider( byte[] imageByteArray, String fieldName, FieldMetadata metadata )
        throws IOException
    {
        return new ByteArrayImageProvider( imageByteArray, isUseImageSize( fieldName, metadata ) );
    }

    protected IImageProvider getImageProvider( File imageFile, String fieldName, FieldMetadata metadata )
        throws IOException
    {
        return new FileImageProvider( imageFile, isUseImageSize( fieldName, metadata ) );
    }

    public boolean isUseImageSize( String fieldName, FieldMetadata metadata )
    {
        if ( metadata != null )
        {
            return metadata.isUseImageSize();
        }
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

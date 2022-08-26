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
import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IEntryOutputStreamProvider;
import fr.opensagres.xdocreport.core.io.IEntryReaderProvider;
import fr.opensagres.xdocreport.core.io.IEntryWriterProvider;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.DocumentContextHelper;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.formatter.FieldMetadata;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.NullImageBehaviour;

/**
 * Abstract class for {@link IImageRegistry}.
 */
public abstract class AbstractImageRegistry
    implements IImageRegistry
{

    private static final String XDOCREPORT_PREFIX = "xdocreport_";

    private List<ImageProviderInfo> imageProviderInfos;

    protected final IEntryReaderProvider readerProvider;

    protected final IEntryWriterProvider writerProvider;

    protected final IEntryOutputStreamProvider outputStreamProvider;

    private final FieldsMetadata fieldsMetadata;

    private final NullImageBehaviour defaultBehaviour;

    public AbstractImageRegistry( IEntryReaderProvider readerProvider, IEntryWriterProvider writerProvider,
                                  IEntryOutputStreamProvider outputStreamProvider, FieldsMetadata fieldsMetadata )
    {
        this.readerProvider = readerProvider;
        this.writerProvider = writerProvider;
        this.outputStreamProvider = outputStreamProvider;
        this.fieldsMetadata = fieldsMetadata;
        this.defaultBehaviour = ( fieldsMetadata != null ? fieldsMetadata.getBehaviour() : null );
    }

    public ImageProviderInfo registerImage( Object image, String fieldName, IContext context )
        throws XDocReportException, IOException
    {

        // 1) Get image provider.
        IImageProvider imageProvider = getImageProvider( image, fieldName, context );
        if ( imageProvider == null )
        {
            return processNullImage( fieldName, imageProvider );
        }
        // 2) test if input stream of image provider is not null.
        if ( !imageProvider.isValid() )
        {
            return processNullImage( fieldName, imageProvider );
        }
        // 3) Image provider is OK, create image info.
        ImageProviderInfo info = createImageProviderInfo( imageProvider );
        getImageProviderInfos().add( info );
        return info;
    }

    private ImageProviderInfo processNullImage( String fieldName, IImageProvider imageProvider )
        throws XDocReportException
    {
        NullImageBehaviour behaviour = getBehaviour( imageProvider, getFieldBehaviour( fieldName ), defaultBehaviour );
        switch ( behaviour )
        {
            case RemoveImageTemplate:
                return ImageProviderInfo.RemoveImageTemplate;
            case KeepImageTemplate:
                return ImageProviderInfo.KeepImageTemplate;
        }
        throw new XDocReportException( "Image provider for field [" + fieldName + "] cannot be null!" );
    }

    private NullImageBehaviour getBehaviour( IImageProvider imageProvider, NullImageBehaviour fieldBehaviour,
                                             NullImageBehaviour defaultBehaviour )
    {
        NullImageBehaviour behaviour = null;
        // 1) Retrieves the behaviour from the image provider.
        if ( imageProvider != null )
        {
            behaviour = imageProvider.getBehaviour();
        }
        if ( behaviour == null )
        {
            // 2) Retrieves the behaviour from the field of the fields metadata.
            behaviour = fieldBehaviour;
        }
        if ( behaviour == null )
        {
            // 3) Retrieves the behaviour from the fields metadata.
            behaviour = defaultBehaviour;
        }
        if ( behaviour == null )
        {
            // None behaviour, use the the throw stragegy to thow an error when image provider or stream is null.
            behaviour = NullImageBehaviour.ThrowsError;
        }
        return behaviour;
    }

    private NullImageBehaviour getFieldBehaviour( String fieldName )
    {
        if ( fieldsMetadata == null )
        {
            return null;
        }
        FieldMetadata field = fieldsMetadata.getFieldAsImage( fieldName );
        return field != null ? field.getBehaviour() : null;
    }

    private IImageProvider getImageProvider( Object imageProvider, String fieldName, IContext context )
        throws XDocReportException, IOException
    {
        if ( imageProvider == null )
        {
            return null;
        }
        if ( imageProvider instanceof IImageProvider )
        {
            return (IImageProvider) imageProvider;
        }

        IImageHandler handler = DocumentContextHelper.getImageHandler( context );
        if ( handler != null )
        {
            FieldMetadata field = fieldsMetadata.getFieldAsImage( fieldName );
            return handler.getImageProvider( imageProvider, fieldName, field );
        }
        return null;
    }

    public List<ImageProviderInfo> getImageProviderInfos()
    {
        if ( imageProviderInfos == null )
        {
            imageProviderInfos = new ArrayList<ImageProviderInfo>();
        }
        return imageProviderInfos;
    }

    protected ImageProviderInfo createImageProviderInfo( IImageProvider imageProvider )
    {
        String imageId = getImageId();
        String imageBasePath = getImageBasePath();
        String imageFileName = imageId + "." + imageProvider.getImageFormat();
        return new ImageProviderInfo( imageProvider, imageId, imageBasePath, imageFileName );
    }

    protected String getImageId()
    {
        return XDOCREPORT_PREFIX + getImageProviderInfos().size();
    }

    public void preProcess()
        throws XDocReportException
    {
        // Do nothing
    }

    public void postProcess()
        throws XDocReportException
    {
        if ( imageProviderInfos != null )
        {
            // There are dynamic images, images binary must be stored in the
            // document
            // archive and some XML entries must be modified.
            // 1) Save binary images
            saveBinaryImages();

            // 2) dispose
            imageProviderInfos.clear();
            imageProviderInfos = null;
        }
    }

    protected void saveBinaryImages()
        throws XDocReportException
    {
        for ( ImageProviderInfo imageProviderInfo : imageProviderInfos )
        {
            saveBinaryImage( imageProviderInfo );
        }
    }

    protected void saveBinaryImage( ImageProviderInfo imageProviderInfo )
        throws XDocReportException
    {
        String entryName = getImageEntryName( imageProviderInfo );
        OutputStream out = outputStreamProvider.getEntryOutputStream( entryName );
        try
        {
            imageProviderInfo.getImageProvider().write( out );
        }
        catch ( IOException e )
        {
            throw new XDocReportException( e );
        }
        finally
        {
            IOUtils.closeQuietly( out );
        }
    }

    protected String getImageEntryName( ImageProviderInfo imageProviderInfo )
    {
        return imageProviderInfo.getImageBasePath() + imageProviderInfo.getImageFileName();
    }

    protected abstract String getImageBasePath();

    public String getPath( ImageProviderInfo info, String defaultPath )
    {
        if ( !info.isKeepImageTemplate() )
        {
            return getPath( info );
        }
        return defaultPath;
    }

    protected abstract String getPath( ImageProviderInfo info );

    public String getWidth( ImageProviderInfo info, String defaultWidth )
        throws IOException
    {
        return getWidth(info, defaultWidth, null);
    }

    public String getWidth( ImageProviderInfo info, String defaultWidth, String defaultHeight )
        throws IOException
    {
        if ( info.isKeepImageTemplate() )
        {
            return defaultWidth;
        }
        IImageProvider imageProvider = info.getImageProvider();
        Float width = imageProvider.getWidth(getSize(defaultWidth), getSize(defaultHeight));
        if ( width != null )
        {
            return getSize( width );
        }
        return defaultWidth;
    }

    public String getHeight( ImageProviderInfo info, String defaultHeight )
        throws IOException
    {
        return getHeight(info, null, defaultHeight);
    }

    public String getHeight( ImageProviderInfo info, String defaultWidth, String defaultHeight )
        throws IOException
    {
        if ( info.isKeepImageTemplate() )
        {
            return defaultHeight;
        }
        IImageProvider imageProvider = info.getImageProvider();
        Float height = imageProvider.getHeight(getSize(defaultWidth), getSize(defaultHeight));
        if ( height != null )
        {
            return getSize( height );
        }
        return defaultHeight;
    }

    public FieldsMetadata getFieldsMetadata()
    {
        return fieldsMetadata;
    }

    /**
     * 
     * @param sizeAsPixel
     * @return sizeAsDxa
     */
    public abstract String getSize( float sizeAsPixel );

    /**
     * 
     * @param sizeAsDxa
     * @return sizeAsPixel null in case the size cannot be computed
     */
    public abstract Float getSize( String sizeAsDxa );
}

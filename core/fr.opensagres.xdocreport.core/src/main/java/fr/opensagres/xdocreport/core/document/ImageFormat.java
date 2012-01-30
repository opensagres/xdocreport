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
package fr.opensagres.xdocreport.core.document;

/**
 * Image format.
 */
public enum ImageFormat
{

    bmp( "bmp" ), jpg( "jpeg" ), jpeg( "jpeg" ), jpe( "jpeg" ), jfif( "jpeg" ), gif( "gif" ), tif( "tiff" ), tiff(
        "tiff" ), png( "png" );

    private final String type;

    ImageFormat( String type )
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    /**
     * Returns the image format retrieved by the given extension (bmp, jpeg...).
     * 
     * @param extension
     * @return
     */
    public static ImageFormat getFormatByExtension( String extension )
    {
        if ( extension == null )
        {
            return null;
        }
        extension = extension.toLowerCase();
        ImageFormat format = null;
        for ( int i = 0; i < values().length; i++ )
        {
            format = values()[i];
            if ( format.name().equals( extension ) )
            {
                return format;
            }
        }
        return null;
    }

    /**
     * Returns the image format retrieved by the extension of the given resource name.
     * 
     * @param resourceName
     * @return
     */
    public static ImageFormat getFormatByResourceName( String resourceName )
    {
        String extension = resourceName;
        int extensionIndex = resourceName.lastIndexOf( '.' );
        if ( extensionIndex != -1 )
        {
            extension = resourceName.substring( extensionIndex + 1, resourceName.length() );
        }
        return ImageFormat.getFormatByExtension( extension );
    }
}

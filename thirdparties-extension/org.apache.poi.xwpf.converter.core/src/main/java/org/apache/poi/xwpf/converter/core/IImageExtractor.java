package org.apache.poi.xwpf.converter.core;

import java.io.IOException;

/**
 * Image extractor.
 */
public interface IImageExtractor
{

    /**
     * Extract the given image data with the given image path.
     * 
     * @param imagePath image path.
     * @param imageData image data.
     * @throws IOException
     */
    void extract( String imagePath, byte[] imageData )
        throws IOException;
}

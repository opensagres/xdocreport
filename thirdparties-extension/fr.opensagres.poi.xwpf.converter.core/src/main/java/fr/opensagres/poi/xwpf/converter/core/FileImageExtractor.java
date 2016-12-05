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
package fr.opensagres.poi.xwpf.converter.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.util.IOUtils;

/**
 * File image extractor implementation.
 */
public class FileImageExtractor
    implements IImageExtractor
{

    private final File baseDir;

    public FileImageExtractor( File baseDir )
    {
        this.baseDir = baseDir;
    }

    public void extract( String imagePath, byte[] imageData )
        throws IOException
    {
        File imageFile = new File( baseDir, imagePath );
        imageFile.getParentFile().mkdirs();
        InputStream in = null;
        OutputStream out = null;
        try
        {
            in = new ByteArrayInputStream( imageData );
            out = new FileOutputStream( imageFile );
            IOUtils.copy( in, out );
        }
        finally
        {
            if ( in != null )
            {
                IOUtils.closeQuietly( in );
            }
            if ( out != null )
            {
                IOUtils.closeQuietly( out );
            }
        }
    }

}

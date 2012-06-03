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
package fr.opensagres.xdocreport.remoting.resources.domain;

import java.io.InputStream;

import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Represention of a Binary Stream.
 * <p>
 * This class tries to avoid as much as possible to avoid loading the binary data in the JVM Memory.
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 */
@XmlRootElement
public class BinaryData
{

    private static final String DEFAULT_MIMETYPE = "application/octet-stream";

    private String resourceId;

    private InputStream content;

    private String fileName;

    private String mimeType = DEFAULT_MIMETYPE;

    private long length;

    public BinaryData()
    {
    }


//
//    public BinaryData( File file )
//        throws FileNotFoundException
//    {
//        this( file, DEFAULT_MIMETYPE );
//    }
//
//    public BinaryData( File file, String mimetype )
//        throws FileNotFoundException
//    {
//        this( new FileInputStream( file ), file.getName(), mimetype, file.length() );
//    }
//
//    public BinaryData( InputStream in, String filename )
//    {
//        this( in, filename, DEFAULT_MIMETYPE, -1 );
//    }
//
//    public BinaryData( InputStream in, String filename, String mimetype )
//    {
//        this( in, filename, mimetype, -1 );
//    }
//
//    public BinaryData( InputStream in, String filename, String mimetype, long length )
//    {
//        this.in = in;
//        this.fileName = filename;
//        this.mimeType = mimetype;
//        this.length = length;
//    }

//    public BinaryData(byte[] content, String fileName) {
//		this(content,fileName,DEFAULT_MIMETYPE);
//	}
//    public BinaryData(byte[] content, String fileName, String mimeType) {
//		super();
//		this.content = content;
//		this.fileName = fileName;
//		this.mimeType = mimeType;
//	}


	public String getResourceId()
    {
        return resourceId;
    }

    public void setResourceId( String resourceId )
    {
        this.resourceId = resourceId;
    }

    /*
     * This method is only here for JAXB and compatibility
     */
    @XmlMimeType( DEFAULT_MIMETYPE )
    public InputStream getContent()
    {
        return content;
    }

    public void setContent(InputStream content )
    {
        this.content=content;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName( String fileName )
    {
        this.fileName = fileName;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public void setMimeType( String mimeType )
    {
        this.mimeType = mimeType;
    }

    public long getLength()
    {
        return length;
    }

    public void setLength( long length )
    {
        this.length = length;
    }

//    @XmlTransient
//    public InputStream getStream()
//    {
//        return in;
//    }

}

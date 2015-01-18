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
package fr.opensagres.xdocreport.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Set;
import java.util.logging.Logger;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.core.logging.LogUtils;

/**
 * Test case for {@link XDocArchive}.
 */
public class XDocArchiveTestCase
    extends TestCase
{
    /**
     * Logger for this class
     */
    private static final Logger logger = LogUtils.getLogger( XDocArchiveTestCase.class.getName() );

    /**
     * Read non existing zip file.
     * 
     * @throws IOException
     */
    public void testReadZipNotFound()
        throws IOException
    {
        IOException e = null;
        InputStream input = XDocArchiveTestCase.class.getResourceAsStream( "not_found.odt" );
        try
        {
            XDocArchive.readZip( input );
        }
        catch ( IOException ex )
        {
            logger.severe( "exception: " + ex ); //$NON-NLS-1$

            e = ex;
        }
        assertNotNull( e );
        assertEquals( "InputStream cannot be null.", e.getMessage() );
    }

    /**
     * Read file which is not a Zip.
     * 
     * @throws IOException
     */
    public void testReadNotZip()
        throws IOException
    {
        IOException e = null;
        InputStream input = XDocArchiveTestCase.class.getResourceAsStream( "notZip.txt" );
        try
        {
            XDocArchive.readZip( input );
        }
        catch ( IOException ex )
        {
            logger.severe( "exception: " + ex ); //$NON-NLS-1$

            e = ex;
        }
        assertNotNull( e );
        assertEquals( "InputStream is not a zip.", e.getMessage() );
    }

    /**
     * Read existing file.
     * 
     * @throws IOException
     */
    public void testReadODT()
        throws IOException
    {

        InputStream input = XDocArchiveTestCase.class.getResourceAsStream( "empty.odt" );
        XDocArchive archive = XDocArchive.readZip( input );

        assertNotNull( "null archive", archive );
        Set<String> entryNames = archive.getEntryNames();
        assertFalse( "no entries", entryNames.isEmpty() );

        String mimetypeString = IOUtils.toString( archive.getEntryReader( "mimetype" ) );
        assertEquals( "application/vnd.oasis.opendocument.text", mimetypeString );
    }

    public void testWrite()
        throws IOException
    {

        XDocArchive archive = new XDocArchive();
        Writer contentWriter = archive.getEntryWriter( "content.xml" );
        contentWriter.write( "bla bla bla" );
        contentWriter.close();

        Reader contentReader = archive.getEntryReader( "content.xml" );
        String s1 = IOUtils.toString( contentReader );

        contentWriter = archive.getEntryWriter( "content.xml" );

        // Application du moteur de template
        contentWriter.write( "bla bla bla2" );
        contentWriter.close();
    }

    /**
     * docx with entry name with '\'.
     * 
     * @see https://code.google.com/p/xdocreport/issues/detail?id=234
     * @throws IOException
     */
    public void testReadDocxWithNotSlashEntryName()
        throws IOException
    {

        InputStream input = XDocArchiveTestCase.class.getResourceAsStream( "Issue234.docx" );
        XDocArchive archive = XDocArchive.readZip( input );

        assertNotNull( "null archive", archive );
        Set<String> entryNames = archive.getEntryNames();
        assertFalse( "no entries", entryNames.isEmpty() );

        String document = IOUtils.toString( archive.getEntryReader( "word/document.xml" ) );
        assertNotNull( document );
    }
}

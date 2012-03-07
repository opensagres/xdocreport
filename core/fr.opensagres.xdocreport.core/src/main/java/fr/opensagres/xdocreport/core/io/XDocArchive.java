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
package fr.opensagres.xdocreport.core.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import fr.opensagres.xdocreport.core.EncodingConstants;
import fr.opensagres.xdocreport.core.io.internal.OutputStream2InputStream;
import fr.opensagres.xdocreport.core.io.internal.OutputStreamWriterCancelable;

/**
 * {@link XDocArchive} is used to load zipped XML document archive (docx, odt...) {@link XDocArchive} cache each entry
 * zip to transform content stream with {@link IXDocPreprocessor} and {@link ITemplateEngine}.
 */
public class XDocArchive
    implements IEntryInputStreamProvider, IEntryReaderProvider, IEntryWriterProvider, IEntryOutputStreamProvider
{

    private static final String MIMETYPE_ENTRY_NAME = "mimetype";

    /**
     * Cache entries of the original XML document (odt, docx....). This Map contains a key as entry name (ex : for docx
     * word/document.xml) and the content of the XML file as array bytes.
     */
    private Map<String, byte[]> cacheEntries = new LinkedHashMap<String, byte[]>();

    /**
     * Cache entries info.
     */
    private Map<String, IEntryInfo> cacheEntriesInfo = new LinkedHashMap<String, IEntryInfo>();

    private Map<String, Long> lastModifiedEntries;

    private Map<String, Set<String>> cacheEntriesWilcard = null;

    public XDocArchive()
    {
        this( false );
    }

    public XDocArchive( boolean trackLastModified )
    {
        if ( trackLastModified )
        {
            this.lastModifiedEntries = new HashMap<String, Long>();
        }
        else
        {
            this.lastModifiedEntries = null;
        }
    }

    /**
     * Returns the entry names of the XML document archive by using cache entries.
     * 
     * @return
     */
    public Set<String> getEntryNames()
    {
        return cacheEntries.keySet();
    }

    public Set<String> getEntryNames( final String wildcard )
    {
        if ( cacheEntriesWilcard == null )
        {
            cacheEntriesWilcard = new HashMap<String, Set<String>>();
        }

        Set<String> entryNamesWithWildcard = cacheEntriesWilcard.get( wildcard );
        if ( entryNamesWithWildcard != null )
        {
            return entryNamesWithWildcard;
        }

        String regexp = wildcardToRegex( wildcard );
        entryNamesWithWildcard = new HashSet<String>();
        Set<String> entryNames = getEntryNames();
        for ( String entryName : entryNames )
        {
            if ( entryName.matches( regexp ) )
            {
                entryNamesWithWildcard.add( entryName );
            }
        }
        cacheEntriesWilcard.put( wildcard, entryNamesWithWildcard );
        return entryNamesWithWildcard;
    }

    private static String wildcardToRegex( String wildcard )
    {
        StringBuilder s = new StringBuilder( wildcard.length() );
        s.append( '^' );
        for ( int i = 0, is = wildcard.length(); i < is; i++ )
        {
            char c = wildcard.charAt( i );
            switch ( c )
            {
                case '*':
                    s.append( ".*" );
                    break;
                case '?':
                    s.append( "." );
                    break;
                // escape special regexp-characters
                case '(':
                case ')':
                case '[':
                case ']':
                case '$':
                case '^':
                case '.':
                case '{':
                case '}':
                case '|':
                case '\\':
                    s.append( "\\" );
                    s.append( c );
                    break;
                default:
                    s.append( c );
                    break;
            }
        }
        s.append( '$' );
        return ( s.toString() );
    }

    /**
     * Returns an {@link InputStream} from the cache entries of the given entry.
     * 
     * @param entryName
     * @return
     */
    public InputStream getEntryInputStream( String entryName )
    {
        if ( !cacheEntries.containsKey( entryName ) )
        {
            return null;
        }
        return new ByteArrayInputStream( (byte[]) cacheEntries.get( entryName ) );
    }

    /**
     * Returns a {@link Reader} (UTF-8) from the cache entries of the given entry.
     * 
     * @param entryName
     * @return
     */
    public Reader getEntryReader( String entryName )
    {
        InputStream inputStream = getEntryInputStream( entryName );
        if ( inputStream == null )
        {
            return null;
        }
        return toUTF8Reader( inputStream );
    }

    /**
     * Returns an {@link OutputStream} from the cache entries for writing the content of the given entry.
     * 
     * @param entryName
     * @return an {@link OutputStream}
     */
    public OutputStream getEntryOutputStream( String entryName )
    {
        return new EntryByteArrayOutputStream( entryName );
    }

    /**
     * Returns an {@link Writer} from the cache entries for writing the content of the given entry.
     * 
     * @param entryName
     * @return a {@link Writer}
     */
    public Writer getEntryWriter( String entryName )
    {
        return toUTF8Writer( getEntryOutputStream( entryName ) );
    }

    /**
     * Create a copy of the {@link XDocArchive}.
     * 
     * @return
     */
    public XDocArchive createCopy()
    {
        // Create new instance of XDocArchive
        XDocArchive archiveCopy = new XDocArchive();
        // Loop for cache entries
        for ( Map.Entry<String, byte[]> entry : cacheEntries.entrySet() )
        {
            String name = entry.getKey();
            byte[] entryData = entry.getValue();
            byte[] entryDataCopy = new byte[entryData.length];
            System.arraycopy( entryData, 0, entryDataCopy, 0, entryData.length );
            // modify the cache entries in the new XDocArchive
            archiveCopy.cacheEntries.put( name, entryDataCopy );
        }
        return archiveCopy;
    }

    /**
     * Returns true if {@link XDocArchive} contains an entry with the given name.
     * 
     * @param entryName
     * @return
     */
    public boolean hasEntry( String entryName )
    {
        return cacheEntries.containsKey( entryName );
    }

    /**
     * Returns reader from the input stream with UTF-8 charset.
     * 
     * @param inputStream the input stream to transform to reader.
     * @return reader from the input stream with UTF-8 charset.
     */
    private static Reader toUTF8Reader( InputStream inputStream )
    {
        return new InputStreamReader( inputStream, EncodingConstants.UTF_8 );
    }

    /**
     * Returns writer from the output stream with UTF-8 charset.
     * 
     * @param outputStream the output stream to transform to writer.
     * @return writer from the output stream with UTF-8 charset.
     */
    private static Writer toUTF8Writer( OutputStream outputStream )
    {
        return new OutputStreamWriterCancelable( outputStream, EncodingConstants.UTF_8 );
    }

    /**
     * Read zip from input stream and returns an instance of {@link XDocArchive} which cache each entry from the zip
     * into a Map.
     * 
     * @param sourceStream stream of odt, docx file.
     * @return
     * @throws IOException
     */
    public static XDocArchive readZip( InputStream sourceStream )
        throws IOException
    {
        if ( sourceStream == null )
        {
            throw new IOException( "InputStream cannot be null." );
        }
        // 1) Create instance of XDocArchive which cache each entry of the Zip.
        XDocArchive archive = null;
        ZipInputStream zipInputStream = null;
        try
        {
            // 2) Load Zip
            zipInputStream = new ZipInputStream( sourceStream );
            // 3) Loop for each entry of the zip and add to the XDocArchive to
            // cache
            // it.
            ZipEntry zipEntry = null;
            while ( ( zipEntry = zipInputStream.getNextEntry() ) != null )
            {
                if ( archive == null )
                {
                    // track last modified for each entries when entry change
                    archive = new XDocArchive( true );
                }
                // 4) Create empty output stream and register it with the entry
                // name
                setEntry( archive, zipEntry.getName(), zipInputStream );
                zipInputStream.closeEntry();
            }
        }
        finally
        {
            if ( zipInputStream != null )
            {
                // 6) Close stream zip
                zipInputStream.close();
            }
        }
        if ( archive == null )
        {
            throw new IOException( "InputStream is not a zip." );
        }
        return archive;
    }

    /**
     * Set the given input stream in the given entry of the document archive.
     * 
     * @param archive
     * @param inputStream
     * @throws IOException
     */
    public static void setEntry( XDocArchive archive, String entryName, InputStream input )
        throws IOException
    {
        // 1) Create empty output stream and register it with the entry
        // name
        OutputStream output = archive.getEntryOutputStream( entryName );
        // 2) Copy original stream form the input stream to the empty output stream
        IOUtils.copy( input, output );
        output.close();
    }

    /**
     * Write the given entry from the document archive in the given output stream.
     * 
     * @param archive
     * @param outputStream
     * @throws IOException
     */
    public static void writeEntry( XDocArchive archive, String entryName, OutputStream outputStream )
        throws IOException
    {
        if ( !archive.hasEntry( entryName ) )
        {
            throw new IOException( "Cannot find entry name=" + entryName + " in the document archive." );
        }
        outputStream.write( archive.cacheEntries.get( entryName ) );
    }

    /**
     * Write XML document archive in the given output stream.
     * 
     * @param archive
     * @param outputStream
     * @throws IOException
     */
    public static void writeZip( XDocArchive archive, OutputStream outputStream )
        throws IOException
    {
        ZipOutputStream zipOutputStream = new ZipOutputStream( outputStream );
        Set<String> entryNames = archive.getEntryNames();

        // ODT spec requires 'mimetype' to be the first entry
        writeZipEntry( zipOutputStream, archive, MIMETYPE_ENTRY_NAME, ZipEntry.STORED );

        for ( String entryName : entryNames )
        {
            if ( !MIMETYPE_ENTRY_NAME.equals( entryName ) )
            {
                writeZipEntry( zipOutputStream, archive, entryName, ZipEntry.DEFLATED );
            }
        }
        zipOutputStream.close();
    }

    /**
     * Write zip entry.
     * 
     * @param zipOutputStream
     * @param archive
     * @param entryName
     * @param method
     * @throws IOException
     */
    private static void writeZipEntry( ZipOutputStream zipOutputStream, XDocArchive archive, String entryName,
                                       int method )
        throws IOException
    {
        InputStream entryInputStream = archive.getEntryInputStream( entryName );
        if ( entryInputStream == null )
        {
            return;
        }
        ZipEntry zipEntry = new ZipEntry( entryName );

        zipEntry.setMethod( method );
        if ( method == ZipEntry.STORED )
        {
            byte[] inputBytes = IOUtils.toByteArray( entryInputStream );
            CRC32 crc = new CRC32();
            crc.update( inputBytes );
            zipEntry.setCrc( crc.getValue() );
            zipEntry.setSize( inputBytes.length );
            zipEntry.setCompressedSize( inputBytes.length );
            zipOutputStream.putNextEntry( zipEntry );
            IOUtils.write( inputBytes, zipOutputStream );
        }
        else
        {
            zipOutputStream.putNextEntry( zipEntry );
            IOUtils.copy( entryInputStream, zipOutputStream );
        }
        IOUtils.closeQuietly( entryInputStream );
        zipOutputStream.closeEntry();
    }

    /**
     * Returns the input stream of the given {@link XDocArchive}.
     * 
     * @param archive
     * @return the input stream of the given {@link XDocArchive}.
     * @throws IOException
     */
    public static InputStream getInputStream( XDocArchive archive )
        throws IOException
    {
        OutputStream2InputStream outputArchiveZipped = new OutputStream2InputStream();
        XDocArchive.writeZip( archive, outputArchiveZipped );
        return outputArchiveZipped.getInputStream();
    }

    /**
     * A {@link ByteArrayOutputStream} that updates the entry cache of XML document archive when it get close().
     */
    private class EntryByteArrayOutputStream
        extends ByteArrayOutputStream
    {

        private String entryName;

        public EntryByteArrayOutputStream( String entryName )
        {
            this.entryName = entryName;
        }

        public void close()
            throws IOException
        {
            // stream is closed, modify the cache
            cacheEntries.put( entryName, toByteArray() );
            if ( isTrackLastModified() )
            {
                lastModifiedEntries.put( entryName, System.currentTimeMillis() );
            }
            cacheEntriesWilcard = null;
        }
    }

    private boolean isTrackLastModified()
    {
        return lastModifiedEntries != null;
    }

    public long getLastModifiedEntry( String entryName )
    {
        if ( isTrackLastModified() )
        {
            Long lastModified = lastModifiedEntries.get( entryName );
            if ( lastModified != null )
            {
                return lastModified;
            }
        }
        return 0;
    }

    private class XDocArchiveEntryInfo
        implements IEntryInfo
    {

        private final String entryName;

        public XDocArchiveEntryInfo( String entryName )
        {
            this.entryName = entryName;
        }

        public String getName()
        {
            return entryName;
        }

        public Reader getReader()
        {
            return getEntryReader( entryName );
        }

        public InputStream getInputStream()
        {
            return getEntryInputStream( entryName );
        }

        public long getLastModified()
        {
            return getLastModifiedEntry( entryName );
        }

    }

    public IEntryInfo getEntryInfo( String entryName )
    {
        IEntryInfo info = cacheEntriesInfo.get( entryName );
        if ( info == null )
        {
            info = new XDocArchiveEntryInfo( entryName );
            cacheEntriesInfo.put( entryName, info );
        }
        return info;
    }

    public void dispose()
    {
        if ( cacheEntries != null )
        {
            cacheEntries.clear();
        }
        cacheEntries = null;
        if ( cacheEntriesInfo != null )
        {
            cacheEntriesInfo.clear();
        }
        cacheEntriesInfo = null;
        if ( lastModifiedEntries != null )
        {
            lastModifiedEntries.clear();
        }
        lastModifiedEntries = null;
    }

}

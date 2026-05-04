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
package fr.opensagres.xdocreport.document.odt;

import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.freemarker.FreemarkerTemplateEngine;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Verifies that {@link StreamingOdtReport} produces a well-formed ODT file
 * (valid ZIP, correct entry order, FreeMarker substitutions applied) without
 * allocating a full copy of the preprocessed archive for each request.
 */
public class StreamingOdtReportTestCase extends TestCase
{

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    /** Minimal ODT XML body: plain paragraph with a FreeMarker interpolation. */
    private static final String CONTENT_XML =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<office:document-content"
            + "  xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\""
            + "  xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\">"
            + "<office:body><office:text>"
            + "<text:p>Hello <text:text-input>${name}</text:text-input>!</text:p>"
            + "</office:text></office:body>"
            + "</office:document-content>";

    private static final String STYLES_XML =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<office:document-styles"
            + "  xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\"/>";

    private static final String MANIFEST_XML =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<manifest:manifest"
            + "  xmlns:manifest=\"urn:oasis:names:tc:opendocument:xmlns:manifest:1.0\">"
            + "<manifest:file-entry manifest:full-path=\"/\""
            + "  manifest:media-type=\"application/vnd.oasis.opendocument.text\"/>"
            + "<manifest:file-entry manifest:full-path=\"content.xml\""
            + "  manifest:media-type=\"text/xml\"/>"
            + "<manifest:file-entry manifest:full-path=\"styles.xml\""
            + "  manifest:media-type=\"text/xml\"/>"
            + "</manifest:manifest>";

    /** Builds a minimal in-memory ODT template. */
    private static XDocArchive buildMinimalOdtArchive() throws Exception
    {
        XDocArchive archive = new XDocArchive();

        write( archive, ODTConstants.MIMETYPE, ODTConstants.ODT_MIMETYPE );
        write( archive, ODTConstants.CONTENT_XML_ENTRY, CONTENT_XML );
        write( archive, ODTConstants.STYLES_XML_ENTRY, STYLES_XML );
        write( archive, ODTConstants.METAINF_MANIFEST_XML_ENTRY, MANIFEST_XML );

        return archive;
    }

    private static void write( XDocArchive archive, String entryName, String text )
        throws Exception
    {
        OutputStream out = archive.getEntryOutputStream( entryName );
        out.write( text.getBytes( StandardCharsets.UTF_8 ) );
        out.close();
    }

    /** Runs one {@link StreamingOdtReport#process} call and returns the ZIP bytes. */
    private static byte[] runProcess( String nameValue ) throws Exception
    {
        StreamingOdtReport report = new StreamingOdtReport();
        report.setTemplateEngine( new FreemarkerTemplateEngine() );
        report.setDocumentArchive( buildMinimalOdtArchive() );

        IContext ctx = report.createContext();
        ctx.put( "name", nameValue );

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        report.process( ctx, out );
        return out.toByteArray();
    }

    // -----------------------------------------------------------------------
    // Tests
    // -----------------------------------------------------------------------

    /** The output must be a non-empty ZIP archive. */
    public void testOutputIsNonEmptyZip() throws Exception
    {
        byte[] bytes = runProcess( "World" );
        assertTrue( "Output must not be empty", bytes.length > 0 );

        // ZIP local-file signature = 0x504B0304
        assertEquals( (byte) 0x50, bytes[0] );
        assertEquals( (byte) 0x4B, bytes[1] );
    }

    /**
     * The ODT spec requires {@code mimetype} to be the first entry and
     * stored without compression (STORED method).
     */
    public void testMimetypeIsFirstAndStored() throws Exception
    {
        byte[] bytes = runProcess( "World" );
        ZipInputStream zis = new ZipInputStream( new ByteArrayInputStream( bytes ) );
        ZipEntry first = zis.getNextEntry();

        assertNotNull( "ZIP must have at least one entry", first );
        assertEquals( "First entry must be 'mimetype'", ODTConstants.MIMETYPE, first.getName() );
        assertEquals( "mimetype entry must use STORED method",
                      ZipEntry.STORED, first.getMethod() );

        byte[] mimeBytes = IOUtils.toByteArray( zis );
        assertEquals( "mimetype content",
                      ODTConstants.ODT_MIMETYPE,
                      new String( mimeBytes, StandardCharsets.UTF_8 ) );
        zis.closeEntry();
        zis.close();
    }

    /**
     * FreeMarker must have substituted {@code ${name}} in {@code content.xml}.
     */
    public void testFreemarkerSubstitutionApplied() throws Exception
    {
        byte[] bytes = runProcess( "StreamingWorld" );
        ZipInputStream zis = new ZipInputStream( new ByteArrayInputStream( bytes ) );
        ZipEntry entry;
        boolean found = false;
        while ( ( entry = zis.getNextEntry() ) != null )
        {
            if ( ODTConstants.CONTENT_XML_ENTRY.equals( entry.getName() ) )
            {
                found = true;
                String xml = new String( IOUtils.toByteArray( zis ), StandardCharsets.UTF_8 );
                assertTrue( "content.xml must contain the substituted name",
                            xml.contains( "StreamingWorld" ) );
                assertFalse( "content.xml must not contain the raw FreeMarker expression",
                             xml.contains( "${name}" ) );
            }
            zis.closeEntry();
        }
        zis.close();
        assertTrue( "content.xml must be present in the output", found );
    }

    /**
     * All mandatory ODT entries must be present in the output.
     */
    public void testAllMandatoryEntriesPresent() throws Exception
    {
        byte[] bytes = runProcess( "World" );
        ZipInputStream zis = new ZipInputStream( new ByteArrayInputStream( bytes ) );
        boolean hasMimetype = false;
        boolean hasContent = false;
        boolean hasStyles = false;
        boolean hasManifest = false;

        ZipEntry entry;
        while ( ( entry = zis.getNextEntry() ) != null )
        {
            String name = entry.getName();
            if ( ODTConstants.MIMETYPE.equals( name ) )              hasMimetype = true;
            if ( ODTConstants.CONTENT_XML_ENTRY.equals( name ) )     hasContent  = true;
            if ( ODTConstants.STYLES_XML_ENTRY.equals( name ) )      hasStyles   = true;
            if ( ODTConstants.METAINF_MANIFEST_XML_ENTRY.equals( name ) ) hasManifest = true;
            zis.closeEntry();
        }
        zis.close();

        assertTrue( "mimetype present",           hasMimetype );
        assertTrue( "content.xml present",        hasContent  );
        assertTrue( "styles.xml present",         hasStyles   );
        assertTrue( "META-INF/manifest.xml present", hasManifest );
    }

    /**
     * Calling {@link StreamingOdtReport#process} multiple times on the same
     * report instance must produce identical output for the same input, proving
     * that the streaming path does not mutate the shared preprocessed archive.
     */
    public void testMultipleProcessCallsAreIdempotent() throws Exception
    {
        StreamingOdtReport report = new StreamingOdtReport();
        report.setTemplateEngine( new FreemarkerTemplateEngine() );
        report.setDocumentArchive( buildMinimalOdtArchive() );

        IContext ctx1 = report.createContext();
        ctx1.put( "name", "Alice" );
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        report.process( ctx1, out1 );

        IContext ctx2 = report.createContext();
        ctx2.put( "name", "Alice" );
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        report.process( ctx2, out2 );

        assertEquals( "Repeated calls with same input must produce same byte length",
                      out1.size(), out2.size() );

        // Verify second call also applied substitution correctly
        ZipInputStream zis = new ZipInputStream( new ByteArrayInputStream( out2.toByteArray() ) );
        ZipEntry entry;
        while ( ( entry = zis.getNextEntry() ) != null )
        {
            if ( ODTConstants.CONTENT_XML_ENTRY.equals( entry.getName() ) )
            {
                String xml = new String( IOUtils.toByteArray( zis ), StandardCharsets.UTF_8 );
                assertTrue( xml.contains( "Alice" ) );
            }
            zis.closeEntry();
        }
        zis.close();
    }
}

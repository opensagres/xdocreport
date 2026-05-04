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

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ODT report that generates documents without creating a per-request in-memory
 * copy of the preprocessed archive.
 *
 * <p>The standard {@link ODTReport#process} pipeline is:
 * <ol>
 *   <li>Preprocess the template (SAX transform) → {@code preprocessedArchive}
 *       (done once, cached in memory).</li>
 *   <li>Per request: {@code outputArchive = preprocessedArchive.createCopy()}
 *       — a full copy of every zip entry, held in memory.</li>
 *   <li>FreeMarker reads from {@code outputArchive}, writes back to
 *       {@code outputArchive} (same object).</li>
 *   <li>{@code XDocArchive.writeZip(outputArchive, out)} — serialise to ZIP.</li>
 * </ol>
 *
 * <p>This class eliminates step 2 and merges steps 3 and 4 into a single
 * streaming pass: FreeMarker reads directly from the preprocessed archive and
 * writes each XML entry straight into a {@link ZipOutputStream}, so the
 * per-request memory footprint is bounded by the size of the largest
 * individual ZIP entry rather than the whole archive.
 *
 * <p><b>Limitation:</b> dynamic images (fields registered via
 * {@link FieldsMetadata#addFieldAsImage}) require writing new image files into
 * the output archive, which is incompatible with pure streaming. When image
 * fields are present the implementation transparently falls back to the
 * buffered {@link ODTReport#process} path.
 */
public class StreamingOdtReport extends ODTReport
{

    /**
     * Streaming override of {@link ODTReport#process}.
     *
     * <p>For documents without image fields the output is streamed directly
     * into {@code out} via a {@link ZipOutputStream}. For documents with image
     * fields the standard buffered path is used as a safe fallback.
     */
    @Override
    public void process( IContext context, String requestedEntry, OutputStream out )
        throws XDocReportException, IOException
    {
        FieldsMetadata fm = getFieldsMetadata();
        if ( fm != null && fm.hasFieldsAsImage() )
        {
            // Image processing requires writing new files into the output archive,
            // which cannot be done while streaming. Fall back to buffered path.
            super.process( context, requestedEntry, out );
            return;
        }

        // Ensure SAX preprocessing has run (idempotent).
        preprocess();

        XDocArchive archive = getPreprocessedDocumentArchive();
        ITemplateEngine engine = getTemplateEngine();
        if ( engine == null )
        {
            throw new XDocReportException(
                "Template engine is not set. Call setTemplateEngine() before process()." );
        }

        Set<String> xmlEntries = new HashSet<>( Arrays.asList( getDefaultXMLEntries() ) );

        onBeforeProcessTemplateEngine( context, archive );
        try
        {
            if ( requestedEntry != null && !requestedEntry.isEmpty() )
            {
                streamSingleEntry( requestedEntry, archive, engine, context, out );
            }
            else
            {
                streamFullDocument( archive, engine, xmlEntries, context, out );
            }
        }
        finally
        {
            onAfterProcessTemplateEngine( context, archive );
        }
    }

    /**
     * Returns a stable, non-null report ID for use as the FreeMarker template
     * cache key.  When the report is loaded via {@link
     * fr.opensagres.xdocreport.document.registry.XDocReportRegistry} a
     * meaningful ID is set via {@link #setId}; otherwise we fall back to the
     * instance's identity string so that the cache key is still unique per
     * report instance.
     */
    private String effectiveReportId()
    {
        String id = getId();
        return id != null ? id : toString();
    }

    /**
     * Processes a single XML entry and writes the raw (non-ZIP-wrapped) bytes
     * to {@code out}, mirroring the behaviour of
     * {@link XDocArchive#writeEntry(XDocArchive, String, OutputStream)}.
     */
    private void streamSingleEntry( String entryName, XDocArchive archive,
                                    ITemplateEngine engine, IContext context,
                                    OutputStream out )
        throws XDocReportException, IOException
    {
        if ( !archive.hasEntry( entryName ) )
        {
            throw new XDocReportException(
                "No entry '" + entryName + "' in the document archive." );
        }
        Writer w = new NonClosingWriter( out );
        engine.process( effectiveReportId(), entryName, archive, w, context );
        w.flush();
    }

    /**
     * Streams all archive entries into a new ZIP written to {@code out}.
     *
     * <p>The ODT spec requires {@code mimetype} to be the very first entry
     * and to be stored without compression (STORED method) so that file-type
     * detectors can read it with a simple byte scan.  Every other entry is
     * written with DEFLATE compression.  XML template entries are piped
     * through FreeMarker; all other entries are copied as-is.
     */
    private void streamFullDocument( XDocArchive archive, ITemplateEngine engine,
                                     Set<String> xmlEntries, IContext context,
                                     OutputStream out )
        throws XDocReportException, IOException
    {
        String reportId = effectiveReportId();
        ZipOutputStream zos = new ZipOutputStream( out );

        writeMimeTypeEntry( zos, archive );

        for ( String name : archive.getEntryNames() )
        {
            if ( ODTConstants.MIMETYPE.equals( name ) )
            {
                continue; // already written as first STORED entry
            }

            zos.putNextEntry( new ZipEntry( name ) ); // DEFLATED by default

            if ( xmlEntries.contains( name ) && archive.hasEntry( name ) )
            {
                Writer w = new NonClosingWriter( zos );
                engine.process( reportId, name, archive, w, context );
                w.flush();
            }
            else
            {
                InputStream in = archive.getEntryInputStream( name );
                if ( in != null )
                {
                    IOUtils.copy( in, zos );
                    IOUtils.closeQuietly( in );
                }
            }

            zos.closeEntry();
        }

        // finish() writes the ZIP central directory without closing 'out'
        // (we do not own the caller's stream).
        zos.finish();
    }

    /**
     * Writes the {@code mimetype} entry as the first, uncompressed (STORED)
     * ZIP entry, as required by the ODF specification (§2.2.1).
     */
    private static void writeMimeTypeEntry( ZipOutputStream zos, XDocArchive archive )
        throws IOException
    {
        InputStream in = archive.getEntryInputStream( ODTConstants.MIMETYPE );
        if ( in == null )
        {
            return;
        }
        byte[] bytes = IOUtils.toByteArray( in );
        IOUtils.closeQuietly( in );

        CRC32 crc = new CRC32();
        crc.update( bytes );

        ZipEntry entry = new ZipEntry( ODTConstants.MIMETYPE );
        entry.setMethod( ZipEntry.STORED );
        entry.setSize( bytes.length );
        entry.setCompressedSize( bytes.length );
        entry.setCrc( crc.getValue() );

        zos.putNextEntry( entry );
        zos.write( bytes );
        zos.closeEntry();
    }

    /**
     * A {@link OutputStreamWriter} whose {@link #close()} flushes but does
     * NOT propagate to the underlying stream.
     *
     * <p>{@link fr.opensagres.xdocreport.template.AbstractTemplateEngine}
     * calls {@code IOUtils.closeQuietly(writer)} after processing each entry.
     * Without this wrapper that would close the shared {@link ZipOutputStream}
     * after the very first entry, corrupting all subsequent entries.
     */
    private static final class NonClosingWriter extends OutputStreamWriter
    {

        NonClosingWriter( OutputStream out )
        {
            super( out, StandardCharsets.UTF_8 );
        }

        @Override
        public void close() throws IOException
        {
            flush(); // flush buffered chars into the ZipOutputStream – do NOT close it
        }
    }
}

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
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

/**
 * Test case for {@link XDocArchive#getEntryNames(String)} wildcard.
 */
public class XDocArchiveWildcardTestCase
    extends TestCase
{

    private XDocArchive archive;

    @Override
    protected void setUp()
        throws Exception
    {
        archive = new XDocArchive();
        Writer textWriter = archive.getEntryWriter( "document.txt" );
        textWriter.write( "Text" );
        textWriter.close();
        Writer documentWriter = archive.getEntryWriter( "document.xml" );
        documentWriter.write( "Document" );
        documentWriter.close();
        Writer header1Writer = archive.getEntryWriter( "header1.xml" );
        header1Writer.write( "Header1" );
        header1Writer.close();
        Writer header2Writer = archive.getEntryWriter( "header2.xml" );
        header2Writer.write( "Header2" );
        header2Writer.close();

        super.setUp();
    }

    @Override
    protected void tearDown()
        throws Exception
    {
        archive.dispose();
        super.tearDown();
    }

    public void testBadWildcard()
        throws IOException
    {

        // *.zip wildcard
        assertEquals( 0, archive.getEntryNames( "*.zip" ).size() );

    }

    public void testXMLWildcard()
        throws IOException
    {
        Set<String> entries = new HashSet<String>();
        entries.add( "document.xml" );
        entries.add( "header1.xml" );
        entries.add( "header2.xml" );

        // *.xml wildcard
        assertEquals( entries, archive.getEntryNames( "*.xml" ) );
    }

    public void testTxtWildcard()
        throws IOException
    {

        Set<String> entries = new HashSet<String>();
        entries.add( "document.txt" );

        // *.txt wildcard
        assertEquals( entries, archive.getEntryNames( "*.txt" ) );
    }

    public void testHeaderXMLWildcard()
        throws IOException
    {
        Set<String> entries = new HashSet<String>();
        entries.add( "header1.xml" );
        entries.add( "header2.xml" );

        // header*.xml wildcard
        assertEquals( entries, archive.getEntryNames( "header*.xml" ) );
    }

    public void testHeaderXMLWildcardCache()
        throws IOException
    {
        Set<String> entries = new HashSet<String>();
        entries.add( "header1.xml" );
        entries.add( "header2.xml" );

        // header*.xml wildcard
        assertEquals( entries, archive.getEntryNames( "header*.xml" ) );

        // header*.xml wildcard
        assertEquals( entries, archive.getEntryNames( "header*.xml" ) );

    }
}

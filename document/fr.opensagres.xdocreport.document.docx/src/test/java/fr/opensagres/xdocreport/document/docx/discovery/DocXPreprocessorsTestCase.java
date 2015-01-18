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
package fr.opensagres.xdocreport.document.docx.discovery;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.sax.DocxPreprocessor;

/**
 * {@link DocxPreprocessor} JUNit tests.
 */
public class DocXPreprocessorsTestCase
    extends TestCase
{

    public void testIgnoreFldSimple()
        throws Exception
    {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        InputStream stream = IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                        + "<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" >"
                        + "<w:fldSimple w:instr=\" MERGEFIELD  ${name} \">" + "<w:r w:rsidR=\"00396432\">"
                        + "<w:rPr><w:noProof/></w:rPr><w:t>�${name}�</w:t></w:r>" + "</w:fldSimple>" + "</w:document>" );
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, new HashMap<String, Object>() );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                          + "<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
                          + "<w:r w:rsidR=\"00396432\">" + "<w:rPr><w:noProof/></w:rPr><w:t>${name}</w:t></w:r>"
                          + "</w:document>",
                      writer.toString() );
    }

    public void testIgnoreFdlSimpleVelocityForeach()
        throws Exception
    {
        DocxPreprocessor preprocessor = new DocxPreprocessor();
        InputStream stream = IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                        + "<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
                        + "<w:p w:rsidR=\"00EB1008\" w:rsidRDefault=\"00EB1008\" w:rsidP=\"00EB1008\">"
                        + "<w:fldSimple w:instr=\"MERGEFIELD &quot;#foreach($developer in $developers)&quot; \\* MERGEFORMAT\">"
                        + "<w:r>" + "<w:rPr>" + "<w:noProof/>" + "</w:rPr>"
                        + "<w:t>�#foreach($developer in $developers)�</w:t>" + "</w:r>"
                        + "</w:fldSimple>" + "</w:p>"
                        + "<w:p w:rsidR=\"00EB1008\" w:rsidRDefault=\"00EB1008\" w:rsidP=\"00EB1008\">"
                        + "<w:fldSimple w:instr=\"MERGEFIELD $developer.Name \\* MERGEFORMAT \">" + "<w:r>"
                        + "<w:rPr>" + "<w:noProof/>" + "</w:rPr>" + "<w:t>�$developer.Name�</w:t>"
                        + "</w:r>" + "</w:fldSimple>" + "</w:p>"
                        + "<w:p w:rsidR=\"00EB1008\" w:rsidRDefault=\"00EB1008\">"
                        + "<w:fldSimple w:instr=\"MERGEFIELD #end \\* MERGEFORMAT\">" + "<w:r>" + "<w:rPr>"
                        + "<w:noProof />" + "</w:rPr>" + "<w:t>�#end�</w:t>" + "</w:r>"
                        + "</w:fldSimple>" + "</w:p>" + "</w:document>" );

        
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, new HashMap<String, Object>() );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">"
            + "<w:p w:rsidR=\"00EB1008\" w:rsidRDefault=\"00EB1008\" w:rsidP=\"00EB1008\">" + "<w:r>" + "<w:rPr>"
            + "<w:noProof/>" + "</w:rPr>" + "<w:t>#foreach($developer in $developers)</w:t>" + "</w:r>" + "</w:p>"
            + "<w:p w:rsidR=\"00EB1008\" w:rsidRDefault=\"00EB1008\" w:rsidP=\"00EB1008\">" + "<w:r>" + "<w:rPr>"
            + "<w:noProof/>" + "</w:rPr>" + "<w:t>$developer.Name</w:t>" + "</w:r>" + "</w:p>"
            + "<w:p w:rsidR=\"00EB1008\" w:rsidRDefault=\"00EB1008\">" + "<w:r>" + "<w:rPr>" + "<w:noProof/>"
            + "</w:rPr>" + "<w:t>#end</w:t>" + "</w:r>" + "</w:p>" + "</w:document>", writer.toString() );
    }
}

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
package fr.opensagres.xdocreport.document.pptx.discovery;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.document.pptx.preprocessor.PPTXSlidePreprocessor;

public class PPTXSlidePreprocessorTestCase
    extends TestCase
{

    public void testname()
        throws Exception
    {
        PPTXSlidePreprocessor preprocessor = new PPTXSlidePreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                                        + "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\""
                                        + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                                        + " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">" + "<p:txBody>"
                                        + "<a:bodyPr>" + "<a:spAutoFit/>" + "</a:bodyPr>" + "<a:lstStyle/>" + "<a:p>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>" + "<a:t>x</a:t>" + "</a:r>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>" + "<a:t>project.Name</a:t>" + "</a:r>"
                                        + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>" + "</a:p>" + "</p:txBody>" + "</p:sld>" );
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, new HashMap<String, Object>() );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\""
            + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
            + " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">" + "<p:txBody>" + "<a:bodyPr>"
            + "<a:spAutoFit/>" + "</a:bodyPr>" + "<a:lstStyle/>" + "<a:p>" + "<a:r>"
            + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>" + "<a:t>x</a:t>" + "</a:r>" + "<a:r>"
            + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>" + "<a:t>project.Name</a:t>" + "</a:r>"
            + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>" + "</a:p>" + "</p:txBody>" + "</p:sld>", writer.toString() );
    }

    public void testname2()
        throws Exception
    {
        PPTXSlidePreprocessor preprocessor = new PPTXSlidePreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                                        + "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\""
                                        + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                                        + " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">" + "<p:txBody>"
                                        + "<a:bodyPr>" + "<a:spAutoFit/>" + "</a:bodyPr>" + "<a:lstStyle/>" + "<a:p>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>" + "<a:t>$</a:t>" + "</a:r>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>" + "<a:t>project.Name</a:t>" + "</a:r>"
                                        + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>" + "</a:p>" + "</p:txBody>" + "</p:sld>"  );
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null,null, new HashMap<String, Object>() );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                          + "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\""
                          + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                          + " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
                          + "<p:txBody>"
                          + "<a:bodyPr>"
                          + "<a:spAutoFit/>"
                          + "</a:bodyPr>"
                          + "<a:lstStyle/>"
                          + "<a:p>"
                          // + "<a:r>"
                          // + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
                          // + "<a:t>$</a:t>"
                          // + "</a:r>"
                          + "<a:r>" + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
                          + "<a:t>$project.Name</a:t>" + "</a:r>" + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>"
                          + "</a:p>" + "</p:txBody>" + "</p:sld>",
                      writer.toString() );
    }

    public void testname3()
        throws Exception
    {
        PPTXSlidePreprocessor preprocessor = new PPTXSlidePreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                                        + "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\""
                                        + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                                        + " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">" + "<p:txBody>"
                                        + "<a:bodyPr>" + "<a:spAutoFit/>" + "</a:bodyPr>" + "<a:lstStyle/>" + "<a:p>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>" + "<a:t>Project: </a:t>" + "</a:r>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>" + "<a:t>$</a:t>" + "</a:r>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>" + "<a:t>project.Name</a:t>" + "</a:r>"
                                        + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>" + "</a:p>" + "</p:txBody>" + "</p:sld>" );
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null,new HashMap<String, Object>() );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                          + "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\""
                          + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                          + " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">" + "<p:txBody>"
                          + "<a:bodyPr>"
                          + "<a:spAutoFit/>"
                          + "</a:bodyPr>"
                          + "<a:lstStyle/>"
                          + "<a:p>"
                          + "<a:r>"
                          + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
                          + "<a:t>Project: </a:t>"
                          + "</a:r>"
                          // + "<a:r>"
                          // + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
                          // + "<a:t>$</a:t>"
                          // + "</a:r>"
                          + "<a:r>" + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
                          + "<a:t>$project.Name</a:t>" + "</a:r>" + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>"
                          + "</a:p>" + "</p:txBody>" + "</p:sld>",
                      writer.toString() );
    }

    public void testname4()
        throws Exception
    {
        PPTXSlidePreprocessor preprocessor = new PPTXSlidePreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                                        + "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\""
                                        + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                                        + " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">" + "<p:txBody>"
                                        + "<a:bodyPr>" + "<a:spAutoFit/>" + "</a:bodyPr>" + "<a:lstStyle/>" + "<a:p>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>" + "<a:t>Project: </a:t>" + "</a:r>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>" + "<a:t>$</a:t>" + "</a:r>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>" + "<a:t>project.Name</a:t>" + "</a:r>"
                                        + "<a:r>" + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>" + "<a:t> </a:t>" + "</a:r>"
                                        + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>" + "</a:p>" + "</p:txBody>" + "</p:sld>" );        
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, new HashMap<String, Object>() );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                          + "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\""
                          + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                          + " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
                          + "<p:txBody>"
                          + "<a:bodyPr>"
                          + "<a:spAutoFit/>"
                          + "</a:bodyPr>"
                          + "<a:lstStyle/>"
                          + "<a:p>"
                          + "<a:r>"
                          + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
                          + "<a:t>Project: </a:t>"
                          + "</a:r>"
                          // + "<a:r>"
                          // + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
                          // + "<a:t>$</a:t>"
                          // + "</a:r>"
                          + "<a:r>" + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
                          + "<a:t>$project.Name</a:t>" + "</a:r>" + "<a:r>"
                          + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>" + "<a:t> </a:t>" + "</a:r>"
                          + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>" + "</a:p>" + "</p:txBody>" + "</p:sld>",
                      writer.toString() );
    }

    public void testname5()
        throws Exception
    {
        PPTXSlidePreprocessor preprocessor = new PPTXSlidePreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                                        + "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\""
                                        + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                                        + " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">" + "<p:txBody>"
                                        + "<a:bodyPr>" + "<a:spAutoFit/>" + "</a:bodyPr>" + "<a:lstStyle/>" + "<a:p>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>" + "<a:t>$name</a:t>" + "</a:r>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>" + "<a:t>$name2</a:t>" + "</a:r>"
                                        + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>" + "</a:p>" + "</p:txBody>" + "</p:sld>" );
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, new HashMap<String, Object>() );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\""
            + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
            + " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">" + "<p:txBody>" + "<a:bodyPr>"
            + "<a:spAutoFit/>" + "</a:bodyPr>" + "<a:lstStyle/>" + "<a:p>" + "<a:r>"
            + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>" + "<a:t>$name</a:t>" + "</a:r>" + "<a:r>"
            + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>" + "<a:t>$name2</a:t>" + "</a:r>"
            + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>" + "</a:p>" + "</p:txBody>" + "</p:sld>", writer.toString() );
    }

    public void testname6()
        throws Exception
    {
        PPTXSlidePreprocessor preprocessor = new PPTXSlidePreprocessor();
        InputStream stream =
                        IOUtils.toInputStream( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                                        + "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\""
                                        + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                                        + " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">" + "<p:txBody>"
                                        + "<a:bodyPr>" + "<a:spAutoFit/>" + "</a:bodyPr>" + "<a:lstStyle/>" + "<a:p>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>" + "<a:t>$</a:t>" + "</a:r>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>" + "<a:t>project.Name</a:t>" + "</a:r>"
                                        + "<a:r>" + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>" + "<a:t> $</a:t>" + "</a:r>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>" + "<a:t>project.Name</a:t>" + "</a:r>"
                                        + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>" + "</a:p>" + "</p:txBody>" + "</p:sld>" );
        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, null, null, new HashMap<String, Object>() );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                          + "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\""
                          + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
                          + " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
                          + "<p:txBody>"
                          + "<a:bodyPr>"
                          + "<a:spAutoFit/>"
                          + "</a:bodyPr>"
                          + "<a:lstStyle/>"
                          + "<a:p>"
                          // + "<a:r>"
                          // + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
                          // + "<a:t>$</a:t>"
                          // + "</a:r>"
                          + "<a:r>"
                          + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
                          + "<a:t>$project.Name</a:t>"
                          + "</a:r>"
                          // + "<a:r>"
                          // + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
                          // + "<a:t>$</a:t>"
                          // + "</a:r>"
                          + "<a:r>" + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
                          + "<a:t> $project.Name</a:t>" + "</a:r>" + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>"
                          + "</a:p>" + "</p:txBody>" + "</p:sld>",
                      writer.toString() );
    }
}

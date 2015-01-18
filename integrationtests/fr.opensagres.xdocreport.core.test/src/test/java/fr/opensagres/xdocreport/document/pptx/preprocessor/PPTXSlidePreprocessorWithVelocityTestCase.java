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
package fr.opensagres.xdocreport.document.pptx.preprocessor;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.core.io.IOUtils;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;
import fr.opensagres.xdocreport.template.velocity.VelocityDocumentFormatter;

public class PPTXSlidePreprocessorWithVelocityTestCase
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
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>" + "<a:t>$</a:t>" + "</a:r>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>" + "<a:t>developers.Name</a:t>"
                                        + "</a:r>" + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>" + "</a:p>" + "</p:txBody>" + "</p:sld>", "UTF-8" );
        FieldsMetadata metadata = new FieldsMetadata();
        metadata.addFieldAsList( "developers.Name" );
        IDocumentFormatter formatter = new VelocityDocumentFormatter();

        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, metadata, formatter, new HashMap<String, Object>() );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\""
            + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
            + " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">" + "<p:txBody>" + "<a:bodyPr>"
            + "<a:spAutoFit/>" + "</a:bodyPr>"
            + "<a:lstStyle/>"

            + "#foreach($item_developers in $developers)"

            + "<a:p>"
            // + "<a:r>"
            // + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
            // + "<a:t>$</a:t>"
            // + "</a:r>"
            + "<a:r>" + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
            + "<a:t>$item_developers.Name</a:t>" + "</a:r>" + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>" + "</a:p>"

            + "#{end}"

            + "</p:txBody>" + "</p:sld>", writer.toString() );
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
                                        + "<a:bodyPr/>" + "<a:lstStyle/>" + "<a:p>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" b=\"1\" dirty=\"0\" smtClean=\"0\"/>" + "<a:t>$</a:t>" + "</a:r>" + "<a:r>"
                                        + "<a:rPr lang=\"fr-FR\" b=\"1\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>" + "<a:t>developers.Name</a:t>"
                                        + "</a:r>" + "<a:r>" + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>" + "<a:t>$</a:t>" + "</a:r>"
                                        + "<a:r>" + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
                                        + "<a:t>developers.LastName</a:t>" + "</a:r>"
                                        + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>" + "</a:p>" + "<a:p>"
                                        + "<a:pPr lvl=\"1\"/>" + "<a:r>" + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
                                        + "<a:t>Mail: </a:t>" + "</a:r>" + "<a:r>" + "<a:rPr lang=\"fr-FR\" smtClean=\"0\"/>" + "<a:t>$</a:t>"
                                        + "</a:r>" + "<a:r>" + "<a:rPr lang=\"fr-FR\" smtClean=\"0\"/>" + "<a:t>developers.Mail</a:t>"
                                        + "</a:r>" + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>" + "</a:p>" + "</p:txBody>" + "</p:sld>", "UTF-8" );
        FieldsMetadata metadata = new FieldsMetadata();
        metadata.addFieldAsList( "developers.Name" );
        metadata.addFieldAsList( "developers.LastName" );
        metadata.addFieldAsList( "developers.Mail" );
        IDocumentFormatter formatter = new VelocityDocumentFormatter();

        StringWriter writer = new StringWriter();
        preprocessor.preprocess( "test", stream, writer, metadata, formatter, new HashMap<String, Object>() );
        assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<p:sld xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\""
            + " xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\""
            + " xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
            + "<p:txBody>"
            + "<a:bodyPr/>"
            + "<a:lstStyle/>"

            + "#foreach($item_developers in $developers)"

            + "<a:p>"
            // + "<a:r>"
            // + "<a:rPr lang=\"fr-FR\" b=\"1\" dirty=\"0\" smtClean=\"0\"/>"
            // + "<a:t>$</a:t>"
            // + "</a:r>"
            // + "<a:r>"
            // + "<a:rPr lang=\"fr-FR\" b=\"1\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
            // + "<a:t>developers.Name</a:t>"
            // + "</a:r>"
            + "<a:r>"
            + "<a:rPr lang=\"fr-FR\" b=\"1\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
            + "<a:t>$item_developers.Name</a:t>"
            + "</a:r>"
            // + "<a:r>"
            // + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
            // + "<a:t>$</a:t>"
            // + "</a:r>"
            // + "<a:r>"
            // + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
            // + "<a:t>developers.LastName</a:t>"
            // + "</a:r>"
            + "<a:r>" + "<a:rPr lang=\"fr-FR\" dirty=\"0\" err=\"1\" smtClean=\"0\"/>"
            + "<a:t>$item_developers.LastName</a:t>"
            + "</a:r>"
            + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
            + "</a:p>"

            + "<a:p>"
            + "<a:pPr lvl=\"1\"/>"
            + "<a:r>"
            + "<a:rPr lang=\"fr-FR\" dirty=\"0\" smtClean=\"0\"/>"
            + "<a:t>Mail: </a:t>"
            + "</a:r>"
            // + "<a:r>"
            // + "<a:rPr lang=\"fr-FR\" smtClean=\"0\"/>"
            // + "<a:t>$</a:t>"
            // + "</a:r>"
            // + "<a:r>"
            // + "<a:rPr lang=\"fr-FR\" smtClean=\"0\"/>"
            // + "<a:t>developers.Mail</a:t>"
            // + "</a:r>"
            + "<a:r>" + "<a:rPr lang=\"fr-FR\" smtClean=\"0\"/>" + "<a:t>$item_developers.Mail</a:t>" + "</a:r>"
            + "<a:endParaRPr lang=\"fr-FR\" dirty=\"0\"/>" + "</a:p>"

            + "#{end}"

            + "</p:txBody>" + "</p:sld>", writer.toString() );
    }

}

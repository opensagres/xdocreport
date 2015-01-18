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
package fr.opensagres.xdocreport.examples.openoffice.odt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

/**
 * Example with Open Office ODT which contains the content Hello !${name}. Merge with Freemarker template engine will
 * replace this cell with Hello world!
 */
public class ODTHelloWordWithFreemarker
{

    public static void main( String[] args )
    {
        try
        {

            // 1) Load ODT file by filling Freemarker template engine and cache
            // it
            // to the registry
            IXDocReport report =
                XDocReportRegistry.getRegistry().loadReport( ODTHelloWordWithFreemarker.class.getResourceAsStream( "ODTHelloWordWithFreemarker.odt" ),
                                                             TemplateEngineKind.Freemarker );

            // 2) Create context Java model
            IContext context = report.createContext();
            context.put( "name", "world&word\nbla bla bla" );

            // 3) Merge Java model with the ODT
            File out = new File( "out" );
            if ( !out.exists() )
            {
                out.mkdir();
            }
            File file = new File( out, "ODTHelloWordWithFreemarker.odt" );
            report.process( context, new FileOutputStream( file ) );

        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        catch ( XDocReportException e )
        {
            e.printStackTrace();
        }
    }
}

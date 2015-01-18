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
package fr.opensagres.xdocreport.template.velocity.internal;

import java.util.Collection;

import org.apache.velocity.app.event.implement.EscapeXmlReference;
import org.apache.velocity.runtime.RuntimeServices;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.template.ITemplateEngine;
import fr.opensagres.xdocreport.template.config.ReplaceText;
import fr.opensagres.xdocreport.template.formatter.AbstractDocumentFormatter;
import fr.opensagres.xdocreport.template.velocity.VelocityConstants;

public class XDocReportEscapeReference
    extends EscapeXmlReference
    implements VelocityConstants
{

    private static final String NO_ESCAPE = "$" + AbstractDocumentFormatter.NO_ESCAPE;

    private static final String NO_ESCAPE_FCT = "${" + AbstractDocumentFormatter.NO_ESCAPE;

    private String[] searchList;

    private String[] replacementList;

    @Override
    public void setRuntimeServices( RuntimeServices rs )
    {
        super.setRuntimeServices( rs );
        ITemplateEngine templateEngine = (ITemplateEngine) rs.getProperty( VELOCITY_TEMPLATE_ENGINE_KEY );
        Collection<ReplaceText> replacment = templateEngine.getConfiguration().getReplacment();
        if ( replacment != null && replacment.size() > 0 )
        {
            searchList = new String[replacment.size()];
            replacementList = new String[replacment.size()];
            int i = 0;
            for ( ReplaceText replaceText : replacment )
            {
                searchList[i] = replaceText.getOldText();
                replacementList[i] = replaceText.getNewText();
                i++;
            }
        }
    }

    @Override
    protected String escape( Object text )
    {
        String result = super.escape( text );
        if ( result != null && searchList != null )
        {
            result = StringUtils.replaceEach( result, searchList, replacementList );
        }
        return result;
    }

    @Override
    public Object referenceInsert( String reference, Object value )
    {
        if ( reference != null && ( reference.startsWith( NO_ESCAPE ) || reference.startsWith( NO_ESCAPE_FCT ) ) )
        {
            // Emulate [#noescape] directive of Freemarker.
            return value;
        }
        return super.referenceInsert( reference, value );
    }

}

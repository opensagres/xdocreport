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
package fr.opensagres.xdocreport.document.preprocessor.sax;

import org.xml.sax.Attributes;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.template.formatter.Directive;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

/**
 * Table row buffered region.
 */
public class RowBufferedRegion
    extends BufferedElement
{

    private String itemNameList;

    public RowBufferedRegion( BufferedElement parent, String uri, String localName, String name, Attributes attributes )
    {
        super( parent, uri, localName, name, attributes );
    }

    public void setLoopTemplateDirective( String startLoopDirective, String endLoopDirective )
    {
        setStartLoopDirective( startLoopDirective );
        setEndLoopDirective( endLoopDirective );
    }

    public void setStartLoopDirective( String startLoopDirective )
    {
        this.startTagElement.setBefore( startLoopDirective );
    }

    public void setEndLoopDirective( String endLoopDirective )
    {
        this.endTagElement.setAfter( endLoopDirective );
    }

    public boolean isLoopTemplateDirectiveInitilalized()
    {
        return StringUtils.isNotEmpty( getStartLoopDirective() ) && StringUtils.isNotEmpty( getEndLoopDirective() );
    }

    public void initializeLoopTemplateDirective( String itemNameList, IDocumentFormatter formatter,
                                                 String startNoParse, String endNoParse )
    {
        this.itemNameList = itemNameList;
        setStartLoopDirective( Directive.formatDirective( formatter.getStartLoopDirective( itemNameList ),
                                                          startNoParse, endNoParse ) );
        setEndLoopDirective( Directive.formatDirective( formatter.getEndLoopDirective( itemNameList ), startNoParse,
                                                        endNoParse ) );
    }

    public String getItemNameList()
    {
        return itemNameList;
    }

    public String getStartLoopDirective()
    {
        return this.startTagElement.getBefore();
    }

    public String getEndLoopDirective()
    {
        return this.endTagElement.getAfter();
    }

}

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

/**
 * Result of the process row which store information about loop directive of the template engine.
 */
public class ProcessRowResult
{

    private final String content;

    private final String fieldName;

    private final String itemNameList;

    private final String startLoopDirective;

    private final String endLoopDirective;;

    public ProcessRowResult( String content, String fieldName, String itemNameList, String startLoopDirective,
                             String endLoopDirective )
    {
        this.content = content;
        this.fieldName = fieldName;
        this.itemNameList = itemNameList;
        this.startLoopDirective = startLoopDirective;
        this.endLoopDirective = endLoopDirective;
    }

    /**
     * The content of the result of process row. This content can be the original content or modified content with
     * fields transformed.
     * 
     * @return
     */
    public String getContent()
    {
        return content;
    }

    /**
     * Returns the field name which was used to transform the content if process row has modified the content and null
     * otherwise.
     * 
     * @return
     */
    public String getFieldName()
    {
        return fieldName;
    }

    /**
     * Returns the itame name list if process row has modified the content and null otherwise.
     * 
     * @return
     */
    public String getItemNameList()
    {
        return itemNameList;
    }

    /**
     * Returns the start loop directive if process row has modified the content and null otherwise.
     * 
     * @return
     */
    public String getStartLoopDirective()
    {
        return startLoopDirective;
    }

    /**
     * Returns the end loop directive if process row has modified the content and null otherwise.
     * 
     * @return
     */
    public String getEndLoopDirective()
    {
        return endLoopDirective;
    }
}

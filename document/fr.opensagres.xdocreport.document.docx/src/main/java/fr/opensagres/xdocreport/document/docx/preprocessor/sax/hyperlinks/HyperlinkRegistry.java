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
package fr.opensagres.xdocreport.document.docx.preprocessor.sax.hyperlinks;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry which stores each Hyperlink of the final generated report.
 */
public class HyperlinkRegistry
{

    public static final String KEY = "___HyperlinkRegistry";

    private static final String EXTERNAL_TARGET_NODE = "External";

    private List<HyperlinkInfo> hyperlinks;

    /**
     * Register in the registry the given Hyperlink and returns the computed if of the hyperlink.
     * <p>
     * This method is called at first by the entry "document.xml" which defines hyperlink (after preprocessing) like
     * this :
     * 
     * <pre>
     *     <w:hyperlink w:history="1" r:id="${___HyperlinkRegistry.registerHyperlink("rId5","mailto:${d.mail}","External")}">
     * </pre>
     * 
     * To generate for instance
     * 
     * <pre>
     *     <w:hyperlink w:history="1" r:id="___rId0">
     * </pre>
     * 
     * </p>
     * <p>
     * The registry stores each hyperlink which are used in the second XML entry "word/_rels/document.xml.rels" :
     * 
     * <pre>
     * 	  [#if ___HyperlinkRegistry??]
     *    [#list ___HyperlinkRegistry.hyperlinks as ___info]
     *    <Relationship Id="${___info.id}" 
     *    				Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink" 
     *    				Target="${___info.target}" 
     *    				TargetMode="${___info.targetMode}" />
     *    [/#list]
     *    [/#if]
     * </pre>
     * 
     * To generate for instance
     * 
     * <pre>
     *     <Relationship Id="___rId0" 
     *    				Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink" 
     *    				Target="mailto:angelo.zerr@gmail.com" 
     *    				TargetMode="External" />
     * </pre>
     * 
     * </p>
     * 
     * @param id
     * @param target
     * @param targetMode
     * @return
     */
    public String registerHyperlink( String target, String targetMode )
    {
        if ( hyperlinks == null )
        {
            hyperlinks = new ArrayList<HyperlinkInfo>();
        }
        String id = "___rId" + hyperlinks.size();
        hyperlinks.add( new HyperlinkInfo( id, target, targetMode ) );
        return id;
    }

    /**
     * Register external hyperlink.
     * 
     * @param target
     * @return
     */
    public String registerHyperlink( String target )
    {
        return registerHyperlink( target, EXTERNAL_TARGET_NODE );
    }

    /**
     * Returns list of Hyperlink.
     * 
     * @return
     */
    public List<HyperlinkInfo> getHyperlinks()
    {
        return hyperlinks;
    }

}

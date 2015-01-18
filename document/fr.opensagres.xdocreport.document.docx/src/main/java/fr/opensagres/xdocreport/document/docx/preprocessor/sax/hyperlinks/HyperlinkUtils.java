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

import java.util.Map;

import fr.opensagres.xdocreport.core.utils.StringUtils;

/**
 * Utilities for Hyperlink.
 */
public class HyperlinkUtils
{

    private static final String[] DOT_SLASH = new String[] { ".", "/" };

    private static final String[] UNDERSCORE_UNDERSCORE = new String[] { "_", "_" };

    private static final String[] RELS = new String[] { ".rels", "/_rels" };

    private static final String[] BLANCK = new String[] { "", "" };

    /**
     * Returns the {@link InitialHyperlinkMap} from the given sharedContext for the given entry name.
     * 
     * @param entryName
     * @param sharedContext
     * @return
     */
    public static InitialHyperlinkMap getInitialHyperlinkMap( String entryName, Map<String, Object> sharedContext )
    {
        if ( sharedContext == null )
        {
            return null;
        }
        return (InitialHyperlinkMap) sharedContext.get( getHyperlinkRegistryKey( entryName ) );
    }

    /**
     * Register the given {@link InitialHyperlinkMap} in the given sharedContext for the given entry name.
     * 
     * @param entryName
     * @param sharedContext
     * @param hyperlinkMap
     */
    public static void putInitialHyperlinkMap( String entryName, Map<String, Object> sharedContext,
                                               InitialHyperlinkMap hyperlinkMap )
    {
        sharedContext.put( getHyperlinkRegistryKey( entryName ), hyperlinkMap );
    }

    /**
     * Returns the key used for the template engine directive which must use the hyperlink regitsry for the given
     * entryName (ex :"word/document.xml" will returns "___HyperlinkRegistryword_document_xml")
     * 
     * @param entryName
     * @return
     */
    public static String getHyperlinkRegistryKey( String entryName )
    {
        entryName = StringUtils.replaceEach( entryName, DOT_SLASH, UNDERSCORE_UNDERSCORE );
        return HyperlinkRegistry.KEY + entryName;
    }

    /**
     * Returns the entry name which uses the XML element Relationship from the given *.rels entry. Example :
     * "word/_rels/document.xml.rels" will returns "word/document.xml".
     * 
     * @param relsEntryName
     * @return
     */
    public static String getEntryNameWithoutRels( String relsEntryName )
    {
        return StringUtils.replaceEach( relsEntryName, RELS, BLANCK );
    }
}

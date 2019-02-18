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
package fr.opensagres.xdocreport.document.odt.preprocessor;

import static fr.opensagres.xdocreport.document.odt.ODTConstants.ANNOTATION_DC_NS;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.PARAGRAPH_ELT;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

import fr.opensagres.xdocreport.core.utils.StringUtils;

/**
 * ODT annotation handler. Functions:
 * <ul>
 *   <li>safe iterate over blok: table:table-row, text:list-item, text:section,
 *       using @before#foreach()$after#end syntax</li>
 *   <li>safe replace range annotations with annotation content</li>
 *   <li>annotation content is treated as simple unformatted text</li>
 * </ul>
 *
 * <p>Created on 2018-07-06</p>
 *
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @version $Id$
 */
public class ODTAnnotationParsingHelper
{
    private static final String BEFORE_LABEL = "@before";

    private static final String AFTER_LABEL = "@after";

    private static final List<String> PARRENTS = unmodifiableList(
        asList( "table:table-row", "text:list-item", "text:section") );

    private boolean parsing = false;

    private StringBuilder content;

    private boolean ignore;

    private String replacement;

    private String after;

    private String before;

    private String name;

    private int index;

    private boolean notReplacedYet;

    /**
     * The annotation tag is parsing now.
     *
     * @return <code>true</code> if annotation tag is parsing now,
     *      <code>false</code> otherwise
     */
    public boolean isParsing()
    {
        return parsing;
    }

    /**
     * Inform this helper which component is parsed now.
     *
     * @param uri
     * @param localName
     * @param name
     */
    public void setCurrentElement( String uri, String localName, String name )
    {
        if ( ANNOTATION_DC_NS.equals(uri) )
        {
            ignore = true;
            return;
        }
        ignore = false;
        if( PARAGRAPH_ELT.equals(localName) && content.length()>0 )
        {
            content.append('\n');
        }
    }

    /**
     * Set the helper in the state of parsing annotation tag content.
     *
     * @param name the name of the annotation. Not <code>null</code>
     *      when annotation has end element somewere in document
     * @param index index of the annotation element. It is used to close
     *      replacement block in proper place (replacement block is not allowed
     *      to span over the end of enclosed tag)
     */
    public void setParsingBegin( String name, int index )
    {
        this.parsing = true;
        this.content = new StringBuilder();
        this.ignore = false;
        this.before = null;
        this.after = null;
        this.replacement = null;
        // avoid empty string
        if( StringUtils.isNotEmpty( name ) )
        {
            this.name = name;
        }
        else
        {
            this.name = null;
        }
        this.index = index;
        this.notReplacedYet = true;
    }

    public void setParsingEnd()
    {
        parse();
        parsing = false;
    }

    /**
     * Close range annotation.
     *
     * @param name the name of the range annotation
     * @param force if <code>true</code> block will be closed even if name
     *      does not match
     * @return <code>true</code> if the name is the name of the first oppened
     *      annotation, <code>false</code> otherwise
     */
    public boolean resetRangeAnnotation( String name, boolean force )
    {
        if( force || ( StringUtils.isNotEmpty( name ) && name.equals(this.name) ) )
        {
            this.name = null;
            this.parsing = false;
            return true;
        }
        return false;
    }

    /**
     * Parse the annotation content. It splits into 3 section:
     * <ul>
     *   <li>replacement</li>
     *   <li>before</li>
     *   <li>after</li>
     * </ul>
     */
    private void parse()
    {
        int indexBefore = content.indexOf(BEFORE_LABEL);
        int indexAfter = content.indexOf(AFTER_LABEL);
        if ( indexBefore < 0 && indexAfter < 0 )
        {
            if( content.length() > 0 )
            {
                replacement = content.toString();
            }
        }
        else if( indexBefore < 0 || indexAfter < 0)
        {
            if( indexBefore > 0 || indexAfter > 0 )
            {
                replacement = content.substring(0, Math.max( indexBefore, indexAfter) );
            }
            if( indexBefore < 0 )
            {
                after = content.substring(indexAfter + AFTER_LABEL.length(), content.length());
            }
            else
            {
                before = content.substring(indexBefore + BEFORE_LABEL.length(), content.length());
            }
        }
        else if( indexBefore < indexAfter )
        {
            if( indexBefore > 0 )
            {
                replacement = content.substring(0, indexBefore);
            }
            before = content.substring(indexBefore + BEFORE_LABEL.length(), indexAfter);
            after = content.substring(indexAfter + AFTER_LABEL.length(), content.length());
        }
        else
        {
            if( indexAfter > 0)
            {
                replacement = content.substring(0, indexAfter);
            }
            after = content.substring(indexAfter + AFTER_LABEL.length(), indexBefore);
            before = content.substring(indexBefore + BEFORE_LABEL.length(), content.length());
        }
    }

    /**
     * Append new string into annotation content.
     *
     * @param value the value being appended
     */
    public void append( String value )
    {
        if( ignore )
        {
            return;
        }
        content.append(value);
    }

    /**
     * Return type of parrents which are itrable using point-annotation.
     *
     * @return the list of tag names
     */
    public List<String> getParents()
    {
        return PARRENTS;
    }

    /**
     * Check if annotation has "before" part.
     *
     * @return <code>true</code> if annotation has "before" part, <code>false</code>
     *      otherwise
     */
    public boolean hasBefore()
    {
        return before != null;
    }

    /**
     * Returns "before" part of annotation content.
     *
     * @return "before" part
     */
    public String getBefore()
    {
        return before;
    }

    /**
     * Check if annotation has "after" part.
     *
     * @return <code>true</code> if annotation has "after" part, <code>false</code>
     *      otherwise
     */
    public boolean hasAfter()
    {
        return after != null;
    }

    /**
     * Returns "after" part of annotation content.
     *
     * @return "after" part
     */
    public String getAfter()
    {
        return after;
    }

    /**
     * Check if annotation has "replacement" part.
     *
     * @return <code>true</code> if annotation has "replacement" part,
     *      <code>false</code> otherwise
     */
    public boolean hasReplacement()
    {
        return replacement != null;
    }

    /**
     * Returns "replacement" part of annotation content.
     *
     * @return "replacement" part
     */
    public String getReplacement()
    {
        return replacement;
    }

    /**
     * Check if this anotation is "point" or "range" anotation.
     *
     * @return <code>true</code> if this is range annotation, <code>false</code>
     *      otherwise
     */
    public boolean isRangeAnnotation()
    {
        return name != null;
    }

    /**
     * Check if index of current element shows we are in the same block like
     * anotation is. It is used to check if we should force to close range
     * annotation which is setup across many tags.
     *
     * @param currentElementIndex the index of current tag
     * @return <code>true</code> if current tag is the same container like
     *      anotation is, <code>false</code> otherwise
     */
    public boolean isTheSameBlock(int currentElementIndex)
    {
        return currentElementIndex >= index;
    }

    /**
     * Check if in case of range annotation replacement has been already done.
     *
     * @return <code>true</code> when replacement has been already performed,
     *      <code>false</code> otherwise
     */
    public boolean isNotReplacedYet()
    {
        return notReplacedYet;
    }

    /**
     * Should be executed, when range annotation replacemen is done. It prevents
     * unwanted multiple relacement when several containers is present in
     * annotation range.
     */
    public void setReplacementDone()
    {
        this.notReplacedYet = false;
    }

}

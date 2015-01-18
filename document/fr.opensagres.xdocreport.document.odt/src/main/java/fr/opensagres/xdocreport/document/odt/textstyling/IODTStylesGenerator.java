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
package fr.opensagres.xdocreport.document.odt.textstyling;

import fr.opensagres.xdocreport.document.textstyling.IStylesGenerator;
import fr.opensagres.xdocreport.document.textstyling.properties.ContainerProperties;

/**
 * Interface for ODT Style generator
 * 
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 */
public interface IODTStylesGenerator
    extends IStylesGenerator<ODTDefaultStyle>
{
    /**
     * @param styleName
     * @return the level of the header associated to this style (-1 if this is not a header style)
     */
    int getHeaderStyleNameLevel( String styleName );

    /**
     * @param level
     * @return the name of the style for a given header level
     */
    String getHeaderStyleName( int level );

    /**
     * @param level
     * @return header style declaration for a given level
     */
    String generateHeaderStyle( int level );

    /**
     * @return the style definition for lists (both ul and ol)
     */
    String generateListStyle();

    /**
     * @return number of available header styles
     */
    int getHeaderStylesCount();

    /**
     * @return style name for Ordered Lists
     */
    String getOLStyleName();

    /**
     * @return style name for unordered Lists
     */
    String getULStyleName();

    /**
     * @return the suffix used to name the style for list items
     */
    String getListItemParagraphStyleNameSuffix();

    /**
     * @return the style declaration for Blod, italic ...
     */
    String generateTextStyles();

    /**
     * @return the name of the style used for Bold
     */
    String getBoldStyleName();

    /**
     * @return the name of the style used for Italic
     */
    String getItalicStyleName();

    /**
     * @return the name of the style used for Underline
     */
    String getUnderlineStyleName();

    /**
     * @return the name of the style used for Strike
     */
    String getStrikeStyleName();

    /**
     * @return the name of the style used for Subscript
     */
    String getSubscriptStyleName();

    /**
     * @return the name of the style used for Superscript
     */
    String getSuperscriptStyleName();

    String getParaBreakBeforeStyleName();

    String getParaBreakAfterStyleName();

    /**
     * Returns the paragraph styles for page break.
     * 
     * @return
     */
    String generateParagraphStyles();

    String getTextStyleName( ContainerProperties properties );

    String getDynamicStyles();
}
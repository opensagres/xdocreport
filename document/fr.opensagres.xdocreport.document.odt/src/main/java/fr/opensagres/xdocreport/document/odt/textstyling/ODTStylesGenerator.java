/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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

/**
 * Interface for ODT Style generator
 * 
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 *
 */
public interface ODTStylesGenerator
{
    /**
     * @param styleName
     * @return the level of the header associated to this style (-1 if this is not a header style)
     */
    public abstract int getHeaderStyleNameLevel(String styleName);
    
    /**
     * @param level
     * @return the name of the style for a given header level
     */
    public abstract String getHeaderStyleName( int level );

    /**
     * @param level
     * @return header style declaration for a given level
     */
    public abstract String generateHeaderStyle( int level );

    /**
     * @return the style definition for lists (both ul and ol)
     */
    public abstract String generateListStyle();

    /**
     * @return number of available header styles
     */
    public abstract int getHeaderStylesCount();
    
    /**
     * @return style name for Ordered Lists
     */
    public abstract String getOLStyleName();
    
    /**
     * @return style name for unordered Lists
     */
    public abstract String getULStyleName();
    
    
    /**
     * @return the suffix used to name the style for list items
     */
    public abstract String getListItemParagraphStyleNameSuffix();
    
    /**
     * @return the style declaration for Blod, italic ...
     */
    public abstract String generateTextStyles();
    
    /**
     * @return the name of the style used for Bold
     */
    public abstract String getBoldStyleName();
    
    /**
     * @return the name of the style used for Italic
     */
    public abstract String getItalicStyleName();

    /**
     * 
     * @return the name of the style used for italic + bold
     */    
    public abstract String getBoldItalicStyleName();
}
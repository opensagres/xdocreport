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
package fr.opensagres.xdocreport.document.textstyling.properties;

/**
 * Container properties.
 */
public abstract class ContainerProperties
{

    public enum ContainerType
    {
        SPAN, PARAGRAPH, LIST, LIST_ITEM, HEADER
    }

    private boolean pageBreakBefore;

    private boolean pageBreakAfter;

    private boolean bold;

    private boolean italic;

    private boolean underline;

    private boolean strike;

    private boolean subscript;

    private boolean superscript;

    private TextAlignment textAlignment;

    private String styleName;

    private final ContainerType type;

    public ContainerProperties( ContainerType type )
    {
        this.type = type;
    }

    public ContainerType getType()
    {
        return type;
    }

    public boolean isPageBreakBefore()
    {
        return pageBreakBefore;
    }

    public void setPageBreakBefore( boolean pageBreakBefore )
    {
        this.pageBreakBefore = pageBreakBefore;
    }

    public boolean isPageBreakAfter()
    {
        return pageBreakAfter;
    }

    public void setPageBreakAfter( boolean pageBreakAfter )
    {
        this.pageBreakAfter = pageBreakAfter;
    }

    public boolean isBold()
    {
        return bold;
    }

    public void setBold( boolean bold )
    {
        this.bold = bold;
    }

    public boolean isItalic()
    {
        return italic;
    }

    public void setItalic( boolean italic )
    {
        this.italic = italic;
    }

    public boolean isUnderline()
    {
        return underline;
    }

    public void setUnderline( boolean underline )
    {
        this.underline = underline;
    }

    public boolean isStrike()
    {
        return strike;
    }

    public void setStrike( boolean strike )
    {
        this.strike = strike;
    }

    public boolean isSubscript()
    {
        return subscript;
    }

    public void setSubscript( boolean subscript )
    {
        this.subscript = subscript;
    }

    public boolean isSuperscript()
    {
        return superscript;
    }

    public void setSuperscript( boolean superscript )
    {
        this.superscript = superscript;
    }

    public TextAlignment getTextAlignment()
    {
        return textAlignment;
    }

    public void setTextAlignment( TextAlignment textAlignment )
    {
        this.textAlignment = textAlignment;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

}

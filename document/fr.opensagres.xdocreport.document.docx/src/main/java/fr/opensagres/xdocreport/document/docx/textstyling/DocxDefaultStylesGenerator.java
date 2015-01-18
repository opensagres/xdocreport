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
package fr.opensagres.xdocreport.document.docx.textstyling;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.docx.preprocessor.DefaultStyle;
import fr.opensagres.xdocreport.document.textstyling.AbstractStylesGenerator;
import fr.opensagres.xdocreport.document.textstyling.Style;

public class DocxDefaultStylesGenerator
    extends AbstractStylesGenerator<DefaultStyle>
    implements IDocxStylesGenerator
{

    public static final IDocxStylesGenerator INSTANCE = new DocxDefaultStylesGenerator();

    private static final Style XDocReport_Hyperlink_Style;

    private static final List<Style> XDocReport_Headings_Style;

    private static final Style XDocReport_AbstractNum_decimal;

    private static final Style XDocReport_AbstractNum_bullet;

    public static final int DocReport_AbstractNumId_decimal = 50;

    public static final int DocReport_AbstractNumId_bullet = 51;

    static
    {
        XDocReport_Headings_Style = new ArrayList<Style>();
        for ( int i = 1; i < getHeaderStylesCount() + 1; i++ )
        {
            XDocReport_Headings_Style.add( Style.load( "XDocReport_Heading_" + i, DocxDefaultStylesGenerator.class ) );
        }
        XDocReport_Hyperlink_Style = Style.load( "XDocReport_Hyperlink", DocxDefaultStylesGenerator.class );
        XDocReport_AbstractNum_decimal =
            Style.load( "XDocReport_AbstractNum_decimal", DocxDefaultStylesGenerator.class );
        XDocReport_AbstractNum_bullet = Style.load( "XDocReport_AbstractNum_bullet", DocxDefaultStylesGenerator.class );
    }

    public String generateAllStyles( DefaultStyle defaultStyle )
    {
        StringBuilder styles = new StringBuilder();
        generateHyperlinkStyle( styles, defaultStyle );
        generateHeadersStyle( styles, defaultStyle );
        return styles.toString();
    }

    public String generateAbstractNumBullet( DefaultStyle defaultStyle )
    {
        return XDocReport_AbstractNum_bullet.getContent();
    }

    public String generateAbstractNumDecimal( DefaultStyle defaultStyle, int abstractNumId )
    {
        return MessageFormat.format(XDocReport_AbstractNum_decimal.getContent(), abstractNumId);
    }

    public void generateHyperlinkStyle( StringBuilder style, DefaultStyle defaultStyle )
    {
        if ( !defaultStyle.hasHyperLinkStyleId() )
        {
            style.append( XDocReport_Hyperlink_Style.getContent() );
        }
    }

    public String getHyperLinkStyleId( DefaultStyle defaultStyle )
    {
        if ( defaultStyle == null )
        {
            return XDocReport_Hyperlink_Style.getId();
        }
        return StringUtils.isNotEmpty( defaultStyle.getHyperLinkStyleId() ) ? defaultStyle.getHyperLinkStyleId()
                        : XDocReport_Hyperlink_Style.getId();
    }

    public void generateHeadersStyle( StringBuilder styles, DefaultStyle defaultStyle )
    {
        for ( int i = 1; i < getHeaderStylesCount() + 1; i++ )
        {
            generateHeaderStyle( styles, defaultStyle, i );
        }

    }

    public void generateHeaderStyle( StringBuilder styles, DefaultStyle defaultStyle, int level )
    {
        if ( !defaultStyle.hasHeaderStyle( level ) )
        {
            styles.append( XDocReport_Headings_Style.get( level - 1 ).getContent() );
        }
    }

    public String getHeaderStyleId( int level, DefaultStyle defaultStyle )
    {
        if ( defaultStyle == null )
        {
            return getDefaultHeaderStyleId( level );
        }
        if ( !defaultStyle.hasHeaderStyle( level ) )
        {
            return getDefaultHeaderStyleId( level );
        }
        return defaultStyle.getHeaderStyle( level );
    }

    private String getDefaultHeaderStyleId( int level )
    {
        return XDocReport_Headings_Style.get( level - 1 ).getId();
    }

    /**
     * @return number of available header styles
     */
    public static int getHeaderStylesCount()
    {
        return 6;
    }

    public Integer getAbstractNumIdForList( boolean ordered, DefaultStyle defaultStyle )
    {
        if ( defaultStyle == null )
        {
            return ordered ? DocReport_AbstractNumId_decimal : DocReport_AbstractNumId_bullet;
        }
        Integer abstractNumId =
            ordered ? defaultStyle.getAbstractNumIdForOrdererList() : defaultStyle.getAbstractNumIdForUnordererList();
        if ( abstractNumId != null )
        {
            return abstractNumId;
        }
        return ordered ? DocReport_AbstractNumId_decimal : DocReport_AbstractNumId_bullet;
    }
}

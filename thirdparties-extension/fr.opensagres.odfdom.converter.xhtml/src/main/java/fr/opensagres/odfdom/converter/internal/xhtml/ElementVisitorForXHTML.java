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
package fr.opensagres.odfdom.converter.internal.xhtml;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.activation.MimetypesFileTypeMap;

import fr.opensagres.xdocreport.core.utils.Base64Utility;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.element.OdfStylableElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawFrameElement;
import org.odftoolkit.odfdom.dom.element.draw.DrawImageElement;
import org.odftoolkit.odfdom.dom.element.office.OfficeMasterStylesElement;
import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement;
import org.odftoolkit.odfdom.dom.element.style.StyleFooterElement;
import org.odftoolkit.odfdom.dom.element.style.StyleHeaderElement;
import org.odftoolkit.odfdom.dom.element.style.StyleMasterPageElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTableColumnPropertiesElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableCellElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableColumnElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableElement;
import org.odftoolkit.odfdom.dom.element.table.TableTableRowElement;
import org.odftoolkit.odfdom.dom.element.text.TextAElement;
import org.odftoolkit.odfdom.dom.element.text.TextHElement;
import org.odftoolkit.odfdom.dom.element.text.TextLineBreakElement;
import org.odftoolkit.odfdom.dom.element.text.TextListElement;
import org.odftoolkit.odfdom.dom.element.text.TextListItemElement;
import org.odftoolkit.odfdom.dom.element.text.TextListLevelStyleElementBase;
import org.odftoolkit.odfdom.dom.element.text.TextListLevelStyleNumberElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.dom.element.text.TextSpanElement;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStyle;
import org.odftoolkit.odfdom.incubator.doc.text.OdfTextListStyle;
import org.odftoolkit.odfdom.pkg.OdfElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import fr.opensagres.odfdom.converter.core.ElementVisitorConverter;
import fr.opensagres.odfdom.converter.core.IURIResolver;
import fr.opensagres.odfdom.converter.core.utils.ODFUtils;
import fr.opensagres.odfdom.converter.core.utils.StringUtils;
import fr.opensagres.odfdom.converter.xhtml.XHTMLOptions;
import fr.opensagres.xdocreport.xhtml.extension.StringEscapeUtils;
import fr.opensagres.xdocreport.xhtml.extension.XHTMLConstants;
import fr.opensagres.xdocreport.xhtml.extension.XHTMLPageContentBuffer;

public class ElementVisitorForXHTML
    extends ElementVisitorConverter
    implements XHTMLConstants
{

    private static final String _100 = "100%;";

    private static final String CHARACTER = "character";

    private static final String BLOCK = "block";

    private String masterPageLayoutName = null;

    private String currentMasterPageLayoutName = null;

    private final ODFXHTMLPage xhtml;

    private XHTMLPageContentBuffer currentXHTMLContent;

    private boolean exportImageAsBase64;

    public ElementVisitorForXHTML( ODFXHTMLPage xhtml, XHTMLOptions options, OdfDocument odfDocument, OutputStream out,
                                   Writer writer )
    {
        super( odfDocument, options != null ? options.getExtractor() : null, out, writer );
        this.xhtml = xhtml;
        this.currentXHTMLContent = null;
        this.exportImageAsBase64 = options != null ? options.getExportImageAsBase64() : false;
    }

    @Override
    protected boolean isNeedImageStream()
    {
        return exportImageAsBase64;
    }
    // ---------------------- visit root
    // styles.xml//office:document-styles/office:master-styles

    @Override
    public void visit( OfficeMasterStylesElement ele )
    {
        super.visit( ele );
    }

    // ---------------------- visit root
    // styles.xml//office:document-styles/office:master-styles/style:master-page

    /**
     * Generate XHTML page footer + header
     */
    @Override
    public void visit( StyleMasterPageElement ele )
    {
        currentMasterPageLayoutName = ele.getStylePageLayoutNameAttribute();
        if ( masterPageLayoutName == null )
        {
            masterPageLayoutName = currentMasterPageLayoutName;
            xhtml.getPageBeforeBody().setBodyClass( xhtml.getStyleEngine().getClassName( OdfStyleFamily.List.getName(),
                                                                                         masterPageLayoutName ) );
        }
        super.visit( ele );
    }

    // ---------------------- visit
    // styles.xml//office:document-styles/office:master-styles/style:master-page/style-header

    @Override
    public void visit( StyleHeaderElement ele )
    {
        currentXHTMLContent = xhtml.getPageBodyContentHeader();
        visitMasterPageHeaderFooter( ele,
                                     xhtml.getStyleEngine().getMasterPageHeaderStyleName( currentMasterPageLayoutName ) );
    }

    // ---------------------- visit
    // styles.xml//office:document-styles/office:master-styles/style:master-page/style-footer

    @Override
    public void visit( StyleFooterElement ele )
    {
        currentXHTMLContent = xhtml.getPageBodyContentFooter();
        visitMasterPageHeaderFooter( ele,
                                     xhtml.getStyleEngine().getMasterPageFooterStyleName( currentMasterPageLayoutName ) );
    }

    private void visitMasterPageHeaderFooter( OdfElement ele, String styleName )
    {
        String styleFamilyName = OdfStyleFamily.List.getName();
        visit( DIV_ELEMENT, ele, styleName, styleFamilyName );
    }

    // ---------------------- visit root //office-body/office-text

    @Override
    public void visit( OfficeTextElement ele )
    {
        currentXHTMLContent = xhtml.getPageBodyContentBody();
        // String styleName = masterPageLayoutName;
        // String styleFamilyName = OdfStyleFamily.List.getName();
        visit( DIV_ELEMENT, ele, null, null );
    }

    // ---------------------- visit //text:h

    @Override
    public void visit( TextHElement ele )
    {
        // Get level (Heading levels go only to 6 in XHTML)).
        int level = 1;
        Integer levelAsInteger = ele.getTextOutlineLevelAttribute();
        if ( levelAsInteger != null && levelAsInteger <= 6 )
        {
            level = levelAsInteger;
        }
        visit( H_ELEMENT + level, ele );
    }

    // ---------------------- visit //text:p

    @Override
    public void visit( TextPElement ele )
    {
        boolean addNbsp = ele.getFirstChild() == null;
        visit( P_ELEMENT, ele, addNbsp );
    }

    // ---------------------- visit //text:span

    @Override
    public void visit( TextSpanElement ele )
    {
        boolean addNbsp = ele.getFirstChild() == null;
        visit( SPAN_ELEMENT, ele, addNbsp );
    }

    // ---------------------- visit //text:a

    @Override
    public void visit( TextAElement ele )
    {
        String href = ele.getXlinkHrefAttribute();
        Collection<String> attributes = new ArrayList<String>();
        attributes.add( HREF_ATTR );
        attributes.add( href );
        visit( A_ELEMENT, ele, attributes.toArray( StringUtils.EMPTY_STRING_ARRAY ) );
    }

    // ---------------------- visit text:list

    @Override
    public void visit( TextListElement ele )
    {
        int level = 1;
        boolean numberedList = false;
        String styleName = ele.getTextStyleNameAttribute();
        if ( StringUtils.isNotEmpty( styleName ) )
        {
            try
            {
                OdfTextListStyle listStyle = odfDocument.getContentDom().getAutomaticStyles().getListStyle( styleName );
                if ( listStyle != null )
                {
                    TextListLevelStyleElementBase levelListStyle = listStyle.getLevel( level );
                    numberedList =
                        ( levelListStyle != null && TextListLevelStyleNumberElement.ELEMENT_NAME.equals( levelListStyle.getOdfName() ) );
                }

            }
            catch ( Exception e )
            {
                // Do nothing
            }
        }

        String elementName = numberedList ? OL_ELEMENT : UL_ELEMENT;
        startVisit( elementName, ele );
        // String styleName = null;
        // String styleFamilyName = "";
        // currentXHTMLContent.getStyleEngine().applyStyles(styleName,
        // styleFamilyName,
        // xhtml);
        endVisit( elementName, ele );
    }

    // ---------------------- visit text:listitem

    @Override
    public void visit( TextListItemElement ele )
    {
        visit( LI_ELEMENT, ele, null, null );
    }

    // ---------------------- visit table:table (ex : <table:table
    // table:name="Tableau1" table:style-name="Tableau1">)

    private List<String> columnsWidth;

    @Override
    public void visit( TableTableElement ele )
    {
        try
        {
            columnsWidth = ODFUtils.getColumnWidthsAsString( ele, odfDocument );
            visit( TABLE_ELEMENT, ele );
        }
        finally
        {
            if ( columnsWidth != null )
            {
                columnsWidth = null;
            }
        }
    }

    // ---------------------- visit table:table-column

    @Override
    public void visit( TableTableColumnElement ele )
    {
        String[] attributes = null;
        OdfStyle style = ele.getAutomaticStyle();
        if ( style != null )
        {
            Node node = null;
            NodeList nodes = style.getChildNodes();
            for ( int i = 0; i < nodes.getLength(); i++ )
            {
                node = nodes.item( i );
                if ( StyleTableColumnPropertiesElement.ELEMENT_NAME.getLocalName().equals( node.getLocalName() ) )
                {
                    StyleTableColumnPropertiesElement columnPropertiesElement =
                        (StyleTableColumnPropertiesElement) node;

                    // width
                    String width = columnPropertiesElement.getStyleRelColumnWidthAttribute();
                    if ( StringUtils.isEmpty( width ) )
                    {
                        width = columnPropertiesElement.getStyleColumnWidthAttribute();
                    }
                    if ( StringUtils.isNotEmpty( width ) )
                    {
                        attributes = new String[2];
                        attributes[0] = WIDTH_ATTR;
                        attributes[1] = ODFUtils.getDimensionAsPixel( width );
                    }
                    break;
                }
            }
        }

        Integer numberColumnsRepeated = ele.getTableNumberColumnsRepeatedAttribute();
        if ( numberColumnsRepeated == null )
        {
            numberColumnsRepeated = 1;
        }
        for ( int i = 0; i < numberColumnsRepeated; i++ )
        {
            startVisit( COL_ELEMENT, ele, attributes );
            endVisit( COL_ELEMENT, ele );
        }
    }

    // ---------------------- visit table:table-row

    private int cellIndex = -1;

    @Override
    public void visit( TableTableRowElement ele )
    {
        try
        {
            cellIndex = -1;
            visit( TR_ELEMENT, ele );
        }
        finally
        {
            if ( cellIndex != -1 )
            {
                cellIndex = -1;
            }
        }
    }

    // ---------------------- visit table:table-cell

    @Override
    public void visit( TableTableCellElement ele )
    {

        Collection<String> attributes = new ArrayList<String>();

        // table:number-columns-spanned
        Integer colSpan = ele.getTableNumberColumnsSpannedAttribute();
        if ( colSpan != null && colSpan != 1 )
        {
            attributes.add( COLSPAN_ATTR );
            attributes.add( String.valueOf( colSpan ) );
        }

        // table:number-rows-spanned
        Integer rowSpan = ele.getTableNumberRowsSpannedAttribute();
        if ( rowSpan != null && rowSpan != 1 )
        {
            attributes.add( ROWSPAN_ATTR );
            attributes.add( String.valueOf( rowSpan ) );
        }

        // // compute width
        // cellIndex++;
        // String width = null;
        // if (columnsWidth != null && columnsWidth.size() > cellIndex) {
        // String columnWidth = columnsWidth.get(cellIndex);
        // // if (colSpan != null) {
        // // columnWidth = columnWidth * colSpan;
        // // }
        // attributes.add(WIDTH_ATTR);
        // attributes.add(String.valueOf(columnWidth));
        // }

        visit( TD_ELEMENT, ele, attributes.toArray( StringUtils.EMPTY_STRING_ARRAY ) );
    }

    // ---------------------- visit draw:frame

    @Override
    public void visit( DrawFrameElement ele )
    {
        // ex : <draw:frame draw:style-name="Mfr1" draw:name="images4"
        // text:anchor-type="char" svg:x="-0.603cm" svg:y="-0.358cm"
        // svg:width="21.114cm" svg:height="4.071cm" draw:z-index="11">
        Collection<String> attributes = new ArrayList<String>();

        // draw:name
        String name = ele.getDrawNameAttribute();
        if ( !StringUtils.isEmpty( name ) )
        {
            attributes.add( NAME_ATTR );
            attributes.add( name );
        }

        StringBuilder htmlStyle = new StringBuilder();
        // svg:x
        String x = ele.getSvgXAttribute();
        if ( !StringUtils.isEmpty( x ) )
        {
            htmlStyle.append( "left:" );
            htmlStyle.append( x );
            htmlStyle.append( ';' );
        }

        // svg:y
        String y = ele.getSvgYAttribute();
        if ( !StringUtils.isEmpty( y ) )
        {
            htmlStyle.append( "top:" );
            htmlStyle.append( y );
            htmlStyle.append( ';' );
        }

        // svg:width
        String width = ele.getSvgWidthAttribute();
        if ( !StringUtils.isEmpty( width ) )
        {
            htmlStyle.append( "width:" );
            htmlStyle.append( width );
            htmlStyle.append( ';' );
        }

        // svg:height
        String height = ele.getSvgHeightAttribute();
        if ( !StringUtils.isEmpty( height ) )
        {
            htmlStyle.append( "height:" );
            htmlStyle.append( height );
            htmlStyle.append( ';' );
        }

        // draw:z-index
        Integer zIndex = ele.getDrawZIndexAttribute();
        if ( zIndex != null )
        {
            htmlStyle.append( "z-index:" );
            htmlStyle.append( String.valueOf( zIndex ) );
            htmlStyle.append( ';' );
        }
        if ( htmlStyle.length() > 0 )
        {
            attributes.add( STYLE_ATTR );
            attributes.add( htmlStyle.toString() );
        }

        visit( DIV_ELEMENT, ele, attributes.toArray( StringUtils.EMPTY_STRING_ARRAY ) );
    }

    // ---------------------- visit draw:image

    @Override
    protected void visitImage( DrawImageElement ele, String href, byte[] imageStream )
    {
        Collection<String> attributes = new ArrayList<String>();

        // src
        if( exportImageAsBase64 && imageStream != null )
        {
            String mimeType = new MimetypesFileTypeMap().getContentType( new File( href ) );
            StringBuilder src = new StringBuilder();
            src.append( DATA_ATTR_TAG );
            src.append( mimeType + ";base64,");
            src.append( Base64Utility.encode( imageStream ) );

            attributes.add( SRC_ATTR );
            attributes.add( src.toString() );
        }
        else
        {
            String src = ele.getXlinkHrefAttribute();
            IURIResolver uriResolver = xhtml.getStyleEngine().getURIResolver();
            src = uriResolver.resolve( src );
            attributes.add( SRC_ATTR );
            attributes.add( src );
        }

        // other attributes
        Node parentNode = ele.getParentNode();
        if ( parentNode instanceof DrawFrameElement )
        {
            DrawFrameElement frameElement = (DrawFrameElement) parentNode;

            attributes.add( STYLE_ATTR );
            StringBuilder styleAttr = new StringBuilder();

            // width
            styleAttr.append( WIDTH_ATTR );
            styleAttr.append( ':' );
            styleAttr.append( _100 );

            // height
            styleAttr.append( HEIGHT_ATTR );
            styleAttr.append( ':' );
            styleAttr.append( _100 );

            String anchorType = frameElement.getTextAnchorTypeAttribute();
            if ( !CHARACTER.equals( anchorType ) )
            {
                styleAttr.append( DISPLAY_ATTR );
                styleAttr.append( ':' );
                styleAttr.append( BLOCK );
            }
            attributes.add( styleAttr.toString() );
        }

        visit( IMG_ELEMENT, ele, null, null, attributes.toArray( StringUtils.EMPTY_STRING_ARRAY ) );
    }

    // ---------------------- visit text:line-break

    @Override
    public void visit( TextLineBreakElement ele )
    {
        currentXHTMLContent.startEndElement( BR_ELEMENT );
    }

    private void visit( String elementName, OdfStylableElement ele, String... attributes )
    {
        visit( elementName, ele, false, attributes );
    }

    private void visit( String elementName, OdfStylableElement ele, boolean addNbsp, String... attributes )
    {
        String styleName = ele.getStyleName();
        String styleFamily = ele.getStyleFamily() != null ? ele.getStyleFamily().getName() : null;
        visit( elementName, ele, addNbsp, styleName, styleFamily, attributes );
    }

    private void visit( String elementName, OdfElement ele, String styleName, String styleFamily, String... attributes )
    {
        visit( elementName, ele, false, styleName, styleFamily, attributes );
    }

    private void visit( String elementName, OdfElement ele, boolean addNbsp, String styleName, String styleFamily,
                        String... attributes )
    {
        startVisit( elementName, ele, attributes );
        xhtml.getStyleEngine().applyStyles( styleFamily, styleName, currentXHTMLContent );
        endVisit( elementName, ele, addNbsp );
    }

    private void startVisit( String elementName, OdfElement element, String... attributes )
    {
        currentXHTMLContent.startElementNotEnclosed( elementName );
        if ( attributes != null )
        {
            String value = null;
            String name = null;
            for ( int i = 0; i < attributes.length; i++ )
            {
                String attr = attributes[i];
                if ( i % 2 == 0 )
                {
                    // Attribute name
                    name = attr;
                }
                else
                {
                    // Attribute value
                    value = attr;
                    currentXHTMLContent.setAttribute( name, value );
                    name = null;
                    value = null;
                }
            }
        }
    }

    private void endVisit( String elementName, OdfElement element )
    {
        endVisit( elementName, element, false );
    }

    private void endVisit( String elementName, OdfElement element, boolean addNbsp )
    {
        currentXHTMLContent.endElementNotEnclosed();
        super.visit( element );
        if ( addNbsp )
        {
            currentXHTMLContent.setText( "&nbsp;" );
        }
        currentXHTMLContent.endElement( elementName );
    }

    @Override
    public void save()
        throws IOException
    {
        if ( writer != null )
        {
            xhtml.save( writer );
        }
        else
        {
            xhtml.save( out );
        }
        super.save();
    }

    @Override
    protected void processTextNode( Text node )
    {
        String content = node.getTextContent();
        if ( StringUtils.isNotEmpty( content ) )
        {
            // Escape with HTML characters
            currentXHTMLContent.setText( StringEscapeUtils.escapeHtml( content ) );
        }
        else
        {
            currentXHTMLContent.setText( "&nbsp;" );
        }

    }
}

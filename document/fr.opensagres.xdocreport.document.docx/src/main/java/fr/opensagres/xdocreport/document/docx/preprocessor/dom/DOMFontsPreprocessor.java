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
package fr.opensagres.xdocreport.document.docx.preprocessor.dom;

import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.utils.DOMUtils;
import fr.opensagres.xdocreport.core.utils.XPathUtils;
import fr.opensagres.xdocreport.document.preprocessor.dom.DOMPreprocessor;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import fr.opensagres.xdocreport.template.formatter.IDocumentFormatter;

public class DOMFontsPreprocessor
    extends DOMPreprocessor
{

    public static final String FONT_NAME_KEY = "___fontName";

    public static final String FONT_SIZE_KEY = "___fontSize";

    public static final String FONT_SIZE_TWO_KEY = "___fontSizeTwo";

    public static final String FONT_SIZE_KEY_MULT_BY_2 = "___fontSize * 2";

    public static DOMFontsPreprocessor INSTANCE = new DOMFontsPreprocessor();

    @Override
    protected void visit( Document document, String entryName, FieldsMetadata fieldsMetadata,
                          IDocumentFormatter formatter, Map<String, Object> sharedContext )
        throws XDocReportException
    {

        /**
         * <w:rPr> <w:rFonts w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial" /> <w:sz w:val="24" /> <w:szCs w:val="24" />
         * </w:rPr>
         */
        // get list of w:rPr
        try
        {
            Element rFontsElt = null;
            NodeList rFontsNodeList =
                XPathUtils.evaluateNodeSet( document, "//w:rFonts", DocxNamespaceContext.INSTANCE );
            for ( int i = 0; i < rFontsNodeList.getLength(); i++ )
            {

                rFontsElt = (Element) rFontsNodeList.item( i );
                if ( i == 0 )
                {
                    // Set variable which multiply the font size
                    String set = formatter.getSetDirective( FONT_SIZE_TWO_KEY, FONT_SIZE_KEY_MULT_BY_2 );

                    StringBuilder setWithIf = new StringBuilder();
                    setWithIf.append( formatter.getStartIfDirective( FONT_SIZE_KEY ) );
                    setWithIf.append( set );
                    setWithIf.append( formatter.getEndIfDirective( FONT_SIZE_KEY ) );

                    Node firstChild = rFontsElt.getOwnerDocument().getDocumentElement().getFirstChild();
                    Text newChild = rFontsElt.getOwnerDocument().createTextNode( setWithIf.toString() );
                    rFontsElt.getOwnerDocument().getDocumentElement().insertBefore( newChild, firstChild );

                }
                // 1) Manage fontName (w:rFonts). Modify :

                /**
                 * <w:rFonts w:ascii="Arial" w:hAnsi="Arial" w:cs="Arial" />
                 */

                // to

                /**
                 * <w:rFonts w:ascii="[#if ___fontName??]${___fontName}[#else]Arial[/#if]"
                 * w:cs="[#if ___fontName??]${___fontName}[#else]Arial[/#if]"
                 * w:hAnsi="[#if ___fontName??]${___fontName}[#else]Arial[/#if]"/>
                 */

                updateDynamicAttr( rFontsElt, "w:ascii", FONT_NAME_KEY, formatter );
                updateDynamicAttr( rFontsElt, "w:cs", FONT_NAME_KEY, formatter );
                updateDynamicAttr( rFontsElt, "w:hAnsi", FONT_NAME_KEY, formatter );

                // 2) Manage fontSize <w:sz w:val="24" /> <w:szCs w:val="24" />. Modify :

                /**
                 * <w:sz w:val="24" /> <w:szCs w:val="24" />
                 */

                // to

                /**
                 * <w:sz w:val="[#if ___fontSize??]${___fontSize}[#else]24[/#if]" /> <w:szCs
                 * w:val="[#if ___fontSize??]${___fontSize}[#else]24[/#if]" />
                 */
                Element szCsElt = DOMUtils.getFirstChildElementByTagName( rFontsElt.getParentNode(), "w:szCs" );
                if ( szCsElt != null )
                {
                    updateDynamicAttr( szCsElt, "w:val", FONT_SIZE_KEY, formatter );
                }
                Element szElt = DOMUtils.getFirstChildElementByTagName( rFontsElt.getParentNode(), "w:sz" );
                if ( szElt != null )
                {
                    if ( szCsElt != null )
                    {
                        // <w:szCs is defined, multiply the value of font size with 2
                        updateDynamicAttr( szElt, "w:val", FONT_SIZE_TWO_KEY, formatter );
                    }
                    else
                    {
                        updateDynamicAttr( szElt, "w:val", FONT_SIZE_KEY, formatter );
                    }

                }
                // try
                // {
                // DOMUtils.save( rFontsElt, System.out );
                // }
                // catch ( TransformerException e )
                // {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
            }
        }
        catch ( XPathExpressionException e )
        {
            throw new XDocReportException( e );
        }

    }

}

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

import static fr.opensagres.xdocreport.document.docx.DocxConstants.RELATIONSHIPS_HYPERLINK_NS;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.RELATIONSHIP_ELT;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.RELATIONSHIP_ID_ATTR;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.RELATIONSHIP_TARGET_ATTR;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.RELATIONSHIP_TARGET_MODE_ATTR;
import static fr.opensagres.xdocreport.document.docx.DocxConstants.RELATIONSHIP_TYPE_ATTR;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX Handler which loads Relationship type of Hyperlink declared in the "word/_rels/document.xml.rels" in the initial
 * {@link InitialHyperlinkMap}.
 */
public class HyperlinkContentHandler
    extends DefaultHandler
{

    private InitialHyperlinkMap hyperlinks;

    @Override
    public void startElement( String uri, String localName, String name, Attributes attributes )
        throws SAXException
    {
        if ( RELATIONSHIP_ELT.equals( name ) )
        {
            String type = attributes.getValue( RELATIONSHIP_TYPE_ATTR );
            if ( RELATIONSHIPS_HYPERLINK_NS.equals( type ) )
            {

                // <Relationship Id="rId1"
                // Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink"
                // Target="mailto:$mail"
                // TargetMode="External" />

                // Create Java model HyperlinkInfo of this XML declaration and
                // register it in the Map
                String id = attributes.getValue( RELATIONSHIP_ID_ATTR );
                String target = attributes.getValue( RELATIONSHIP_TARGET_ATTR );
                String targetMode = attributes.getValue( RELATIONSHIP_TARGET_MODE_ATTR );

                if ( hyperlinks == null )
                {
                    hyperlinks = new InitialHyperlinkMap();
                }
                hyperlinks.put( id, new HyperlinkInfo( id, target, targetMode ) );
            }
        }
    }

    /**
     * Returns Map of initial Relationship type of Hyperlink declared in the "word/_rels/document.xml.rels" and null if
     * there is no Hyperlink declared.
     * 
     * @return
     */
    public InitialHyperlinkMap getHyperlinks()
    {
        return hyperlinks;
    }
}

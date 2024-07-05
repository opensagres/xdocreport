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
package fr.opensagres.xdocreport.document.odt;

import static fr.opensagres.xdocreport.document.odt.ODTConstants.CONTENT_XML_ENTRY;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.METAINF_MANIFEST_XML_ENTRY;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.MIME_MAPPING;
import static fr.opensagres.xdocreport.document.odt.ODTConstants.STYLES_XML_ENTRY;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import fr.opensagres.xdocreport.converter.MimeMapping;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.core.io.IEntryOutputStreamProvider;
import fr.opensagres.xdocreport.core.io.IEntryReaderProvider;
import fr.opensagres.xdocreport.core.io.IEntryWriterProvider;
import fr.opensagres.xdocreport.core.io.XDocArchive;
import fr.opensagres.xdocreport.core.io.internal.ByteArrayOutputStream;
import fr.opensagres.xdocreport.document.AbstractXDocReport;
import fr.opensagres.xdocreport.document.images.IImageRegistry;
import fr.opensagres.xdocreport.document.odt.images.ODTImageRegistry;
import fr.opensagres.xdocreport.document.odt.preprocessor.ODTManifestXMLProcessor;
import fr.opensagres.xdocreport.document.odt.preprocessor.ODTPreprocessor;
import fr.opensagres.xdocreport.document.odt.preprocessor.ODTStylesPreprocessor;
import fr.opensagres.xdocreport.document.odt.template.ODTContextHelper;
import fr.opensagres.xdocreport.document.odt.textstyling.ODTDefaultStyle;
import fr.opensagres.xdocreport.template.IContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Open Office ODT report. For mime mapping please see {@see
 * http://framework.openoffice.org/documentation/mimetypes/mimetypes.html}.
 */
public class ODTReport
    extends AbstractXDocReport
{

    private static final long serialVersionUID = 5974669564624835649L;

    private static final String[] DEFAULT_XML_ENTRIES = { CONTENT_XML_ENTRY, STYLES_XML_ENTRY,
        METAINF_MANIFEST_XML_ENTRY };

    private ODTDefaultStyle defaultStyle;

    public ODTReport()
    {
        this.defaultStyle = new ODTDefaultStyle();
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.IXDocReport#getKind()
     */
    public String getKind()
    {
        return DocumentKind.ODT.name();
    }

    /*
     * (non-Javadoc)
     * @see fr.opensagres.xdocreport.document.IXDocReport#getMimeMapping()
     */
    public MimeMapping getMimeMapping()
    {
        return MIME_MAPPING;
    }

    @Override
    protected void registerPreprocessors()
    {
        // processor to modify content.xml
        super.addPreprocessor( CONTENT_XML_ENTRY, ODTPreprocessor.INSTANCE );
        // processor to modify META-INF/manifest.xml
        super.addPreprocessor( METAINF_MANIFEST_XML_ENTRY, ODTManifestXMLProcessor.INSTANCE );
        // processor to modify global styles
        super.addPreprocessor( STYLES_XML_ENTRY, ODTStylesPreprocessor.INSTANCE );
    }

    @Override
    protected void onBeforePreprocessing( Map<String, Object> sharedContext, XDocArchive preprocessedArchive )
        throws XDocReportException
    {
        super.onBeforePreprocessing( sharedContext, preprocessedArchive );
        // Default style
        sharedContext.put( ODTContextHelper.DEFAULT_STYLE_KEY, defaultStyle );
    }

    @Override
    protected void onBeforeProcessTemplateEngine( IContext context, XDocArchive outputArchive )
        throws XDocReportException
    {
        // 1) Register commons Java model in the context
        super.onBeforeProcessTemplateEngine( context, outputArchive );
        // 2) Register default style instance
        ODTContextHelper.putDefaultStyle( context, defaultStyle );
        // 3) Register styles generator if not exists.
        ODTContextHelper.getStylesGenerator( context ); 

    }

    @Override
    protected void doPostprocessIfNeeded( XDocArchive outputArchive )
        throws Exception
    {
        applyParentStyleToChildren(outputArchive);
    }

    /**
     * Text styling will style the nodes with some generated style such as XDocReport_Bold. These styles inherit
     * from the default paragraph styles so will ignore the style of the enclosing mail merge field. Another issue
     * was that nested styles would not stack. For example <b>bold <i>italic</i></b> would look <b>bold</b> <i>italic</i>
     * because unlike HTML and CSS, styling rules aren't inherited from enclosing tags.
     *
     * This post processing loops through each element in the document body and generate bespoke styles for each element
     * with an XDocReport style. This new style will inherit from the parent style but copy all the style rules from the
     * XDocReport style.
     *
     * @param outputArchive the output archive after processing the report. Any changes will be written back into this
     *                      archive.
     */
    private static void applyParentStyleToChildren( XDocArchive outputArchive )
        throws ParserConfigurationException, IOException, SAXException
    {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder xmlDocBuilder = documentBuilderFactory.newDocumentBuilder();

        // This is a document of the base styles for the document
        Document styleXML= xmlDocBuilder
          .parse(outputArchive.getEntryInputStream("styles.xml"));

        // This document contains the text content as well as a number of "automatic" styles for each element. We generate
        // a new automatic style for each text node with a XDocReport style.
        Document contentXML = xmlDocBuilder
          .parse(outputArchive.getEntryInputStream("content.xml"));


        // recursively apply the parent styles to child nodes
        applyParentStyleToChildren(contentXML, styleXML);

        // write the changes back into the archive
        ByteArrayOutputStream contentOutStream = new ByteArrayOutputStream();
        new XMLSerializer(contentOutStream, new OutputFormat(contentXML)).serialize(contentXML);
        XDocArchive.setEntry(outputArchive, "content.xml", new ByteArrayInputStream(contentOutStream.toByteArray()));
    }

    private static void applyParentStyleToChildren(Document contentXML,
                                                   Document styleXML )
    {
        // Get the list of styles under the office:styles element and add them to styles
        Element officeStyles = (Element) styleXML.getDocumentElement().getElementsByTagName("office:styles").item(0);
        NodeList styleNodeList = officeStyles.getElementsByTagName("style:style");
        Map<String, Element> styles = new HashMap<String, Element>();

        for (int i = 0; i < styleNodeList.getLength(); i++)
        {
            Element styleElement = (Element) styleNodeList.item(i);
            String styleName =  styleElement.getAttribute("style:name");
            styles.put(styleName, styleElement);
        }

        // Recurse through all the elements in the document body
        Element body = (Element) contentXML.getDocumentElement().getElementsByTagName("office:body").item(0);
        Map<String, Element> generatedStyles = new HashMap<String, Element>();
        updateChildStyles(body, null, styles, generatedStyles);

        // Copy the generated styles to the automatic style list in content.xml
        Element automaticStyles = (Element) contentXML.getDocumentElement().getElementsByTagName("office:automatic-styles").item(0);
        for (Element style : generatedStyles.values())
        {
            automaticStyles.appendChild(contentXML.importNode(style, true));
        }
    }

    private static void updateChildStyles(Element element,
                                          String parentStyleName,
                                          Map<String, Element> styles,
                                          Map<String, Element> generatedStyles )
    {
        String textNS = element.getOwnerDocument().lookupNamespaceURI("text");

        // Loop through each child node setting the style appropriately
        NodeList children = element.getChildNodes();
        for(int i = 0; i < children.getLength(); i++)
        {
            if (children.item(i) instanceof Element)
            {
                Element childElement = (Element) children.item(i);
                String nextParentStyleName = parentStyleName;
                if (childElement.hasAttribute("text:style-name"))
                {
                    String childStyleName = childElement.getAttribute("text:style-name");

                    // If this node is a XDocReport style, generate a new style with the XDocReport styling rules that inherits
                    // from parentStyleName
                    if (childStyleName.startsWith("XDocReport_") && parentStyleName != null)
                    {
                        String newStyleName = getOrCreateStyle(parentStyleName, styles.get(childStyleName), generatedStyles);
                        childElement.setAttributeNS(textNS, "text:style-name", newStyleName);

                        // Any child should inherit from this new style
                        nextParentStyleName = newStyleName;
                    }
                    else
                    {
                        // Any child should inherit from this elements style
                        nextParentStyleName = childStyleName;
                    }
                }

                // Finally we should update any children of this element
                updateChildStyles(childElement, nextParentStyleName, styles, generatedStyles);
            }
        }
    }

    private static String getOrCreateStyle(String parentStyleName, Element styleToMerge, Map<String, Element> newStyles)
    {
        String styleNsUri = styleToMerge.getOwnerDocument().lookupNamespaceURI("style");
        String styleToMergeName = styleToMerge.getAttribute("style:name");
        String mergedStyleName = parentStyleName + "_" + styleToMergeName;

        // if we've not already generated this style then copy the XDocReport style making it inherit from parentStyleName
        if (!newStyles.containsKey(mergedStyleName))
        {
            Element newStyle = (Element) styleToMerge.cloneNode(true);
            newStyle.setAttributeNS(styleNsUri, "style:name", mergedStyleName);
            newStyle.setAttributeNS(styleNsUri, "style:parent-style-name", parentStyleName);
            newStyles.put(mergedStyleName, newStyle);
        }

        return mergedStyleName;
    }

    @Override
    protected String[] getDefaultXMLEntries()
    {
        return DEFAULT_XML_ENTRIES;
    }

    @Override
    protected IImageRegistry createImageRegistry( IEntryReaderProvider readerProvider,
                                                  IEntryWriterProvider writerProvider,
                                                  IEntryOutputStreamProvider outputStreamProvider )
    {
        return new ODTImageRegistry( readerProvider, writerProvider, outputStreamProvider, getFieldsMetadata() );
    }
}

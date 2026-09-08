package fr.opensagres.xdocreport.document.odt;

import fr.opensagres.xdocreport.core.io.XDocArchive;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestODTPostProcessing
{
    class ElementWrapper
    {
        private Element mElement;
        ElementWrapper(Element pElement)
        {
            mElement = pElement;
        }

        private ElementWrapper getElement( String pTagName)
        {
            return new ElementWrapper((Element) mElement.getElementsByTagName(pTagName).item(0));
        }

        private ElementWrapper assertStyleName(String pName)
        {
            assertEquals(pName, getStyleName());
            return this;
        }

        private ElementWrapper assertAttributeEqual(String pAttributeName, String pExpectedValue)
        {
            assertEquals(pExpectedValue, mElement.getAttribute(pAttributeName));
            return this;
        }

        private String getStyleName()
        {
            return  mElement.getAttribute("text:style-name");
        }

        private List<ElementWrapper> getChildElements()
        {
            List<ElementWrapper> lChildren = new LinkedList<ElementWrapper>();
            NodeList lChildNodes = mElement.getChildNodes();

            for (int i = 0; i < lChildNodes.getLength(); i++)
            {
                if (lChildNodes.item(i) instanceof Element){
                    lChildren.add(new ElementWrapper((Element) lChildNodes.item(i)));
                }
            }

            return lChildren;
        }
    }

    private Document parseXMLInputStream(InputStream pInputStream)
        throws ParserConfigurationException, IOException, SAXException
    {
        DocumentBuilderFactory lDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        lDocumentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder lDocumentBuilder = lDocumentBuilderFactory.newDocumentBuilder();

        return lDocumentBuilder.parse(pInputStream);
    }

    private XDocArchive loadTestArchive()
        throws IOException, ParserConfigurationException
    {


        XDocArchive lXDocArchive = new XDocArchive();
        XDocArchive.setEntry(lXDocArchive, "content.xml", getClass().getResourceAsStream("postprocesstestxml/content.xml"));
        XDocArchive.setEntry(lXDocArchive, "styles.xml", getClass().getResourceAsStream("postprocesstestxml/styles.xml"));

        return lXDocArchive;
    }

    @Test
    public void testStyleNameGeneration()
        throws Exception
    {
        XDocArchive lXDocArchive = loadTestArchive();

        new ODTReport().doPostprocessIfNeeded(lXDocArchive);

        Document lContent = parseXMLInputStream(lXDocArchive.getEntryInputStream("content.xml"));

        new ElementWrapper(lContent.getDocumentElement())
            .getElement("office:body")
            .getElement("office:text")
            .getElement("text:p")
            .getElement("text:span")
            .assertStyleName("T2")
            .getElement("text:span")
            .assertStyleName("T2_XDocReport_Bold")
            .getElement("text:span")
            .assertStyleName("T2_XDocReport_Bold_XDocReport_Italic");
    }

    @Test
    public void testStyleT2BoldGeneration()
        throws Exception
    {
        XDocArchive lXDocArchive = loadTestArchive();
        new ODTReport().doPostprocessIfNeeded(lXDocArchive);

        Document lContent = parseXMLInputStream(lXDocArchive.getEntryInputStream("content.xml"));

        List<ElementWrapper> automaticStyles = new ElementWrapper(lContent.getDocumentElement())
            .getElement("office:automatic-styles")
            .getChildElements();

        ElementWrapper T2BoldStyle = null;
        for (ElementWrapper styleElement : automaticStyles)
        {
            if("T2_XDocReport_Bold".equals(styleElement.mElement.getAttribute("style:name")))
            {
                T2BoldStyle = styleElement;
            }
        }


        if (T2BoldStyle != null){
            T2BoldStyle
                .assertAttributeEqual("style:parent-style-name", "T2")
                .assertAttributeEqual("style:name", "T2_XDocReport_Bold")
                .getElement("style:text-properties")
                .assertAttributeEqual("fo:font-weight", "bold");
        }
        else
        {
            fail("Missing style T2_XDocReport_Bold");
        }
    }

    @Test
    public void testStyleT2BoldItalicGeneration()
        throws Exception
    {
        XDocArchive lXDocArchive = loadTestArchive();
        new ODTReport().doPostprocessIfNeeded(lXDocArchive);

        Document lContent = parseXMLInputStream(lXDocArchive.getEntryInputStream("content.xml"));

        List<ElementWrapper> automaticStyles = new ElementWrapper(lContent.getDocumentElement())
            .getElement("office:automatic-styles")
            .getChildElements();

        ElementWrapper T2BoldStyle = null;
        for (ElementWrapper styleElement : automaticStyles)
        {
            if("T2_XDocReport_Bold_XDocReport_Italic".equals(styleElement.mElement.getAttribute("style:name")))
            {
                T2BoldStyle = styleElement;
            }
        }


        if ( T2BoldStyle != null )
        {
            T2BoldStyle
                .assertAttributeEqual("style:parent-style-name", "T2_XDocReport_Bold")
                .assertAttributeEqual("style:name", "T2_XDocReport_Bold_XDocReport_Italic")
                .getElement("style:text-properties")
                .assertAttributeEqual("fo:font-style", "italic");
        }
        else
        {
            fail("Missing style T2_XDocReport_Bold_XDocReport_Italic");
        }
    }

}

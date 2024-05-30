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
package fr.opensagres.xdocreport.remoting.resources.domain;

import javax.xml.namespace.QName;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.opensagres.xdocreport.remoting.resources.domain package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Content_QNAME = new QName("http://domain.resources.remoting.xdocreport.opensagres.fr/", "content");
    private final static QName _ResourceId_QNAME = new QName("http://domain.resources.remoting.xdocreport.opensagres.fr/", "resourceId");
    private final static QName _BinaryData_QNAME = new QName("http://domain.resources.remoting.xdocreport.opensagres.fr/", "binaryData");
    private final static QName _Filter_QNAME = new QName("http://domain.resources.remoting.xdocreport.opensagres.fr/", "filter");
    private final static QName _Resource_QNAME = new QName("http://domain.resources.remoting.xdocreport.opensagres.fr/", "resource");
    private final static QName _DownloadMultipleResponse_QNAME = new QName("http://domain.resources.remoting.xdocreport.opensagres.fr/", "downloadMultipleResponse");
    private final static QName _DownloadMultiple_QNAME = new QName("http://domain.resources.remoting.xdocreport.opensagres.fr/", "downloadMultiple");
    private final static QName _Parameters_QNAME = new QName("http://domain.resources.remoting.xdocreport.opensagres.fr/", "parameters");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.opensagres.xdocreport.remoting.resources.domain
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BinaryData }
     * 
     */
    public BinaryData createBinaryData() {
        return new BinaryData();
    }

    /**
     * Create an instance of {@link Resource }
     * 
     */
    public Resource createResource() {
        return new Resource();
    }

    /**
     * Create an instance of {@link DownloadMultipleResponse }
     * 
     */
    public DownloadMultipleResponse createDownloadMultipleResponse() {
        return new DownloadMultipleResponse();
    }

    /**
     * Create an instance of {@link DownloadMultiple }
     * 
     */
    public DownloadMultiple createDownloadMultiple() {
        return new DownloadMultiple();
    }

    /**
     * Create an instance of {@link Filter }
     * 
     */
    public Filter createFilter() {
        return new Filter();
    }

    /**
     * Create an instance of {@link PropertyRepresentation }
     * 
     */
    public PropertyRepresentation createPropertyRepresentation() {
        return new PropertyRepresentation();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BinaryData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://domain.resources.remoting.xdocreport.opensagres.fr/", name = "content")
    public JAXBElement<BinaryData> createContent(BinaryData value) {
        return new JAXBElement<BinaryData>(_Content_QNAME, BinaryData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://domain.resources.remoting.xdocreport.opensagres.fr/", name = "resourceId")
    public JAXBElement<String> createResourceId(String value) {
        return new JAXBElement<String>(_ResourceId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BinaryData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://domain.resources.remoting.xdocreport.opensagres.fr/", name = "binaryData")
    public JAXBElement<BinaryData> createBinaryData(BinaryData value) {
        return new JAXBElement<BinaryData>(_BinaryData_QNAME, BinaryData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Filter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://domain.resources.remoting.xdocreport.opensagres.fr/", name = "filter")
    public JAXBElement<Filter> createFilter(Filter value) {
        return new JAXBElement<Filter>(_Filter_QNAME, Filter.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Resource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://domain.resources.remoting.xdocreport.opensagres.fr/", name = "resource")
    public JAXBElement<Resource> createResource(Resource value) {
        return new JAXBElement<Resource>(_Resource_QNAME, Resource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadMultipleResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://domain.resources.remoting.xdocreport.opensagres.fr/", name = "downloadMultipleResponse")
    public JAXBElement<DownloadMultipleResponse> createDownloadMultipleResponse(DownloadMultipleResponse value) {
        return new JAXBElement<DownloadMultipleResponse>(_DownloadMultipleResponse_QNAME, DownloadMultipleResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadMultiple }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://domain.resources.remoting.xdocreport.opensagres.fr/", name = "downloadMultiple")
    public JAXBElement<DownloadMultiple> createDownloadMultiple(DownloadMultiple value) {
        return new JAXBElement<DownloadMultiple>(_DownloadMultiple_QNAME, DownloadMultiple.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://domain.resources.remoting.xdocreport.opensagres.fr/", name = "parameters")
    public JAXBElement<String> createParameters(String value) {
        return new JAXBElement<String>(_Parameters_QNAME, String.class, null, value);
    }

}

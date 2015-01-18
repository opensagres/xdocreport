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
package fr.opensagres.xdocreport.document.preprocessor.sax;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Buffered document.
 */
public class BufferedDocument
    extends BufferedElement
{

    // Stack of element
    private final Stack<BufferedElement> elementsStack;

    public BufferedDocument()
    {
        super( null, null, null, null, null );
        this.elementsStack = new Stack<BufferedElement>();
    }

    // ----------------------- START element events ------------------------

    /**
     * On start of Start element.
     * 
     * @param uri
     * @param localName
     * @param name
     * @param attributes
     * @return
     * @throws SAXException
     */
    public BufferedElement onStartStartElement( String uri, String localName, String name, Attributes attributes )
        throws SAXException
    {
        // 1) Get parent element
        BufferedElement parent = getCurrentElement();
        if ( parent == null )
        {
            // Parent element is null, teh parent is the document
            parent = this;
        }
        // 2) Create element
        BufferedElement element = createElement( parent, uri, localName, name, attributes );
        // 3) Add the element in the elements stack
        elementsStack.push( element );
        // 4) Flag the element as start element
        element.start();
        // 5) Add the start tag element region in the parent element
        parent.addRegion( element.getStartTagElement() );
        return element;
    }

    /**
     * Create an element instance.
     * 
     * @param parent
     * @param uri
     * @param localName
     * @param name
     * @param attributes
     * @return
     * @throws SAXException
     */
    protected BufferedElement createElement( BufferedElement parent, String uri, String localName, String name,
                                             Attributes attributes )
        throws SAXException
    {
        return new BufferedElement( parent, uri, localName, name, attributes );
    }

    /**
     * On end of Start element.
     * 
     * @param element
     * @param uri
     * @param localName
     * @param name
     * @param attributes
     * @throws SAXException
     */
    public void onEndStartElement( BufferedElement element, String uri, String localName, String name,
                                   Attributes attributes )
        throws SAXException
    {
        // Do nothing
    }

    // ----------------------- END element events ------------------------

    /**
     * On start of End element.
     * 
     * @param uri
     * @param localName
     * @param name
     */
    public void onStartEndElement( String uri, String localName, String name )
    {
        // 1) get the current parsed element.
        BufferedElement element = getCurrentElement();
        // 2) Flag the element as end element
        element.end();
        // 3) Add the end tag element region in the parent element
        element.getParent().addRegion( element.getEndTagElement() );
    }

    /**
     * On end of End element.
     * 
     * @param uri
     * @param localName
     * @param name
     */
    public void onEndEndElement( String uri, String localName, String name )
    {
        // remove the current element from the stack.
        elementsStack.pop();
    }

    /**
     * Returns the current element from the stack and null otherwise.
     * 
     * @return
     */
    public BufferedElement getCurrentElement()
    {
        if ( !elementsStack.isEmpty() )
        {
            return elementsStack.peek();
        }
        return null;
    }

}

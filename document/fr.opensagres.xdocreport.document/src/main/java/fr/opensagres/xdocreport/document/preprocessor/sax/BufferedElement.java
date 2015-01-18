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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;

/**
 * Buffered element which stores start Tag element (ex: <a>) and end Tag element
 * (ex: </a>). The start Tag element stores too the {@link BufferedElement}
 * children.
 */
public class BufferedElement implements IBufferedRegion {

	private final BufferedElement parent;

	private final String name;

	private final String startTagElementName;

	private final String endTagElementName;

	protected final BufferedStartTagElement startTagElement;

	protected final BufferedEndTagElement endTagElement;

	private BufferedTagElement currentTagElement;

	private final Attributes attributes;

	private Collection<BufferedAttribute> dynamicAttributes = null;

	private boolean reseted;

	private Map<String, String> data;

	public BufferedElement(BufferedElement parent, String uri,
			String localName, String name, Attributes attributes) {
		this.parent = parent;
		this.name = name;
		this.startTagElementName = "<" + name + ">";
		this.endTagElementName = "</" + name + ">";
		this.attributes = attributes;
		this.startTagElement = new BufferedStartTagElement(this);
		this.endTagElement = new BufferedEndTagElement(this);
		this.currentTagElement = startTagElement;
		this.reseted = false;
	}

	/**
	 * Returns true if the given name match the name of this element and false
	 * otherwise.
	 * 
	 * @param name
	 * @return
	 */
	public boolean match(String name) {
		if (name == null) {
			return false;
		}
		return name.equals(this.name);
	}

	/**
	 * Returns the buffer of the start tag element.
	 * 
	 * @return
	 */
	public BufferedStartTagElement getStartTagElement() {
		return startTagElement;
	}

	/**
	 * Returns the buffer of the end tag element.
	 * 
	 * @return
	 */
	public BufferedEndTagElement getEndTagElement() {
		return endTagElement;
	}

	/**
	 * Set content on the before start tag element.
	 * 
	 * @param before
	 */
	public void setContentBeforeStartTagElement(String before) {
		this.startTagElement.setBefore(before);
	}

	/**
	 * Set content on the after end tag element.
	 * 
	 * @param before
	 */
	public void setContentAfterEndTagElement(String after) {
		this.endTagElement.setAfter(after);
	}

	/**
	 * Returns the parent buffered element of this element.
	 */
	public BufferedElement getParent() {
		return parent;
	}

	/**
	 * Returns the current tag element (start or end).
	 * 
	 * @return
	 */
	private BufferedTagElement getCurrentTagElement() {
		return currentTagElement;
	}

	/**
	 * Write the content of this element in the given writer.
	 */
	public void save(Writer writer) throws IOException {
		// Write start tag element content
		getStartTagElement().save(writer);
		// Write end tag element content
		getEndTagElement().save(writer);
	}

	/**
	 * Add a savable region in the current tag element.
	 */
	public void addRegion(ISavable region) {
		getCurrentTagElement().addRegion(region);
	}

	/**
	 * Returns false
	 */
	public boolean isString() {
		return getCurrentTagElement().isString();
	}

	/**
	 * Append content in the current tag element.
	 */
	public void append(String content) {
		getCurrentTagElement().append(content);
	}

	/**
	 * Append content in the current tag element.
	 */
	public void append(char[] ch, int start, int length) {
		getCurrentTagElement().append(ch, start, length);
	}

	/**
	 * Append content in the current tag element.
	 */
	public void append(char c) {
		getCurrentTagElement().append(c);
	}

	/**
	 * Reset the whole content of the element.
	 */
	public void reset() {
		startTagElement.reset();
		endTagElement.reset();
		this.reseted = true;
	}

	public boolean isReseted() {
		return reseted;
	}

	/**
	 * Remove the collection of element.
	 * 
	 * @param elements
	 */
	public void removeAll(Collection<BufferedElement> elements) {
		BufferedTagElement tagElement = null;
		for (BufferedElement element : elements) {
			// remove start tag element
			tagElement = element.getStartTagElement();
			if (tagElement != null) {
				getStartTagElement().regions.remove(tagElement);
			}
			// remove end tag element
			tagElement = element.getEndTagElement();
			if (tagElement != null) {
				getStartTagElement().regions.remove(tagElement);
			}
		}
	}

	@Override
	public String toString() {
		StringWriter writer = new StringWriter();
		try {
			save(writer);
		} catch (IOException e) {
			// Do nothing
		}
		return writer.toString();
	}

	/**
	 * Returns the owner element.
	 */
	public BufferedElement getOwnerElement() {
		return this;
	}

	/**
	 * Returns the parent element of this element which match the given name.
	 * 
	 * @param element
	 * @param name
	 * @return
	 */
	public BufferedElement findParent(String name) {
		return findParent(this, name);
	}

	/**
	 * Returns the parent element of the given element which match the given
	 * name.
	 * 
	 * @param element
	 * @param name
	 * @return
	 */
	public BufferedElement findParent(BufferedElement element, String name) {
		if (element == null) {
			return null;
		}
		if (element.match(name)) {
			return element;
		}
		return findParent(element.getParent(), name);
	}

	/**
	 * Returns the children element of this element which match the given name.
	 * 
	 * @param name
	 * @return
	 */
	public List<BufferedElement> findChildren(String name) {
		return findChildren(this, name);
	}

	/**
	 * Returns the children element of the given element which match the given
	 * name.
	 * 
	 * @param element
	 * @param name
	 * @return
	 */
	public List<BufferedElement> findChildren(BufferedElement element,
			String name) {
		List<BufferedElement> elements = new ArrayList<BufferedElement>();
		List<ISavable> regions = element.getStartTagElement().regions;
		findChildren(regions, name, elements);
		return elements;
	}

	/**
	 * Returns the first child element of the given element which match the
	 * given name and null otherwise.
	 * 
	 * @param name
	 * @return
	 */
	public BufferedElement findFirstChild(String name) {
		return findFirstChild(this, name);
	}

	/**
	 * Returns the first child element of this element which match the given
	 * name and null otherwise.
	 * 
	 * @param name
	 * @return
	 */
	public BufferedElement findFirstChild(BufferedElement element, String name) {
		return findChildAt(element, name, 0);
	}

	/**
	 * Returns the first child element of the given element which match the
	 * given name and null otherwise.
	 * 
	 * @param name
	 * @return
	 */
	public BufferedElement findChildAt(String name, int index) {
		return findChildAt(this, name, index);
	}

	/**
	 * Returns the first child element of this element which match the given
	 * name and null otherwise.
	 * 
	 * @param name
	 * @return
	 */
	public BufferedElement findChildAt(BufferedElement element, String name,
			int index) {
		List<BufferedElement> elements = findChildren(element, name);
		if (index < elements.size()) {
			return elements.get(index);
		}
		return null;
	}

	/**
	 * Populate list elements with children element which match the given name.
	 * 
	 * @param regions
	 * @param name
	 * @param elements
	 */
	private void findChildren(List<ISavable> regions, String name,
			List<BufferedElement> elements) {
		for (ISavable region : regions) {
			if (region instanceof IBufferedRegion) {
				IBufferedRegion r = (IBufferedRegion) region;
				if (r.getOwnerElement().match(name)
						&& !elements.contains(r.getOwnerElement())) {
					elements.add(r.getOwnerElement());
				}
				if (r instanceof BufferedRegion) {
					findChildren(((BufferedRegion) r).regions, name, elements);
				}
			}
		}
	}

	/**
	 * Set the current buffer with start tag element.
	 */
	public void start() {
		this.currentTagElement = startTagElement;
	}

	/**
	 * Set the current buffer with end tag element.
	 */
	public void end() {
		this.currentTagElement = endTagElement;
	}

	/**
	 * Returns true if current buffer is end tag element and false otherwise.
	 * 
	 * @return
	 */
	public boolean isEnded() {
		return this.currentTagElement == endTagElement;
	}

	/**
	 * Returns the name of this start tag element (ex : <w:t>).
	 * 
	 * @return
	 */
	public String getStartTagElementName() {
		return startTagElementName;
	}

	/**
	 * Returns the name of this end tag element (ex : </w:t>).
	 * 
	 * @return
	 */
	public String getEndTagElementName() {
		return endTagElementName;
	}

	/**
	 * Set text content for this element.
	 * 
	 * @param content
	 */
	public void setTextContent(String content) {
		if (isEnded()) {
			// end tag element is parsed (ex: <w:t>XXXX</w:t>), reset the buffer
			// and rebuild the buffer with the new content.
			reset();
			getStartTagElement().append(getStartTagElementName());
			getStartTagElement().append(content);
			getEndTagElement().append(getEndTagElementName());
		} else {
			// end tag element is not parsed (ex: <w:t>) append teh content.
			getCurrentTagElement().append(content);
		}
	}

	/**
	 * Returns the text content for this element.
	 * 
	 * @return
	 */
	public String getTextContent() {
		// get content of the element (ex : <w:t>XXXX</w:t>)
		StringWriter writer = new StringWriter();
		try {
			save(writer);
		} catch (IOException e) {

			e.printStackTrace();
		}
		// remove the start/end tag ex : XXXX)
		String textContent = writer.toString();
		if (textContent.startsWith(getStartTagElementName())) {
			textContent = textContent.substring(getStartTagElementName()
					.length(), textContent.length());
		}
		if (textContent.endsWith(getEndTagElementName())) {
			textContent = textContent.substring(0, textContent.length()
					- getStartTagElementName().length() - 1);
		}
		return textContent;
	}

	/**
	 * Returns the static SAX attributes.
	 * 
	 * @return
	 */
	public Attributes getAttributes() {
		return attributes;
	}

	/**
	 * Register dynamic attributes if needed.
	 */
	public void registerDynamicAttributes() {
		if (dynamicAttributes == null) {
			return;
		}
		for (BufferedAttribute attribute : dynamicAttributes) {
			getCurrentTagElement().addRegion(attribute);
		}
	}

	/**
	 * Set dynamic attribute.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public BufferedAttribute setAttribute(String name, String value) {
		if (dynamicAttributes == null) {
			dynamicAttributes = new ArrayList<BufferedAttribute>();
		}
		BufferedAttribute attribute = new BufferedAttribute(this, name, value);
		dynamicAttributes.add(attribute);
		return attribute;
	}

	public String getName() {
		return name;
	}

	public String getInnerText() {
		StringWriter writer = new StringWriter();
		List<ISavable> regions = startTagElement.regions;
		boolean startTagParsing = true;
		for (ISavable region : regions) {
			if (startTagParsing) {
				if (region instanceof BufferedStartTagElement) {
					startTagParsing = false;
				}
			}
			if (!startTagParsing) {
				try {
					region.save(writer);
				} catch (IOException e) {
					// Should never thrown.
				}
			}
		}
		return writer.toString();
	}

	public void setInnerText(String innerText) {
		List<ISavable> regionsToAdd = new ArrayList<ISavable>();
		List<ISavable> regions = startTagElement.regions;
		boolean startTagParsing = true;
		for (ISavable region : regions) {
			startTagParsing = !(region instanceof BufferedStartTagElement);
			if (startTagParsing) {
				regionsToAdd.add(region);
			} else {
				break;
			}
		}
		startTagElement.reset();
		startTagElement.regions.addAll(regionsToAdd);
		startTagElement.append(innerText);

	}

	public String get(String key) {
		if (data == null) {
			return null;
		}
		return data.get(key);
	}

	public void put(String key, String value) {
		if (data == null) {
			data = new HashMap<String, String>();
		}
		data.put(key, value);
	}

}

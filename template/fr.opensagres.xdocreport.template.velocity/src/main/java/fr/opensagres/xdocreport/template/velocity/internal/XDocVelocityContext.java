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
package fr.opensagres.xdocreport.template.velocity.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.velocity.VelocityContext;

import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.utils.TemplateUtils;

/**
 * Velocity context.
 */
public class XDocVelocityContext extends VelocityContext implements IContext {

	public XDocVelocityContext() {
		super();
	}

	public XDocVelocityContext(Map<String, Object> contextMap) {
		super(contextMap);
	}

	/**
	 * Overridden so that the <code>null</code> values are accepted.
	 * 
	 * @see AbstractVelocityContext#put(String,Object)
	 */
	public Object put(String key, Object value) {
		if (key == null) {
			return null;
		}
		Object result = TemplateUtils.putContextForDottedKey(this, key, value);
		if (result == null) {
			return super.internalPut(key, value);
		}
		return result;
	}

	//@Override
	public void putMap(Map<String, Object> contextMap) {
		Set<Entry<String, Object>> entries = contextMap.entrySet();
		for (Entry<String, Object> entry : entries) {
			put(entry.getKey(), entry.getValue());
		}
	}

	//@Override
	public Map<String, Object> getContextMap() {
		Map<String, Object> contextMap = new HashMap<String, Object>();
		Object[] keys = super.getKeys();
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] != null) {
				String key = keys[i].toString();
				contextMap.put(key, get(key));
			}
		}
		return contextMap;
	}

}

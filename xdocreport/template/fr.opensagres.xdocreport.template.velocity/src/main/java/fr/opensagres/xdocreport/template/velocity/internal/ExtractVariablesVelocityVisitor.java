/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
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

import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.visitor.BaseVisitor;

import fr.opensagres.xdocreport.template.FieldsExtractor;

public class ExtractVariablesVelocityVisitor extends BaseVisitor {

	private final FieldsExtractor extractor;

	public ExtractVariablesVelocityVisitor(FieldsExtractor extractor) {
		this.extractor = extractor;
	}

	@Override
	public Object visit(ASTReference node, Object data) {
		String variableName = node.literal();
		if (variableName.startsWith("$")) {
			variableName = variableName.substring(1, variableName.length());
		}
		extractor.addFieldName(variableName);
		return super.visit(node, data);
	}
}

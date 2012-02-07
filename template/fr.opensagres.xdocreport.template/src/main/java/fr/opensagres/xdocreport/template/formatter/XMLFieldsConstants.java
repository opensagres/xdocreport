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
package fr.opensagres.xdocreport.template.formatter;

public class XMLFieldsConstants
{

    public static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

    // Attributes
    public static final String NAME_ATTR = "name";

    public static final String LIST_ATTR = "list";

    public static final String IMAGE_NAME_ATTR = "imageName";

    public static final String SYNTAX_KIND_ATTR = "syntaxKind";

    public static final String TEMPLATE_ENGINE_KIND_ATTR = "templateEngineKind";

    public static final String HAS_DIRECTIVE = "hasDirective";
    
    // Elements
    public static final String FIELDS_ELT = "fields";

    public static final String FIELD_ELT = "field";

    public static final String DESCRIPTION_ELT = "description";

    public static final String START_CDATA = "<![CDATA[";

    public static final String END_CDATA = "]]>";

    public static final String FIELDS_TAG_START_ELT = "<" + FIELDS_ELT;

    public static final String FIELDS_END_ELT = "</" + FIELDS_ELT + ">";

    public static final String FIELD_TAG_START_ELT = "<" + FIELD_ELT;

    public static final String FIELD_END_ELT = "</" + FIELD_ELT + ">";

    public static final String DESCRIPTION_START_ELT = "<" + DESCRIPTION_ELT + ">";

    public static final String DESCRIPTION_END_ELT = "</" + DESCRIPTION_ELT + ">";
    
}

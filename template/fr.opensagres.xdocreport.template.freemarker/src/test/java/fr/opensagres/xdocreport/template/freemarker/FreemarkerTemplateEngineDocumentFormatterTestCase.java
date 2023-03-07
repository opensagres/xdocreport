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
package fr.opensagres.xdocreport.template.freemarker;

import junit.framework.TestCase;

public class FreemarkerTemplateEngineDocumentFormatterTestCase
    extends TestCase
{

    public void testFormatAsFieldItemList()
        throws Exception
    {
        FreemarkerDocumentFormatter formatter = new FreemarkerDocumentFormatter();
        String fieldName = formatter.formatAsFieldItemList( "${cds.reference}", "cds.reference", false );
        assertEquals( "${item_cds.reference}", fieldName );
    }
    
    /**
     * Checks if syntaxWithDirective is passed as boolean
     * @throws Exception 
     */
    public void testFormatAsCallTextStylingIsSyntaxWithDirectiveBoolean()
            throws Exception
    {
        FreemarkerDocumentFormatter formatter = new FreemarkerDocumentFormatter();
        String textStylingCall = formatter.formatAsCallTextStyling(0,"textHtml","documentKind","Html",true,"0_elementId","content.xml");
        assertEquals( "[#assign ___NoEscape0=___TextStylingRegistry.transform(textHtml,\"Html\",true,\"documentKind\",\"0_elementId\",___context,\"content.xml\")]", textStylingCall);
    }  
    
    /**
     * Checks if tagContent is instruction
     * @throws Exception 
     */
    public void testIsInstruction()
            throws Exception
    {
        FreemarkerDocumentFormatter formatter = new FreemarkerDocumentFormatter();
        boolean isInstruction = formatter.isInstruction("[#list d as cds.reference]");
        assertTrue(isInstruction);
        isInstruction = formatter.isInstruction("[#if d == \"freemarker\"]");
        assertTrue(isInstruction);
        isInstruction = formatter.isInstruction("d");
        assertFalse(isInstruction);
    } 
}

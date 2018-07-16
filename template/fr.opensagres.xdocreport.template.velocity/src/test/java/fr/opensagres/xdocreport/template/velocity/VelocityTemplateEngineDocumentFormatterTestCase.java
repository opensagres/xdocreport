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
package fr.opensagres.xdocreport.template.velocity;

import junit.framework.TestCase;

public class VelocityTemplateEngineDocumentFormatterTestCase
    extends TestCase
{

    public void testFormatAsFieldItemList()
        throws Exception
    {
        VelocityDocumentFormatter formatter = new VelocityDocumentFormatter();
        String fieldName = formatter.formatAsFieldItemList( "$cds.reference", "cds.reference" );
        assertEquals( "$item_cds.reference", fieldName );
    }

    public void testFormatAsFieldItemListEnclosed()
            throws Exception
    {
        VelocityDocumentFormatter formatter = new VelocityDocumentFormatter();
        String fieldName = formatter.formatAsFieldItemList( "${cds.reference}", "cds.reference"  );
        assertEquals( "${item_cds.reference}", fieldName );
    }

    public void testFormatAsFieldItemListNullable()
            throws Exception
    {
        VelocityDocumentFormatter formatter = new VelocityDocumentFormatter();
        String fieldName = formatter.formatAsFieldItemList( "$!cds.reference", "cds.reference" );
        assertEquals( "$!item_cds.reference", fieldName );
    }

    public void testFormatAsFieldItemListThree()
            throws Exception
    {
        VelocityDocumentFormatter formatter = new VelocityDocumentFormatter();
        String fieldName = formatter.formatAsFieldItemList( "$!cds.reference$cds.reference${cds.reference}", "cds.reference" );
        assertEquals( "$!item_cds.reference$item_cds.reference${item_cds.reference}", fieldName );
    }

    public void testFormatAsFieldItemListThreeSeparated()
            throws Exception
    {
        VelocityDocumentFormatter formatter = new VelocityDocumentFormatter();
        String fieldName = formatter.formatAsFieldItemList( "$!cds.reference $cds.reference ${cds.reference}", "cds.reference" );
        assertEquals( "$!item_cds.reference $item_cds.reference ${item_cds.reference}", fieldName );
    }

    public void testFormatAsFieldItemListThreeSeparated_and_directive()
            throws Exception
    {
        VelocityDocumentFormatter formatter = new VelocityDocumentFormatter();
        String fieldName = formatter.formatAsFieldItemList( "Before loop #foreach($d in $cds.reference)$!cds.reference#end $cds.reference ${cds.reference}", "cds.reference" );
        assertEquals( "Before loop #foreach($d in $item_cds.reference)$!item_cds.reference#end $item_cds.reference ${item_cds.reference}", fieldName );
    }
}

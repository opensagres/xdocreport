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

import fr.opensagres.xdocreport.template.FieldExtractor;
import fr.opensagres.xdocreport.template.FieldsExtractor;
import org.junit.Assert;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

public class VelocityFieldsExtractorTestCase

{

    @Test
    public void testExtractVariables()
            throws Exception
    {
        Reader reader = new StringReader( "Hello $name!" );
        FieldsExtractor<FieldExtractor> extractor = FieldsExtractor.create();
        VelocityFieldsExtractor.getInstance().extractFields( reader, "hello", extractor );
        Assert.assertEquals( 1, extractor.getFields().size() );
        Assert.assertEquals( "name", extractor.getFields().get( 0 ).getName() );
    }

    @Test
    public void testExtractVariableInList()
        throws Exception
    {
        Reader reader = new StringReader( "#foreach($d in $developers) $d #end" );
        FieldsExtractor<FieldExtractor> extractor = FieldsExtractor.create();
        VelocityFieldsExtractor.getInstance().extractFields( reader, "hello", extractor );
        Assert.assertEquals( 1, extractor.getFields().size() );
        Assert.assertEquals( "developers", extractor.getFields().get( 0 ).getName() );
    }

    @Test
    public void testExtractVariableInListWithFieldName()
        throws Exception
    {
        Reader reader = new StringReader( "#foreach($d in $developers) $d.Name $d.LastName #end" );
        FieldsExtractor<FieldExtractor> extractor = FieldsExtractor.create();
        VelocityFieldsExtractor.getInstance().extractFields( reader, "hello", extractor );
        Assert.assertEquals( 2, extractor.getFields().size() );
        Assert.assertEquals( "developers.Name", extractor.getFields().get( 0 ).getName() );
        Assert.assertTrue( extractor.getFields().get( 0 ).isList() );
        Assert.assertEquals( "developers.LastName", extractor.getFields().get( 1 ).getName() );
        Assert.assertTrue( extractor.getFields().get( 1 ).isList() );
    }

    @Test
    public void testExtractVariableInListWithFieldNameAndSimpleField()
        throws Exception
    {
        Reader reader = new StringReader( "#foreach($d in $developers) $d.Name $d.LastName $user #end" );
        FieldsExtractor<FieldExtractor> extractor = FieldsExtractor.create();
        VelocityFieldsExtractor.getInstance().extractFields( reader, "hello", extractor );
        Assert.assertEquals( 3, extractor.getFields().size() );
        Assert.assertEquals( "developers.Name", extractor.getFields().get( 0 ).getName() );
        Assert.assertTrue( extractor.getFields().get( 0 ).isList() );
        Assert.assertEquals( "developers.LastName", extractor.getFields().get( 1 ).getName() );
        Assert.assertTrue( extractor.getFields().get( 1 ).isList() );
        Assert.assertEquals( "user", extractor.getFields().get( 2 ).getName() );
        Assert.assertFalse( extractor.getFields().get( 2 ).isList() );
    }
}

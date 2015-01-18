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

import java.io.Reader;
import java.io.StringReader;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import fr.opensagres.xdocreport.template.FieldExtractor;
import fr.opensagres.xdocreport.template.FieldsExtractor;

public class FreemarkerTemplateEngineExtractVariablesTestCase

{

    @Test
    public void testExtractVariables()
        throws Exception
    {
        Reader reader = new StringReader( "Hello ${name}!" );
        FieldsExtractor<FieldExtractor> extractor = FieldsExtractor.create();
        new FreemarkerTemplateEngine().extractFields( reader, "hello", extractor );
        Assert.assertEquals( 1, extractor.getFields().size() );
        Assert.assertEquals( "name", extractor.getFields().get( 0 ).getName() );
    }

    @Ignore( "How to do that with Freemarker?" )
    @Test
    public void testExtractVariableInList()
        throws Exception
    {
        Reader reader = new StringReader( "[#list developers as d] ${d} [/#list]" );
        FieldsExtractor<FieldExtractor> extractor = FieldsExtractor.create();
        new FreemarkerTemplateEngine().extractFields( reader, "hello", extractor );
        Assert.assertEquals( 1, extractor.getFields().size() );
        Assert.assertEquals( "developers", extractor.getFields().get( 0 ).getName() );
    }

    @Ignore( "How to do that with Freemarker?" )
    @Test
    public void testExtractVariableInListWithFieldName()
        throws Exception
    {
        Reader reader = new StringReader( "[#list developers as d] ${d.name} ${d.lastName} [/#list]" );
        FieldsExtractor<FieldExtractor> extractor = FieldsExtractor.create();
        new FreemarkerTemplateEngine().extractFields( reader, "hello", extractor );
        Assert.assertEquals( 2, extractor.getFields().size() );
        Assert.assertEquals( "developers.name", extractor.getFields().get( 0 ).getName() );
        Assert.assertTrue( extractor.getFields().get( 0 ).isList() );
        Assert.assertEquals( "developers.lastName", extractor.getFields().get( 1 ).getName() );
        Assert.assertTrue( extractor.getFields().get( 1 ).isList() );
    }

    @Ignore( "How to do that with Freemarker?" )
    @Test
    public void testExtractVariableInListWithFieldNameAndSimpleField()
        throws Exception
    {
        Reader reader = new StringReader( "[#list developers as d] ${d.name} ${d.lastName} ${user} [/#list]" );
        FieldsExtractor<FieldExtractor> extractor = FieldsExtractor.create();
        new FreemarkerTemplateEngine().extractFields( reader, "hello", extractor );
        Assert.assertEquals( 3, extractor.getFields().size() );
        Assert.assertEquals( "developers.name", extractor.getFields().get( 0 ).getName() );
        Assert.assertTrue( extractor.getFields().get( 0 ).isList() );
        Assert.assertEquals( "developers.lastName", extractor.getFields().get( 1 ).getName() );
        Assert.assertEquals( "user", extractor.getFields().get( 2 ).getName() );
        Assert.assertFalse( extractor.getFields().get( 2 ).isList() );
    }
    
    @Test
    public void testExtractVariablesWithNoParse()
        throws Exception
    {
        Reader reader = new StringReader( "Hello [#noparse]${escaped_name}![/#noparse]${name}" );
        FieldsExtractor<FieldExtractor> extractor = FieldsExtractor.create();
        new FreemarkerTemplateEngine().extractFields( reader, "hello", extractor );
        Assert.assertEquals( 1, extractor.getFields().size() );
        Assert.assertEquals( "name", extractor.getFields().get( 0 ).getName() );
    }

}

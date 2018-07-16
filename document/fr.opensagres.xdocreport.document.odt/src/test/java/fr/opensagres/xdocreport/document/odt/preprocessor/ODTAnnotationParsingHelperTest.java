package fr.opensagres.xdocreport.document.odt.preprocessor;

import junit.framework.TestCase;

/**
 * JUnit test for {@link ODTAnnotationParsingHelper}.
 *
 * <p>Created on 2018-07-06</p>
 *
 * @author <a href="mailto:marcin.golebski@verbis.pl">Marcin Golebski</a>
 * @version $Id$
 */
public class ODTAnnotationParsingHelperTest extends TestCase
{
    private ODTAnnotationParsingHelper helper;

    public ODTAnnotationParsingHelperTest()
    {
        helper = new ODTAnnotationParsingHelper();
    }

    @Override
    protected void setUp() throws Exception
    {
        helper.setParsingBegin(null, 0);
    }

    public void testParse1() throws Exception
    {
        helper.append("#set($i='abc')\n");
        helper.append("@before#foreach($j in ['a','b','c'])");
        helper.append("@after#end");
        helper.setParsingEnd();

        assertTrue(helper.hasReplacement());
        assertEquals("#set($i='abc')\n", helper.getReplacement());
        assertTrue(helper.hasBefore());
        assertEquals("#foreach($j in ['a','b','c'])", helper.getBefore());
        assertTrue(helper.hasAfter());
        assertEquals("#end", helper.getAfter());
    }

    public void testParse2() throws Exception
    {
        helper.append("#set($i='abc')\n");
        helper.append("@after#end");
        helper.append("@before#foreach($j in ['a','b','c'])");
        helper.setParsingEnd();

        assertTrue(helper.hasReplacement());
        assertEquals("#set($i='abc')\n", helper.getReplacement());
        assertTrue(helper.hasBefore());
        assertEquals("#foreach($j in ['a','b','c'])", helper.getBefore());
        assertTrue(helper.hasAfter());
        assertEquals("#end", helper.getAfter());
    }

    public void testParse3() throws Exception
    {
        helper.append("@after#end");
        helper.append("@before#foreach($j in ['a','b','c'])");
        helper.setParsingEnd();

        assertFalse(helper.hasReplacement());
        assertTrue(helper.hasBefore());
        assertEquals("#foreach($j in ['a','b','c'])", helper.getBefore());
        assertTrue(helper.hasAfter());
        assertEquals("#end", helper.getAfter());
    }

    public void testParse4() throws Exception
    {
        helper.append("@before#foreach($j in ['a','b','c'])");
        helper.append("@after#end");
        helper.setParsingEnd();

        assertFalse(helper.hasReplacement());
        assertTrue(helper.hasBefore());
        assertEquals("#foreach($j in ['a','b','c'])", helper.getBefore());
        assertTrue(helper.hasAfter());
        assertEquals("#end", helper.getAfter());
    }

    public void testParse5() throws Exception
    {
        helper.append("#set($i='abc')\n");
        helper.append("@after#end");
        helper.setParsingEnd();

        assertTrue(helper.hasReplacement());
        assertEquals("#set($i='abc')\n", helper.getReplacement());
        assertFalse(helper.hasBefore());
        assertTrue(helper.hasAfter());
        assertEquals("#end", helper.getAfter());
    }

    public void testParse6() throws Exception
    {
        helper.append("#set($i='abc')\n");
        helper.append("@before#foreach($j in ['a','b','c'])");
        helper.setParsingEnd();

        assertTrue(helper.hasReplacement());
        assertEquals("#set($i='abc')\n", helper.getReplacement());
        assertTrue(helper.hasBefore());
        assertEquals("#foreach($j in ['a','b','c'])", helper.getBefore());
        assertFalse(helper.hasAfter());
    }

    public void testParse7() throws Exception
    {
        helper.append("#set($i='abc')\n");
        helper.setParsingEnd();

        assertTrue(helper.hasReplacement());
        assertEquals("#set($i='abc')\n", helper.getReplacement());
        assertFalse(helper.hasBefore());
        assertFalse(helper.hasAfter());
    }

    public void testParse8() throws Exception
    {
        helper.append("@before#foreach($j in ['a','b','c'])");
        helper.setParsingEnd();

        assertFalse(helper.hasReplacement());
        assertTrue(helper.hasBefore());
        assertEquals("#foreach($j in ['a','b','c'])", helper.getBefore());
        assertFalse(helper.hasAfter());
    }

    public void testParse9() throws Exception
    {
        helper.append("@after#end");
        helper.setParsingEnd();

        assertFalse(helper.hasReplacement());
        assertFalse(helper.hasBefore());
        assertTrue(helper.hasAfter());
        assertEquals("#end", helper.getAfter());
    }

    public void testIsNotReplacedYet() throws Exception
    {
        assertTrue(helper.isNotReplacedYet());
        helper.setReplacementDone();
        assertFalse(helper.isNotReplacedYet());
    }

    public void testIsParsing() throws Exception
    {
        helper.setParsingEnd();
        assertFalse(helper.isParsing());
        helper.setParsingBegin("test1", 10);
        assertTrue(helper.isTheSameBlock(11));
        assertFalse(helper.isTheSameBlock(9));
        assertTrue(helper.isParsing());
        helper.setParsingEnd();
        assertFalse(helper.isParsing());
        assertTrue(helper.isRangeAnnotation());
        helper.resetRangeAnnotation("test1_wrongname", false);
        assertTrue(helper.isRangeAnnotation());
        helper.resetRangeAnnotation("test1", false);
        assertFalse(helper.isRangeAnnotation());
    }

    public void testIsRangeAnnotation() throws Exception
    {
        assertFalse(helper.isRangeAnnotation());
    }

}

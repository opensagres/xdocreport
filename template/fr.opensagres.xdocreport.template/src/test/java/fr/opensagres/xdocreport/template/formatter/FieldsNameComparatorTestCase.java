package fr.opensagres.xdocreport.template.formatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class FieldsNameComparatorTestCase
{

    @Test
    public void testname()
        throws Exception
    {
        List<String> metadatas = new ArrayList<String>();
        metadatas.add( "test" );
        metadatas.add( "test3" );
        metadatas.add( "test1" );
        metadatas.add( "aaatest3" );

        Collections.sort( metadatas, FieldsNameComparator.getInstance() );

        Assert.assertEquals( "test3", metadatas.get( 0 ) );
        Assert.assertEquals( "test1", metadatas.get( 1 ) );
        Assert.assertEquals( "test", metadatas.get( 2 ) );
        Assert.assertEquals( "aaatest3", metadatas.get( 3 ) );
    }

}

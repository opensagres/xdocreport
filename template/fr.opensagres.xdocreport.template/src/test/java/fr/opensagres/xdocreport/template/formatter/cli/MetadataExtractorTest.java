package fr.opensagres.xdocreport.template.formatter.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class MetadataExtractorTest
{

    @Test
    public void testMain()
    {
        String[] args = { "-rootClass", "fr.opensagres.xdocreport.template.formatter.cli.Project" };
        MetadataExtractor.main( args );
        // fail("Not yet implemented");
    }

}

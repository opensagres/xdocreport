package fr.opensagres.xdocreport.document.docx.images;

import org.junit.Test;

import junit.framework.TestCase;

public class DocxImageRegistryTestCase extends TestCase{
	
	@Test
	public void test(){
		
		DocxImageRegistry imageRegistry = new DocxImageRegistry(null, null, null, null);
		Float result = imageRegistry.getSize("123456");
		assertNotNull("Result should not be null", result);
		assertTrue("Result should be equal to 12.961", Math.abs( result.floatValue() - 12.961) < 10E-3);
		
		
		result = imageRegistry.getSize("123cm");
		assertNull("Currently only valid fload can be parsed", result);
		
		
		result = imageRegistry.getSize(null);
		assertNull("No error should happen", result);
	}
}

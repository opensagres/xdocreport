package fr.opensagres.xdocreport.document.odt.images;

import org.junit.Test;

import junit.framework.TestCase;

public class ODTImageRegistryTestCase extends TestCase{
	
	@Test
	public void test(){
		
		ODTImageRegistry imageRegistry = new ODTImageRegistry(null, null, null, null);
		Float result = imageRegistry.getSize("123");
		assertNotNull("Result should not be null", result);
		assertTrue("Result should be equal to 164", Math.abs( result.floatValue() - 164.0) < 10E-3);
		
		
		result = imageRegistry.getSize("123cm");
		assertNull("Currently only valid fload can be parsed", result);
		
		
		result = imageRegistry.getSize(null);
		assertNull("No error should happen", result);
	}
}

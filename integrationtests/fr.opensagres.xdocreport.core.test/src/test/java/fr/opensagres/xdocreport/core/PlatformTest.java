package fr.opensagres.xdocreport.core;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class PlatformTest {

	
	@Test
	public void getVersion() throws Exception {
		
		String version = Platform.getVersion();
		
		Assert.assertTrue("version should start wuth 1.", version.startsWith("1."));
	}
}

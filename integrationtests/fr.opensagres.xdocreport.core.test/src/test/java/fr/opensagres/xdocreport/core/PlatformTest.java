package fr.opensagres.xdocreport.core;

import org.junit.Assert;
import org.junit.Test;

public class PlatformTest {

	
	@Test
	public void getVersion() throws Exception {
		String version = Platform.getVersion();
		Assert.assertTrue("version should start with 1 but is "+version, version.startsWith("1."));
	}
}

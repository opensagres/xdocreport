package fr.opensagres.xdocreport.core;

import org.junit.Assert;
import org.junit.Test;

public class PlatformTest {

	
	@Test
	public void getVersion() throws Exception {
		String version = Platform.getVersion();
		System.out.println(version);
		Assert.assertTrue("version should start with 1.", version.startsWith("1."));
	}
}

package org.platform.utils.system;

import java.util.Properties;

import org.junit.Test;
import org.platform.utils.resource.ResourceManager;

public class ResourceTest {

	@Test
	public void testResource() {
		Properties props = ResourceManager.getProperties("jdbc/jdbc.properties");
		boolean initialTable = Boolean.parseBoolean(props.getProperty("initialTable"));
		boolean initialData = Boolean.parseBoolean(props.getProperty("initialData"));
		System.out.println(initialTable);
		System.out.println(initialData);
		System.out.println(ResourceManager.getAbsolutePath("jdbc/jdbc.properties"));
	}
	
}

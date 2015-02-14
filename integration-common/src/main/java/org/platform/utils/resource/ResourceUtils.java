package org.platform.utils.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(ResourceUtils.class);
	
	public static String getWebRootAbsolutePath() {
		String classpath = ResourceUtils.class.getClassLoader().getResource("").getPath();
		LOG.info("classpath: {}", classpath);
		return classpath.indexOf("webapp") == -1 ? classpath :
			classpath.substring(0, classpath.lastIndexOf("webapp") + 7);
	}
	
	public static String getRelativePath(String path) {
		return getWebRootAbsolutePath() + path;
	}
	
	public static String getAbsolutePath(String path) {
		return ResourceUtils.class.getClassLoader().getResource(path).getPath();
	}
	
}

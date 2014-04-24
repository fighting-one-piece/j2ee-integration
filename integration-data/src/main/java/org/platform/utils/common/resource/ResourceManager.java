package org.platform.utils.common.resource;

public class ResourceManager {
	
	private ResourceManager(){}

	public static String getWebRootAbsolutePath() {
		String classpath = ResourceManager.class.getClassLoader().getResource("").getPath();
		return classpath.substring(0, classpath.lastIndexOf("webapp") + 7);
	}
	
	public static String getRelativePath(String path) {
		return getWebRootAbsolutePath() + path;
	}
	
	public static String getAbsolutePath(String path) {
		return ResourceManager.class.getClassLoader().getResource(path).getPath();
	}
	
}

package org.platform.utils.resource;

public class ResourceUtils {
	
	private ResourceUtils(){}

	public static String getWebRootAbsolutePath() {
		String classpath = ResourceUtils.class.getClassLoader().getResource("").getPath();
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

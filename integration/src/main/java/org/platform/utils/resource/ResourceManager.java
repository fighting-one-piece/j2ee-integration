package org.platform.utils.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ResourceManager {
	
	private static Logger logger = Logger.getLogger(ResourceManager.class);

	private ResourceManager(){}
	
	public static Properties getProperties(String propertiesPath) {
		InputStream in = ResourceManager.class.getClassLoader().getResourceAsStream(propertiesPath);
		Properties props = new Properties();
		try {
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				logger.debug(e.getMessage(), e);
			}
		}
		return props;
	}
	
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

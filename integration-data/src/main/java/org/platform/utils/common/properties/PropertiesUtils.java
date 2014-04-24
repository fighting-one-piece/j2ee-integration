package org.platform.utils.common.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
	
	public static Properties obtainValues(String file) {
		Properties properties = new Properties();
		InputStream inStream = null;
		try {
			inStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(file);
			properties.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != inStream) inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}

	public static String obtainValue(String file, String key) {
		InputStream inStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(file);
		Properties properties = new Properties();
		try {
			properties.load(inStream);
			return properties.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}

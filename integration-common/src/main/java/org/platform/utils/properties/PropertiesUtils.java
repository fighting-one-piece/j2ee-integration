package org.platform.utils.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtils.class);
	
	public static Properties newInstance(String file) {
		InputStream inStream = null;
		try {
			inStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(file);
			if (null == inStream) {
				inStream = new FileInputStream(new File(file));
			}
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} 
		return newInstance(inStream);
	}
	
	public static Properties newInstance(InputStream in) {
		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return properties;
	}
	
	public static Collection<Object> obtainValues(String file) {
		return newInstance(file).values();
	}

	public static String obtainValue(String file, String key) {
		return newInstance(file).getProperty(key);
	}

}

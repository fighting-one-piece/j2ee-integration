package org.platform.utils.common.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtils {
	
	private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtils.class);
	
	public static Properties obtainValues(String file) {
		Properties properties = new Properties();
		InputStream in = null;
		try {
			in = PropertiesUtils.class.getClassLoader().getResourceAsStream(file);
			properties.load(in);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return properties;
	}

	public static String obtainValue(String file, String key) {
		InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream(file);
		Properties properties = new Properties();
		try {
			properties.load(in);
			return properties.getProperty(key);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return null;
	}

}

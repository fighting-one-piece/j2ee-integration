package org.platform.utils.template;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class VelocityUtils {

	static {
		String templatePath = System.getProperty("user.dir") + "\\src\\main\\resources\\template\\vm\\";
		Properties p = new Properties();
		p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, templatePath);
		Velocity.init(p);
	}

	public static String getTemplate(Map<String, Object> data, String name, Locale locale, String encoding, boolean parse) {
		Writer writer = new StringWriter();
		VelocityContext context = new VelocityContext(data);
		Template template = Velocity.getTemplate(name);
		template.merge(context, writer);
		return writer.toString();
	}

}

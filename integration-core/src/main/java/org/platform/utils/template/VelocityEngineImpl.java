package org.platform.utils.template;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

public class VelocityEngineImpl extends TemplateEngineAbstrImpl {

	@Override
	public String getTemplatePath() {
		return System.getProperty("user.dir") + "/src/main/resources/template/vm/";
	}

	@Override
	public String getTemplate(Map<String, Object> data, String name) {
		Writer writer = new StringWriter();
		Properties p = new Properties();
		p.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, getTemplatePath());
		Velocity.init(p);
		VelocityContext context = new VelocityContext(data);
		Template template = Velocity.getTemplate(name);
		template.merge(context, writer);
		return writer.toString();
	}
}

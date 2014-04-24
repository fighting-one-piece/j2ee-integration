package org.platform.utils.template;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.cache.MruCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreeMarkerEngineImpl extends TemplateEngineAbstrImpl {

	private static Configuration configuration = null;

	static {
		try {
			configuration = new Configuration();
			File templatePath = new File(System.getProperty("user.dir") + "/src/main/resources/template/ftl");
			configuration.setDirectoryForTemplateLoading(templatePath);
			configuration.setObjectWrapper(new DefaultObjectWrapper());
			configuration.setCacheStorage(new MruCacheStorage(20, 250));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getTemplatePath() {
		return null;
	}

	@Override
	public String getTemplate(Map<String, Object> data, String name) {
		Writer writer = new StringWriter();
		try {
			Template template = configuration.getTemplate(name);
			template.process(data, writer);
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
		return writer.toString();
	}
}

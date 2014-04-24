package org.platform.utils.template;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import freemarker.cache.MruCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

/** FreeMarker工具类*/
public class FreeMarkerUtils {

	private Logger logger = Logger.getLogger(getClass());

	private static Configuration configuration = null;

	static {
		configuration = new Configuration();
//		configuration.setTemplateLoader(new ClassTemplateLoader(FreeMarkerUtils.class, "template\\ftl"));
//		configuration.setClassForTemplateLoading(clazz, pathPrefix);
		File templatePath = new File(System.getProperty("user.dir") + "/src/main/resources/template/ftl");
		try {
			configuration.setDirectoryForTemplateLoading(templatePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		ServletContext servletContext = GlobalContext.getServletContext();
//		configuration.setServletContextForTemplateLoading(servletContext,"\\WEB-INF\\view\\freemarker\\template");
		configuration.setObjectWrapper(new DefaultObjectWrapper());
		configuration.setCacheStorage(new MruCacheStorage(20, 250));
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
	}

	private FreeMarkerUtils(){}

	public String getTemplate(Map<String, Object> data, String name, Locale locale, String encoding, boolean parse) {
		Writer writer = new StringWriter();
		try {
			Template template = configuration.getTemplate(name, locale, encoding, parse);
			template.process(data, writer);
		} catch (Exception e) {
			logger.debug(e.getMessage(), e);
		}
		return writer.toString();
	}
}

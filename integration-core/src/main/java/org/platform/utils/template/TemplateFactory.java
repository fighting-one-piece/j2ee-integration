package org.platform.utils.template;

import java.util.HashMap;
import java.util.Map;

public class TemplateFactory {

	/** FreeMarker模版引擎*/
	public static final String TEMPLATE_ENGINE_FREEMARKER = "FreeMarker";
	/** Velocity模版引擎*/
	public static final String TEMPLATE_ENGINE_VELOCITY = "Velocity";

	private static TemplateFactory factory = null;

	private Map<String, ITemplateEngine> templateEngineMap = null;

	private TemplateFactory() {
		templateEngineMap = new HashMap<String, ITemplateEngine>();
		templateEngineMap.put(TEMPLATE_ENGINE_FREEMARKER, new FreeMarkerEngineImpl());
		templateEngineMap.put(TEMPLATE_ENGINE_VELOCITY, new VelocityEngineImpl());
	}

	public static synchronized TemplateFactory getInstance() {
		if (null == factory) {
			factory = new TemplateFactory();
		}
		return factory;
	}

	public ITemplateEngine getEngine(String engineName) {
		return templateEngineMap.get(engineName);
	}
}
